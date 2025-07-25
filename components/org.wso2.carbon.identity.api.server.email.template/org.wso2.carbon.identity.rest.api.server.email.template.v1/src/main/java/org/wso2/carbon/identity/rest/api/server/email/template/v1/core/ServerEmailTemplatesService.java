/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.server.email.template.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.email.mgt.EmailTemplateManager;
import org.wso2.carbon.email.mgt.constants.I18nMgtConstants;
import org.wso2.carbon.email.mgt.exceptions.I18nEmailMgtClientException;
import org.wso2.carbon.email.mgt.exceptions.I18nEmailMgtException;
import org.wso2.carbon.email.mgt.exceptions.I18nEmailMgtInternalException;
import org.wso2.carbon.email.mgt.model.EmailTemplate;
import org.wso2.carbon.email.mgt.util.I18nEmailUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.email.template.common.Constants;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithoutTemplates;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.SimpleEmailTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_API_BASE_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATE_TYPES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.PATH_SEPARATOR;

/**
 * Call internal osgi services to perform email templates related operations.
 *
 * Note: email-template-type-id is created by base64URL encoding the template display name.
 */
public class ServerEmailTemplatesService {

    private final EmailTemplateManager emailTemplateManager;
    private static final Log log = LogFactory.getLog(ServerEmailTemplatesService.class);

    public ServerEmailTemplatesService(EmailTemplateManager emailTemplateManager) {

        this.emailTemplateManager = emailTemplateManager;
    }

    /**
     * Return all email template types in the system with limited information of the templates inside.
     *
     * @param limit     Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset    Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy    Element to sort the responses. **Not supported at the moment**
     * @param requiredAttributes Required attributes in the email template types list response.
     * @return A list of email template types.
     */
    public List<EmailTemplateTypeWithoutTemplates> getAllEmailTemplateTypes(Integer limit, Integer offset,
                                                                            String sortOrder, String sortBy,
                                                                            String requiredAttributes) {

        handleNoteSupportedParameters(limit, offset, sortOrder, sortBy);

        try {
            if (log.isDebugEnabled()) {
                log.debug("Retrieving all email template types from tenant: " + getTenantDomainFromContext());
            }
            List<String> requestedAttributeList = null;
            List<EmailTemplate> allTemplates = null;
            List<String> availableTemplateTypes = emailTemplateManager
                    .getAvailableTemplateTypes(getTenantDomainFromContext());
            if (StringUtils.isNotBlank(requiredAttributes)) {
                requestedAttributeList = new ArrayList<>(Arrays.asList(requiredAttributes.split(",")));
                List<String> supportedAttributes =
                        Arrays.asList(Constants.TEMPLATES, Constants.LOCALE, Constants.CONTENT_TYPE, Constants.SUBJECT,
                                Constants.BODY, Constants.FOOTER);
                for (String requestedAttribute : requestedAttributeList) {
                    if (!supportedAttributes.contains(requestedAttribute)) {
                        throw handleError(Constants.ErrorMessage.ERROR_ATTRIBUTE_NOT_SUPPORTED);
                    }
                }
                allTemplates = emailTemplateManager.getAllEmailTemplates(getTenantDomainFromContext());
            }

            return buildEmailTemplateTypeWithoutTemplatesList(availableTemplateTypes, allTemplates,
                    requestedAttributeList);
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPES);
        }
    }

    /**
     * Return a specific email template type identified by the email template type id.
     *
     * @param templateTypeId Email template type id.
     * @param limit          Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset         Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder      Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return The email template type identified by the given id, 404 if not found.
     */
    public EmailTemplateTypeWithID getEmailTemplateType(String templateTypeId, Integer limit, Integer offset,
                                                        String sortOrder, String sortBy) {

        handleNoteSupportedParameters(limit, offset, sortOrder, sortBy);

        String decodedTemplateTypeId = decodeTemplateTypeId(templateTypeId);

        try {
            List<EmailTemplate> internalEmailTemplates = emailTemplateManager
                    .getEmailTemplateType(decodedTemplateTypeId, getTenantDomainFromContext());
            return buildEmailTemplateTypeWithID(internalEmailTemplates, templateTypeId);
        } catch (I18nEmailMgtException e) {
            if (StringUtils.equals(I18nMgtConstants.ErrorCodes.EMAIL_TEMPLATE_TYPE_NOT_FOUND, e.getErrorCode())) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
            }
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Return a list of locations of all available templates in a specific email template type.
     *
     * @param templateTypeId Email template type id.
     * @param limit          Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset         Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder      Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return List of SimpleEmailTemplate objects in the template type identified by the given id, 404 if not found.
     */
    public List<SimpleEmailTemplate> getTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit,
                                                                         Integer offset, String sortOrder,
                                                                         String sortBy) {

        handleNoteSupportedParameters(limit, offset, sortOrder, sortBy);

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            List<EmailTemplate> internalEmailTemplates = emailTemplateManager
                    .getEmailTemplateType(templateTypeDisplayName, getTenantDomainFromContext());
            return buildSimpleEmailTemplatesList(internalEmailTemplates, templateTypeId);
        } catch (I18nEmailMgtException e) {
            if (StringUtils.equals(I18nMgtConstants.ErrorCodes.EMAIL_TEMPLATE_TYPE_NOT_FOUND, e.getErrorCode())) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
            }
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Return a single email template that matches to the given template-type-id and the template-id.
     *
     * @param templateTypeId Email template type id.
     * @param templateId     Email template id.
     * @param limit          Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset         Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder      Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return Email template identified by the given template-type-id and the template-id, 404 if not found.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, Integer limit, Integer offset,
                                                String sortOrder, String sortBy) {

        handleNoteSupportedParameters(limit, offset, sortOrder, sortBy);

        try {
            String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
            templateId = I18nEmailUtil.normalizeLocaleFormat(templateId);
            EmailTemplate internalEmailTemplate = emailTemplateManager.getEmailTemplate(templateTypeDisplayName,
                    templateId, getTenantDomainFromContext());
            // EmailTemplateManager sends the default template if no matching template found. We need to check for
            // the locale specifically.
            if (!internalEmailTemplate.getLocale().equals(templateId)) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
            } else {
                return buildEmailTemplateWithID(internalEmailTemplate);
            }

        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Adds a new email template type to the system. Another template with the same display name should not exists in
     * the system. 0 or more email templates can be provided.
     *
     * @param emailTemplateType  Email template type with or without templates.
     * @return Object with id and location of the newly created template type.
     */
    public EmailTemplateTypeWithoutTemplates addEmailTemplateType(EmailTemplateType emailTemplateType) {

        String templateTypeDisplayName = emailTemplateType.getDisplayName();
        try {
            if (log.isDebugEnabled()) {
                log.debug("Adding email template type: " + templateTypeDisplayName + 
                        " for tenant: " + getTenantDomainFromContext());
            }
            // Add email template type without templates, first.
            emailTemplateManager.addEmailTemplateType(templateTypeDisplayName, getTenantDomainFromContext());

            // Add email templates if present, to the template type.
            if (!emailTemplateType.getTemplates().isEmpty()) {
                for (EmailTemplateWithID emailTemplate : emailTemplateType.getTemplates()) {
                    addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplate);
                }
            }

            // Build a response object and send if everything is successful.
            EmailTemplateTypeWithoutTemplates response = new EmailTemplateTypeWithoutTemplates();
            response.setDisplayName(templateTypeDisplayName);
            String templateTypeId = getEmailTemplateIdFromDisplayName(templateTypeDisplayName);
            response.setId(templateTypeId);
            response.setSelf(getTemplateTypeLocation(templateTypeId));

            if (log.isDebugEnabled()) {
                log.debug("Email template type added successfully: " + templateTypeDisplayName);
            }
            return response;
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_ADDING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Adds a new email template to the given template type. Template Id should not exists in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param emailTemplateWithID New email template.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleEmailTemplate addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Adding email template: " + emailTemplateWithID.getId() + 
                        " to template type: " + templateTypeDisplayName);
            }
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName,
                    emailTemplateWithID.getId(), getTenantDomainFromContext());
            if (!isTemplateExists) {
                // Email template is new, hence add to the system.
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID);

                // Create and send the location of the created object as the response.
                SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
                simpleEmailTemplate.setSelf(getTemplateLocation(templateTypeId, emailTemplateWithID.getId()));
                simpleEmailTemplate.setId(emailTemplateWithID.getId());
                if (log.isDebugEnabled()) {
                    log.debug("Email template added successfully: " + emailTemplateWithID.getId());
                }
                return simpleEmailTemplate;
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_ALREADY_EXISTS);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_ADDING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Delete the email template type from the system.
     *
     * @param templateTypeId ID of the template type to be deleted.
     */
    public void deleteEmailTemplateType(String templateTypeId) {

        String templateTypeDisplayName;
        try {
            templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        } catch (APIError e) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }
        try {
            boolean isTemplateTypeExists = emailTemplateManager.isEmailTemplateTypeExists(templateTypeDisplayName,
                    getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                if (log.isDebugEnabled()) {
                    log.debug("Deleting email template type: " + templateTypeDisplayName);
                }
                emailTemplateManager.deleteEmailTemplateType(templateTypeDisplayName, getTenantDomainFromContext());
                if (log.isDebugEnabled()) {
                    log.debug("Email template type deleted successfully: " + templateTypeDisplayName);
                }
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_DELETING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Delete the email template from the system.
     *
     * @param templateTypeId ID of the template type.
     * @param templateId   ID of the template.
     */
    public void deleteEmailTemplate(String templateTypeId, String templateId) {

        String templateTypeDisplayName;
        try {
            templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        } catch (APIError e) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }
        try {
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName, templateId,
                    getTenantDomainFromContext());
            if (isTemplateExists) {
                if (log.isDebugEnabled()) {
                    log.debug("Deleting email template: " + templateId + " from type: " + templateTypeDisplayName);
                }
                emailTemplateManager.deleteEmailTemplate(templateTypeDisplayName, templateId,
                        getTenantDomainFromContext());
                if (log.isDebugEnabled()) {
                    log.debug("Email template deleted successfully: " + templateId);
                }
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_DELETING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Replace the email template Identified by the template type id and the template id.
     *
     * @param templateTypeId      ID of the email template type.
     * @param templateId          ID of the email template.
     * @param emailTemplateWithID Content of the email template to be saved.
     */
    public void updateEmailTemplate(String templateTypeId, String templateId, EmailTemplateWithID emailTemplateWithID) {

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Updating email template: " + templateId + " in type: " + templateTypeDisplayName);
            }
            // Check whether the email template exists, first.
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName, templateId,
                    getTenantDomainFromContext());
            if (isTemplateExists) {
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID);
                if (log.isDebugEnabled()) {
                    log.debug("Email template updated successfully: " + templateId);
                }
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_UPDATING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Replace all the email templates of the template type.
     *
     * @param templateTypeId ID of the email template type.
     * @param emailTemplates List of email templates to be replaced by.
     */
    public void updateEmailTemplateType(String templateTypeId, List<EmailTemplateWithID> emailTemplates) {

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            // Check whether the email template type exists.
            boolean isTemplateTypeExists = emailTemplateManager.isEmailTemplateTypeExists(templateTypeDisplayName,
                    getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                // Delete all existing templates in the template type
                EmailTemplateTypeWithID templateType = getEmailTemplateType(templateTypeId, null, null, null, null);
                for (EmailTemplateWithID template : templateType.getTemplates()) {
                    deleteEmailTemplate(templateTypeId, template.getId());
                }

                // Add new email templates.
                for (EmailTemplateWithID emailTemplate : emailTemplates) {
                    addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplate);
                }

            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_UPDATING_EMAIL_TEMPLATE_TYPE);
        }
    }

    private void addEmailTemplateToTheSystem(String templateTypeDisplayName, EmailTemplateWithID emailTemplateWithID)
            throws I18nEmailMgtException {

        EmailTemplate internalEmailTemplate = new EmailTemplate();
        internalEmailTemplate.setTemplateDisplayName(templateTypeDisplayName);
        internalEmailTemplate.setTemplateType(I18nEmailUtil.getNormalizedName(templateTypeDisplayName));
        internalEmailTemplate.setLocale(emailTemplateWithID.getId());
        internalEmailTemplate.setEmailContentType(emailTemplateWithID.getContentType());
        internalEmailTemplate.setSubject(emailTemplateWithID.getSubject());
        internalEmailTemplate.setBody(emailTemplateWithID.getBody());
        internalEmailTemplate.setFooter(emailTemplateWithID.getFooter());

        emailTemplateManager.addEmailTemplate(internalEmailTemplate, getTenantDomainFromContext());
    }

    /**
     * Create a list SimpleEmailTemplate objects by reading an internal EmailTemplate list.
     *
     * @param internalEmailTemplates List of internal email templates.
     * @param templateTypeId         Email template type to be extracted.
     * @return List of SimpleEmailTemplate objects.
     */
    private List<SimpleEmailTemplate> buildSimpleEmailTemplatesList(List<EmailTemplate> internalEmailTemplates,
                                                                    String templateTypeId) {

        List<SimpleEmailTemplate> simpleEmailTemplates = new ArrayList<>();
        for (EmailTemplate internalTemplate : internalEmailTemplates) {
            SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
            String templateLocation = getTemplateLocation(templateTypeId, internalTemplate.getLocale());
            simpleEmailTemplate.setId(internalTemplate.getLocale());
            simpleEmailTemplate.setSelf(templateLocation);
            simpleEmailTemplates.add(simpleEmailTemplate);
        }
        return simpleEmailTemplates;
    }

    /**
     * Create a list EmailTemplateTypeWithoutTemplates objects by reading an internal EmailTemplate list.
     *
     * @param emailTemplateTypes List of available email template types.
     * @param internalEmailTemplates List of EmailTemplate objects.
     * @param requestedAttributeList List of required attributes.
     * @return List of EmailTemplateTypeWithoutTemplates objects.
     */
    private List<EmailTemplateTypeWithoutTemplates> buildEmailTemplateTypeWithoutTemplatesList(
            List<String> emailTemplateTypes, List<EmailTemplate> internalEmailTemplates,
            List<String> requestedAttributeList) {

        Map<String, EmailTemplateTypeWithoutTemplates> templateTypeMap = new HashMap<>();
        for (String templateType : emailTemplateTypes) {

            EmailTemplateTypeWithoutTemplates emailTemplateType = new EmailTemplateTypeWithoutTemplates();
            // Set display name.
            emailTemplateType.setDisplayName(templateType);
            // Set id.
            String templateTypeId = getEmailTemplateIdFromDisplayName(templateType);
            emailTemplateType.setId(templateTypeId);
            // Set location.
            emailTemplateType.setSelf(getTemplateTypeLocation(templateTypeId));

            if (requestedAttributeList != null) {
                emailTemplateType.setTemplates(new ArrayList<>());
            }
            templateTypeMap.put(templateType, emailTemplateType);
        }

        // Populate optional email template information if exists.
        if (requestedAttributeList != null) {
            for (EmailTemplate emailTemplate : internalEmailTemplates) {
                EmailTemplateWithID templateWithID = new EmailTemplateWithID();
                for (String requestedAttribute : requestedAttributeList) {
                    switch (requestedAttribute) {
                        case Constants.TEMPLATES:
                            templateWithID.setContentType(emailTemplate.getEmailContentType());
                            templateWithID.setSubject(emailTemplate.getSubject());
                            templateWithID.setBody(emailTemplate.getBody());
                            templateWithID.setFooter(emailTemplate.getFooter());
                            templateWithID.setId(emailTemplate.getLocale());
                            break;
                        case Constants.LOCALE:
                            templateWithID.setId(emailTemplate.getLocale());
                            break;
                        case Constants.CONTENT_TYPE:
                            templateWithID.setContentType(emailTemplate.getEmailContentType());
                            break;
                        case Constants.SUBJECT:
                            templateWithID.setSubject(emailTemplate.getSubject());
                            break;
                        case Constants.BODY:
                            templateWithID.setBody(emailTemplate.getBody());
                            break;
                        case Constants.FOOTER:
                            templateWithID.setFooter(emailTemplate.getFooter());
                            break;
                        default:
                            if (log.isDebugEnabled()) {
                                log.debug("Unknown requested attribute: " + requestedAttribute);
                            }
                            break;
                    }
                }
                /*
                  Email template's display name is used to search templateTypeMap key because
                  Template's display name and Template type's display name are equal.
                 */
                templateTypeMap.get(emailTemplate.getTemplateDisplayName()).getTemplates().add(templateWithID);
            }
        }

        return new ArrayList<>(templateTypeMap.values());
    }

    /**
     * Create an EmailTemplateTypeWithID object by reading an internal EmailTemplate list.
     *
     * @param internalEmailTemplates List of internal email templates.
     * @param templateTypeId         Email template type to be extracted.
     * @return Extracted EmailTemplateTypeWithID object.
     */
    private EmailTemplateTypeWithID buildEmailTemplateTypeWithID(List<EmailTemplate> internalEmailTemplates,
                                                                 String templateTypeId) {

        EmailTemplateTypeWithID emailTemplateType = new EmailTemplateTypeWithID();
        boolean isFirst = true;
        for (EmailTemplate internalTemplate : internalEmailTemplates) {
            if (isFirst) {
                // Template type details should only be set once.
                emailTemplateType.setDisplayName(internalTemplate.getTemplateDisplayName());
                emailTemplateType.setId(templateTypeId);
                isFirst = false;
            }
            emailTemplateType.getTemplates().add(buildEmailTemplateWithID(internalTemplate));
        }
        // Build response if no template exists for the template type.
        if (CollectionUtils.isEmpty(internalEmailTemplates)) {
            emailTemplateType.setDisplayName(base64URLDecode(templateTypeId));
            emailTemplateType.setId(templateTypeId);
        }
        return emailTemplateType;
    }

    /**
     * Convert an internal email template to a new Email Template object.
     */
    private EmailTemplateWithID buildEmailTemplateWithID(EmailTemplate internalTemplate) {

        EmailTemplateWithID templateWithID = new EmailTemplateWithID();
        templateWithID.setId(internalTemplate.getLocale());
        templateWithID.setContentType(internalTemplate.getEmailContentType());
        templateWithID.setSubject(internalTemplate.getSubject());
        templateWithID.setBody(internalTemplate.getBody());
        templateWithID.setFooter(internalTemplate.getFooter());
        return templateWithID;
    }

    /**
     * Base64 decode a given encoded template type id, return appropriate error if failed.
     *
     * @param encodedTemplateTypeId Encoded template type id.
     * @return Decoded template type id if possible, API Error otherwise.
     */
    private String decodeTemplateTypeId(String encodedTemplateTypeId) {

        try {
            return base64URLDecode(encodedTemplateTypeId);
        } catch (Throwable e) {
            throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
        }
    }

    private String getEmailTemplateIdFromDisplayName(String templateTypeDisplayName) {

        return base64URLEncode(templateTypeDisplayName);
    }

    private String getTemplateTypeLocation(String templateTypeId) {

        String location = V1_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                PATH_SEPARATOR + templateTypeId;
        return ContextLoader.buildURIForBody(location).toString();
    }

    private String getTemplateLocation(String templateTypeId, String templateId) {

        String templateLocation = getTemplateTypeLocation(templateTypeId);
        return templateLocation + EMAIL_TEMPLATES_PATH + PATH_SEPARATOR + templateId;
    }

    private void handleNoteSupportedParameters(Integer limit, Integer offset, String sortOrder, String sortBy) {

        if (limit != null || offset != null) {
            throw handleError(Constants.ErrorMessage.ERROR_PAGINATION_NOT_SUPPORTED);
        }
        if (StringUtils.isNotBlank(sortOrder) || StringUtils.isNotBlank(sortBy)) {
            throw handleError(Constants.ErrorMessage.ERROR_SORTING_NOT_SUPPORTED);
        }
    }

    /**
     * Handle I18nEmailMgtException, i.e. extract error description from the exception and set to the
     * API Error Response, along with an status code to be sent in the response.
     *
     * @param exception Exception thrown
     * @param errorEnum Corresponding error enum
     * @return API Error object.
     */
    private APIError handleI18nEmailMgtException(I18nEmailMgtException exception, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (exception instanceof I18nEmailMgtInternalException &&
                Constants.getMappedErrorMessage(exception.getErrorCode()) != null) {
            // Specific error with code is found.
            Constants.ErrorMessage errorMessage = Constants.getMappedErrorMessage(exception.getErrorCode());
            errorResponse = getErrorBuilder(errorMessage).build(log, exception, errorEnum.getDescription());
            status = errorMessage.getHttpStatus();

        } else if (exception instanceof I18nEmailMgtClientException) {
            // Send client error with original exception message.
            errorResponse = getErrorBuilder(errorEnum).build(log, exception.getMessage());
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.BAD_REQUEST;

        } else {
            // Server error
            errorResponse = getErrorBuilder(errorEnum).build(log, exception, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleError(Constants.ErrorMessage error) {

        return new APIError(error.getHttpStatus(), getErrorBuilder(error).build());
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).
                withMessage(errorMsg.getMessage()).withDescription(errorMsg.getDescription());
    }
}

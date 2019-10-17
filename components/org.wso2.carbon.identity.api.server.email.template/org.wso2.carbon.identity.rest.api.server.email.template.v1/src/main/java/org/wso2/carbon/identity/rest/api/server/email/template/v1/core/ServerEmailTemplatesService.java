/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.email.mgt.exceptions.I18nEmailMgtException;
import org.wso2.carbon.email.mgt.model.EmailTemplate;
import org.wso2.carbon.email.mgt.util.I18nEmailUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.email.template.common.Constants;
import org.wso2.carbon.identity.api.server.email.template.common.EmailTemplatesServiceHolder;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithoutTemplates;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.SimpleEmailTemplate;

import java.util.ArrayList;
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
 * <p>
 * Note: email-template-type-id is created by base64URL encoding the template display name.
 */
public class ServerEmailTemplatesService {

    private static final Log log = LogFactory.getLog(ServerEmailTemplatesService.class);

    /**
     * Return all email template types in the system with limited information of the templates inside.
     *
     * @param limit  Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sort   Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy Element to sort the responses. **Not supported at the moment**
     * @return A list of email template types.
     */
    public List<EmailTemplateTypeWithoutTemplates> getAllEmailTemplateTypes(Integer limit, Integer offset, String sort,
                                                                            String sortBy) {

        try {
            List<EmailTemplate> legacyEmailTemplates = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    getAllEmailTemplates(getTenantDomainFromContext());
            return buildEmailTemplateTypeWithoutTemplatesList(legacyEmailTemplates);
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
     * @param sort           Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return The email template type identified by the given id, 404 if not found.
     */
    public EmailTemplateTypeWithID getEmailTemplateType(String templateTypeId, Integer limit, Integer offset,
                                                        String sort, String sortBy) {

        try {
            List<EmailTemplate> legacyEmailTemplates = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    getAllEmailTemplates(getTenantDomainFromContext());
            return getMatchingEmailTemplateType(legacyEmailTemplates, templateTypeId);
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Return a list of locations of all available templates in a specific email template type.
     *
     * @param templateTypeId Email template type id.
     * @param limit          Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset         Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sort           Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return List of SimpleEmailTemplate objects in the template type identified by the given id, 404 if not found.
     */
    public List<SimpleEmailTemplate> getTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit,
                                                                         Integer offset, String sort, String sortBy) {

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateTypeExists =
                    EmailTemplatesServiceHolder.getEmailTemplateManager().isEmailTemplateTypeExists(
                            templateTypeDisplayName, getTenantDomainFromContext());
            if (!isTemplateTypeExists) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
            }
            List<EmailTemplate> legacyEmailTemplates = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    getAllEmailTemplates(getTenantDomainFromContext());
            return getTemplatesListOfEmailTemplateType(legacyEmailTemplates, templateTypeId);
        } catch (I18nEmailMgtException e) {
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
     * @param sort           Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy         Element to sort the responses. **Not supported at the moment**
     * @return Email template identified by the given template-type-id and the template-id, 404 if not found.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, Integer limit, Integer offset,
                                                String sort, String sortBy) {

        try {
            String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
            EmailTemplate legacyEmailTemplate = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    getEmailTemplate(templateTypeDisplayName, templateId, getTenantDomainFromContext());
            // EmailTemplateManager sends the default template if no matching template found. We need to check for
            // the locale specifically.
            if (!legacyEmailTemplate.getLocale().equals(templateId)) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
            } else {
                return buildEmailTemplateWithID(legacyEmailTemplate);
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
            // Add email template type without templates, first.
            EmailTemplatesServiceHolder.getEmailTemplateManager().addEmailTemplateType(templateTypeDisplayName,
                    getTenantDomainFromContext());

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
            boolean isTemplateExists = EmailTemplatesServiceHolder.getEmailTemplateManager()
                    .isEmailTemplateExists(templateTypeDisplayName, emailTemplateWithID.getId(),
                            getTenantDomainFromContext());
            if (!isTemplateExists) {
                // Email template is new, hence add to the system.
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID);

                // Create and send the location of the created object as the response.
                SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
                simpleEmailTemplate.setSelf(getTemplateLocation(templateTypeId, emailTemplateWithID.getId()));
                simpleEmailTemplate.setId(emailTemplateWithID.getId());
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

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateTypeExists =
                    EmailTemplatesServiceHolder.getEmailTemplateManager().isEmailTemplateTypeExists(
                            templateTypeDisplayName, getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                EmailTemplatesServiceHolder.getEmailTemplateManager().deleteEmailTemplateType(templateTypeDisplayName,
                        getTenantDomainFromContext());
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
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

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateExists = EmailTemplatesServiceHolder.getEmailTemplateManager().isEmailTemplateExists(
                    templateTypeDisplayName, templateId, getTenantDomainFromContext());
            if (isTemplateExists) {
                EmailTemplatesServiceHolder.getEmailTemplateManager().deleteEmailTemplate(templateTypeDisplayName,
                        templateId, getTenantDomainFromContext());
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
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
            // Check whether the email template exists, first.
            boolean isTemplateExists = EmailTemplatesServiceHolder.getEmailTemplateManager().isEmailTemplateExists(
                    templateTypeDisplayName, templateId, getTenantDomainFromContext());
            if (isTemplateExists) {
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID);
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
            boolean isTemplateTypeExists = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    isEmailTemplateTypeExists(templateTypeDisplayName, getTenantDomainFromContext());
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

        EmailTemplate legacyEmailTemplate = new EmailTemplate();
        legacyEmailTemplate.setTemplateDisplayName(templateTypeDisplayName);
        legacyEmailTemplate.setTemplateType(I18nEmailUtil.getNormalizedName(templateTypeDisplayName));
        legacyEmailTemplate.setLocale(emailTemplateWithID.getId());
        legacyEmailTemplate.setEmailContentType(emailTemplateWithID.getContentType());
        legacyEmailTemplate.setSubject(emailTemplateWithID.getSubject());
        legacyEmailTemplate.setBody(emailTemplateWithID.getBody());
        legacyEmailTemplate.setFooter(emailTemplateWithID.getFooter());

        EmailTemplatesServiceHolder.getEmailTemplateManager().addEmailTemplate(legacyEmailTemplate,
                getTenantDomainFromContext());
    }

    /**
     * Iterate through a given legacy email templates list, extract a list of templates in it and build a list of
     * locations of those templates.
     *
     * @param legacyEmailTemplates List of legacy email templates.
     * @param templateTypeId       Email template type to be extracted.
     * @return Extracted locations list in the template type, 404 if not found.
     */
    private List<SimpleEmailTemplate> getTemplatesListOfEmailTemplateType(List<EmailTemplate> legacyEmailTemplates,
                                                             String templateTypeId) {

        List<SimpleEmailTemplate> simpleEmailTemplates = new ArrayList<>();
        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        for (EmailTemplate legacyTemplate : legacyEmailTemplates) {
            if (templateTypeDisplayName.equals(legacyTemplate.getTemplateDisplayName())) {
                SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
                String templateLocation = getTemplateLocation(templateTypeId, legacyTemplate.getLocale());
                simpleEmailTemplate.setId(legacyTemplate.getLocale());
                simpleEmailTemplate.setSelf(templateLocation);
                simpleEmailTemplates.add(simpleEmailTemplate);
            }
        }
        return simpleEmailTemplates;
    }

    /**
     * Create a list EmailTemplateTypeWithoutTemplates objects by reading a legacy EmailTemplate list.
     *
     * @param legacyEmailTemplates List of EmailTemplate objects.
     * @return List of EmailTemplateTypeWithoutTemplates objects.
     */
    private List<EmailTemplateTypeWithoutTemplates> buildEmailTemplateTypeWithoutTemplatesList(
            List<EmailTemplate> legacyEmailTemplates) {

        Map<String, EmailTemplateTypeWithoutTemplates> templateTypeMap = new HashMap<>();
        for (EmailTemplate emailTemplate : legacyEmailTemplates) {
            if (!templateTypeMap.containsKey(emailTemplate.getTemplateType())) {

                EmailTemplateTypeWithoutTemplates emailTemplateType = new EmailTemplateTypeWithoutTemplates();
                // Set display name.
                emailTemplateType.setDisplayName(emailTemplate.getTemplateDisplayName());
                // Set id.
                String templateTypeId = getEmailTemplateIdFromDisplayName(emailTemplate.getTemplateDisplayName());
                emailTemplateType.setId(templateTypeId);
                // Set location.
                emailTemplateType.setSelf(getTemplateTypeLocation(templateTypeId));

                templateTypeMap.put(emailTemplate.getTemplateType(), emailTemplateType);
            }
        }

        return new ArrayList<>(templateTypeMap.values());
    }

    public String getTemplateTypeLocation(String templateTypeId) {

        String location = V1_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                PATH_SEPARATOR + templateTypeId;
        return ContextLoader.buildURIForBody(location).toString();
    }

    public String getTemplateLocation(String templateTypeId, String templateId) {

        String templateLocation = getTemplateTypeLocation(templateTypeId);
        return templateLocation + EMAIL_TEMPLATES_PATH + PATH_SEPARATOR + templateId;
    }

    /**
     * Iterate through a given legacy email templates list and extract a given single email template type with all
     * of it's templates.
     *
     * @param legacyEmailTemplates List of legacy email templates.
     * @param templateTypeId       Email template type to be extracted.
     * @return Extracted EmailTemplateTypeWithID, 404 if not found.
     */
    private EmailTemplateTypeWithID getMatchingEmailTemplateType(List<EmailTemplate> legacyEmailTemplates,
                                                                 String templateTypeId) {

        EmailTemplateTypeWithID emailTemplateType = new EmailTemplateTypeWithID();
        String decodedTemplateTypeId = decodeTemplateTypeId(templateTypeId);
        boolean isFirst = true;
        for (EmailTemplate legacyTemplate : legacyEmailTemplates) {
            if (decodedTemplateTypeId.equals(legacyTemplate.getTemplateDisplayName())) {
                if (isFirst) {
                    // Template type details should only be set once.
                    emailTemplateType.setDisplayName(legacyTemplate.getTemplateDisplayName());
                    emailTemplateType.setId(templateTypeId);
                    isFirst = false;
                }
                emailTemplateType.getTemplates().add(buildEmailTemplateWithID(legacyTemplate));
            }
        }
        if (StringUtils.isBlank(emailTemplateType.getId())) {
            throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
        }
        return emailTemplateType;
    }

    /**
     * Convert a legacy email template to a new Email Template object.
     */
    private EmailTemplateWithID buildEmailTemplateWithID(EmailTemplate legacyTemplate) {

        EmailTemplateWithID templateWithID = new EmailTemplateWithID();
        templateWithID.setId(legacyTemplate.getLocale());
        templateWithID.setContentType(legacyTemplate.getEmailContentType());
        templateWithID.setSubject(legacyTemplate.getSubject());
        templateWithID.setBody(legacyTemplate.getBody());
        templateWithID.setFooter(legacyTemplate.getFooter());
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
            throw handleError(Constants.ErrorMessage.ERROR_INVALID_TEMPLATE_TYPE_ID);
        }
    }

    private String getEmailTemplateIdFromDisplayName(String templateTypeDisplayName) {

        return base64URLEncode(templateTypeDisplayName);
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

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, exception, errorEnum.getDescription());
        Response.Status status;

        if (exception != null) {
            if (StringUtils.isNotBlank(exception.getErrorCode()) &&
                    Constants.getMappedErrorMessage(exception.getErrorCode()) != null) {
                // More specific error has been found.
                Constants.ErrorMessage errorMessage = Constants.getMappedErrorMessage(exception.getErrorCode());
                errorResponse = getErrorBuilder(errorMessage).build(log, exception, errorEnum.getDescription());
                status = errorMessage.getHttpStatus();
            } else {
                status = Response.Status.INTERNAL_SERVER_ERROR;
            }
        } else {
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

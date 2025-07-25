/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.email.template.v2.core;

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
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.SimpleEmailTemplate;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.APP_EMAIL_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_API_BASE_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATE_TYPES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.PATH_SEPARATOR;

/**
 * Call internal osgi services to perform application email templates related operations.
 * <p>
 * Note: email-template-type-id is created by base64URL encoding the template display name.
 */
public class ApplicationEmailTemplatesService {

    private final EmailTemplateManager emailTemplateManager;
    private static final Log log = LogFactory.getLog(ApplicationEmailTemplatesService.class);

    public ApplicationEmailTemplatesService(EmailTemplateManager emailTemplateManager) {

        this.emailTemplateManager = emailTemplateManager;
    }

    /**
     * Return the application email template that matches to the given template-type-id and the template-id.
     *
     * @param templateTypeId  Email template type id.
     * @param templateId      Email template id.
     * @param applicationUuid Application UUID.
     * @param resolve         Whether to retrieve the template resolved through the ancestor organization hierarchy.
     * @param limit           Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset          Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder       Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy          Element to sort the responses. **Not supported at the moment**
     * @return Email template identified by the given template-type-id and the template-id, 404 if not found.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, String applicationUuid,
                                                boolean resolve, Integer limit, Integer offset,
                                                String sortOrder, String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving email template. TemplateTypeId: %s, TemplateId: %s, AppUuid: %s",
                    templateTypeId, templateId, applicationUuid));
        }
        handleNotSupportedParameters(limit, offset, sortOrder, sortBy);

        try {
            String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
            templateId = I18nEmailUtil.normalizeLocaleFormat(templateId);
            EmailTemplate internalEmailTemplate = emailTemplateManager.getEmailTemplate(templateTypeDisplayName,
                    templateId, getTenantDomainFromContext(), applicationUuid, resolve);
            // EmailTemplateManager sends the default template if no matching template found. We need to check for
            // the locale specifically.
            if (!internalEmailTemplate.getLocale().equals(templateId)) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Successfully retrieved email template for locale: %s", templateId));
                }
                return buildEmailTemplateWithID(internalEmailTemplate);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Return the location list of all available application email templates of a specific email template type.
     *
     * @param templateTypeId  Email template type id.
     * @param applicationUuid Application UUID.
     * @param resolve         Whether to retrieve templates resolved through the ancestor organization hierarchy,
     *                        returning templates that are applicable across the tenant's organizational structure.
     * @param limit           Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset          Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sortOrder       Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy          Element to sort the responses. **Not supported at the moment**
     * @return List of SimpleEmailTemplate objects in the template type identified by the given id, 404 if not found.
     */
    public List<SimpleEmailTemplate> getTemplatesListOfEmailTemplateType(String templateTypeId, String applicationUuid,
                                                                         boolean resolve,
                                                                         Integer limit, Integer offset,
                                                                         String sortOrder, String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving templates list for type: %s, AppUuid: %s", templateTypeId,
                    applicationUuid));
        }
        handleNotSupportedParameters(limit, offset, sortOrder, sortBy);

        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            List<EmailTemplate> internalEmailTemplates = emailTemplateManager.getEmailTemplateType(
                    templateTypeDisplayName, getTenantDomainFromContext(), applicationUuid, resolve);
            if (log.isDebugEnabled()) {
                log.debug(String.format("Found %d templates for type: %s", internalEmailTemplates.size(),
                        templateTypeDisplayName));
            }
            return buildSimpleEmailTemplatesList(internalEmailTemplates, templateTypeId, applicationUuid);
        } catch (I18nEmailMgtException e) {
            if (StringUtils.equals(I18nMgtConstants.ErrorCodes.EMAIL_TEMPLATE_TYPE_NOT_FOUND, e.getErrorCode())) {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
            }
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPE);
        }
    }

    /**
     * Adds a new application email template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param emailTemplateWithID New email template.
     * @param applicationUuid     Application UUID.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleEmailTemplate addEmailTemplate(
            String templateTypeId, EmailTemplateWithID emailTemplateWithID, String applicationUuid) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Adding email template. TemplateTypeId: %s, Locale: %s, AppUuid: %s",
                    templateTypeId, emailTemplateWithID != null ? emailTemplateWithID.getLocale() : "null",
                    applicationUuid));
        }
        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName,
                    emailTemplateWithID.getLocale(), getTenantDomainFromContext(), applicationUuid, false);
            if (!isTemplateExists) {
                // Email template is new, hence add to the system.
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID, applicationUuid);
                log.info(String.format("Successfully added email template for type: %s, locale: %s in application: %s",
                        templateTypeDisplayName, emailTemplateWithID.getLocale(), applicationUuid));

                // Create and send the location of the created object as the response.
                SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
                simpleEmailTemplate.setSelf(
                        getTemplateLocation(templateTypeId, emailTemplateWithID.getLocale(), applicationUuid));
                simpleEmailTemplate.setLocale(emailTemplateWithID.getLocale());
                return simpleEmailTemplate;
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_ALREADY_EXISTS);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_ADDING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Delete the application email template from the system.
     *
     * @param templateTypeId  ID of the template type.
     * @param templateId      ID of the template.
     * @param applicationUuid Application UUID.
     */
    public void deleteEmailTemplate(String templateTypeId, String templateId, String applicationUuid) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting email template. TemplateTypeId: %s, TemplateId: %s, AppUuid: %s",
                    templateTypeId, templateId, applicationUuid));
        }
        String templateTypeDisplayName;
        try {
            templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        } catch (APIError e) {
            // Ignoring the delete operation and return 204 response code, since the resource does not exist.
            return;
        }
        try {
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName, templateId,
                    getTenantDomainFromContext(), applicationUuid, false);
            if (isTemplateExists) {
                emailTemplateManager.deleteEmailTemplate(templateTypeDisplayName, templateId,
                        getTenantDomainFromContext(), applicationUuid);
                log.info(String.format("Successfully deleted email template for type: %s, locale: %s in " +
                        "application: %s", templateTypeDisplayName, templateId, applicationUuid));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Email template not found for deletion. Type: %s, Locale: %s",
                            templateTypeDisplayName, templateId));
                }
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_DELETING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Delete all email templates of a given template type for application.
     *
     * @param templateTypeId ID of the template type.
     * @param applicationUuid Application UUID.
     */
    public void deleteAllAppEmailTemplates(String templateTypeId, String applicationUuid) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting all email templates for type: %s, AppUuid: %s", templateTypeId,
                    applicationUuid));
        }
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
                emailTemplateManager.deleteEmailTemplates(templateTypeDisplayName, getTenantDomainFromContext(),
                        applicationUuid);
                log.info(String.format("Successfully deleted all email templates for type: %s in application: %s",
                        templateTypeDisplayName, applicationUuid));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Template type not found for deletion: %s", templateTypeDisplayName));
                }
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_DELETING_APP_EMAIL_TEMPLATES);
        }
    }

    /**
     * Replace the application email template Identified by the template type id and the template id.
     *
     * @param templateTypeId      ID of the email template type.
     * @param templateId          ID of the email template.
     * @param emailTemplateWithID Content of the email template to be saved.
     */
    public void updateEmailTemplate(String templateTypeId, String templateId, EmailTemplateWithID emailTemplateWithID,
                                    String applicationUuid) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating email template. TemplateTypeId: %s, TemplateId: %s, AppUuid: %s",
                    templateTypeId, templateId, applicationUuid));
        }
        String templateTypeDisplayName = decodeTemplateTypeId(templateTypeId);
        try {
            /* Check whether the email template exists, first. Here, resolve param is specified as true since
              resolved org templates are returned in GET endpoint, by default. Therefore, resolved template existence
              is checked before updating. */
            boolean isTemplateExists = emailTemplateManager.isEmailTemplateExists(templateTypeDisplayName, templateId,
                    getTenantDomainFromContext(), applicationUuid, true);
            if (isTemplateExists) {
                addEmailTemplateToTheSystem(templateTypeDisplayName, emailTemplateWithID, applicationUuid);
                log.info(String.format("Successfully updated email template for type: %s, locale: %s in " +
                        "application: %s", templateTypeDisplayName, templateId, applicationUuid));
            } else {
                throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_NOT_FOUND);
            }
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e, Constants.ErrorMessage.ERROR_UPDATING_EMAIL_TEMPLATE);
        }
    }

    private void addEmailTemplateToTheSystem(String templateTypeDisplayName,
                                             EmailTemplateWithID emailTemplateWithID,
                                             String applicationUuid) throws I18nEmailMgtException {

        EmailTemplate internalEmailTemplate = new EmailTemplate();
        internalEmailTemplate.setTemplateDisplayName(templateTypeDisplayName);
        internalEmailTemplate.setTemplateType(I18nEmailUtil.getNormalizedName(templateTypeDisplayName));
        internalEmailTemplate.setLocale(emailTemplateWithID.getLocale());
        internalEmailTemplate.setEmailContentType(emailTemplateWithID.getContentType());
        internalEmailTemplate.setSubject(emailTemplateWithID.getSubject());
        internalEmailTemplate.setBody(emailTemplateWithID.getBody());
        internalEmailTemplate.setFooter(emailTemplateWithID.getFooter());

        emailTemplateManager.addEmailTemplate(internalEmailTemplate, getTenantDomainFromContext(), applicationUuid);
    }

    /**
     * Create a list SimpleEmailTemplate objects by reading an internal EmailTemplate list.
     *
     * @param internalEmailTemplates List of internal email templates.
     * @param templateTypeId         Email template type to be extracted.
     * @return List of SimpleEmailTemplate objects.
     */
    private List<SimpleEmailTemplate> buildSimpleEmailTemplatesList(List<EmailTemplate> internalEmailTemplates,
                                                                    String templateTypeId, String applicationUuid) {

        List<SimpleEmailTemplate> simpleEmailTemplates = new ArrayList<>();
        for (EmailTemplate internalTemplate : internalEmailTemplates) {
            SimpleEmailTemplate simpleEmailTemplate = new SimpleEmailTemplate();
            String templateLocation = getTemplateLocation(
                    templateTypeId, internalTemplate.getLocale(), applicationUuid);
            simpleEmailTemplate.setLocale(internalTemplate.getLocale());
            simpleEmailTemplate.setSelf(templateLocation);
            simpleEmailTemplates.add(simpleEmailTemplate);
        }
        return simpleEmailTemplates;
    }

    /**
     * Convert an internal email template to a new Email Template object.
     */
    private EmailTemplateWithID buildEmailTemplateWithID(EmailTemplate internalTemplate) {

        EmailTemplateWithID templateWithID = new EmailTemplateWithID();
        templateWithID.setLocale(internalTemplate.getLocale());
        templateWithID.setContentType(internalTemplate.getEmailContentType());
        templateWithID.setSubject(internalTemplate.getSubject());
        templateWithID.setBody(internalTemplate.getBody());
        templateWithID.setFooter(internalTemplate.getFooter());
        return templateWithID;
    }

    /**
     * Base64 decode a given encoded template type id.
     *
     * @param encodedTemplateTypeId Encoded template type id.
     * @return Decoded template type id if valid, API Error otherwise.
     */
    private String decodeTemplateTypeId(String encodedTemplateTypeId) {

        try {
            return base64URLDecode(encodedTemplateTypeId);
        } catch (Throwable e) {
            throw handleError(Constants.ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
        }
    }

    /**
     * Get the location of the template type.
     *
     * @param templateTypeId Template type id.
     * @return Location of the template type.
     */
    private String getTemplateTypeLocation(String templateTypeId) {

        String location = V2_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                PATH_SEPARATOR + templateTypeId;
        return ContextLoader.buildURIForBody(location).toString();
    }

    /**
     * Get the location of the application email template.
     *
     * @param templateTypeId  Template type id.
     * @param templateId      Template id.
     * @param applicationUuid Application UUID.
     * @return Location of the email template.
     */
    private String getTemplateLocation(String templateTypeId, String templateId, String applicationUuid) {

        String templateLocation = getTemplateTypeLocation(templateTypeId);
        return templateLocation + APP_EMAIL_TEMPLATES_PATH + PATH_SEPARATOR
                + applicationUuid + PATH_SEPARATOR + templateId;
    }

    /**
     * Handle not supported parameters.
     *
     * @param limit     Limit parameter.
     * @param offset    Offset parameter.
     * @param sortOrder Sort order parameter.
     * @param sortBy    Sort by parameter.
     */
    private void handleNotSupportedParameters(Integer limit, Integer offset, String sortOrder, String sortBy) {

        if (limit != null || offset != null) {
            throw handleError(Constants.ErrorMessage.ERROR_PAGINATION_NOT_SUPPORTED);
        }
        if (StringUtils.isNotBlank(sortOrder) || StringUtils.isNotBlank(sortBy)) {
            throw handleError(Constants.ErrorMessage.ERROR_SORTING_NOT_SUPPORTED);
        }
    }

    /**
     * Handle I18nEmailMgtException, i.e. extract error description from the exception and set to the
     * API Error Response, along with a status code to be sent in the response.
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

    /**
     * Handle API error.
     *
     * @param error Error message.
     * @return API Error object.
     */
    private APIError handleError(Constants.ErrorMessage error) {

        return new APIError(error.getHttpStatus(), getErrorBuilder(error).build());
    }

    /**
     * Get error builder.
     *
     * @param errorMsg Error message.
     * @return Error builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).
                withMessage(errorMsg.getMessage()).withDescription(errorMsg.getDescription());
    }
}

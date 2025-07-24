/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.email.template.v2.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.EmailApiService;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.core.ApplicationEmailTemplatesService;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.core.ServerEmailTemplatesService;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.factories.ApplicationEmailTemplatesServiceFactory;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.factories.ServerEmailTemplatesServiceFactory;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.SimpleEmailTemplate;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.APP_EMAIL_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_API_BASE_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATE_TYPES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.ORG_EMAIL_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.PATH_SEPARATOR;

/**
 * Implementation of the Email Templates API v2.
 */
public class EmailApiServiceImpl implements EmailApiService {

    private static final Log log = LogFactory.getLog(EmailApiServiceImpl.class);
    private final ServerEmailTemplatesService emailTemplatesService;
    private final ApplicationEmailTemplatesService applicationEmailTemplatesService;

    public EmailApiServiceImpl() {

        if (log.isDebugEnabled()) {
            log.debug("Initializing EmailApiServiceImpl");
        }
        try {
            this.emailTemplatesService = ServerEmailTemplatesServiceFactory.getServerEmailTemplatesService();
            this.applicationEmailTemplatesService = ApplicationEmailTemplatesServiceFactory
                    .getApplicationEmailTemplatesService();
            if (log.isDebugEnabled()) {
                log.debug("EmailApiServiceImpl initialized successfully");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating email template management services.", e);
            throw new RuntimeException("Error occurred while initiating email template management services.", e);
        }
    }

    @Override
    public Response addAppEmailTemplate(
            String templateTypeId, String appUuid, EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to add app email template. TemplateTypeId: %s, AppUuid: %s",
                    templateTypeId, appUuid));
        }
        SimpleEmailTemplate simpleEmailTemplate = applicationEmailTemplatesService.addEmailTemplate(templateTypeId,
                emailTemplateWithID, appUuid);
        URI headerLocation = buildURIForHeader(
                V2_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                        PATH_SEPARATOR + templateTypeId + APP_EMAIL_TEMPLATES_PATH +
                        PATH_SEPARATOR + simpleEmailTemplate.getLocale());
        return Response.created(headerLocation).entity(simpleEmailTemplate).build();
    }

    @Override
    public Response addEmailTemplateType(EmailTemplateTypeOverview emailTemplateTypeOverview) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to add email template type: %s",
                    emailTemplateTypeOverview != null ? emailTemplateTypeOverview.getDisplayName() : "null"));
        }
        EmailTemplateTypeWithID templateType = emailTemplatesService.addEmailTemplateType(emailTemplateTypeOverview);
        URI headerLocation = buildURIForHeader(
                V2_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                        PATH_SEPARATOR + templateType.getId());
        return Response.created(headerLocation).entity(templateType).build();
    }

    @Override
    public Response addOrgEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to add org email template. TemplateTypeId: %s", templateTypeId));
        }
        SimpleEmailTemplate simpleEmailTemplate = emailTemplatesService.addEmailTemplate(templateTypeId,
                emailTemplateWithID);
        URI headerLocation = buildURIForHeader(
                V2_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                        PATH_SEPARATOR + templateTypeId + ORG_EMAIL_TEMPLATES_PATH +
                        PATH_SEPARATOR + simpleEmailTemplate.getLocale());
        return Response.created(headerLocation).entity(simpleEmailTemplate).build();
    }

    @Override
    public Response deleteAppEmailTemplate(String templateTypeId, String appUuid, String locale) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to delete app email template. TemplateTypeId: %s, AppUuid: %s, " +
                    "Locale: %s", templateTypeId, appUuid, locale));
        }
        applicationEmailTemplatesService.deleteEmailTemplate(templateTypeId, locale, appUuid);
        return Response.noContent().build();
    }

    @Override
    public Response deleteEmailTemplateType(String templateTypeId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to delete email template type: %s", templateTypeId));
        }
        emailTemplatesService.deleteEmailTemplateType(templateTypeId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteOrgEmailTemplate(String templateTypeId, String locale) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to delete org email template. TemplateTypeId: %s, Locale: %s",
                    templateTypeId, locale));
        }
        emailTemplatesService.deleteEmailTemplate(templateTypeId, locale);
        return Response.noContent().build();
    }

    @Override
    public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sortOrder, String sortBy) {

        return Response.ok().entity(
                emailTemplatesService.getAllEmailTemplateTypes(limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getAppEmailTemplate(String templateTypeId, String appUuid, String locale, Boolean resolve,
                                        Integer limit, Integer offset, String sortOrder, String sortBy) {

        return Response.ok()
                .entity(applicationEmailTemplatesService.getEmailTemplate(templateTypeId, locale, appUuid, resolve,
                        limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getAppTemplatesListOfEmailTemplateType(String templateTypeId, String appUuid, Boolean resolve,
                                                           Integer limit, Integer offset, String sortOrder,
                                                           String sortBy) {

        return Response.ok()
                .entity(applicationEmailTemplatesService.getTemplatesListOfEmailTemplateType(templateTypeId, appUuid,
                        resolve, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getEmailTemplateType(String templateTypeId) {

        return Response.ok().entity(emailTemplatesService.getEmailTemplateType(templateTypeId)).build();
    }

    @Override
    public Response getOrgEmailTemplate(String templateTypeId, String locale, Integer limit, Integer offset,
                                        String sortOrder, String sortBy) {

        return Response.ok().entity(emailTemplatesService.
                getEmailTemplate(templateTypeId, locale, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getOrgTemplatesListOfEmailTemplateType(
            String templateTypeId, Integer limit, Integer offset, String sortOrder, String sortBy) {

        return Response.ok().entity(emailTemplatesService.
                getTemplatesListOfEmailTemplateType(templateTypeId, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response updateAppEmailTemplate(
            String templateTypeId, String appUuid, String locale, EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to update app email template. TemplateTypeId: %s, AppUuid: %s, " +
                    "Locale: %s", templateTypeId, appUuid, locale));
        }
        applicationEmailTemplatesService.updateEmailTemplate(templateTypeId, locale, emailTemplateWithID, appUuid);
        return Response.ok().build();
    }

    @Override
    public Response updateOrgEmailTemplate(
            String templateTypeId, String locale, EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("API request to update org email template. TemplateTypeId: %s, Locale: %s",
                    templateTypeId, locale));
        }
        emailTemplatesService.updateEmailTemplate(templateTypeId, locale, emailTemplateWithID);
        return Response.ok().build();
    }
}

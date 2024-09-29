/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.NotificationApiService;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.core.TemplateTypeService;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.core.TemplatesService;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeWithID;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.APP_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.
        Constants.NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL;
import static org.wso2.carbon.identity.api.server.notification.template.common.
        Constants.NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS;
import static org.wso2.carbon.identity.api.server.notification.template.common.
        Constants.NOTIFICATION_TEMPLATES_API_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.ORG_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.TEMPLATE_TYPES_PATH;

/**
 * This is the service implementation class for notification template management related operations.
 */
public class NotificationApiServiceImpl implements NotificationApiService {

    @Autowired
    private TemplatesService templatesService;
    @Autowired
    private TemplateTypeService templateTypeService;

    @Override
    public Response addAppEmailTemplate(String templateTypeId, String appUuid,
                                        EmailTemplateWithID emailTemplateWithID) {

        SimpleTemplate simpleEmailTemplate = templatesService.addEmailTemplate(templateTypeId,
                emailTemplateWithID, appUuid);
        URI headerLocation = buildURIForHeader(V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                        + NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL + TEMPLATE_TYPES_PATH + PATH_SEPARATOR
                        + templateTypeId + APP_TEMPLATES_PATH + PATH_SEPARATOR + appUuid + PATH_SEPARATOR
                        + simpleEmailTemplate.getLocale());
        return Response.created(headerLocation).entity(simpleEmailTemplate).build();
    }

    @Override
    public Response addAppSMSTemplate(String templateTypeId, String appUuid, SMSTemplateWithID smSTemplateWithID) {

        SimpleTemplate simpleSMSTemplate = templatesService.addSMSTemplate(templateTypeId,
                smSTemplateWithID, appUuid);
        URI headerLocation = buildURIForHeader(V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                + NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS + TEMPLATE_TYPES_PATH + PATH_SEPARATOR
                + templateTypeId + APP_TEMPLATES_PATH + PATH_SEPARATOR + appUuid + PATH_SEPARATOR
                + smSTemplateWithID.getLocale());
        return Response.created(headerLocation).entity(simpleSMSTemplate).build();
    }

    @Override
    public Response addEmailTemplateType(TemplateTypeOverview templateTypeOverview) {

        TemplateTypeWithID templateType = templateTypeService
                .addNotificationTemplateType(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeOverview);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                        + NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL + TEMPLATE_TYPES_PATH
                        + PATH_SEPARATOR + templateType.getId());
        return Response.created(headerLocation).entity(templateType).build();
    }

    @Override
    public Response addOrgEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        SimpleTemplate simpleEmailTemplate = templatesService.addEmailTemplate(templateTypeId,
                emailTemplateWithID);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                        + NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL + PATH_SEPARATOR + templateTypeId
                        + ORG_TEMPLATES_PATH + PATH_SEPARATOR + simpleEmailTemplate.getLocale());
        return Response.created(headerLocation).entity(simpleEmailTemplate).build();
    }

    @Override
    public Response addOrgSMSTemplate(String templateTypeId, SMSTemplateWithID smSTemplateWithID) {

        SimpleTemplate simpleSMSTemplate = templatesService.addSMSTemplate(templateTypeId,
                smSTemplateWithID);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                        + NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS + PATH_SEPARATOR + templateTypeId
                        + ORG_TEMPLATES_PATH + PATH_SEPARATOR + simpleSMSTemplate.getLocale());
        return Response.created(headerLocation).entity(simpleSMSTemplate).build();
    }

    @Override
    public Response addSMSTemplateType(TemplateTypeOverview templateTypeOverview) {

        TemplateTypeWithID templateType = templateTypeService
                .addNotificationTemplateType(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeOverview);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH
                        + NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS + TEMPLATE_TYPES_PATH
                        + PATH_SEPARATOR + templateType.getId());
        return Response.created(headerLocation).entity(templateType).build();
    }

    @Override
    public Response deleteAllAppEmailTemplates(String templateTypeId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteAllAppSMSTemplates(String templateTypeId, String appUuid, String locale) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteAllOrgEmailTemplates(String templateTypeId) {

        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteAllOrgSMSTemplates(String templateTypeId) {

        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteAppEmailTemplate(String templateTypeId, String appUuid, String locale) {

        templatesService.deleteEmailTemplate(templateTypeId, locale, appUuid);
        return Response.noContent().build();
    }

    @Override
    public Response deleteAppSMSTemplate(String templateTypeId, String appUuid, String locale) {

        templatesService.deleteSMSTemplate(templateTypeId, locale, appUuid);
        return Response.noContent().build();
    }

    @Override
    public Response deleteEmailTemplateType(String templateTypeId) {

        templateTypeService.deleteNotificationTemplateType(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteOrgEmailTemplate(String templateTypeId, String locale) {

        templatesService.deleteEmailTemplate(templateTypeId, locale);
        return Response.noContent().build();
    }

    @Override
    public Response deleteOrgSMSTemplate(String templateTypeId, String locale) {

        templatesService.deleteSMSTemplate(templateTypeId, locale);
        return Response.noContent().build();
    }

    @Override
    public Response deleteSMSTemplateType(String templateTypeId) {

        templateTypeService.deleteNotificationTemplateType(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeId);
        return Response.noContent().build();
    }

    @Override
    public Response getAllAppTemplatesOfEmailTemplateType(String templateTypeId, String appUuid) {

        return Response.ok().entity(
                templatesService.getAllTemplatesOfTemplateType(templateTypeId, appUuid,
                        Constants.NOTIFICATION_CHANNEL_EMAIL)).build();
    }

    @Override
    public Response getAllAppTemplatesOfSMSTemplateType(String templateTypeId, String appUuid) {

        return Response.ok().entity(
                templatesService.getAllTemplatesOfTemplateType(templateTypeId, appUuid,
                        Constants.NOTIFICATION_CHANNEL_SMS)).build();
    }

    @Override
    public Response getAllEmailTemplateTypes() {

        return Response.ok().entity(templateTypeService
                .getAllNotificationTemplateTypes(Constants.NOTIFICATION_CHANNEL_EMAIL)).build();
    }

    @Override
    public Response getAllOrgTemplatesOfEmailTemplateType(String templateTypeId) {

        return Response.ok().entity(templatesService.getAllTemplatesOfTemplateType(templateTypeId,
                        Constants.NOTIFICATION_CHANNEL_EMAIL)).build();
    }

    @Override
    public Response getAllOrgTemplatesOfSMSTemplateType(String templateTypeId) {

        return Response.ok().entity(templatesService.getAllTemplatesOfTemplateType(templateTypeId,
                        Constants.NOTIFICATION_CHANNEL_SMS)).build();
    }

    @Override
    public Response getAllSMSTemplateTypes() {

        return Response.ok().entity(templateTypeService
                .getAllNotificationTemplateTypes(Constants.NOTIFICATION_CHANNEL_SMS)).build();
    }

    @Override
    public Response getAllSystemTemplatesOfEmailTemplateType(String templateTypeId) {

        return Response.ok().entity(templatesService
                .getAllSystemTemplatesOfTemplateType(templateTypeId, Constants.NOTIFICATION_CHANNEL_EMAIL)).build();
    }

    @Override
    public Response getAllSystemTemplatesOfSMSTemplateType(String templateTypeId) {

        return Response.ok().entity(templatesService
                .getAllSystemTemplatesOfTemplateType(templateTypeId, Constants.NOTIFICATION_CHANNEL_SMS)).build();
    }

    @Override
    public Response getAppEmailTemplate(String templateTypeId, String appUuid, String locale) {

        return Response.ok().entity(templatesService.getEmailTemplate(templateTypeId, locale, appUuid))
                .build();
    }

    @Override
    public Response getAppSMSTemplate(String templateTypeId, String appUuid, String locale) {

        return Response.ok().entity(templatesService.getSMSTemplate(templateTypeId, locale, appUuid))
                .build();
    }

    @Override
    public Response getEmailTemplateType(String templateTypeId) {

        return Response.ok().entity(templateTypeService.getNotificationTemplateType(
                Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeId)).build();
    }

    @Override
    public Response getOrgEmailTemplate(String templateTypeId, String locale) {

        return Response.ok().entity(templatesService.getEmailTemplate(templateTypeId, locale)).build();
    }

    @Override
    public Response getOrgSMSTemplate(String templateTypeId, String locale) {

        return Response.ok().entity(templatesService.getSMSTemplate(templateTypeId, locale)).build();
    }

    @Override
    public Response getSMSTemplateType(String templateTypeId) {

        return Response.ok().entity(templateTypeService.getNotificationTemplateType(Constants.NOTIFICATION_CHANNEL_SMS,
                templateTypeId)).build();
    }

    @Override
    public Response getSystemEmailTemplate(String templateTypeId, String locale) {

        return Response.ok().entity(templatesService.getSystemEmailTemplate(templateTypeId, locale)).build();
    }

    @Override
    public Response getSystemSMSTemplate(String templateTypeId, String locale) {

        return Response.ok().entity(templatesService.getSystemSmsTemplate(templateTypeId, locale)).build();
    }

    @Override
    public Response updateAppEmailTemplate(String templateTypeId, String appUuid, String locale,
                                           EmailTemplate emailTemplate) {

        templatesService.updateEmailTemplate(templateTypeId, locale, emailTemplate, appUuid);
        return Response.ok().build();
    }

    @Override
    public Response updateAppSMSTemplate(String templateTypeId, String appUuid, String locale,
                                         SMSTemplate smsTemplate) {

        templatesService.updateSMSTemplate(templateTypeId, locale, smsTemplate, appUuid);
        return Response.ok().build();
    }

    @Override
    public Response updateOrgEmailTemplate(String templateTypeId, String locale, EmailTemplate emailTemplate) {

        templatesService.updateEmailTemplate(templateTypeId, locale, emailTemplate);
        return Response.ok().build();
    }

    @Override
    public Response updateOrgSMSTemplate(String templateTypeId, String locale, SMSTemplate smsTemplate) {

        templatesService.updateSMSTemplate(templateTypeId, locale, smsTemplate);
        return Response.ok().build();
    }
}

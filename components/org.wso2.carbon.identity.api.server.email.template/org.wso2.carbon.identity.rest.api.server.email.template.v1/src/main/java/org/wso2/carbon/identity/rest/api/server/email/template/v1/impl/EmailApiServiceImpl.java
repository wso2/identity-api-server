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

package org.wso2.carbon.identity.rest.api.server.email.template.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.EmailApiService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.core.ServerEmailTemplatesService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.factories.ServerEmailTemplatesServiceFactory;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithoutTemplates;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.SimpleEmailTemplate;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_API_BASE_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATE_TYPES_PATH;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.PATH_SEPARATOR;

/**
 * Implementation of the Email Templates API.
 */
public class EmailApiServiceImpl implements EmailApiService {

    private static final Log log = LogFactory.getLog(EmailApiServiceImpl.class);
    private final ServerEmailTemplatesService emailTemplatesService;

    public EmailApiServiceImpl() {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Initializing EmailApiServiceImpl");
            }
            this.emailTemplatesService = ServerEmailTemplatesServiceFactory.getServerEmailTemplatesService();
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating email template manager service", e);
            throw new RuntimeException("Error occurred while initiating email template manager service.", e);
        }
    }

    @Override
    public Response addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: addEmailTemplate for templateTypeId: " + templateTypeId);
        }
        SimpleEmailTemplate simpleEmailTemplate = emailTemplatesService.addEmailTemplate(templateTypeId,
                emailTemplateWithID);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                PATH_SEPARATOR + templateTypeId + EMAIL_TEMPLATES_PATH + PATH_SEPARATOR + simpleEmailTemplate.getId());
        return Response.created(headerLocation).entity(simpleEmailTemplate).build();
    }

    @Override
    public Response addEmailTemplateType(EmailTemplateType emailTemplateType) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: addEmailTemplateType for displayName: " + 
                    (emailTemplateType != null ? emailTemplateType.getDisplayName() : null));
        }
        EmailTemplateTypeWithoutTemplates templateType = emailTemplatesService.addEmailTemplateType(emailTemplateType);
        URI headerLocation = buildURIForHeader(
                V1_API_PATH_COMPONENT + EMAIL_TEMPLATES_API_BASE_PATH + EMAIL_TEMPLATE_TYPES_PATH +
                        PATH_SEPARATOR + templateType.getId());
        return Response.created(headerLocation).entity(templateType).build();
    }

    @Override
    public Response deleteEmailTemplate(String templateTypeId, String templateId) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: deleteEmailTemplate for templateTypeId: " + templateTypeId + 
                    ", templateId: " + templateId);
        }
        emailTemplatesService.deleteEmailTemplate(templateTypeId, templateId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteEmailTemplateType(String templateTypeId) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: deleteEmailTemplateType for templateTypeId: " + templateTypeId);
        }
        emailTemplatesService.deleteEmailTemplateType(templateTypeId);
        return Response.noContent().build();
    }

    @Override
    public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sortOrder, String sortBy,
                                             String requiredAttributes) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: getAllEmailTemplateTypes");
        }
        return Response.ok().entity(emailTemplatesService
                .getAllEmailTemplateTypes(limit, offset, sortOrder, sortBy, requiredAttributes)).build();
    }

    @Override
    public Response getEmailTemplate(String templateTypeId, String templateId, Integer limit, Integer offset,
                                     String sortOrder, String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: getEmailTemplate for templateTypeId: " + templateTypeId + 
                    ", templateId: " + templateId);
        }
        return Response.ok().entity(emailTemplatesService.
                getEmailTemplate(templateTypeId, templateId, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sortOrder,
                                         String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: getEmailTemplateType for templateTypeId: " + templateTypeId);
        }
        return Response.ok().entity(emailTemplatesService.
                getEmailTemplateType(templateTypeId, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response getTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit, Integer offset,
                                                        String sortOrder, String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: getTemplatesListOfEmailTemplateType for templateTypeId: " + templateTypeId);
        }
        return Response.ok().entity(emailTemplatesService.
                getTemplatesListOfEmailTemplateType(templateTypeId, limit, offset, sortOrder, sortBy)).build();
    }

    @Override
    public Response updateEmailTemplate(String templateTypeId, String templateId,
                                        EmailTemplateWithID emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: updateEmailTemplate for templateTypeId: " + templateTypeId + 
                    ", templateId: " + templateId);
        }
        emailTemplatesService.updateEmailTemplate(templateTypeId, templateId, emailTemplateWithID);
        return Response.ok().build();
    }

    @Override
    public Response updateEmailTemplateType(String templateTypeId, List<EmailTemplateWithID> emailTemplateWithID) {

        if (log.isDebugEnabled()) {
            log.debug("REST API call: updateEmailTemplateType for templateTypeId: " + templateTypeId + 
                    " with " + (emailTemplateWithID != null ? emailTemplateWithID.size() : 0) + " templates");
        }
        emailTemplatesService.updateEmailTemplateType(templateTypeId, emailTemplateWithID);
        return Response.ok().build();
    }
}

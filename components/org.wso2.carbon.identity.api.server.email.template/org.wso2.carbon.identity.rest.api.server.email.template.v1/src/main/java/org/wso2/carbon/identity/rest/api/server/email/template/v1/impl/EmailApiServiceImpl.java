/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.server.email.template.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.EmailApiService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.core.ServerEmailTemplatesService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Implementation of the Email Templates API.
 */
public class EmailApiServiceImpl implements EmailApiService {

    @Autowired
    private ServerEmailTemplatesService emailTemplatesService;

    @Override
    public Response addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        return Response.ok().entity(emailTemplatesService.addEmailTemplate(templateTypeId, emailTemplateWithID)).
                build();
    }

    @Override
    public Response addEmailTemplateType(EmailTemplateType emailTemplateType) {

        return Response.ok().entity(emailTemplatesService.addEmailTemplateType(emailTemplateType)).build();
    }

    @Override
    public Response deleteEmailTemplate(String templateTypeId, String templateId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteEmailTemplateType(String templateTypeId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sort, String sortBy) {

        return Response.ok().entity(emailTemplatesService.getAllEmailTemplateTypes(limit, offset, sort, sortBy)).
                build();
    }

    @Override
    public Response getEmailTemplate(String templateTypeId, String templateId, Integer limit, Integer offset,
                                     String sort, String sortBy) {

        return Response.ok().entity(emailTemplatesService.
                getEmailTemplate(templateTypeId, templateId, limit, offset, sort, sortBy)).build();
    }

    @Override
    public Response getEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sort,
                                         String sortBy) {

        return Response.ok().entity(emailTemplatesService.
                getEmailTemplateType(templateTypeId, limit, offset, sort, sortBy)).build();
    }

    @Override
    public Response getTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit, Integer offset,
                                                        String sort, String sortBy) {

        return Response.ok().entity(emailTemplatesService.
                getTemplatesListOfEmailTemplateType(templateTypeId, limit, offset, sort, sortBy)).build();
    }

    @Override
    public Response updateEmailTemplate(String templateTypeId, String templateId,
                                        EmailTemplateWithID emailTemplateWithID) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateEmailTemplateType(String templateTypeId, List<EmailTemplateWithID> emailTemplateWithID) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}

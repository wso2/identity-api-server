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
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateRequestDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateTypeDTO;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Implementation of the Email API Service.
 */
public class EmailTemplatesApiServiceImpl extends EmailApiService {

    @Autowired
    private ServerEmailTemplatesService emailTemplatesService;

    @Override
    public Response addEmailTemplate(String emailTemplateTypeId, CompleteEmailTemplateRequestDTO type) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response addEmailTemplateType(CompleteEmailTemplateTypeDTO type) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteEmailTemplate(String emailTemplateTypeId, String localeCode) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteEmailTemplateType(String emailTemplateTypeId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getAllEmailTemplateTypeLocale(String emailTemplateTypeId, String localeCode, Integer limit,
                                                  Integer offset, String sort, String sortBy) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sort, String sortBy) {

        return Response.ok().entity(emailTemplatesService.getAllEmailTemplateTypes(limit, offset, sort, sortBy)).
                build();
    }

    @Override
    public Response getEmailTemplateType(String emailTemplateTypeId, Integer limit, Integer offset, String sort,
                                         String sortBy) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getEmailTemplateTypeForLocale(String emailTemplateTypeId, Integer limit, Integer offset,
                                                  String sort, String sortBy) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateEmailTemplate(String emailTemplateTypeId, String localeCode,
                                        CompleteEmailTemplateRequestDTO templates) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateEmailTemplateType(String emailTemplateTypeId,
                                            List<CompleteEmailTemplateRequestDTO> templates) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}

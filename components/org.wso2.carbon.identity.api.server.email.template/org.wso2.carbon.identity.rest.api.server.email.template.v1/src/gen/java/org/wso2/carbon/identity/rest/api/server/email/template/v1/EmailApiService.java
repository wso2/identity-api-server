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

package org.wso2.carbon.identity.rest.api.server.email.template.v1;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.*;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithoutTemplates;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.SimpleEmailTemplate;
import javax.ws.rs.core.Response;


public interface EmailApiService {

      public Response addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID);

      public Response addEmailTemplateType(EmailTemplateType emailTemplateType);

      public Response deleteEmailTemplate(String templateTypeId, String templateId);

      public Response deleteEmailTemplateType(String templateTypeId);

      public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getEmailTemplate(String templateTypeId, String templateId, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response updateEmailTemplate(String templateTypeId, String templateId, EmailTemplateWithID emailTemplateWithID);

      public Response updateEmailTemplateType(String templateTypeId, List<EmailTemplateWithID> emailTemplateWithID);
}

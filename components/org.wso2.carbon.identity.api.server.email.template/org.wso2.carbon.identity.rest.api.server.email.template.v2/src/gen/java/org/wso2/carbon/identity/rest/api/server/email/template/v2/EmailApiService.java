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

package org.wso2.carbon.identity.rest.api.server.email.template.v2;

import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v2.model.EmailTemplateWithID;

import javax.ws.rs.core.Response;
import java.util.List;


public interface EmailApiService {

      public Response addAppEmailTemplate(String templateTypeId, String appUuid, EmailTemplateWithID emailTemplateWithID);

      public Response addEmailTemplateType(EmailTemplateType emailTemplateType);

      public Response addOrgEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID);

      public Response deleteAppEmailTemplate(String templateTypeId, String appUuid, String locale);

      public Response deleteEmailTemplateType(String templateTypeId);

      public Response deleteOrgEmailTemplate(String templateTypeId, String locale);

      public Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sortOrder, String sortBy, String requiredAttributes);

      public Response getAppEmailTemplate(String templateTypeId, String appUuid, String locale, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getAppTemplatesListOfEmailTemplateType(String templateTypeId, String appUuid, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getOrgEmailTemplate(String templateTypeId, String locale, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response getOrgTemplatesListOfEmailTemplateType(String templateTypeId, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response updateAppEmailTemplate(String templateTypeId, String appUuid, String locale, EmailTemplateWithID emailTemplateWithID);

      public Response updateEmailTemplateType(String templateTypeId, List<EmailTemplateWithID> emailTemplateWithID);

      public Response updateOrgEmailTemplate(String templateTypeId, String locale, EmailTemplateWithID emailTemplateWithID);
}

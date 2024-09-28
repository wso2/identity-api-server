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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1;

import org.wso2.carbon.identity.rest.api.server.notification.template.v1.*;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.Error;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeWithID;
import javax.ws.rs.core.Response;


public interface NotificationApiService {

      public Response addAppEmailTemplate(String templateTypeId, String appUuid, EmailTemplateWithID emailTemplateWithID);

      public Response addAppSMSTemplate(String templateTypeId, String appUuid, SMSTemplateWithID smSTemplateWithID);

      public Response addEmailTemplateType(TemplateTypeOverview templateTypeOverview);

      public Response addOrgEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID);

      public Response addOrgSMSTemplate(String templateTypeId, SMSTemplateWithID smSTemplateWithID);

      public Response addSMSTemplateType(TemplateTypeOverview templateTypeOverview);

      public Response deleteAllOrgEmailTemplates(String templateTypeId);

      public Response deleteAllOrgSMSTemplates(String templateTypeId);

      public Response deleteAppEmailTemplate(String templateTypeId, String appUuid, String locale);

      public Response deleteAppEmailTemplates(String templateTypeId);

      public Response deleteAppSMSTemplate(String templateTypeId, String appUuid, String locale);

      public Response deleteAppSMSTemplates(String templateTypeId, String appUuid, String locale);

      public Response deleteEmailTemplateType(String templateTypeId);

      public Response deleteOrgEmailTemplate(String templateTypeId, String locale);

      public Response deleteOrgSMSTemplate(String templateTypeId, String locale);

      public Response deleteSMSTemplateType(String templateTypeId);

      public Response getAllEmailTemplateTypes();

      public Response getAllSMSTemplateTypes();

      public Response getAppEmailTemplate(String templateTypeId, String appUuid, String locale);

      public Response getAppSMSTemplate(String templateTypeId, String appUuid, String locale);

      public Response getAppTemplatesListOfEmailTemplateType(String templateTypeId, String appUuid);

      public Response getAppTemplatesListOfSMSTemplateType(String templateTypeId, String appUuid);

      public Response getEmailTemplateType(String templateTypeId);

      public Response getOrgEmailTemplate(String templateTypeId, String locale);

      public Response getOrgSMSTemplate(String templateTypeId, String locale);

      public Response getOrgTemplatesListOfEmailTemplateType(String templateTypeId);

      public Response getOrgTemplatesListOfSMSTemplateType(String templateTypeId);

      public Response getSMSTemplateType(String templateTypeId);

      public Response getSystemEmailTemplate(String templateTypeId, String locale);

      public Response getSystemSMSTemplate(String templateTypeId, String locale);

      public Response getSystemTemplatesListOfEmailTemplateType(String templateTypeId);

      public Response getSystemTemplatesListOfSMSTemplateType(String templateTypeId);

      public Response updateAppEmailTemplate(String templateTypeId, String appUuid, String locale, EmailTemplate emailTemplate);

      public Response updateAppSMSTemplate(String templateTypeId, String appUuid, String locale, SMSTemplate smSTemplate);

      public Response updateOrgEmailTemplate(String templateTypeId, String locale, EmailTemplate emailTemplate);

      public Response updateOrgSMSTemplate(String templateTypeId, String locale, SMSTemplate smSTemplate);
}

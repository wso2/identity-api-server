/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.notification.sender.v1;

import org.wso2.carbon.identity.api.server.notification.sender.v1.*;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Error;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;
import javax.ws.rs.core.Response;


public interface NotificationSendersApiService {

      public Response createEmailSender(EmailSenderAdd emailSenderAdd);

      public Response createSMSSender(SMSSenderAdd smSSenderAdd);

      public Response deleteEmailSender(String senderName);

      public Response deleteSMSSender(String senderName);

      public Response getEmailSender(String senderName);

      public Response getEmailSenders();

      public Response getSMSSender(String senderName);

      public Response getSMSSenders();

      public Response updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest);

      public Response updateSMSSender(String senderName, SMSSenderUpdateRequest smSSenderUpdateRequest);
}

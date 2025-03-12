/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.authenticators.v1;

import org.wso2.carbon.identity.api.server.authenticators.v1.*;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Error;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.SystemLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;
import javax.ws.rs.core.Response;


public interface AuthenticatorsApiService {

      public Response addUserDefinedLocalAuthenticator(UserDefinedLocalAuthenticatorCreation userDefinedLocalAuthenticatorCreation);

      public Response authenticatorsGet(String filter, Integer limit, Integer offset);

      public Response authenticatorsMetaTagsGet();

      public Response deleteUserDefinedLocalAuthenticator(String authenticatorId);

      public Response getAllSystemLocalAuthenticators(String filter, Integer limit, Integer offset);

      public Response getConnectedAppsOfLocalAuthenticator(String authenticatorId, Integer limit, Integer offset);

      public Response getSystemLocalAuthenticatorById(String authenticatorId, Integer limit, Integer offset);

      public Response updateSystemLocalAuthenticatorAmrValueById(String authenticatorId, SystemLocalAuthenticatorUpdate systemLocalAuthenticatorUpdate);

      public Response updateUserDefinedLocalAuthenticator(String authenticatorId, UserDefinedLocalAuthenticatorUpdate userDefinedLocalAuthenticatorUpdate);

}

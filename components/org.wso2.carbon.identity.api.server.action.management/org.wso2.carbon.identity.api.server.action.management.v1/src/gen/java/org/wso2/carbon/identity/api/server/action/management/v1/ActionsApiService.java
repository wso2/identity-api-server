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

package org.wso2.carbon.identity.api.server.action.management.v1;

import org.wso2.carbon.identity.api.server.action.management.v1.*;
import org.wso2.carbon.identity.api.server.action.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.Error;
import javax.ws.rs.core.Response;


public interface ActionsApiService {

      public Response activateAction(String actionType, String actionId);

      public Response createAction(String actionType, ActionModel actionModel);

      public Response deactivateAction(String actionType, String actionId);

      public Response deleteAction(String actionType, String actionId);

      public Response getActionsByActionType(String actionType);

      public Response getActions();

      public Response updateAction(String actionType, String actionId, ActionModel actionModel);
}

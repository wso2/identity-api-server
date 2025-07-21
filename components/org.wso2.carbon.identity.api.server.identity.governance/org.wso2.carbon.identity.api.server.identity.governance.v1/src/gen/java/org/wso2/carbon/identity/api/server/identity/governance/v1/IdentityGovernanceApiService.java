/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.identity.governance.v1;

import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceSearchAttribute;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.MultipleConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyRevertReq;

import java.util.List;

import javax.ws.rs.core.Response;


public interface IdentityGovernanceApiService {

      public Response getCategories(Integer limit, Integer offset, String filter, String sort);

      public Response getConnector(String categoryId, String connectorId);

      public Response getConnectorCategory(String categoryId);

      public Response getConnectorsOfCategory(String categoryId);

      public Response getPreferenceByPost(List<PreferenceSearchAttribute> preferenceSearchAttribute);

      public Response patchConnector(String categoryId, String connectorId, ConnectorsPatchReq connectorsPatchReq);

      public Response patchConnectorsOfCategory(String categoryId, MultipleConnectorsPatchReq multipleConnectorsPatchReq);

      public Response revertConnectorProperties(String categoryId, String connectorId, PropertyRevertReq propertyRevertReq);
}

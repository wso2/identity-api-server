/*
 * Copyright (c) 2023-2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1;

import org.wso2.carbon.identity.api.server.configs.v1.model.*;

import java.util.List;

import org.wso2.carbon.identity.api.server.configs.v1.model.CompatibilitySettings;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthOAuth2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthPassiveSTSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthSAML2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTKeyValidatorPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfigListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;

import javax.ws.rs.core.Response;


public interface ConfigsApiService {

      public Response deleteOAuth2InboundAuthConfig();

      public Response deletePassiveSTSInboundAuthConfig();

      public Response deleteSAMLInboundAuthConfig();

      public Response getAuthenticator(String authenticatorId);

      public Response getCORSConfiguration();

      public Response getConfigs();

      public Response getFraudDetectionConfigs();

      public Response getHomeRealmIdentifiers();

      public Response getImpersonationConfiguration();

      public Response getInboundScimConfigs();

      public Response getIssuerUsageScopeConfig();

      public Response getOAuth2InboundAuthConfig();

      public Response getPassiveSTSInboundAuthConfig();

      public Response getPrivatKeyJWTValidationConfiguration();
      public Response getDCRConfiguration();

      public Response getRemoteLoggingConfig(String logType);

      public Response getRemoteLoggingConfigs();

      public Response getSAMLInboundAuthConfig();

      public Response getSchema(String schemaId);

      public Response getSchemas();

      public Response listAuthenticators(String type);

      public Response patchCORSConfiguration(List<CORSPatch> coRSPatch);

      public Response patchConfigs(List<Patch> patch);

      public Response patchImpersonationConfiguration(List<ImpersonationPatch> impersonationPatch);

      public Response patchPrivatKeyJWTValidationConfiguration(List<JWTKeyValidatorPatch> jwTKeyValidatorPatch);

      public Response patchDCRConfiguration(List<DCRPatch> patch);

      public Response restoreServerRemoteLoggingConfiguration(String logType);

      public Response restoreServerRemoteLoggingConfigurations();

      public Response deleteImpersonationConfiguration();

      public Response updateFraudDetectionConfigs(FraudDetectionConfig fraudDetectionConfig);

      public Response updateInboundScimConfigs(ScimConfig scimConfig);

      public Response updateIssuerUsageScopeConfig(UsageScopePatch usageScopePatch);

      public Response updateOAuth2InboundAuthConfig(InboundAuthOAuth2Config inboundAuthOAuth2Config);

      public Response updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig);

      public Response updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig);

      public Response updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem);

      public Response updateSAMLInboundAuthConfig(InboundAuthSAML2Config inboundAuthSAML2Config);

      public Response getCompatibilitySettings();

      public Response patchCompatibilitySettings(CompatibilitySettings compatibilitySettings);

      public Response getCompatibilitySettingsByGroup(String settingGroup);
}

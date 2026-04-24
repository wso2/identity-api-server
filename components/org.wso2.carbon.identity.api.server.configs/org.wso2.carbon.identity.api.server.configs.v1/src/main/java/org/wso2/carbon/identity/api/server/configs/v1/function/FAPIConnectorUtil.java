/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1.function;

import org.wso2.carbon.identity.api.server.configs.v1.model.FapiConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.FapiProfile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class for converting between the API-layer FAPI model and the OAuth-bundle FAPI model.
 */
public class FAPIConnectorUtil {

    private FAPIConnectorUtil() {
        // Utility class — no instantiation.
    }

    /**
     * Convert an OAuth-bundle {@link org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig}
     * to the API-layer {@link FapiConfig}.
     *
     * @param oauthModel the OAuth bundle model; may be null.
     * @return the API model; never null.
     */
    public static FapiConfig toApiModel(
            org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig oauthModel) {

        FapiConfig apiModel = new FapiConfig();
        if (oauthModel == null) {
            apiModel.setEnabled(false);
            apiModel.setSupportedProfiles(Collections.emptyList());
            return apiModel;
        }
        apiModel.setEnabled(oauthModel.isEnabled());
        List<org.wso2.carbon.identity.oauth2.fapi.models.FapiProfileEnum> oauthProfiles =
                oauthModel.getSupportedProfiles();
        if (oauthProfiles == null || oauthProfiles.isEmpty()) {
            apiModel.setSupportedProfiles(Collections.emptyList());
        } else {
            apiModel.setSupportedProfiles(
                    oauthProfiles.stream()
                            .map(p -> FapiProfile.fromValue(p.value()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
        }
        return apiModel;
    }

    /**
     * Convert an API-layer {@link FapiConfig} to the OAuth-bundle
     * {@link org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig}.
     *
     * @param apiModel the API layer model; may be null.
     * @return the OAuth model; never null.
     */
    public static org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig toOAuthModel(FapiConfig apiModel) {

        org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig oauthModel =
                new org.wso2.carbon.identity.oauth2.fapi.models.FapiConfig();
        if (apiModel == null) {
            return oauthModel;
        }
        oauthModel.setEnabled(Boolean.TRUE.equals(apiModel.getEnabled()));
        List<FapiProfile> apiProfiles = apiModel.getSupportedProfiles();
        if (apiProfiles == null || apiProfiles.isEmpty()) {
            oauthModel.setSupportedProfiles(Collections.emptyList());
        } else {
            oauthModel.setSupportedProfiles(
                    apiProfiles.stream()
                            .map(p -> org.wso2.carbon.identity.oauth2.fapi.models.FapiProfileEnum.fromValue(
                                    p.value()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
        }
        return oauthModel;
    }
}

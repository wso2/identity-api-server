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

package org.wso2.carbon.identity.api.server.configs.common;

import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;

import java.util.List;

/**
 * Utility class for Configs Service.
 */
public class Utils {

    private Utils() {}

    /**
     * Get external claims for a given external claim dialect.
     *
     * @param externalClaimDialect External claim dialect.
     * @return List of external claims.
     * @throws ClaimMetadataException If an error occurs while retrieving external claims.
     */
    public static List<ExternalClaim> getExternalClaims(String externalClaimDialect, String tenantDomain)
            throws ClaimMetadataException {

        try {
            return ConfigsServiceHolder.getClaimMetadataManagementService().
                    getExternalClaims(externalClaimDialect, tenantDomain);
        } catch (ClaimMetadataException e) {
            throw new ClaimMetadataException("Error while retrieving external claims.", e);
        }
    }
}

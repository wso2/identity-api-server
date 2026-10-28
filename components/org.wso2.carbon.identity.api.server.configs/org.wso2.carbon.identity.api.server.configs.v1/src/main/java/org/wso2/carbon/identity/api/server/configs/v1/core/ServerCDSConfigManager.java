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

package org.wso2.carbon.identity.api.server.configs.v1.core;

import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.v1.exception.CDSConfigClientException;
import org.wso2.carbon.identity.api.server.configs.v1.exception.CDSConfigException;
import org.wso2.carbon.identity.api.server.configs.v1.exception.CDSConfigServerException;
import org.wso2.carbon.identity.api.server.configs.v1.model.CDSConfig;
import org.wso2.identity.cds.config.mgt.constant.CDSConfigMgtConstants;
import org.wso2.identity.cds.config.mgt.exception.CDSConfigMgtClientException;
import org.wso2.identity.cds.config.mgt.exception.CDSConfigMgtException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the Customer Data Service (CDS) configuration with the CDSConfigService of the CDS extensions.
 * The CDS extensions are optional. The JVM loads the exception types of a class when it verifies the class, so only
 * this class refers to the classes of the CDS extensions. If the extensions are not in the pack, the other classes of
 * the configs API still load.
 */
final class ServerCDSConfigManager {

    /**
     * Maps the error codes of the CDS config service to the error codes of the configs API.
     */
    private static final Map<String, Constants.ErrorMessage> ERROR_CODE_MAP = new HashMap<>();

    static {
        ERROR_CODE_MAP.put(CDSConfigMgtConstants.ErrorMessages.ERROR_CODE_CDS_NOT_ENABLED.getCode(),
                Constants.ErrorMessage.ERROR_CODE_CDS_NOT_ENABLED);
        ERROR_CODE_MAP.put(CDSConfigMgtConstants.ErrorMessages.ERROR_CODE_INVALID_CDS_CONFIG.getCode(),
                Constants.ErrorMessage.ERROR_CODE_INVALID_CDS_CONFIG);
        ERROR_CODE_MAP.put(CDSConfigMgtConstants.ErrorMessages.ERROR_CODE_CDS_CONFIG_RETRIEVE.getCode(),
                Constants.ErrorMessage.ERROR_CODE_CDS_CONFIG_RETRIEVE);
        ERROR_CODE_MAP.put(CDSConfigMgtConstants.ErrorMessages.ERROR_CODE_CDS_CONFIG_UPDATE.getCode(),
                Constants.ErrorMessage.ERROR_CODE_CDS_CONFIG_UPDATE);
        ERROR_CODE_MAP.put(CDSConfigMgtConstants.ErrorMessages.ERROR_CODE_CDS_CONFIG_SYNC.getCode(),
                Constants.ErrorMessage.ERROR_CODE_CDS_CONFIG_SYNC);
    }

    private ServerCDSConfigManager() {

    }

    /**
     * Get the CDS configuration of the tenant.
     *
     * @param tenantDomain Tenant domain.
     * @return CDS configuration of the tenant.
     * @throws CDSConfigException If the configuration cannot be read.
     */
    static CDSConfig getCDSConfig(String tenantDomain) throws CDSConfigException {

        try {
            return new CDSConfig().enabled(
                    ConfigsServiceHolder.getCDSConfigService().getCDSConfig(tenantDomain).isEnabled());
        } catch (CDSConfigMgtException e) {
            throw toCDSConfigException(e);
        }
    }

    /**
     * Update the CDS configuration of the tenant.
     *
     * @param cdsConfig    CDS configuration.
     * @param tenantDomain Tenant domain of a root organization.
     * @throws CDSConfigException If the configuration cannot be updated.
     */
    static void updateCDSConfig(CDSConfig cdsConfig, String tenantDomain) throws CDSConfigException {

        try {
            ConfigsServiceHolder.getCDSConfigService().updateCDSConfig(
                    new org.wso2.identity.cds.config.mgt.model.CDSConfig(cdsConfig.getEnabled()), tenantDomain);
        } catch (CDSConfigMgtException e) {
            throw toCDSConfigException(e);
        }
    }

    /**
     * Convert an exception of the CDS config service to an exception of the configs API. If the error code has no
     * mapping, the exception has no error code and the API uses its default error code.
     */
    private static CDSConfigException toCDSConfigException(CDSConfigMgtException e) {

        Constants.ErrorMessage error = ERROR_CODE_MAP.get(e.getErrorCode());
        String errorCode = error != null ? error.code() : null;
        if (e instanceof CDSConfigMgtClientException) {
            return new CDSConfigClientException(e.getMessage(), errorCode, e);
        }
        return new CDSConfigServerException(e.getMessage(), errorCode, e);
    }
}

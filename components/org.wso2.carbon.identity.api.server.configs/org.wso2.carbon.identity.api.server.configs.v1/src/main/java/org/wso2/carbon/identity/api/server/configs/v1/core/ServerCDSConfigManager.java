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
import org.wso2.carbon.identity.api.server.configs.v1.exception.CDSConfigException;
import org.wso2.identity.cds.client.CDSClientException;

/**
 * Manages the Customer Data Service (CDS) configuration with the CDSConfigService of the CDS extensions.
 * The CDS extensions are optional. The JVM loads the exception types of a class when it verifies the class, so only
 * this class refers to CDSClientException. If the extensions are not in the pack, the first call to this class gives
 * NoClassDefFoundError, and the other classes of the configs API still load.
 */
final class ServerCDSConfigManager {

    private ServerCDSConfigManager() {

    }

    /**
     * Check whether the CDSConfigService is active.
     *
     * @return True if the CDSConfigService is active.
     */
    static boolean isServiceAvailable() {

        return ConfigsServiceHolder.getCDSConfigService() != null;
    }

    /**
     * Get the effective value of the CDS enable config of the tenant.
     *
     * @param tenantDomain Tenant domain.
     * @return True if CDS is enabled for the tenant.
     * @throws CDSConfigException If the value cannot be read.
     */
    static boolean isCDSEnabled(String tenantDomain) throws CDSConfigException {

        try {
            return ConfigsServiceHolder.getCDSConfigService().isCDSEnabled(tenantDomain);
        } catch (CDSClientException e) {
            throw new CDSConfigException(e.getMessage(), e.isClientError(), e);
        }
    }

    /**
     * Update the CDS enable config of the tenant. The CDS extensions send the value to CDS first, and then store it.
     *
     * @param tenantDomain Tenant domain of a root organization.
     * @param cdsEnabled   The new value.
     * @throws CDSConfigException If CDS does not accept the value, or if the value cannot be stored.
     */
    static void updateCDSEnabled(String tenantDomain, boolean cdsEnabled) throws CDSConfigException {

        try {
            ConfigsServiceHolder.getCDSConfigService().updateCDSEnabled(tenantDomain, cdsEnabled);
        } catch (CDSClientException e) {
            throw new CDSConfigException(e.getMessage(), e.isClientError(), e);
        }
    }
}

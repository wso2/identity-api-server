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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.identity.cds.client.CDSClientException;
import org.wso2.identity.cds.client.config.CDSConfigService;

/**
 * Calls the CDSConfigService of the CDS extensions (wso2-extensions/identity-customer-data-service-extensions).
 * The extensions are optional dropins. Only this class refers to their classes, so that the other configs APIs
 * work without them. If the extensions are not in the pack, a call to this class gives NoClassDefFoundError.
 */
final class CDSConfigServiceAdapter {

    private CDSConfigServiceAdapter() {

    }

    /**
     * @return true if the CDS config service is registered.
     */
    static boolean isServiceAvailable() {

        return getService() != null;
    }

    static boolean isCDSEnabled(String tenantDomain) throws CDSConfigException {

        try {
            return getService().isCDSEnabled(tenantDomain);
        } catch (CDSClientException e) {
            throw new CDSConfigException(e.getMessage(), e.isClientError(), e);
        }
    }

    static void updateCDSEnabled(String tenantDomain, boolean cdsEnabled) throws CDSConfigException {

        try {
            getService().updateCDSEnabled(tenantDomain, cdsEnabled);
        } catch (CDSClientException e) {
            throw new CDSConfigException(e.getMessage(), e.isClientError(), e);
        }
    }

    private static CDSConfigService getService() {

        return (CDSConfigService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(CDSConfigService.class, null);
    }

    /**
     * Error from the CDS config service. This class does not refer to the classes of the extensions.
     */
    static class CDSConfigException extends Exception {

        private static final long serialVersionUID = -2934718406521735126L;
        private final boolean clientError;

        CDSConfigException(String message, boolean clientError, Throwable cause) {

            super(message, cause);
            this.clientError = clientError;
        }

        /**
         * @return true if CDS refused the value because of the request content.
         */
        boolean isClientError() {

            return clientError;
        }
    }
}

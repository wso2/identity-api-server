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

package org.wso2.carbon.identity.api.server.certificate.validation.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.x509Certificate.validation.service.CertificateValidationManagementService;

/**
 * Service holder class for server configuration related services.
 */
public class CertificateValidationManagementServiceHolder {

    private static final Log LOG = LogFactory.getLog(CertificateValidationManagementServiceHolder.class);

    private CertificateValidationManagementServiceHolder() {

    }

    /**
     * Get CertificateValidationManagementService osgi service.
     *
     * @return CertificateValidationManagementService
     */
    public static CertificateValidationManagementService getCertificateValidationService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving CertificateValidationManagementService from OSGi service registry.");
        }
        
        CertificateValidationManagementService service = CertificateValidationServiceHolder.SERVICE;
        if (service == null) {
            LOG.warn("CertificateValidationManagementService is not available from OSGi service registry.");
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved CertificateValidationManagementService from OSGi service registry.");
            }
        }
        return service;
    }

    /**
     * Private CertificateValidationManagementServiceHolder class.
     */
    private static class CertificateValidationServiceHolder {

        static final CertificateValidationManagementService SERVICE;

        static {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing CertificateValidationManagementService from OSGi service registry.");
            }
            SERVICE = (CertificateValidationManagementService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(CertificateValidationManagementService.class, null);
            if (SERVICE == null) {
                LOG.warn("Failed to initialize CertificateValidationManagementService from OSGi service registry.");
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Successfully initialized CertificateValidationManagementService from OSGi service " +
                            "registry.");
                }
            }
        }
    }
}

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

package org.wso2.carbon.identity.api.server.input.validation.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.input.validation.common.InputValidationServiceHolder;
import org.wso2.carbon.identity.api.server.input.validation.v1.core.ValidationRulesManagementApiService;
import org.wso2.carbon.identity.input.validation.mgt.services.InputValidationManagementService;

/**
 * Factory to return ValidationRulesManagementApiService.
 */
public class ValidationRulesManagementApiServiceFactory {

    private static final Log LOGGER = LogFactory.getLog(ValidationRulesManagementApiServiceFactory.class);
    private static final ValidationRulesManagementApiService SERVICE;

    static {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initializing ValidationRulesManagementApiService");
        }
        InputValidationManagementService inputValidationManagementService = InputValidationServiceHolder
                .getInputValidationMgtService();

        if (inputValidationManagementService == null) {
            LOGGER.error("InputValidationManagementService is not available from OSGi context");
            throw new IllegalStateException("InputValidationManagementService is not available from OSGi context.");
        }

        SERVICE = new ValidationRulesManagementApiService(inputValidationManagementService);
        LOGGER.info("ValidationRulesManagementApiService initialized successfully");
    }

    /**
     * Get ValidationRulesManagementApiService.
     *
     * @return ValidationRulesManagementApiService.
     */
    public static ValidationRulesManagementApiService getValidationRulesManagementApiService() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieving ValidationRulesManagementApiService instance");
        }
        return SERVICE;
    }
}

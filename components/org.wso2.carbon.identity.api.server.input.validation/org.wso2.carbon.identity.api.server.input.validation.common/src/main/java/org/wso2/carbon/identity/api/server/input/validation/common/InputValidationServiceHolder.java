/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.common;

import org.wso2.carbon.identity.input.validation.mgt.services.InputValidationManagementService;

/**
 * Service holder class for input validation management.
 */
public class InputValidationServiceHolder {

    private static InputValidationManagementService inputValidationMgtService;

    /**
     * Get InputValidationMgtService OSGi service.
     *
     * @return Input Validation Management Service.
     */
    public static InputValidationManagementService getInputValidationMgtService() {

        return inputValidationMgtService;
    }

    /**
     * Set InputValidationMgtService OSGi service.
     *
     * @param inputValidationMgtService Input Validation Management Service.
     */
    public static void setInputValidationMgtService(InputValidationManagementService inputValidationMgtService) {

        InputValidationServiceHolder.inputValidationMgtService = inputValidationMgtService;
    }
}

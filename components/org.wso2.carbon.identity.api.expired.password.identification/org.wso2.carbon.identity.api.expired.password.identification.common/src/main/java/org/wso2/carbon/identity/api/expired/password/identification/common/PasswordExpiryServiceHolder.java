/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.expired.password.identification.common;

import org.wso2.carbon.identity.password.expiry.services.ExpiredPasswordIdentificationService;

/**
 * Service holder class for password expired users identification.
 */
public class PasswordExpiryServiceHolder {

    private static ExpiredPasswordIdentificationService expiredPasswordIdentificationService;

    /**
     * Get ExpiredPasswordIdentificationService OSGi service.
     *
     * @return ExpiredPassword identification Service.
     */
    public static ExpiredPasswordIdentificationService getExpiredPasswordIdentificationService() {

        return expiredPasswordIdentificationService;
    }

    /**
     * Set ExpiredPasswordIdentificationService OSGi service.
     *
     * @param expiredPasswordIdentificationService ExpiredPassword identification Service.
     */
    public static void setExpiredPasswordIdentificationService(
            ExpiredPasswordIdentificationService expiredPasswordIdentificationService) {

        PasswordExpiryServiceHolder.expiredPasswordIdentificationService = expiredPasswordIdentificationService;
    }
}

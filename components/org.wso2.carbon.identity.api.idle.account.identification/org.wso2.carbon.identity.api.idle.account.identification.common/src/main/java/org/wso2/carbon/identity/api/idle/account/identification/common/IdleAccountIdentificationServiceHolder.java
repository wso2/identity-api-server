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

package org.wso2.carbon.identity.api.idle.account.identification.common;

import org.wso2.carbon.identity.idle.account.identification.services.IdleAccountIdentificationService;

/**
 * Service holder class for idle account identification.
 */
public class IdleAccountIdentificationServiceHolder {

    private static IdleAccountIdentificationService idleAccountIdentificationService;

    /**
     * Get IdleAccountIdentificationService OSGi service.
     *
     * @return Idle account identification Service.
     */
    public static IdleAccountIdentificationService getIdleAccountIdentificationService() {

        return idleAccountIdentificationService;
    }

    /**
     * Set IdleAccountIdentificationService OSGi service.
     *
     * @param idleAccountIdentificationService Idle account identification Service.
     */
    public static void setIdleAccountIdentificationService(
            IdleAccountIdentificationService idleAccountIdentificationService) {

        IdleAccountIdentificationServiceHolder.idleAccountIdentificationService = idleAccountIdentificationService;
    }
}

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

package org.wso2.carbon.identity.api.server.credential.management.common.utils;

import org.apache.commons.lang.ArrayUtils;

import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.ErrorMessages;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtServerException;

/**
 * Utility class of Credential Management.
 */
public class CredentialManagementUtils {

    private CredentialManagementUtils() {

    }

    /**
     * Handle credential management server exceptions.
     *
     * @param error Error message.
     * @param e     Throwable.
     * @param data  Data to be included in the error message.
     * @return CredentialMgtServerException
     */
    public static CredentialMgtServerException handleServerException(ErrorMessages error, Throwable e, Object... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new CredentialMgtServerException(error.getCode(), error.getMessage(), description, e);
    }

    /**
     * Handle credential management client exceptions.
     *
     * @param error Error message.
     * @param e     Throwable.
     * @param data  Data to be included in the error message.
     * @return CredentialMgtClientException
     */
    public static CredentialMgtClientException handleClientException(ErrorMessages error, Throwable e, Object... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new CredentialMgtClientException(error.getCode(), error.getMessage(), description, e);
    }
}

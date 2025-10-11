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

package org.wso2.carbon.identity.api.server.user.credential.management.common.exception;

import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.ErrorMessages;

/**
 * Client exception for Admin Credential Management operations.
 */
public class CredentialMgtClientException extends CredentialMgtException {

    /**
     * Constructor with {@code message}, {@code errorCode} and {@code description} parameters.
     *
     * @param message     Message to be included in the exception.
     * @param errorCode   Error code of the exception.
     * @param description Description of the exception.
     */
    public CredentialMgtClientException(String errorCode, String message, String description) {

        super(message, errorCode, description);
    }

    /**
     * Constructor with {@code message}, {@code errorCode}, {@code description} and {@code cause} parameters.
     *
     * @param message     Message to be included in the exception.
     * @param errorCode   Error code of the exception.
     * @param description Description of the exception.
     * @param cause       Exception to be wrapped.
     */
    public CredentialMgtClientException(String errorCode, String message, String description, Throwable cause) {

        super(message, errorCode, description, cause);
    }

    /**
     * Constructor with {@code error} parameter.
     *
     * @param error Error message.
     */
    public CredentialMgtClientException(ErrorMessages error) {

        super(error.getMessage(), error.getCode(), error.getDescription());
    }
}

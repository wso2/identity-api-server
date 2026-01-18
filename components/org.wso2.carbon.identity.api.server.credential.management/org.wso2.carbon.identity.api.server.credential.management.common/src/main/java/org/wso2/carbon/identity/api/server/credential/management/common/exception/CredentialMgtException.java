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

package org.wso2.carbon.identity.api.server.credential.management.common.exception;

/**
 * Base exception class for the Admin Credential Management Service.
 */
public class CredentialMgtException extends Exception {

    private String errorCode;
    private String description;

    /**
     * Constructor with {message, errorCode and description parameters.
     *
     * @param message     Message to be included in the exception.
     * @param errorCode   Error code of the exception.
     * @param description Description of the exception.
     */
    public CredentialMgtException(String message, String errorCode, String description) {

        super(message);
        this.errorCode = errorCode;
        this.description = description;
    }

    /**
     * Constructor with message, errorCode, description and cause parameters.
     *
     * @param message     Message to be included in the exception.
     * @param errorCode   Error code of the exception.
     * @param description Description of the exception.
     * @param cause       Exception to be wrapped.
     */
    public CredentialMgtException(String message, String errorCode, String description, Throwable cause) {

        super(message, cause);
        this.errorCode = errorCode;
        this.description = description;
    }

    /**
     * Get the description.
     *
     * @return Returns the description.
     */
    public String getDescription() {

        return description;
    }

    /**
     * Get the errorCode.
     *
     * @return Returns the errorCode.
     */
    public String getErrorCode() {

        return errorCode;
    }

    /**
     * Set the errorCode.
     *
     * @param errorCode The value to be set as the errorCode.
     */
    protected void setErrorCode(String errorCode) {

        this.errorCode = errorCode;
    }

    /**
     * Set the description.
     *
     * @param description The value to be set as the description.
     */
    protected void setDescription(String description) {

        this.description = description;
    }
}

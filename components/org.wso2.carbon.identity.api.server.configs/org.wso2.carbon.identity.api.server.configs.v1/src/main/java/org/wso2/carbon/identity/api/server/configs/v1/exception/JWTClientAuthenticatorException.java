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

package org.wso2.carbon.identity.api.server.configs.v1.exception;

/**
 * Base exception class for the JWT Authentication Service.
 */
public class JWTClientAuthenticatorException extends Exception {

    /**
     * The error code.
     */
    private String errorCode;

    /**
     * The default constructor.
     */
    public JWTClientAuthenticatorException() {

        super();
    }

    /**
     * Constructor with {@code message} and {@code errorCode} parameters.
     *
     * @param message   Message to be included in the exception.
     * @param errorCode Error code of the exception.
     */
    public JWTClientAuthenticatorException(String message, String errorCode) {

        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with {@code message}, {@code errorCode} and {@code cause} parameters.
     *
     * @param message   Message to be included in the exception.
     * @param errorCode Error code of the exception.
     * @param cause     Exception to be wrapped.
     */
    public JWTClientAuthenticatorException(String message, String errorCode, Throwable cause) {

        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with {@code cause} parameter.
     *
     * @param cause Exception to be wrapped.
     */
    public JWTClientAuthenticatorException(Throwable cause) {

        super(cause);
    }

    /**
     * Get the {@code errorCode}.
     *
     * @return Returns the {@code errorCode}.
     */
    public String getErrorCode() {

        return errorCode;
    }

    /**
     * Set the {@code errorCode}.
     *
     * @param errorCode The value to be set as the {@code errorCode}.
     */
    protected void setErrorCode(String errorCode) {

        this.errorCode = errorCode;
    }
}

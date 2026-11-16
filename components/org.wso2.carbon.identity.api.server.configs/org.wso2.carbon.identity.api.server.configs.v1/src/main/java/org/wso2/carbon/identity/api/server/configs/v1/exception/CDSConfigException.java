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

package org.wso2.carbon.identity.api.server.configs.v1.exception;

/**
 * Exception for the Customer Data Service (CDS) configuration.
 * This class must not refer to the classes of the CDS extensions, because the extensions are optional.
 */
public class CDSConfigException extends Exception {

    private static final long serialVersionUID = -2934718406521735126L;

    private String errorCode;

    public CDSConfigException() {

        super();
    }

    public CDSConfigException(String message, String errorCode, Throwable cause) {

        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {

        return errorCode;
    }
}

/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.keystore.management.common;

/**
 * Contains all the Keystore Management Service related constants.
 */
public class KeyStoreConstants {

    public static final String KEYSTORES_API_PATH_COMPONENT = "/keystores";
    public static final String CERTIFICATE_PATH_COMPONENT = "/certs" + "/%s";
    public static final String CLIENT_CERTIFICATE_PATH_COMPONENT = "/client-certs" + "/%s";

    public static final String CERTIFICATE_FILE_EXTENSION = ".cer";
    public static final String CERTIFICATE_TEMPORARY_DIRECTORY_PATH = "tmp/certs";
    public static final String PATH_SEPERATOR = "/";
    public static final String HTTP_REQUEST_MESSAGE_KEY = "HTTP.REQUEST";
    public static final String ACCEPT_HEADER = "Accept";

    /**
     * Enum for Keystore management service related errors.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_ALIAS("KSS-60010", "There exists no certificate with alias: %s."),
        ERROR_CODE_FILE_WRITE("KSS-65010", "Unable to create file: %s"),
        ERROR_CODE_ENCODE_CERTIFICATE("KSS-65011", "Unable to encode the certificate with alias: %s.");

        private final String code;
        private final String message;

        ErrorMessage(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return code + " : " + message;
        }
    }
 }

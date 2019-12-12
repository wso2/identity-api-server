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
package org.wso2.carbon.identity.api.server.image.service.common;

/**
 * Contains the constants related to image api.
 */
public class ImageServiceConstants {

    private static final String IMAGE_SERVICE = "IMG-";
    public static final String IMAGE_SERVICE_PATH_COMPONENT = "/images";
    public static final String IDP = "idp";
    public static final String SP = "app";
    public static final String USER = "user";

    /**
     * Enum for error messages related to image service.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_UPLOADING_IMAGE("65001", "Unable to upload the provided image.",
                "Server encountered an error " + "while uploading the image."),
        ERROR_CODE_ERROR_DOWNLOADING_IMAGE(
                "65002", "Unable to download the specified image.",
                "Server encountered an " + "error while downloading the image."),
        ERROR_CODE_ERROR_DELETING_IMAGE(
                "65003", "Unable to delete the specified image.",
                "Server encountered an error " + "while deleting the image."),
        ;

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return IMAGE_SERVICE + code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }
    }
}

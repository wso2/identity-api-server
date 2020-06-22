/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.media.service.common;

/**
 * Contains the constants related to media service.
 */
public class MediaServiceConstants {

    private MediaServiceConstants() {

    }

    private static final String MEDIA_SERVICE = "MED-";
    public static final String MEDIA_SERVICE_PATH_COMPONENT = "/media";
    public static final String CONTENT_PATH_COMPONENT = "content";
    public static final String PUBLIC_PATH_COMPONENT = "public";

    /**
     * Enum for error messages related to media service.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_ERROR_UPLOADING_MEDIA_CONTENT_TYPE_MISMATCH("60001", "Unable to upload the provided media.",
                "Expected file content type: %s"),
        ERROR_CODE_ERROR_UPLOADING_MEDIA_UNSUPPORTED_CONTENT_TYPE("60002", "Unable to upload the provided media.",
                "Unsupported file content type."),
        ERROR_CODE_ERROR_DOWNLOADING_MEDIA_FILE_NOT_FOUND("60003", "Unable to download the requested media.",
                "File with id: %s not found."),

        // Server errors.
        ERROR_CODE_ERROR_UPLOADING_MEDIA("65001", "Unable to upload the provided media.",
                "Server encountered an error while uploading the media."),
        ERROR_CODE_ERROR_EVALUATING_ACCESS_SECURITY("65002", "Unable to evaluate access security for the" +
                " requested media.", "Server encountered an error while evaluating security access to the media."),
        ERROR_CODE_ERROR_DOWNLOADING_MEDIA("65003", "Unable to download the specified media.",
                "Server encountered an error while downloading the media."),
        ERROR_CODE_ERROR_DELETING_MEDIA("65003", "Unable to delete the specified media.", "Server encountered " +
                "an error while deleting the media.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return MEDIA_SERVICE + code;
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

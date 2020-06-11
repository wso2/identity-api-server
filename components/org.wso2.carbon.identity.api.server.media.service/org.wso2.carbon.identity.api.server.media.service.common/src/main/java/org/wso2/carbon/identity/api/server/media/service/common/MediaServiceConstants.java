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

import java.util.ArrayList;

/**
 * Contains the constants related to media service.
 */
public class MediaServiceConstants {

    private static final String MEDIA_SERVICE = "MED-";
    public static final String MEDIA_SERVICE_PATH_COMPONENT = "/media";
    public static final String DATA_PATH_COMPONENT = "/data";
    public static final String ACCESS_LEVEL_ME_PATH_COMPONENT = "me";
    public static final String ACCESS_LEVEL_USER_PATH_COMPONENT = "user";
    public static final String ACCESS_LEVEL_PUBLIC_PATH_COMPONENT = "public";

    /**
     * Contains the list of allowed content types of the files to be uploaded.
     */
    public static class AllowedContentTypes {

        public static final ArrayList<String> ALLOWED_CONTENT_TYPES = new ArrayList<>();

        static {
            ALLOWED_CONTENT_TYPES.add("image/apng");
            ALLOWED_CONTENT_TYPES.add("image/bmp");
            ALLOWED_CONTENT_TYPES.add("image/gif");
            ALLOWED_CONTENT_TYPES.add("image/x-icon");
            ALLOWED_CONTENT_TYPES.add("image/jpeg");
            ALLOWED_CONTENT_TYPES.add("image/png");
            ALLOWED_CONTENT_TYPES.add("image/svg+xml");
            ALLOWED_CONTENT_TYPES.add("image/tiff");
            ALLOWED_CONTENT_TYPES.add("image/webp");
            ALLOWED_CONTENT_TYPES.add("text/css");
        }
    }

    /**
     * Enum for error messages related to media service.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_UPLOADING_MEDIA("65001", "Unable to upload the provided media.",
                "Server encountered an error while uploading the media.");

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

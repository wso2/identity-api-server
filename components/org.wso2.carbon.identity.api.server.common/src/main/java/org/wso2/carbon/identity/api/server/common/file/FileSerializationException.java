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

package org.wso2.carbon.identity.api.server.common.file;

import java.util.Locale;

/**
 * Generic exception for file serialization/deserialization operations.
 * Entities should catch and wrap this in their domain-specific exceptions.
 */
public class FileSerializationException extends Exception {

    private final String fileName;
    private final String mediaType;
    private final Operation operation;

    /**
     * Type of serialization operation.
     */
    public enum Operation {
        SERIALIZE, DESERIALIZE
    }

    public FileSerializationException(String message, Throwable cause,
                                      String fileName, String mediaType, Operation operation) {

        super(buildMessage(message, fileName, mediaType, operation), cause);
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.operation = operation;
    }

    private static String buildMessage(String message, String fileName,
                                        String mediaType, Operation operation) {

        return String.format("Error during %s operation for file '%s' (media type: %s): %s",
                operation.name().toLowerCase(Locale.ENGLISH), fileName, mediaType, message);
    }

    public String getFileName() {
        return fileName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Operation getOperation() {
        return operation;
    }
}

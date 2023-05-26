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

package org.wso2.carbon.identity.api.server.extension.management.common.utils;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.extension.management.common.ExtensionManagementServiceHolder;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.extension.management.common.utils.ExtensionMgtConstants.EXTENSION_MGT_PATH_COMPONENT;

/**
 * Utility class for extension management.
 */
public class ExtensionMgtUtils {

    /**
     * Get the path of the extension type.
     *
     * @param extensionType Type of the extension.
     * @return Path of the extension type.
     */
    public static String getExtensionInfoLocation(String extensionType, String extensionId) {

        return ContextLoader.buildURIForBody(
                Constants.V1_API_PATH_COMPONENT + EXTENSION_MGT_PATH_COMPONENT + '/' +
                        extensionType + "/" + extensionId).toString();
    }

    /**
     * Handle client exception.
     *
     * @param status Status of the response.
     * @param error Error message.
     * @param data Data to be added to the error message.
     * @return APIError.
     */
    public static APIError handleClientException(Response.Status status, ExtensionMgtConstants.ErrorMessage error,
                                                  String... data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Handle server exception.
     *
     * @param status Status of the response.
     * @param error Error message.
     * @param data Data to be added to the error message.
     * @return APIError.
     */
    public static APIError handleServerException(Response.Status status, ExtensionMgtConstants.ErrorMessage error,
                                                  String... data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Build the error response.
     *
     * @param error Error message.
     * @param data Data to be added to the error message.
     * @return Error builder.
     */
    private static ErrorResponse.Builder getErrorBuilder(ExtensionMgtConstants.ErrorMessage error, String... data) {

        String description = ArrayUtils.isNotEmpty(data)
                ? String.format(error.getDescription(), data)
                : error.getDescription();
        return new ErrorResponse.Builder()
                .withCode(error.getCode())
                .withMessage(error.getMessage())
                .withDescription(description);
    }

    /**
     * Validate the extension type.
     *
     * @param extensionType Type of the extension.
     */
    public static void validateExtensionType(String extensionType) {

        if (!ArrayUtils.contains(ExtensionManagementServiceHolder.getInstance().getExtensionManager()
                .getExtensionTypes(), extensionType)) {
            throw handleClientException(Response.Status.BAD_REQUEST, ExtensionMgtConstants.ErrorMessage
                    .ERROR_CODE_INVALID_EXTENSION_TYPE, extensionType);
        }
    }
}

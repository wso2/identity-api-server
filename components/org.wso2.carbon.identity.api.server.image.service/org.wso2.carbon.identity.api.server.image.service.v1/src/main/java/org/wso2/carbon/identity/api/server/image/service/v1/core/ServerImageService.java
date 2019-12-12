/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
package org.wso2.carbon.identity.api.server.image.service.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.context.PrivilegedCarbonContext;

import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.image.service.common.ImageServiceConstants;
import org.wso2.carbon.identity.api.server.image.service.common.ImageServiceDataHolder;
import org.wso2.carbon.identity.image.StorageSystemManager;
import org.wso2.carbon.identity.image.exception.StorageSystemException;

import java.io.InputStream;
import javax.ws.rs.core.Response;

/**
 * Perform image service related operations.
 */
public class ServerImageService {

    private static final Log log = LogFactory.getLog(ServerImageService.class);

    /**
     * Method to upload the image to file system.
     *
     * @param fileInputStream
     * @param fileDetail
     * @param type
     * @return
     */
    public String uploadImage(InputStream fileInputStream, Attachment fileDetail, String type) {

        StorageSystemManager storageSystemManager = ImageServiceDataHolder.getStorageSystemManager();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            return storageSystemManager.addFile(fileInputStream, type, tenantDomain);
        } catch (StorageSystemException e) {
            ImageServiceConstants.ErrorMessage errorMessage = ImageServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_UPLOADING_IMAGE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }

    }

    public byte[] downloadImage(String id, String type) {

        StorageSystemManager storageSystemManager = ImageServiceDataHolder.getStorageSystemManager();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            return storageSystemManager.getFile(id, type, tenantDomain);
        } catch (StorageSystemException e) {
            ImageServiceConstants.ErrorMessage errorMessage = ImageServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DOWNLOADING_IMAGE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }
    }

    public void deleteImage(String id, String type) {

        StorageSystemManager storageSystemManager = ImageServiceDataHolder.getStorageSystemManager();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            storageSystemManager.deleteFile(id, type, tenantDomain);
        } catch (StorageSystemException e) {
            ImageServiceConstants.ErrorMessage errorMessage = ImageServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DELETING_IMAGE;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }

    }

    private APIError handleException(Exception e, ImageServiceConstants.ErrorMessage errorEnum,
            Response.Status status) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(ImageServiceConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(ImageServiceConstants.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            if (data.length == 1) {
                errorDescription = String.format(errorEnum.getDescription(), (Object) data);
            } else {
                errorDescription = String.format(errorEnum.getDescription(), (Object[]) data);
            }
        } else {
            errorDescription = errorEnum.getDescription();
        }

        return errorDescription;
    }
}

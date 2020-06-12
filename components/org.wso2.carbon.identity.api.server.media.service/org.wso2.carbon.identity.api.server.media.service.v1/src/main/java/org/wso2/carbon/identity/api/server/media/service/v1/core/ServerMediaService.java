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

package org.wso2.carbon.identity.api.server.media.service.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceDataHolder;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadataFileSecurity;
import org.wso2.carbon.identity.media.StorageSystemManager;
import org.wso2.carbon.identity.media.exception.StorageSystemException;
import org.wso2.carbon.identity.media.model.FileSecurity;
import org.wso2.carbon.identity.media.model.MediaMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Perform media service related operations.
 */
public class ServerMediaService {

    private static final Log LOG = LogFactory.getLog(ServerMediaService.class);

    /**
     * Upload media.
     *
     * @param filesInputStream      The list of files to be uploaded as input streams.
     * @param filesDetail           File details of the list of files to be uploaded.
     * @param resourceFilesMetadata Metadata associated with the file upload.
     * @param onlyEndUserAccess     If the resource is end-user protected.
     * @return unique identifier of the uploaded media.
     */
    public String uploadMedia(List<InputStream> filesInputStream, List<Attachment> filesDetail,
                              ResourceFilesMetadata resourceFilesMetadata, boolean onlyEndUserAccess) {

        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.setFileName(filesDetail.get(0).getContentDisposition().getFilename());
        mediaMetadata.setFileContentType(filesDetail.get(0).getContentType().toString());

        if (resourceFilesMetadata != null) {
            mediaMetadata.setFileTag(resourceFilesMetadata.getFileTag());
            FileSecurity fileSecurity = null;
            if (onlyEndUserAccess) {
                ArrayList<String> users = new ArrayList<>();
                users.add(ContextLoader.getUsernameFromContext());
                fileSecurity = new FileSecurity(false, users, null);
            } else {
                ResourceFilesMetadataFileSecurity fileSecurityMeta = resourceFilesMetadata.getFileSecurity();
                if (fileSecurityMeta != null) {
                    if (fileSecurityMeta.getAllowedAll()) {
                        fileSecurity = new FileSecurity(true, null, null);
                    } else {
                        fileSecurity = new FileSecurity(false, fileSecurityMeta.getAllowedUsers(),
                                fileSecurityMeta.getAllowedScopes());
                    }
                }
            }
            mediaMetadata.setFileSecurity(fileSecurity);
        }

        StorageSystemManager storageSystemManager = MediaServiceDataHolder.getStorageSystemManager();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            return storageSystemManager.addFile(filesInputStream, mediaMetadata, tenantDomain);
        } catch (StorageSystemException e) {
            MediaServiceConstants.ErrorMessage errorMessage = MediaServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_UPLOADING_MEDIA;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }
    }

    private APIError handleException(Exception e, MediaServiceConstants.ErrorMessage errorEnum,
                                     Response.Status status) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, e, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(MediaServiceConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(MediaServiceConstants.ErrorMessage error, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            if (data.length == 1) {
                errorDescription = String.format(error.getDescription(), (Object) data);
            } else {
                errorDescription = String.format(error.getDescription(), (Object[]) data);
            }
        } else {
            errorDescription = error.getDescription();
        }
        return errorDescription;
    }

}

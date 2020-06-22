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

import org.apache.axiom.om.OMElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceDataHolder;
import org.wso2.carbon.identity.api.server.media.service.v1.model.MyResourceFilesMetadata;
import org.wso2.carbon.identity.api.server.media.service.v1.model.MyResourceFilesMetadataSecurity;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadataSecurity;
import org.wso2.carbon.identity.core.util.IdentityConfigParser;
import org.wso2.carbon.identity.core.util.IdentityCoreConstants;
import org.wso2.carbon.identity.media.DataContent;
import org.wso2.carbon.identity.media.StorageSystemManager;
import org.wso2.carbon.identity.media.exception.StorageSystemException;
import org.wso2.carbon.identity.media.model.FileSecurity;
import org.wso2.carbon.identity.media.model.MediaMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

/**
 * Perform media service related operations.
 */
public class ServerMediaService {

    private static final Log LOG = LogFactory.getLog(ServerMediaService.class);

    /**
     * Upload media by a privileged user.
     *
     * @param filesInputStream      The list of files to be uploaded as input streams.
     * @param filesDetail           File details of the list of files to be uploaded.
     * @param resourceFilesMetadata Metadata associated with the file upload.
     * @return unique identifier of the uploaded media.
     */
    public String uploadMedia(List<InputStream> filesInputStream, List<Attachment> filesDetail,
                              ResourceFilesMetadata resourceFilesMetadata) {

        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.setResourceOwner(ContextLoader.getUsernameFromContext());
        mediaMetadata.setFileName(filesDetail.get(0).getContentDisposition().getFilename());
        mediaMetadata.setFileContentType(filesDetail.get(0).getContentType().toString());

        if (resourceFilesMetadata != null) {
            mediaMetadata.setFileTag(resourceFilesMetadata.getTag());
        }

        setSecurityForPrivilegedUserUploadedMedia(resourceFilesMetadata, mediaMetadata);

        return addFile(filesInputStream, mediaMetadata);
    }

    /**
     * Upload media by an end-user.
     *
     * @param filesInputStream        The list of files to be uploaded as input streams.
     * @param filesDetail             File details of the list of files to be uploaded.
     * @param myResourceFilesMetadata Metadata associated with the file upload.
     * @return unique identifier of the uploaded media.
     */
    public String uploadMyMedia(List<InputStream> filesInputStream, List<Attachment> filesDetail,
                                MyResourceFilesMetadata myResourceFilesMetadata) {

        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.setResourceOwner(ContextLoader.getUsernameFromContext());
        mediaMetadata.setFileName(filesDetail.get(0).getContentDisposition().getFilename());
        mediaMetadata.setFileContentType(filesDetail.get(0).getContentType().toString());

        if (myResourceFilesMetadata != null) {
            mediaMetadata.setFileTag(myResourceFilesMetadata.getTag());
        }

        setSecurityForEndUserUploadedMedia(myResourceFilesMetadata, mediaMetadata);

        return addFile(filesInputStream, mediaMetadata);
    }

    private String addFile(List<InputStream> filesInputStream, MediaMetadata mediaMetadata) {

        StorageSystemManager storageSystemManager = MediaServiceDataHolder.getInstance().getStorageSystemManager();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            String uuid = storageSystemManager.addFile(filesInputStream, mediaMetadata, tenantDomain);
            if (StringUtils.isNotBlank(uuid)) {
                return uuid;
            }
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    MediaServiceConstants.ErrorMessage.ERROR_CODE_ERROR_UPLOADING_MEDIA);
        } catch (StorageSystemException e) {
            MediaServiceConstants.ErrorMessage errorMessage = MediaServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_UPLOADING_MEDIA;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }
    }

    private void setSecurityForEndUserUploadedMedia(MyResourceFilesMetadata myResourceFilesMetadata,
                                                    MediaMetadata mediaMetadata) {

        MyResourceFilesMetadataSecurity fileSecurityMeta = null;
        if (myResourceFilesMetadata != null) {
            fileSecurityMeta = myResourceFilesMetadata.getSecurity();
        }
        FileSecurity fileSecurity;
        if (fileSecurityMeta != null && fileSecurityMeta.getAllowedAll()) {
            fileSecurity = new FileSecurity(true);
        } else {
            ArrayList<String> users = new ArrayList<>();
            users.add(ContextLoader.getUsernameFromContext());
            fileSecurity = new FileSecurity(false, users);
        }
        mediaMetadata.setFileSecurity(fileSecurity);
    }

    private void setSecurityForPrivilegedUserUploadedMedia(ResourceFilesMetadata resourceFilesMetadata,
                                                           MediaMetadata mediaMetadata) {

        ResourceFilesMetadataSecurity fileSecurityMeta = null;
        if (resourceFilesMetadata != null) {
            fileSecurityMeta = resourceFilesMetadata.getSecurity();
        }
        FileSecurity fileSecurity;
        if (fileSecurityMeta != null && fileSecurityMeta.getAllowedAll()) {
            fileSecurity = new FileSecurity(true);
        } else {
            List<String> allowedUsers = null;
            if (fileSecurityMeta != null) {
                allowedUsers = fileSecurityMeta.getAllowedUsers();
            }
            if (CollectionUtils.isNotEmpty(allowedUsers)) {
                fileSecurity = new FileSecurity(false, allowedUsers);
            } else {
                fileSecurity = new FileSecurity(false);
            }
        }
        mediaMetadata.setFileSecurity(fileSecurity);
    }

    /**
     * Validate if the uploaded media content type is a supported media type.
     *
     * @param mediaTypePathParam The media type available as a path parameter in the upload request.
     * @param mediaType          The content type of the uploaded media.
     */
    public void validateMediaType(String mediaTypePathParam, MediaType mediaType) {

        if (!mediaTypePathParam.equals(mediaType.getType())) {
            throw handleException(Response.Status.FORBIDDEN,
                    MediaServiceConstants.ErrorMessage.ERROR_CODE_ERROR_UPLOADING_MEDIA_CONTENT_TYPE_MISMATCH,
                    mediaTypePathParam);
        }
        if (!loadAllowedContentTypes().contains(mediaTypePathParam) ||
                !loadAllowedContentTypeSubTypes(mediaType.getType()).contains(mediaType.getSubtype())) {
            throw handleException(Response.Status.FORBIDDEN,
                    MediaServiceConstants.ErrorMessage.ERROR_CODE_ERROR_UPLOADING_MEDIA_UNSUPPORTED_CONTENT_TYPE);
        }
    }

    /**
     * Read configuration values defined in identity.xml.
     */
    private List<String> loadAllowedContentTypes() {

        ArrayList<String> allowedTypes = new ArrayList<>();
        OMElement mediaService = IdentityConfigParser.getInstance().getConfigElement("MediaService");
        Iterator allowedContentTypes = null;
        if (mediaService != null) {
            allowedContentTypes = mediaService.getChildrenWithName(new QName(IdentityCoreConstants
                    .IDENTITY_DEFAULT_NAMESPACE, "AllowedContentType"));
        }

        if (allowedContentTypes != null) {
            while (allowedContentTypes.hasNext()) {
                OMElement allowedContentType = (OMElement) allowedContentTypes.next();
                String type = allowedContentType.getAttributeValue(new QName("type"));
                allowedTypes.add(type);
            }
        }
        return allowedTypes;
    }

    /**
     * Read configuration values defined in identity.xml.
     */
    private List<String> loadAllowedContentTypeSubTypes(String contentType) {

        OMElement mediaService = IdentityConfigParser.getInstance().getConfigElement("MediaService");
        Iterator allowedContentTypes = null;
        if (mediaService != null) {
            allowedContentTypes = mediaService.getChildrenWithName(new QName(IdentityCoreConstants
                    .IDENTITY_DEFAULT_NAMESPACE, "AllowedContentType"));
        }

        ArrayList<String> allowedSubTypesList = new ArrayList<>();
        if (allowedContentTypes != null) {
            while (allowedContentTypes.hasNext()) {
                OMElement allowedContentType = (OMElement) allowedContentTypes.next();
                String type = allowedContentType.getAttributeValue(new QName("type"));
                Iterator allowedSubTypes = null;
                if (StringUtils.isNotBlank(type) && type.equals(contentType)) {
                    allowedSubTypes = allowedContentType.getChildrenWithName(new QName(IdentityCoreConstants
                            .IDENTITY_DEFAULT_NAMESPACE, "AllowedSubTypes"));
                    if (allowedSubTypes != null) {
                        while (allowedSubTypes.hasNext()) {
                            OMElement allowedSubType = (OMElement) allowedSubTypes.next();
                            String subType = allowedSubType.getFirstChildWithName(new QName
                                    (IdentityCoreConstants.IDENTITY_DEFAULT_NAMESPACE, "AllowedSubType")).getText();
                            allowedSubTypesList.add(subType);
                        }
                    }
                    break;
                }
            }
        }
        return allowedSubTypesList;
    }

    /**
     * Method to download the media from file system.
     *
     * @param id   Unique identifier for the uploaded media file.
     * @param type The high level content-type of the resource (if media content-type is image/png then
     *             type would be image).
     * @return requested media file.
     */
    public DataContent downloadMedia(String type, String id) {

        StorageSystemManager storageSystemManager = MediaServiceDataHolder.getInstance().getStorageSystemManager();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            DataContent dataContent = storageSystemManager.readContent(id, tenantDomain, type);
            if (dataContent == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        MediaServiceConstants.ErrorMessage.ERROR_CODE_ERROR_DOWNLOADING_MEDIA_FILE_NOT_FOUND, id);
            }
            return dataContent;
        } catch (StorageSystemException e) {
            MediaServiceConstants.ErrorMessage errorMessage = MediaServiceConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DOWNLOADING_MEDIA;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorMessage, status);
        }
    }

    private APIError handleException(Response.Status status, MediaServiceConstants.ErrorMessage error, String... data) {

        return new APIError(status, getErrorBuilder(error, data).build());
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
            errorDescription = String.format(error.getDescription(), data);
        } else {
            errorDescription = error.getDescription();
        }
        return errorDescription;
    }

}

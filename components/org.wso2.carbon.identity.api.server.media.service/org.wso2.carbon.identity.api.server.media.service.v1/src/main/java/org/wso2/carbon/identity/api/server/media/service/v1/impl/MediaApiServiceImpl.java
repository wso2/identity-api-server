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

package org.wso2.carbon.identity.api.server.media.service.v1.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.media.service.v1.MediaApiService;
import org.wso2.carbon.identity.api.server.media.service.v1.core.ServerMediaService;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.ACCESS_LEVEL_ME_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.ACCESS_LEVEL_PUBLIC_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.ACCESS_LEVEL_USER_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.AllowedContentTypes.ALLOWED_CONTENT_TYPES;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.DATA_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.MEDIA_SERVICE_PATH_COMPONENT;

/**
 * Implementation class of media service.
 */
public class MediaApiServiceImpl implements MediaApiService {

    @Autowired
    private ServerMediaService serverMediaService;

    @Override
    public Response deleteFile(String type, String id) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteMyFile(String type, String id) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response downloadFile(String type, String id, String identifier) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response downloadMyFile(String type, String id, String identifier) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response downloadPublicFile(String type, String id, String identifier) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response uploadFile(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail,
                               ResourceFilesMetadata metadata) {

        // TODO: need to read the allowed content types from a configuration file.
        if (!ALLOWED_CONTENT_TYPES.contains(filesDetail.get(0).getContentType().toString())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        // Only single file upload will be supported in the first phase of the implementation.
        if (filesInputStream.size() > 1) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        } else {
            String uuid = serverMediaService.uploadMedia(filesInputStream, filesDetail, metadata, false);
            String mediaAccessLevel = getMediaAccessLevel(metadata);
            return Response.created(getResourceLocation(uuid, type, mediaAccessLevel)).build();
        }
    }

    @Override
    public Response uploadMyFile(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail,
                                 ResourceFilesMetadata metadata) {

        // TODO: need to read the allowed content types from a configuration file.
        if (!ALLOWED_CONTENT_TYPES.contains(filesDetail.get(0).getContentType().toString())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        // Only single file upload will be supported in the first phase of the implementation.
        if (filesInputStream.size() > 1) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        } else {
            String uuid = serverMediaService.uploadMedia(filesInputStream, filesDetail, metadata, true);
            return Response.created(getResourceLocation(uuid, type, ACCESS_LEVEL_ME_PATH_COMPONENT)).build();
        }
    }

    private URI getResourceLocation(String id, String type, String accessLevel) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + MEDIA_SERVICE_PATH_COMPONENT + "/%s/%s/%s" +
                DATA_PATH_COMPONENT, type, accessLevel, id));
    }

    private String getMediaAccessLevel(ResourceFilesMetadata metadata) {

        if (metadata != null && metadata.getFileSecurity() != null && metadata.getFileSecurity().getAllowedAll()) {
            return ACCESS_LEVEL_PUBLIC_PATH_COMPONENT;
        }
        return ACCESS_LEVEL_USER_PATH_COMPONENT;
    }

}

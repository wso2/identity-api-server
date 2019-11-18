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

package org.wso2.carbon.identity.api.server.image.service.v1.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.image.service.v1.ImagesApiService;
import org.wso2.carbon.identity.api.server.image.service.v1.core.ServerImageService;

import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;

/**
 * Implementation class of image service.
 */
public class ImagesApiServiceImpl implements ImagesApiService {

    @Autowired
    private ServerImageService serverImageService;

    @Override
    public Response deleteImage(String id, String type) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response downloadImage(String id, String type) {
        InputStream resource = serverImageService.downloadImage(id, type);
        return Response.ok().entity(resource).build();
    }

    @Override
    public Response uploadImage(InputStream fileInputStream, Attachment fileDetail, String type) {

        String uuid = serverImageService.uploadImage(fileInputStream, fileDetail, type);
        return Response.created(getResourceLocation(uuid)).build();
    }

    private URI getResourceLocation(String id) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + "/images" + "/%s", id));
    }
}

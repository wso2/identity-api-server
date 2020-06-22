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

package org.wso2.carbon.identity.api.server.media.service.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.api.server.media.service.v1.model.MyResourceFilesMetadata;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;

import java.io.InputStream;
import java.util.List;
import javax.ws.rs.core.Response;


public interface MediaApiService {

      public Response deleteMedia(String type, String id);

      public Response deleteMyMedia(String type, String id);

      public Response downloadMedia(String type, String id, String identifier);

      public Response downloadPublicMedia(String type, String id, String identifier);

      public Response listMediaInformation(String type, String id);

      public Response listMyMediaInformation(String type, String id);

      public Response uploadMedia(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail, ResourceFilesMetadata metadata);

      public Response uploadMyMedia(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail, MyResourceFilesMetadata metadata);
}

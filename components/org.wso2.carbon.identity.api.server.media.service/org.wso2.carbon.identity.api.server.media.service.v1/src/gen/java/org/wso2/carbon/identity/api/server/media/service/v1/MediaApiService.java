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

import org.wso2.carbon.identity.api.server.media.service.v1.*;
import org.wso2.carbon.identity.api.server.media.service.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.media.service.v1.model.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.media.service.v1.model.MultipleFilesUploadResponse;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;
import javax.ws.rs.core.Response;


public interface MediaApiService {

      public Response deleteFile(String type, String id);

      public Response deleteMyFile(String type, String id);

      public Response downloadFile(String type, String id, String identifier);

      public Response downloadMyFile(String type, String id, String identifier);

      public Response downloadPublicFile(String type, String id, String identifier);

      public Response uploadFile(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail, ResourceFilesMetadata metadata);

      public Response uploadMyFile(String type, List<InputStream> filesInputStream, List<Attachment> filesDetail, ResourceFilesMetadata metadata);
}

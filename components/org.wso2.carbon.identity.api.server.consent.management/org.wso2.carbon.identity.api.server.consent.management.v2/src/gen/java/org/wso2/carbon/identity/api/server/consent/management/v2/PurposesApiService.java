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

package org.wso2.carbon.identity.api.server.consent.management.v2;

import org.wso2.carbon.identity.api.server.consent.management.v2.*;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ErrorDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.SetLatestVersionRequest;
import java.util.UUID;
import javax.ws.rs.core.Response;


public interface PurposesApiService {

      public Response purposesCreate(PurposeCreateRequest purposeCreateRequest);

      public Response purposesDelete(UUID purposeId);

      public Response purposesGet(UUID purposeId);

      public Response purposesList(String filter, Integer limit, String after, String before);

      public Response purposesSetLatestVersion(UUID purposeId, SetLatestVersionRequest setLatestVersionRequest);

      public Response purposesVersionsCreate(UUID purposeId, PurposeVersionCreateRequest purposeVersionCreateRequest);

      public Response purposesVersionsDelete(UUID purposeId, UUID versionId);

      public Response purposesVersionsGet(UUID purposeId, UUID versionId);

      public Response purposesVersionsList(UUID purposeId, Integer limit, String after, String before);
}

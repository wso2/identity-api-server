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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentResponseDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentValidateResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ErrorDTO;
import java.util.UUID;
import javax.ws.rs.core.Response;


public interface ConsentsApiService {

      public Response consentsAuthorize(String consentId, AuthorizationCreateRequest authorizationCreateRequest);

      public Response consentsCreate(ConsentCreateRequest consentCreateRequest);

      public Response consentsGet(String consentId);

      public Response consentsList(String subjectId, String serviceId, String state, UUID purposeId, UUID purposeVersionId, Integer limit, String after, String before);

      public Response consentsRevoke(String consentId);

      public Response consentsValidate(String consentId);
}

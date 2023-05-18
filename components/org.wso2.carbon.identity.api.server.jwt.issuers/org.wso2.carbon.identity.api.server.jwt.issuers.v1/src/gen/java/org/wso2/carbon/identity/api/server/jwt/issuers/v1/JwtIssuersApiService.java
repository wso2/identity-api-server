/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.jwt.issuers.v1;

import org.wso2.carbon.identity.api.server.jwt.issuers.v1.*;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.Error;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.JWTIssuerPOSTRequest;
import javax.ws.rs.core.Response;


public interface JwtIssuersApiService {

      public Response addJWTIssuer(JWTIssuerPOSTRequest jwTIssuerPOSTRequest);

      public Response getJwtIssuers(Integer limit, Integer offset, String filter, String sortBy, String sortOrder, String requiredAttributes);
}

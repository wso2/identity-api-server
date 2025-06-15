/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.*;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.*;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimResDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class ClaimManagementApiService {

    public abstract Response addClaimDialect(ClaimDialectReqDTO claimDialect);

    public abstract Response addExternalClaim(String dialectId, ExternalClaimReqDTO externalClaim);

    public abstract Response addLocalClaim(LocalClaimReqDTO localClaim);

    public abstract Response deleteClaimDialect(String dialectId);

    public abstract Response deleteExternalClaim(String dialectId, String claimId);

    public abstract Response deleteLocalClaim(String claimId);

    public abstract Response exportClaimDialectToFile(String dialectId, String accept);

    public abstract Response getClaimDialect(String dialectId);

    public abstract Response getClaimDialects(Integer limit, Integer offset, String filter, String sort);

    public abstract Response getExternalClaim(String dialectId, String claimId);

    public abstract Response getExternalClaims(String dialectId, Integer limit, Integer offset, String filter, String sort);

    public abstract Response getLocalClaim(String claimId);

    public abstract Response getLocalClaims(String attributes, Integer limit, Integer offset, String filter, String sort, Boolean excludeIdentityClaims, Boolean excludeHiddenClaims, String profile);

    public abstract Response importClaimDialectFromFile(InputStream fileInputStream,Attachment fileDetail);

    public abstract Response updateClaimDialect(String dialectId, ClaimDialectReqDTO claimDialect);

    public abstract Response updateClaimDialectFromFile(InputStream fileInputStream,Attachment fileDetail, Boolean preserveClaims);

    public abstract Response updateExternalClaim(String dialectId, String claimId, ExternalClaimReqDTO externalClaim);

    public abstract Response updateLocalClaim(String claimId, LocalClaimReqDTO localClaim);

}

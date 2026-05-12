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

package org.wso2.carbon.identity.api.server.consent.management.v2.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.consent.management.v2.PurposesApiService;
import org.wso2.carbon.identity.api.server.consent.management.v2.core.PurposeManagementService;
import org.wso2.carbon.identity.api.server.consent.management.v2.factories.PurposeManagementServiceFactory;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.SetLatestVersionRequest;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.PURPOSES_PATH;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.VERSIONS_PATH;

/**
 * Implementation of the PurposesApiService interface.
 */
public class PurposesApiServiceImpl implements PurposesApiService {

    private final PurposeManagementService purposesService;

    public PurposesApiServiceImpl() {

        try {
            this.purposesService = PurposeManagementServiceFactory.getPurposeManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating purpose management service.", e);
        }
    }

    @Override
    public Response purposesCreate(PurposeCreateRequest purposeCreateRequest) {

        PurposeDTO dto = purposesService.createPurpose(purposeCreateRequest);
        URI location = ContextLoader.buildURIForHeader(PURPOSES_PATH + "/" + dto.getId());
        return Response.created(location).entity(dto).build();
    }

    @Override
    public Response purposesDelete(String purposeId) {

        purposesService.deletePurpose(purposeId);
        return Response.noContent().build();
    }

    @Override
    public Response purposesGet(String purposeId) {

        return Response.ok().entity(purposesService.getPurpose(purposeId)).build();
    }

    @Override
    public Response purposesList(String filter, Integer limit, String after, String before) {

        return Response.ok().entity(purposesService.listPurposes(filter, limit, after, before)).build();
    }

    @Override
    public Response purposesSetLatestVersion(String purposeId, SetLatestVersionRequest setLatestVersionRequest) {

        purposesService.setLatestVersion(purposeId, setLatestVersionRequest);
        return Response.noContent().build();
    }

    @Override
    public Response purposesVersionsCreate(String purposeId, PurposeVersionCreateRequest purposeVersionCreateRequest) {

        PurposeVersionDTO dto = purposesService.createPurposeVersion(purposeId, purposeVersionCreateRequest);
        URI location = ContextLoader.buildURIForHeader(PURPOSES_PATH + "/" + purposeId + VERSIONS_PATH + "/"
                + dto.getId());
        return Response.created(location).entity(dto).build();
    }

    @Override
    public Response purposesVersionsDelete(String purposeId, String versionId) {

        purposesService.deletePurposeVersion(purposeId, versionId);
        return Response.noContent().build();
    }

    @Override
    public Response purposesVersionsGet(String purposeId, String versionId) {

        return Response.ok().entity(purposesService.getPurposeVersion(purposeId, versionId)).build();
    }

    @Override
    public Response purposesVersionsList(String purposeId, Integer limit, String after, String before) {

        return Response.ok().entity(purposesService.listPurposeVersions(purposeId, limit, after, before)).build();
    }
}

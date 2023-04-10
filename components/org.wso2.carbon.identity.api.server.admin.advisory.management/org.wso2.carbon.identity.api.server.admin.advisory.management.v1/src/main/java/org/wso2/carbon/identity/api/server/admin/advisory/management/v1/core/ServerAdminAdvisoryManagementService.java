/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.admin.advisory.management.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.admin.advisory.management.common.AdminAdvisoryConstants;
import org.wso2.carbon.identity.api.server.admin.advisory.management.common.AdminAdvisoryManagementServiceHolder;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model.AdminAdvisoryConfig;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.mgt.IdentityMgtServiceException;
import org.wso2.carbon.identity.mgt.dto.AdminAdvisoryBannerDTO;
import org.wso2.carbon.identity.mgt.services.AdminAdvisoryManagementService;

import javax.ws.rs.core.Response;

/**
 * Call internal osgi services to perform admin advisory management related operations.
 */
public class ServerAdminAdvisoryManagementService {

    private static final Log LOG = LogFactory.getLog(ServerAdminAdvisoryManagementService.class);

    /**
     * Get admin advisory configuration.
     *
     * @return Admin advisory configuration.
     */
    public AdminAdvisoryConfig getAdminAdvisoryConfig() {

        try {
            AdminAdvisoryManagementService adminAdvisoryManagementService = AdminAdvisoryManagementServiceHolder
                    .getAdminAdvisoryManagementService();
            AdminAdvisoryBannerDTO adminAdvisoryBannerDTO = adminAdvisoryManagementService.getAdminAdvisoryConfig();

            return buildAdminAdvisoryConfigResponse(adminAdvisoryBannerDTO);

        } catch (IdentityMgtServiceException e) {
            AdminAdvisoryConstants.ErrorMessage errorEnum =
                    AdminAdvisoryConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_BANNER_CONFIG;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    private AdminAdvisoryConfig buildAdminAdvisoryConfigResponse(AdminAdvisoryBannerDTO adminAdvisoryBannerDTO) {

        AdminAdvisoryConfig adminAdvisoryConfig = new AdminAdvisoryConfig();
        adminAdvisoryConfig.setEnableBanner(adminAdvisoryBannerDTO.getEnableBanner());
        adminAdvisoryConfig.setBannerContent(adminAdvisoryBannerDTO.getBannerContent());
        return adminAdvisoryConfig;
    }

    private APIError handleException(Exception e, AdminAdvisoryConstants.ErrorMessage errorEnum, Response.Status status,
                                     String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, buildErrorDescription(errorEnum,
                data));
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(AdminAdvisoryConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(AdminAdvisoryConstants.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            errorDescription = String.format(errorEnum.getDescription(), data);
        } else {
            errorDescription = errorEnum.getDescription();
        }

        return errorDescription;
    }
}

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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.advisory.mgt.dto.AdminAdvisoryBannerDTO;
import org.wso2.carbon.admin.advisory.mgt.exception.AdminAdvisoryMgtException;
import org.wso2.carbon.admin.advisory.mgt.service.AdminAdvisoryManagementService;
import org.wso2.carbon.identity.api.server.admin.advisory.management.common.AdminAdvisoryConstants;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model.AdminAdvisoryConfig;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Call internal osgi services to perform admin advisory management related operations.
 */
public class ServerAdminAdvisoryManagementService {

    private final AdminAdvisoryManagementService adminAdvisoryManagementService;
    private static final Log LOG = LogFactory.getLog(ServerAdminAdvisoryManagementService.class);

    public ServerAdminAdvisoryManagementService(AdminAdvisoryManagementService adminAdvisoryManagementService) {

        this.adminAdvisoryManagementService = adminAdvisoryManagementService;
    }

    /**
     * Get admin advisory configuration.
     *
     * @return Admin advisory configuration.
     */
    public AdminAdvisoryConfig getAdminAdvisoryConfig() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving admin advisory configuration.");
        }
        try {
            AdminAdvisoryBannerDTO adminAdvisoryBannerDTO = adminAdvisoryManagementService.getAdminAdvisoryConfig();
            AdminAdvisoryConfig response = buildAdminAdvisoryConfigResponse(adminAdvisoryBannerDTO);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved admin advisory configuration.");
            }
            return response;

        } catch (AdminAdvisoryMgtException e) {
            LOG.error("Error occurred while retrieving admin advisory configuration.", e);
            AdminAdvisoryConstants.ErrorMessage errorEnum =
                    AdminAdvisoryConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_BANNER_CONFIG;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Update admin advisory configuration.
     *
     * @param adminAdvisoryConfig Admin advisory configuration.
     */
    public void saveAdminAdvisoryConfig(AdminAdvisoryConfig adminAdvisoryConfig) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating admin advisory configuration.");
        }
        try {
            AdminAdvisoryBannerDTO modifiedAdminAdvisoryBannerDTO = createModifiedAdminAdvisoryBannerDTO(
                    adminAdvisoryManagementService.getAdminAdvisoryConfig(), adminAdvisoryConfig);

            adminAdvisoryManagementService.saveAdminAdvisoryConfig(modifiedAdminAdvisoryBannerDTO);
            LOG.info("Admin advisory configuration updated successfully.");

        } catch (AdminAdvisoryMgtException e) {
            LOG.error("Error occurred while updating admin advisory configuration.", e);
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

        if (ArrayUtils.isNotEmpty(data)) {
            return String.format(errorEnum.getDescription(), data);
        }
        return errorEnum.getDescription();
    }

    /**
     * Create AdminAdvisoryBannerDTO for given admin advisory configurations.
     *
     * @param adminAdvisoryConfig   Admin advisory configurations.
     * @return AdminAdvisoryBannerDTO.
     */
    private AdminAdvisoryBannerDTO createModifiedAdminAdvisoryBannerDTO(AdminAdvisoryBannerDTO
            currentAdminAdvisoryBannerDTO, AdminAdvisoryConfig adminAdvisoryConfig) throws AdminAdvisoryMgtException {

        AdminAdvisoryBannerDTO modifiedAdminAdvisoryBannerDTO = new AdminAdvisoryBannerDTO();
        boolean isEnableBanner;
        String bannerContent;

        if (adminAdvisoryConfig.getEnableBanner() == null) {
            isEnableBanner = currentAdminAdvisoryBannerDTO.getEnableBanner();
        } else {
            isEnableBanner = adminAdvisoryConfig.getEnableBanner();
        }

        if (StringUtils.isBlank(adminAdvisoryConfig.getBannerContent())) {
            bannerContent = currentAdminAdvisoryBannerDTO.getBannerContent();
        } else {
            bannerContent = adminAdvisoryConfig.getBannerContent();
        }

        modifiedAdminAdvisoryBannerDTO.setEnableBanner(isEnableBanner);
        modifiedAdminAdvisoryBannerDTO.setBannerContent(bannerContent);

        return modifiedAdminAdvisoryBannerDTO;
    }
}

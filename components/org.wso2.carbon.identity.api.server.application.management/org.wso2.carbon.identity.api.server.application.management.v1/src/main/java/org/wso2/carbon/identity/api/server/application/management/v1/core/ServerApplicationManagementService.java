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
package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.Link;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.ApiModelToServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.ApplicationBasicInfoToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.ServiceProviderToApiModel;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Calls internal osgi services to perform server application management related operations.
 */
public class ServerApplicationManagementService {

    private static final Log LOG = LogFactory.getLog(ServerApplicationManagementService.class);

    private static final List<String> SEARCH_SUPPORTED_FIELDS = new ArrayList<>();
    private static final String APP_NAME = "name";

    // Filter related constants.
    private static final String FILTER_STARTS_WITH = "sw";
    private static final String FILTER_ENDS_WITH = "ew";
    private static final String FILTER_EQUALS = "eq";
    private static final String FILTER_CONTAINS = "co";

    // TODO: should we read this from somewhere...
    private static final int DEFAULT_LIMIT = 20;
    private static final int DEFAULT_LIMIT_MAX = 50;

    static {
        SEARCH_SUPPORTED_FIELDS.add(APP_NAME);
    }

    public ApplicationListResponse getAllApplications(Integer limit, Integer offset, String filter, String sortOrder,
                                                      String sortBy, String requiredAttributes) {

        handleNotImplementedCapabilities(sortBy, sortOrder, requiredAttributes);

        // TODO: define default limit at swagger
        // TODO: define a default pagination max limit for identity data..
        limit = (limit != null && limit > 0 && limit <= DEFAULT_LIMIT_MAX) ? limit : DEFAULT_LIMIT;
        offset = (offset != null && offset > 0) ? offset : 0;

        // Format the filter to a value that can be interpreted by the backend.
        String formattedFilter = buildFilter(filter);
        try {
            String username = ContextLoader.getUsernameFromContext();
            String tenantDomain = ContextLoader.getTenantDomainFromContext();

            int totalResultsFromFiltering = getApplicationManagementService()
                    .getCountOfApplications(tenantDomain, username, formattedFilter);

            ApplicationBasicInfo[] filteredAppList = getApplicationManagementService()
                    .getApplicationBasicInfo(tenantDomain, username, formattedFilter, offset, limit);
            int resultsInCurrentPage = filteredAppList.length;

            return new ApplicationListResponse()
                    .totalResults(totalResultsFromFiltering)
                    .startIndex(offset)
                    .count(resultsInCurrentPage)
                    .applications(getApplicationListItems(filteredAppList))
                    .links(buildLinks(limit, offset, filter, totalResultsFromFiltering));

        } catch (IdentityApplicationManagementException e) {
            throw handleServerError(e, "Error while retrieving application basic info.");
        }
    }

    public ApplicationModel getApplication(String applicationId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            ServiceProvider application =
                    getApplicationManagementService().getApplicationByResourceId(applicationId, tenantDomain);
            if (application == null) {
                throw buildApiError(ErrorMessage.ERROR_CODE_APPLICATION_NOT_FOUND, applicationId, tenantDomain);
            }
            return new ServiceProviderToApiModel().apply(application);
        } catch (IdentityApplicationManagementException e) {
            throw handleServerError(e, "Error while retrieving application with id: " + applicationId);
        }
    }

    public void deleteApplication(String applicationId) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getApplicationManagementService().deleteApplicationByResourceId(applicationId, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            throw handleServerError(e, "Error while deleting application with id: " + applicationId);
        }
    }

    public void updateApplication(String applicationId, ApplicationModel applicationModel) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            ServiceProvider updatedApp = new ApiModelToServiceProvider().apply(applicationModel);
            getApplicationManagementService()
                    .updateApplicationByResourceId(applicationId, updatedApp, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            throw handleServerError(e, "Error while updating application with id: " + applicationId);
        }
    }

    public ApplicationModel createApplication(ApplicationModel applicationModel) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            ServiceProvider application = new ApiModelToServiceProvider().apply(applicationModel);
            ServiceProvider createdApp = getApplicationManagementService()
                    .createApplication(application, tenantDomain, username);
            return new ServiceProviderToApiModel().apply(createdApp);
        } catch (IdentityApplicationManagementException e) {
            throw handleServerError(e, "Error while updating application with name: " + applicationModel.getName());
        }
    }

    private List<Link> buildLinks(int limit, int currentOffset, String filter, int totalResultsFromSearch) {

        // TODO: prev and next
        return new ArrayList<>();
    }

    private APIError handleServerError(Exception e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();

        ErrorResponse errorResponse = builder
                .withMessage(e.getMessage())
                .build(LOG, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    private List<ApplicationListItem> getApplicationListItems(ApplicationBasicInfo[] allApplicationBasicInfo) {

        return Arrays.stream(allApplicationBasicInfo)
                .map(new ApplicationBasicInfoToApiModel())
                .collect(Collectors.toList());
    }

    private String buildFilter(String filter) {

        if (StringUtils.isNotBlank(filter)) {
            String[] filterArgs = filter.split(" ");
            if (filterArgs.length == 3) {
                String searchField = filterArgs[0];
                if (SEARCH_SUPPORTED_FIELDS.contains(searchField)) {
                    String searchOperation = filterArgs[1];
                    String searchValue = filterArgs[2];
                    return generateFilterStringForBackend(searchField, searchOperation, searchValue);
                } else {
                    throw buildApiError(ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, searchField);
                }

            } else {
                throw buildApiError(ErrorMessage.ERROR_CODE_INVALID_FILTER_FORMAT);
            }
        } else {
            return null;
        }
    }

    private String generateFilterStringForBackend(String searchField, String searchOperation, String searchValue) {

        // We do not have support for searching any fields other than the name. Therefore we simply format the search
        // value based on the search operation.
        // TODO: input validation for search value.
        String formattedFilter;
        switch (searchOperation) {
            case FILTER_STARTS_WITH:
                formattedFilter = searchValue + "*";
                break;
            case FILTER_ENDS_WITH:
                formattedFilter = "*" + searchValue;
                break;
            case FILTER_EQUALS:
                formattedFilter = searchValue;
                break;
            case FILTER_CONTAINS:
                formattedFilter = "*" + searchValue + "*";
                break;
            default:
                throw buildApiError(ErrorMessage.ERROR_CODE_INVALID_FILTER_OPERATION, searchOperation);
        }

        return formattedFilter;
    }

    private ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.getApplicationManagementService();
    }

    private void handleNotImplementedCapabilities(String sortOrder, String sortBy, String requiredAttributes) {

        ErrorMessage errorEnum = null;

        if (sortBy != null || sortOrder != null) {
            errorEnum = ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        } else if (requiredAttributes != null) {
            errorEnum = ErrorMessage.ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            throw buildApiError(errorEnum);
        }
    }

    private APIError buildApiError(ErrorMessage errorEnum) {

        ErrorResponse errorResponse = buildErrorResponse(errorEnum);
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private APIError buildApiError(ErrorMessage errorEnum,
                                   String... errorContextData) {

        ErrorResponse errorResponse = buildErrorResponse(errorEnum, errorContextData);
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private ErrorResponse buildErrorResponse(ErrorMessage errorEnum,
                                             String... errorContextData) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withDescription(buildFormattedDescription(errorEnum.getDescription(), errorContextData))
                .withMessage(errorEnum.getMessage())
                .build(LOG, errorEnum.getDescription());
    }

    private String buildFormattedDescription(String description, String... formatData) {

        if (formatData != null) {
            return String.format(description, formatData);
        } else {
            return description;
        }
    }
}

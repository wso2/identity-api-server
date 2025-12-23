/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationUnitOperationDetail;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationUnitOperationDetailSummary;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtClientException;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtException;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtServerException;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.models.OperationResponseDTO;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.models.UnitOperationResponseDTO;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.service.AsyncOperationStatusMgtService;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION_LIMIT_PARAMETER;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.FILTER_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.LIMIT_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.NEXT;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_AFTER;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_BEFORE;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PREVIOUS;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.UNIT_OPERATIONS;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil.buildAsyncStatusMgtClientException;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil.buildURIForPagination;

/**
 * Core service class for handling asynchronous operation status management APIs.
 */
public class AsyncOperationsApiServiceCore {

    private final AsyncOperationStatusMgtService asyncOperationStatusMgtService;

    private static final Log LOG = LogFactory.getLog(AsyncOperationsApiServiceCore.class);

    public AsyncOperationsApiServiceCore(AsyncOperationStatusMgtService asyncOperationStatusMgtService) {

        this.asyncOperationStatusMgtService = asyncOperationStatusMgtService;
        if (LOG.isDebugEnabled()) {
            LOG.debug("AsyncOperationsApiServiceCore initialized with AsyncOperationStatusMgtService.");
        }
    }

    public Response getOperations(String after, String before, Integer limit, String filter) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving operations for tenant: %s with parameters - after: %s, " +
                    "before: %s, limit: %s, filter: %s", tenantDomain, after, before, limit, filter));
        }
        try {
            limit = validateLimit(limit);
            List<OperationResponseDTO> records =
                    asyncOperationStatusMgtService.getOperations(tenantDomain, after, before, limit + 1, filter);
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Successfully retrieved %d operations for tenant: %s", 
                        records != null ? records.size() : 0, tenantDomain));
            }
            return Response.ok().entity(getOperationsResponse(limit, after, before, filter, records)).build();
        } catch (AsyncOperationStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncOperationStatusMgtException(e);
        }
    }

    public Response getOperation(String operationId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving operation with ID: %s for tenant: %s", operationId, tenantDomain));
        }
        try {
            OperationResponseDTO record = asyncOperationStatusMgtService.getOperation(operationId, tenantDomain);
            if (record == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Operation not found for ID: %s in tenant: %s", operationId, tenantDomain));
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok().entity(getOperationResponse(record)).build();
        } catch (AsyncOperationStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncOperationStatusMgtException(e);
        }
    }

    public Response getUnitOperation(String unitOperationId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving unit operation with ID: %s for tenant: %s", 
                    unitOperationId, tenantDomain));
        }
        try {
            UnitOperationResponseDTO record = asyncOperationStatusMgtService.getUnitOperation(unitOperationId,
                    tenantDomain);
            if (record == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Unit operation not found for ID: %s in tenant: %s", 
                            unitOperationId, tenantDomain));
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok().entity(getUnitOperationResponse(record)).build();
        } catch (AsyncOperationStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncOperationStatusMgtException(e);
        }
    }

    public Response getUnitOperations(String operationId, String after, String before,
                                      Integer limit, String filter) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving unit operations for operation ID: %s in tenant: %s with " +
                    "parameters - after: %s, before: %s, limit: %s, filter: %s", 
                    operationId, tenantDomain, after, before, limit, filter));
        }
        try {
            limit = validateLimit(limit);
            List<UnitOperationResponseDTO> records =
                    asyncOperationStatusMgtService.getUnitOperationStatusRecords(operationId, tenantDomain, after,
                            before, limit + 1, filter);
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Successfully retrieved %d unit operations for operation ID: %s in tenant: %s", 
                        records != null ? records.size() : 0, operationId, tenantDomain));
            }
            return Response.ok().entity(
                    getUnitOperationsResponse(limit, after, before, filter, records, operationId)).build();
        } catch (AsyncOperationStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncOperationStatusMgtException(e);
        }
    }

    private Operation getOperationResponse(OperationResponseDTO dto) {

        String resourcePath = PATH_SEPARATOR + dto.getOperationId() + PATH_SEPARATOR + UNIT_OPERATIONS;
        String url = "?" + LIMIT_PARAM + "=" + DEFAULT_LIMIT;

        Operation operation = new Operation();
        operation.setOperationId(dto.getOperationId());
        operation.setCorrelationId(dto.getCorrelationId());
        operation.setOperationType(dto.getOperationType());
        operation.setSubjectType(dto.getOperationSubjectType());
        operation.setSubjectId(dto.getOperationSubjectId());
        operation.setInitiatedOrgId(dto.getResidentOrgId());
        operation.setInitiatedUserId(dto.getInitiatorId());
        operation.setStatus(Operation.StatusEnum.valueOf(dto.getOperationStatus()));
        operation.setPolicy(dto.getOperationPolicy());
        operation.setCreatedTime(String.valueOf(dto.getCreatedTime()));
        operation.setModifiedTime(String.valueOf(dto.getModifiedTime()));

        OperationUnitOperationDetail unitOpDetail = new OperationUnitOperationDetail();
        unitOpDetail.setRef(URI.create(buildURIForPagination(url, resourcePath)));
        unitOpDetail.setSummary(getUnitOpSummary(dto));

        operation.setUnitOperationDetail(unitOpDetail);
        return operation;
    }

    private Operations getOperationsResponse(Integer limit, String after, String before, String filter,
                                             List<OperationResponseDTO> operationsDTO)
            throws AsyncOperationStatusMgtServerException {

        Operations response = new Operations();
        if (CollectionUtils.isNotEmpty(operationsDTO)) {
            boolean hasMoreItems = operationsDTO.size() > limit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                    (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String url = "?" + LIMIT_PARAM + "=" + limit;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    url += "&" + FILTER_PARAM + "=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Error encoding filter parameter for pagination URL", e);
                    throw new AsyncOperationStatusMgtServerException("Error encoding filter parameter");
                }
            }

            if (hasMoreItems) {
                operationsDTO.remove(operationsDTO.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(operationsDTO);
            }
            String resourcePath = PATH_SEPARATOR;
            if (!isFirstPage) {
                response.addLinksItem(createLink(operationsDTO.get(0).getCursorKey(), PAGINATION_BEFORE, PREVIOUS,
                        resourcePath, url));
            }
            if (!isLastPage) {
                response.addLinksItem(createLink(operationsDTO.get(operationsDTO.size() - 1).getCursorKey(),
                        PAGINATION_AFTER, NEXT, resourcePath, url));
            }

            List<Operation> operations = new ArrayList<>();
            for (OperationResponseDTO dto : operationsDTO) {
                operations.add(getOperationResponse(dto));
            }
            response.setOperations(operations);
        }
        return response;
    }

    private Link createLink(Integer cursor, String paginationOrder, String rel, String resourcePath, String url) {

        String encodedString = Base64.getEncoder().encodeToString(cursor.toString().getBytes(StandardCharsets.UTF_8));
        Link link = new Link();
        link.setHref(URI.create(
                buildURIForPagination(url, resourcePath) + "&" + paginationOrder + "="
                        + encodedString));
        link.setRel(rel);
        return link;
    }

    private OperationUnitOperationDetailSummary getUnitOpSummary(OperationResponseDTO dto) {

        OperationUnitOperationDetailSummary summary = new OperationUnitOperationDetailSummary();
        summary.setSuccess(dto.getUnitStatusCount().getSuccess());
        summary.setFailed(dto.getUnitStatusCount().getFailed());
        summary.setPartiallyCompleted(dto.getUnitStatusCount().getPartiallyCompleted());
        return summary;
    }

    private UnitOperation getUnitOperationResponse(UnitOperationResponseDTO record) {

        UnitOperation unitOperationRecord = new UnitOperation();
        unitOperationRecord.setUnitOperationId(record.getUnitOperationId());
        unitOperationRecord.setOperationId(record.getOperationId());
        unitOperationRecord.setResidentResourceId(record.getOperationInitiatedResourceId());
        unitOperationRecord.setTargetOrgId(record.getTargetOrgId());
        unitOperationRecord.setTargetOrgName(record.getTargetOrgName());
        unitOperationRecord.setStatus(UnitOperation.StatusEnum.valueOf(record.getUnitOperationStatus()));
        unitOperationRecord.setStatusMessage(record.getStatusMessage());
        unitOperationRecord.setCreatedTime(String.valueOf(record.getCreatedTime()));
        return unitOperationRecord;
    }

    private UnitOperations getUnitOperationsResponse(Integer limit, String after, String before, String filter,
                                                     List<UnitOperationResponseDTO> unitOperations, String operationId)
            throws AsyncOperationStatusMgtServerException {

        UnitOperations response = new UnitOperations();
        if (limit != 0 && CollectionUtils.isNotEmpty(unitOperations)) {
            boolean hasMoreItems = unitOperations.size() > limit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                    (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String url = "?" + LIMIT_PARAM + "=" + limit;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    url += "&" + FILTER_PARAM + "=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Error encoding filter parameter for unit operations pagination URL", e);
                    throw new AsyncOperationStatusMgtServerException("Error encoding filter parameter");
                }
            }

            if (hasMoreItems) {
                unitOperations.remove(unitOperations.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(unitOperations);
            }
            String resourcePath = PATH_SEPARATOR + operationId + PATH_SEPARATOR + "unit-operations";
            if (!isFirstPage) {
                response.addLinksItem(createLink(unitOperations.get(0).getCursorKey(), PAGINATION_BEFORE, PREVIOUS,
                        resourcePath, url));
            }
            if (!isLastPage) {
                response.addLinksItem(createLink(unitOperations.get(unitOperations.size() - 1).getCursorKey(),
                        PAGINATION_AFTER, NEXT, resourcePath, url));
            }

            List<UnitOperation> unitOperationList = new ArrayList<>();
            for (UnitOperationResponseDTO dto : unitOperations) {
                unitOperationList.add(getUnitOperationResponse(dto));
            }
            response.setUnitOperations(unitOperationList);
        }
        return response;
    }

    private int validateLimit(Integer limit) throws AsyncOperationStatusMgtClientException {

        if (limit == null) {
            int defaultItemsPerPage = IdentityUtil.getDefaultItemsPerPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit is null. Therefore the default limit is set to %s.",
                        defaultItemsPerPage));
            }
            return defaultItemsPerPage;
        }

        if (limit <= 0) {
            throw buildAsyncStatusMgtClientException(ERROR_CODE_INVALID_PAGINATION_LIMIT_PARAMETER);
        }

        int maximumItemsPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit > maximumItemsPerPage) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit exceeds the maximum limit. Therefore the limit is set to %s.",
                        maximumItemsPerPage));
            }
            return maximumItemsPerPage;
        }
        return limit;
    }
}

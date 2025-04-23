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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtClientException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtServerException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.models.OperationResponseDTO;
import org.wso2.carbon.identity.framework.async.status.mgt.api.models.UnitOperationResponseDTO;
import org.wso2.carbon.identity.framework.async.status.mgt.api.service.AsyncStatusMgtService;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.FILTER_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.LIMIT_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.NEXT;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_AFTER;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_BEFORE;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PREVIOUS;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil.buildAsyncStatusMgtClientException;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil.buildURIForPagination;

/**
 * Core service class for handling asynchronous operation status management APIs.
 */
public class AsyncOperationsApiServiceCore {

    private final AsyncStatusMgtService asyncStatusMgtService;

    private static final Log LOG = LogFactory.getLog(AsyncOperationsApiServiceCore.class);

    public AsyncOperationsApiServiceCore(AsyncStatusMgtService asyncStatusMgtService) {

        this.asyncStatusMgtService = asyncStatusMgtService;
    }

    public Response getOperations(String after, String before, Integer limit, String filter) {

        PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        context.getUserId(); //todo: complete operateInitiatorId

        try {
            limit = validateLimit(limit);
            List<OperationResponseDTO> records =
                    asyncStatusMgtService.getOperations(after, before, limit + 1, filter);
            return Response.ok().entity(getOperationsResponse(limit, after, before, filter, records)).build();
        } catch (AsyncStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncStatusMgtException(e);
        }
    }

    public Response getOperation(String operationId) {

        PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        context.getUserId(); //todo: complete operateInitiatorId

        if (operationId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            OperationResponseDTO record = asyncStatusMgtService.getOperation(operationId);
            if (record == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok().entity(getOperationResponse(operationId, record)).build();
        } catch (AsyncStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncStatusMgtException(e);
        }
    }

    public Response getUnitOperation(String unitOperationId) {

        PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        context.getUserId(); //todo: complete operateInitiatorId

        if (unitOperationId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            UnitOperationResponseDTO record = asyncStatusMgtService.getUnitOperation(unitOperationId);
            if (record == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok().entity(getUnitOperationResponse(record)).build();
        } catch (AsyncStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncStatusMgtException(e);
        }
    }

    public Response getUnitOperations(String operationId, String after, String before,
                                      Integer limit, String filter) {
        if (operationId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            limit = validateLimit(limit);
            List<UnitOperationResponseDTO> records =
                    asyncStatusMgtService.getUnitOperationStatusRecords(operationId, after, before, limit + 1,
                            filter);
            return Response.ok().entity(
                    getUnitOperationsResponse(limit, after, before, filter, records, operationId)).build();
        } catch (AsyncStatusMgtException e) {
            throw AsyncOperationStatusEndpointUtil.handleAsyncStatusMgtException(e);
        }
    }

    private UnitOperation getUnitOperationResponse(UnitOperationResponseDTO record) {

        UnitOperation unitOperationRecord = new UnitOperation();
        unitOperationRecord.setUnitOperationId(record.getUnitOperationId());
        unitOperationRecord.setOperationId(record.getOperationId());
        unitOperationRecord.setResidentResourceId(record.getOperationInitiatedResourceId());
        unitOperationRecord.setTargetOrgId(record.getTargetOrgId());
        unitOperationRecord.setStatus(UnitOperation.StatusEnum.valueOf(record.getUnitOperationStatus()));
        unitOperationRecord.setStatusMessage(record.getStatusMessage());
        unitOperationRecord.setCreatedTime(String.valueOf(record.getCreatedTime()));
        return unitOperationRecord;
    }

    private Operation getOperationResponse(String operationId, OperationResponseDTO dto) {

        String resourcePath = PATH_SEPARATOR + operationId + PATH_SEPARATOR + "unit-operations";
        String url = "?" + LIMIT_PARAM + "=" + "10";

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
        operation.setUnitOperationRef(URI.create(buildURIForPagination(url, resourcePath)));
        return operation;
    }

    private Operations getOperationsResponse(Integer limit, String after, String before, String filter,
                                             List<OperationResponseDTO> operationsDTO)
            throws AsyncStatusMgtServerException {

        Operations operationsResponse = new Operations();

        if (limit != 0 && CollectionUtils.isNotEmpty(operationsDTO)) {
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
                    throw new AsyncStatusMgtServerException("Error");
                }
            }

            if (hasMoreItems) {
                operationsDTO.remove(operationsDTO.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(operationsDTO);
            }
            if (!isFirstPage) {
                Timestamp createdTimestamp = operationsDTO.get(0).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR;
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_BEFORE + "="
                                + encodedString));
                link.setRel(PREVIOUS);
                operationsResponse.addLinksItem(link);
            }
            if (!isLastPage) {
                Timestamp createdTimestamp = operationsDTO.get(operationsDTO.size() - 1).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR;
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_AFTER + "="
                                + encodedString));
                link.setRel(NEXT);
                operationsResponse.addLinksItem(link);
            }

            List<Operation> operations = new ArrayList<>();

            for (OperationResponseDTO dto : operationsDTO) {
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

                String resourcePath = PATH_SEPARATOR + dto.getOperationId() + PATH_SEPARATOR + "unit-operations";
                String uri = "?" + LIMIT_PARAM + "=" + "10";
                operation.setUnitOperationRef(URI.create(buildURIForPagination(uri, resourcePath)));
                operations.add(operation);
            }
            operationsResponse.setOperations(operations);
        }
        return operationsResponse;
    }

    private UnitOperations getUnitOperationsResponse(Integer limit, String after, String before, String filter,
                                                     List<UnitOperationResponseDTO> unitOperations,
                                                     String operationId)
            throws AsyncStatusMgtServerException {

        UnitOperations unitOperationsResponse = new UnitOperations();

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
                    throw new AsyncStatusMgtServerException("Error");
                }
            }

            if (hasMoreItems) {
                unitOperations.remove(unitOperations.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(unitOperations);
            }
            if (!isFirstPage) {
                Timestamp createdTimestamp = unitOperations.get(0).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR + operationId + PATH_SEPARATOR + "unit-operations";
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_BEFORE + "="
                                + encodedString));
                link.setRel(PREVIOUS);
                unitOperationsResponse.addLinksItem(link);
            }
            if (!isLastPage) {
                Timestamp createdTimestamp = unitOperations.get(unitOperations.size() - 1).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR + operationId + PATH_SEPARATOR + "unit-operations";
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_AFTER + "="
                                + encodedString));
                link.setRel(NEXT);
                unitOperationsResponse.addLinksItem(link);
            }

            List<UnitOperation> unitOperationList = new ArrayList<>();
            for (UnitOperationResponseDTO dto : unitOperations) {
                UnitOperation unitOperation = new UnitOperation();
                unitOperation.setUnitOperationId(dto.getUnitOperationId());
                unitOperation.setOperationId(dto.getOperationId());
                unitOperation.setResidentResourceId(dto.getOperationInitiatedResourceId());
                unitOperation.setTargetOrgId(dto.getTargetOrgId());
                unitOperation.setStatus(UnitOperation.StatusEnum.valueOf(dto.getUnitOperationStatus()));
                unitOperation.setStatusMessage(dto.getStatusMessage());
                unitOperation.setCreatedTime(String.valueOf(dto.getCreatedTime()));
                unitOperationList.add(unitOperation);
            }
            unitOperationsResponse.setUnitOperations(unitOperationList);
        }
        return unitOperationsResponse;
    }

    private int validateLimit(Integer limit) throws AsyncStatusMgtClientException {

        if (limit == null) {
            int defaultItemsPerPage = IdentityUtil.getDefaultItemsPerPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit is null. Therefore the default limit is set to %s.",
                        defaultItemsPerPage));
            }
            return defaultItemsPerPage;
        }

        if (limit < 0) {
            throw buildAsyncStatusMgtClientException(ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT);
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

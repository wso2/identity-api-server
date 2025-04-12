package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationRecord;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationRecordsResponse;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtServerException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.models.ResponseOperationRecord;
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

import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.FILTER_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.LIMIT_PARAM;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.NEXT;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_AFTER;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PAGINATION_BEFORE;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PREVIOUS;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util.AsyncOperationStatusEndpointUtil.buildURIForPagination;

/**
 * Core service class for handling asynchronous operation status management APIs.
 */
public class AsyncOperationStatusApiServiceCore {

    private final AsyncStatusMgtService asyncStatusMgtService;

    public AsyncOperationStatusApiServiceCore(AsyncStatusMgtService asyncStatusMgtService) {

        this.asyncStatusMgtService = asyncStatusMgtService;
    }

    public Response getOperations(String operationSubjectType, String operationSubjectId, String operationType,
                                  String after, String before, Integer limit, String filter) {

        PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        context.getUserId(); //todo: complete operateInitiatorId

        if (operationSubjectType == null || operationSubjectId == null || operationType == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            List<ResponseOperationRecord> records =
                    asyncStatusMgtService.getOperationStatusRecords(operationSubjectType, operationSubjectId,
                            operationType, after, before, limit + 1, filter);
            return Response.ok().entity(getOperationResponse(limit, after, before, filter, records, operationSubjectType, operationSubjectId, operationType)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

//    public Response getOperationsForAsyncJob(String correlationId, String after, String before, Integer limit,
//                                             String filter){
//        if (correlationId == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        try {
//            List<ResponseOperationRecord> records =
//                    asyncStatusMgtService.get(operationSubjectType, operationSubjectId,
//                            operationType, after, before, limit + 1, filter);
//            return Response.ok().entity(getOperationResponse(limit, after, before, filter, records, operationSubjectType, operationSubjectId, operationType)).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    private OperationRecordsResponse getOperationResponse(Integer limit, String after, String before, String filter, List<ResponseOperationRecord> operations, String operationSubjectType, String operationSubjectId, String operationType)
            throws AsyncStatusMgtServerException {

        OperationRecordsResponse operationsResponse = new OperationRecordsResponse();

        if (limit != 0 && CollectionUtils.isNotEmpty(operations)) {
            boolean hasMoreItems = operations.size() > limit;
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
                operations.remove(operations.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(operations);
            }
            if (!isFirstPage) {
                Timestamp createdTimestamp = operations.get(0).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR + "subject-types" + PATH_SEPARATOR + operationSubjectType +
                        PATH_SEPARATOR + "subject" + PATH_SEPARATOR + operationSubjectId + PATH_SEPARATOR +
                        "operation-type" + PATH_SEPARATOR + operationType;
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_BEFORE + "="
                                + encodedString));
                link.setRel(PREVIOUS);
                operationsResponse.addLinksItem(link);
            }
            if (!isLastPage) {
                Timestamp createdTimestamp = operations.get(operations.size() - 1).getCreatedTime();
                String encodedString = Base64.getEncoder().encodeToString(createdTimestamp.toString()
                        .getBytes(StandardCharsets.UTF_8));
                String resourcePath = PATH_SEPARATOR + "subject-types" + PATH_SEPARATOR + operationSubjectType +
                        PATH_SEPARATOR + "subject" + PATH_SEPARATOR + operationSubjectId + PATH_SEPARATOR +
                        "operation-type" + PATH_SEPARATOR + operationType;
                Link link = new Link();
                link.setHref(URI.create(
                        buildURIForPagination(url, resourcePath) + "&" + PAGINATION_AFTER + "="
                                + encodedString));
                link.setRel(NEXT);
                operationsResponse.addLinksItem(link);
            }

            List<OperationRecord> operationRecordsDTO = new ArrayList<>();
            for (ResponseOperationRecord record : operations) {
                OperationRecord operationRecordDTO = new OperationRecord();
                operationRecordDTO.setOperationId(record.getOperationId());
                operationRecordDTO.setCorrelationId(record.getCorrelationId());
                operationRecordDTO.setOperationType(record.getOperationType());
                operationRecordDTO.setOperationSubjectType(record.getOperationSubjectType());
                operationRecordDTO.setOperationSubjectId(record.getOperationSubjectId());
                operationRecordDTO.setResidentOrgId(record.getResidentOrgId());
                operationRecordDTO.setInitiatorId(record.getInitiatorId());
                operationRecordDTO.setOperationStatus(
                        OperationRecord.OperationStatusEnum.valueOf(record.getOperationStatus()));
                operationRecordDTO.setOperationPolicy(record.getOperationPolicy());
                operationRecordDTO.setCreatedTime(String.valueOf(record.getCreatedTime()));
                operationRecordDTO.setModifiedTime(String.valueOf(record.getModifiedTime()));
                operationRecordsDTO.add(operationRecordDTO);
            }
            operationsResponse.setOperationRecords(operationRecordsDTO);
        }
        return operationsResponse;
    }

}

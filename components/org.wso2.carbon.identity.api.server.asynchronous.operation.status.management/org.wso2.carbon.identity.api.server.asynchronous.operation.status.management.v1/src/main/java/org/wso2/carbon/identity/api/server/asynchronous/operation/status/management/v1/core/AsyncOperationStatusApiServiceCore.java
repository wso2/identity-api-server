package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core;

import org.wso2.carbon.identity.framework.async.status.mgt.AsyncStatusMgtService;
import org.wso2.carbon.identity.framework.async.status.mgt.models.dos.ResponseOperationRecord;

import java.util.List;

import javax.ws.rs.core.Response;

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

        if (operationSubjectType == null || operationSubjectId == null || operationType == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            List<ResponseOperationRecord> records =
                    asyncStatusMgtService.getOperationStatusRecords(operationSubjectType, operationSubjectId,
                            operationType, after, before, limit, filter);
            return Response.ok().entity(records).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

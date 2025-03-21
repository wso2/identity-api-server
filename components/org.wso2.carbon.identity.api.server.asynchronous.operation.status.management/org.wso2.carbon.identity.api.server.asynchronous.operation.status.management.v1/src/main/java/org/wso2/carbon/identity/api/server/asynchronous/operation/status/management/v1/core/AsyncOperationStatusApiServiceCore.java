package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core;

import org.wso2.carbon.identity.framework.async.status.mgt.AsyncStatusMgtService;
import org.wso2.carbon.identity.framework.async.status.mgt.models.dos.ResponseOperationRecord;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Core service class for handling asynchronous operation status management APIs.
 */
public class AsyncOperationStatusApiServiceCore {

    private final AsyncStatusMgtService asyncStatusMgtService;

    public AsyncOperationStatusApiServiceCore(AsyncStatusMgtService asyncStatusMgtService) {

        this.asyncStatusMgtService = asyncStatusMgtService;
    }

    public Response getAsyncOperationStatus(String operationSubjectType, String operationSubjectId,
                                            String operationType) {

        if (operationSubjectType == null | operationSubjectId == null | operationType == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            List<ResponseOperationRecord> records =
                    asyncStatusMgtService.getAsyncOperationStatusWithoutCurser(operationSubjectType, operationSubjectId,
                            operationType);
            return Response.ok().entity(records).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

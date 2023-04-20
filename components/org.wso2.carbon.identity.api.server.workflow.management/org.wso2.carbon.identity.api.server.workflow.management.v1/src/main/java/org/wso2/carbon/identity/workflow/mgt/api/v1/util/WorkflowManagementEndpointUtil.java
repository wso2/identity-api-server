//package org.wso2.carbon.identity.workflow.mgt.api.v1.util;
//
//import org.apache.commons.httpclient.URI;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.wso2.carbon.identity.workflow.mgt.api.v1.exceptions.WorkflowManagementEndpointException;
//
//import javax.ws.rs.core.Response;
//
//public class WorkflowManagementEndpointUtil {
//
//    private static final Log LOG = LogFactory.getLog(WorkflowManagementEndpointUtil.class);
//
//    /**
//     * Handles the response for client errors.
//     *
//     * @param e   The client exception thrown.
//     * @param log The logger.
//     * @return The response for the client error.
//     */
//    public static Response handleClientErrorResponse(WorkflowManagementEndpointException e, Log log) {
//
//
//        throw buildException(Response.Status.BAD_REQUEST, log, e);
//    }
//
//
//
//    /**
//     * Handles the response for server errors.
//     *
//     * @param e   The server exception thrown.
//     * @param log The logger.
//     * @return The response for the server error.
//     */
//    public static Response handleServerErrorResponse(WorkflowManagementEndpointException e, Log log) {
//
//        throw buildException(Response.Status.INTERNAL_SERVER_ERROR, log, e);
//    }
//
//    private static WorkflowManagementEndpointException buildException(Response.Status status, Log log,
//                                                                      WorkflowManagementEndpointException e) {
//
//        if (e instanceof WorkflowManagementEndpointException) {
//            logDebug(log, e);
//        } else {
//            logError(log, e);
//        }
//        return new WorkflowManagementEndpointException(status);
//    }
//
//    /**
//     * Returns a generic error object.
//     *
//     * @param errorCode        The error code.
//     * @param errorMessage     The error message.
//     * @param errorDescription The error description.
//     * @return A generic error with the specified details.
//     */
//    public static Error getError(String errorCode, String errorMessage, String errorDescription) {
//
//        Error error = new Error();
//        error.setCode(errorCode);
//        error.setMessage(errorMessage);
//        error.setDescription(errorDescription);
//        return error;
//    }
//
//    private static void logDebug(Log log, WorkflowManagementEndpointException e) {
//
//        if (log.isDebugEnabled()) {
//            String errorMessageFormat = "errorCode: %s | message: %s";
//            String errorMessage = String.format(errorMessageFormat, e.getErrorCode(), e.getDescription());
//            log.debug(errorMessage, e);
//        }
//    }
//
//    private static void logError(Log log, WorkflowManagementEndpointException e) {
//
//        String errorMessageFormat = "errorCode: %s | message: %s";
//        String errorMessage = String.format(errorMessageFormat, e.getErrorCode(), e.getDescription());
//        log.error(errorMessage, e);
//    }
//
//
//
//}
package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util;

import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ORGANIZATION_PATH;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;

/**
 * This class provides util functions to the asynchronous operation status endpoint.
 */
public class AsyncOperationStatusEndpointUtil {
    public static String buildURIForPagination(String paginationURL, String resourcePath) {

        return buildURIForBody(PATH_SEPARATOR + V1_API_PATH_COMPONENT + PATH_SEPARATOR
                + ORGANIZATION_PATH + resourcePath + paginationURL).toString();
    }
}

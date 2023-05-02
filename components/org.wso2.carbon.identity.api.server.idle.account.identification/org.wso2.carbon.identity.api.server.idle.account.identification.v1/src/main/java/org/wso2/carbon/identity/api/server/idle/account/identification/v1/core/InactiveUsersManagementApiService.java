package org.wso2.carbon.identity.api.server.idle.account.identification.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idle.account.identification.common.IdleAccountIdentificationServiceHolder;

import org.wso2.carbon.identity.api.server.idle.account.identification.common.util.IdleAccountIdentificationConstants;
import org.wso2.carbon.identity.api.server.idle.account.identification.v1.model.InactiveUser;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccIdentificationClientException;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccIdentificationException;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccIdentificationServerException;
import org.wso2.carbon.identity.idle.account.identification.models.InactiveUserModel;
import static org.wso2.carbon.identity.api.server.idle.account.identification.common.util.IdleAccountIdentificationConstants.ErrorMessage.ERROR_REQUIRED_PARAMETER_MISSING;
import static org.wso2.carbon.identity.api.server.idle.account.identification.common.util.IdleAccountIdentificationConstants.ErrorMessage.ERROR_RETRIEVING_INACTIVE_USERS;
import static org.wso2.carbon.identity.api.server.idle.account.identification.common.util.Utils.getCorrelation;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Calls internal osgi services to perform idle account identification management related operations.
 */
public class InactiveUsersManagementApiService {

    private static final Log LOGGER = LogFactory.getLog(InactiveUsersManagementApiService.class);

    /**
     * Get inactive users.
     *
     * @param inactiveAfter Latest active date of login.
     * @param excludeBefore Date to exclude the oldest inactive users.
     * @param tenantDomain  Tenant domain.
     * @return List of inactive users.
     */
    public List<InactiveUser> getInactiveUsers(String inactiveAfter, String excludeBefore, String tenantDomain) {

        List<InactiveUserModel> inactiveUsers = null;
        try {
            inactiveUsers = IdleAccountIdentificationServiceHolder.getIdleAccountIdentificationService()
                    .getInactiveUsers(inactiveAfter, excludeBefore, tenantDomain);
            return buildResponse(inactiveUsers);
        } catch (IdleAccIdentificationException e) {
            throw handleIdleAccIdentificationException(e, ERROR_RETRIEVING_INACTIVE_USERS, tenantDomain);
        }
    }

    /**
     * Handle IdleAccIdentificationException.
     *
     * @param exception IdleAccIdentificationException.
     * @param errorEnum Error message.
     * @param data      Context data.
     * @return APIError.
     */
    private APIError handleIdleAccIdentificationException(IdleAccIdentificationException exception,
                                                          IdleAccountIdentificationConstants.ErrorMessage errorEnum,
                                                          String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof IdleAccIdentificationClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception.getMessage());
            if (exception.getErrorCode() != null) {
                errorResponse.setCode(exception.getErrorCode());
            }
            errorResponse.setDescription(exception.getMessage());
            errorResponse.setRef(getCorrelation());
            if (StringUtils.isNotEmpty(exception.getDescription())) {
                errorResponse.setMessage(exception.getMessage());
                errorResponse.setDescription(exception.getDescription());
            }
            if (ERROR_REQUIRED_PARAMETER_MISSING.getCode().equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof IdleAccIdentificationServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception, errorEnum.getDescription());
            if (exception.getErrorCode() != null) {
                errorResponse.setCode(exception.getErrorCode());
            }
            errorResponse.setDescription(exception.getMessage());
            errorResponse.setRef(getCorrelation());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception, errorEnum.getDescription());
            errorResponse.setRef(getCorrelation());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new APIError(status, errorResponse);
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(IdleAccountIdentificationConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private String includeData(IdleAccountIdentificationConstants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }

    private List<InactiveUser> buildResponse(List<InactiveUserModel> inactiveUserModels) {

        List<InactiveUser> inactiveUserList = new ArrayList<>();
        for (InactiveUserModel inactiveUserModel : inactiveUserModels) {
            InactiveUser inactiveUser = new InactiveUser();
            inactiveUser.setUsername(inactiveUserModel.getUsername());
            inactiveUser.setUserStoreDomain(inactiveUserModel.getUserStoreDomain());
            inactiveUser.setEmail(inactiveUserModel.getEmail());
            inactiveUserList.add(inactiveUser);
        }
        return inactiveUserList;
    }
}

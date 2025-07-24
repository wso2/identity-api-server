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

package org.wso2.carbon.identity.api.expired.password.identification.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.expired.password.identification.common.util.
        ExpiredPasswordIdentificationConstants.ErrorMessage;
import org.wso2.carbon.identity.api.expired.password.identification.v1.model.PasswordExpiredUser;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authentication.framework.exception.PostAuthenticationFailedException;
import org.wso2.carbon.identity.password.expiry.exceptions.ExpiredPasswordIdentificationClientException;
import org.wso2.carbon.identity.password.expiry.exceptions.ExpiredPasswordIdentificationException;
import org.wso2.carbon.identity.password.expiry.exceptions.ExpiredPasswordIdentificationServerException;
import org.wso2.carbon.identity.password.expiry.models.PasswordExpiredUserModel;
import org.wso2.carbon.identity.password.expiry.services.ExpiredPasswordIdentificationService;
import org.wso2.carbon.identity.password.expiry.util.PasswordPolicyUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.expired.password.identification.common.util.
        ExpiredPasswordIdentificationConstants.DATE_EXCLUDE_AFTER;
import static org.wso2.carbon.identity.api.expired.password.identification.common.util.
        ExpiredPasswordIdentificationConstants.DATE_EXPIRED_AFTER;
import static org.wso2.carbon.identity.api.expired.password.identification.common.util.
        ExpiredPasswordIdentificationConstants.DATE_FORMAT_REGEX;

/**
 * Calls internal osgi services to perform password expired user identification management related operations.
 */
public class PasswordExpiredUsersManagementApiService {

    private final ExpiredPasswordIdentificationService expiredPasswordIdentificationService;
    private static final Log LOG = LogFactory.getLog(PasswordExpiredUsersManagementApiService.class);

    public PasswordExpiredUsersManagementApiService(
            ExpiredPasswordIdentificationService expiredPasswordIdentificationService) {

        this.expiredPasswordIdentificationService = expiredPasswordIdentificationService;
    }

    /**
     * Get password expired users.
     *
     * @param expiredAfter The date after which passwords will expire.
     * @param excludeAfter The date after which should be excluded.
     * @param tenantDomain  Tenant domain.
     * @return  List of password expired users.
     */
    public List<PasswordExpiredUser> getPasswordExpiredUsers(
            String expiredAfter, String excludeAfter, String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting password expired users for tenant: " + tenantDomain);
        }
        List<PasswordExpiredUserModel> passwordExpiredUsers = null;
        try {
            validateDates(expiredAfter, excludeAfter);
            validatePasswordExpiryFeatureEnabled(tenantDomain);
            LocalDateTime expiredAfterDate = convertToDateObject(expiredAfter, DATE_EXPIRED_AFTER);
            LocalDateTime excludeAfterDate = convertToDateObject(excludeAfter, DATE_EXCLUDE_AFTER);
            if (excludeAfterDate == null) {
                passwordExpiredUsers = expiredPasswordIdentificationService
                        .getPasswordExpiredUsersFromSpecificDate(expiredAfterDate, tenantDomain);
            } else {
                passwordExpiredUsers = expiredPasswordIdentificationService
                        .getPasswordExpiredUsersBetweenSpecificDates(expiredAfterDate, excludeAfterDate, tenantDomain);
            }
            List<PasswordExpiredUser> result = buildResponse(passwordExpiredUsers);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieved " + (result != null ? result.size() : 0) + 
                         " password expired users for tenant: " + tenantDomain);
            }
            return result;
        } catch (ExpiredPasswordIdentificationException e) {
            throw handleExpiredPasswordIdentificationException(e,
                    ErrorMessage.ERROR_RETRIEVING_PASSWORD_EXPIRED_USERS, tenantDomain);
        }
    }

    /**
     * Validate the dates.
     *
     * @param expiredAfter ExpiredAfter date.
     * @param excludeAfter ExcludeAfter date.
     * @throws ExpiredPasswordIdentificationClientException ExpiredPasswordIdentificationClientException.
     */
    private void validateDates(String expiredAfter, String excludeAfter) throws
            ExpiredPasswordIdentificationClientException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating date parameters: expiredAfter=" + expiredAfter + ", excludeAfter=" + excludeAfter);
        }
        // Validate the date format.
        validateDateFormat(expiredAfter, DATE_EXPIRED_AFTER);
        if (StringUtils.isNotEmpty(excludeAfter)) {
            validateDateFormat(excludeAfter, DATE_EXCLUDE_AFTER);
        }
    }

    /**
     * Validate the format of the date.
     *
     * @param dateString Date as a string.
     * @param dateType   Date type.
     * @throws ExpiredPasswordIdentificationClientException ExpiredPasswordIdentificationClientException.
     */
    private void validateDateFormat(String dateString, String dateType) throws
            ExpiredPasswordIdentificationClientException {

        if (Pattern.matches(DATE_FORMAT_REGEX, dateString)) {
            return;
        }
        ErrorMessage error = ErrorMessage.ERROR_DATE_REGEX_MISMATCH;
        throw new ExpiredPasswordIdentificationClientException(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), dateType));
    }

    /**
     * Convert date string into LocalDateTime object.
     *
     * @param dateString Date as a string.
     * @param dateType   Date type.
     * @throws ExpiredPasswordIdentificationClientException ExpiredPasswordIdentificationClientException.
     * @return LocalDateTime of the date.
     */
    private LocalDateTime convertToDateObject(String dateString, String dateType)
            throws ExpiredPasswordIdentificationClientException {

        try {
            if (StringUtils.isEmpty(dateString)) {
                return null;
            }
            return LocalDate.parse(dateString).atStartOfDay();
        } catch (DateTimeParseException e) {
            ErrorMessage error = ErrorMessage.ERROR_INVALID_DATE;
            throw new ExpiredPasswordIdentificationClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), dateType));
        }
    }

    /**
     * Build the password expired users list.
     *
     * @param passwordExpiredUserModels List of password expired users.
     * @return List of password expired users.
     */
    private List<PasswordExpiredUser> buildResponse(List<PasswordExpiredUserModel> passwordExpiredUserModels) {

        List<PasswordExpiredUser> passwordExpiredUsers = new ArrayList<>();
        for (PasswordExpiredUserModel passwordExpiredUserModel : passwordExpiredUserModels) {
            PasswordExpiredUser passwordExpiredUser = new PasswordExpiredUser();
            passwordExpiredUser.setUsername(passwordExpiredUserModel.getUsername());
            passwordExpiredUser.setUserStoreDomain(passwordExpiredUserModel.getUserStoreDomain());
            passwordExpiredUser.setUserId(passwordExpiredUserModel.getUserId());
            passwordExpiredUsers.add(passwordExpiredUser);
        }
        return passwordExpiredUsers;
    }

    /**
     * Handle ExpiredPasswordIdentificationException.
     *
     * @param exception ExpiredPasswordIdentificationException.
     * @param errorEnum Error message.
     * @param data      Context data.
     * @return APIError.
     */
    private APIError handleExpiredPasswordIdentificationException(ExpiredPasswordIdentificationException exception,
                                                                  ErrorMessage errorEnum,
                                                                  String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof ExpiredPasswordIdentificationClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, exception.getMessage());
            if (exception.getErrorCode() != null) {
                errorResponse.setCode(exception.getErrorCode());
            }
            errorResponse.setDescription(exception.getMessage());
            if (StringUtils.isNotEmpty(exception.getDescription())) {
                errorResponse.setMessage(exception.getMessage());
                errorResponse.setDescription(exception.getDescription());
            }
            if (ErrorMessage.ERROR_REQUIRED_PARAMETER_MISSING.getCode().equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else if (ErrorMessage.PASSWORD_EXPIRY_FEATURE_NOT_ENABLED.getCode().equals(exception.getErrorCode())) {
                status = Response.Status.METHOD_NOT_ALLOWED;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof ExpiredPasswordIdentificationServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, exception, errorEnum.getDescription());
            if (exception.getErrorCode() != null) {
                errorResponse.setCode(exception.getErrorCode());
            }
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, exception, errorEnum.getDescription());
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
    private ErrorResponse.Builder getErrorBuilder(ErrorMessage errorMsg, String data) {

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
    private String includeData(ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }

    /**
     * Validate whether password expiry feature is enabled.
     *
     * @param tenantDomain  Tenant Domain.
     * @throws ExpiredPasswordIdentificationException if password expiry feature is not enabled.
     */
    private void validatePasswordExpiryFeatureEnabled (String tenantDomain)
            throws ExpiredPasswordIdentificationException {

        try {
            if (!PasswordPolicyUtils.isPasswordExpiryEnabled(tenantDomain)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Password expiry feature is not enabled for tenant: " + tenantDomain);
                }
                ErrorMessage error = ErrorMessage.PASSWORD_EXPIRY_FEATURE_NOT_ENABLED;
                throw new ExpiredPasswordIdentificationClientException(error.getCode(), error.getMessage(),
                        error.getDescription());
            }
        } catch (PostAuthenticationFailedException e) {
            LOG.error("Error occurred while validating password expiry feature status for tenant: " + tenantDomain, e);
            throw new ExpiredPasswordIdentificationServerException(e);
        }
    }
}

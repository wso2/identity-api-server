/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.idle.account.identification.v1.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.idle.account.identification.common.IdleAccountIdentificationServiceHolder;
import org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants;
import org.wso2.carbon.identity.api.idle.account.identification.v1.model.InactiveUser;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccountIdentificationClientException;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccountIdentificationException;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccountIdentificationServerException;
import org.wso2.carbon.identity.idle.account.identification.models.InactiveUserModel;
import org.wso2.carbon.identity.idle.account.identification.services.IdleAccountIdentificationService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.DATE_EXCLUDE_BEFORE;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.DATE_FORMAT_REGEX;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.DATE_INACTIVE_AFTER;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.ErrorMessage;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.FALSE_VALUE;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.IS_DISABLED;
import static org.wso2.carbon.identity.api.idle.account.identification.common.util.IdleAccountIdentificationConstants.TRUE_VALUE;

/**
 * Calls internal osgi services to perform idle account identification management related operations.
 */
public class InactiveUsersManagementApiService {

    private final IdleAccountIdentificationService idleAccountIdentificationService;
    private static final Log LOG = LogFactory.getLog(InactiveUsersManagementApiService.class);

    public InactiveUsersManagementApiService(IdleAccountIdentificationService idleAccountIdentificationService) {

        this.idleAccountIdentificationService = idleAccountIdentificationService;
    }

    /**
     * Get inactive users.
     *
     * @param inactiveAfter Latest active date of login.
     * @param excludeBefore Date to exclude the oldest inactive users.
     * @param tenantDomain  Tenant domain.
     * @return  List of inactive users.
     */
    public List<InactiveUser> getInactiveUsers(String inactiveAfter, String excludeBefore, String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving inactive users for tenant: %s, inactiveAfter: %s, excludeBefore: %s",
                    tenantDomain, inactiveAfter, excludeBefore));
        }
        try {
            return getInactiveUsers(inactiveAfter, excludeBefore, tenantDomain, null);
        } catch (IdleAccountIdentificationException e) {
            throw handleIdleAccIdentificationException(e, ErrorMessage.ERROR_RETRIEVING_INACTIVE_USERS, tenantDomain);
        }
    }

    /**
     * Get inactive users.
     *
     * @param inactiveAfter Latest active date of login.
     * @param excludeBefore Date to exclude the oldest inactive users.
     * @param tenantDomain  Tenant domain.
     * @return List of inactive users.
     * @throws IdleAccountIdentificationClientException If an error occurs while retrieving inactive users.
     */
    public List<InactiveUser> getInactiveUsers(String inactiveAfter, String excludeBefore, String tenantDomain,
                                               String filter) throws IdleAccountIdentificationClientException {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving filtered inactive users for tenant: %s, filter: %s",
                    tenantDomain, filter));
        }
        List<InactiveUserModel> inactiveUsers = null;
        try {
            validateDates(inactiveAfter, excludeBefore);
            LocalDateTime inactiveAfterDate = convertToDateObject(inactiveAfter, DATE_INACTIVE_AFTER);
            LocalDateTime excludeBeforeDate = convertToDateObject(excludeBefore, DATE_EXCLUDE_BEFORE);

            validateDatesCombination(inactiveAfterDate, excludeBeforeDate);

            if (filter == null) {
                if (excludeBeforeDate == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(String.format("Getting inactive users from specific date for tenant: %s",
                                tenantDomain));
                    }
                    inactiveUsers = idleAccountIdentificationService
                            .getInactiveUsersFromSpecificDate(inactiveAfterDate, tenantDomain);
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(String.format("Getting limited inactive users from specific date for tenant: %s",
                                tenantDomain));
                    }
                    inactiveUsers =
                            idleAccountIdentificationService.getLimitedInactiveUsersFromSpecificDate(inactiveAfterDate,
                                    excludeBeforeDate, tenantDomain);
                }
                return buildResponse(inactiveUsers);
            }

            List<ExpressionNode> expressionNodes = getExpressionNodes(filter);
            if (validateExpressionNodes(expressionNodes)) {
                boolean isDisabled = Boolean.parseBoolean(expressionNodes.get(0).getValue());
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Filtering inactive users by disabled status: %s for tenant: %s",
                            isDisabled, tenantDomain));
                }
                inactiveUsers = IdleAccountIdentificationServiceHolder.getIdleAccountIdentificationService()
                        .filterInactiveUsersIfDisabled(inactiveAfterDate, excludeBeforeDate, tenantDomain,
                                isDisabled);

                return buildResponse(inactiveUsers);
            }
            return getInactiveUsers(inactiveAfter, excludeBefore, tenantDomain);

        } catch (IdleAccountIdentificationException e) {
            throw handleIdleAccIdentificationException(e, ErrorMessage.ERROR_RETRIEVING_INACTIVE_USERS, tenantDomain);
        }
    }

    /**
     * Validate the dates.
     *
     * @param inactiveAfter InactiveAfter date.
     * @param excludeBefore ExcludeBefore date.
     * @throws IdleAccountIdentificationClientException IdleAccIdentificationClientException.
     */
    private void validateDates(String inactiveAfter, String excludeBefore) throws
            IdleAccountIdentificationClientException {

        // Check if the required parameter 'inactiveAfter' is present.
        if (StringUtils.isEmpty(inactiveAfter)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Required parameter 'inactiveAfter' is missing in the request");
            }
            ErrorMessage error = ErrorMessage.ERROR_REQUIRED_PARAMETER_MISSING;
            throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), DATE_INACTIVE_AFTER));
        }

        // Validate the date format.
        validateDateFormat(inactiveAfter, DATE_INACTIVE_AFTER);
        if (StringUtils.isNotEmpty(excludeBefore)) {
            validateDateFormat(excludeBefore, DATE_EXCLUDE_BEFORE);
        }
    }

    /**
     * Validate the format of the date.
     *
     * @param dateString Date as a string.
     * @param dateType   Date type.
     * @throws IdleAccountIdentificationClientException IdleAccIdentificationClientException.
     */
    private void validateDateFormat(String dateString, String dateType) throws
            IdleAccountIdentificationClientException {

        if (Pattern.matches(DATE_FORMAT_REGEX, dateString)) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Date format validation failed for %s: %s", dateType, dateString));
        }
        ErrorMessage error = ErrorMessage.ERROR_DATE_REGEX_MISMATCH;
        throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), dateType));
    }

    /**
     * Convert date string into LocalDateTime object.
     *
     * @param dateString Date as a string.
     * @param dateType   Date type.
     * @return List of inactive users.
     * @throws IdleAccountIdentificationClientException IdleAccIdentificationClientException.
     */
    private LocalDateTime convertToDateObject(String dateString, String dateType)
            throws IdleAccountIdentificationClientException {

        try {
            if (StringUtils.isEmpty(dateString)) {
                return null;
            }
            return LocalDate.parse(dateString).atStartOfDay();
        } catch (DateTimeParseException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Failed to parse date string: %s for type: %s", dateString, dateType));
            }
            ErrorMessage error = ErrorMessage.ERROR_INVALID_DATE;
            throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), dateType));
        }
    }

    /**
     * Build the InactiveUser list.
     *
     * @param inactiveUserModels List of inactive users.
     * @return List of inactive users.
     */
    private List<InactiveUser> buildResponse(List<InactiveUserModel> inactiveUserModels) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Building response with %d inactive users",
                    inactiveUserModels != null ? inactiveUserModels.size() : 0));
        }
        List<InactiveUser> inactiveUserList = new ArrayList<>();
        for (InactiveUserModel inactiveUserModel : inactiveUserModels) {
            InactiveUser inactiveUser = new InactiveUser();
            inactiveUser.setUsername(inactiveUserModel.getUsername());
            inactiveUser.setUserStoreDomain(inactiveUserModel.getUserStoreDomain());
            inactiveUser.setUserId(inactiveUserModel.getUserId());
            inactiveUserList.add(inactiveUser);
        }
        return inactiveUserList;
    }

    /**
     * Handle IdleAccIdentificationException.
     *
     * @param exception IdleAccIdentificationException.
     * @param errorEnum Error message.
     * @param data      Context data.
     * @return APIError.
     */
    private APIError handleIdleAccIdentificationException(IdleAccountIdentificationException exception,
                                                          IdleAccountIdentificationConstants.ErrorMessage errorEnum,
                                                          String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof IdleAccountIdentificationClientException) {
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
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof IdleAccountIdentificationServerException) {
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

    /**
     * Check inactive after date comes before exclude after date.
     *
     * @throws IdleAccountIdentificationClientException if the date combination is invalid.
     */
    private void validateDatesCombination(LocalDateTime inactiveAfterDate, LocalDateTime excludeBeforeDate)
            throws IdleAccountIdentificationClientException {

        // check excludeBefore data is before than inactiveAfterDate date.
        if (inactiveAfterDate != null && excludeBeforeDate != null
                && inactiveAfterDate.isBefore(excludeBeforeDate)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invalid date combination: inactiveAfter date is before excludeBefore date");
            }
            ErrorMessage error = ErrorMessage.ERROR_INVALID_DATE_COMBINATION;
            throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription()));
        }
    }

    /**
     * Get the filter node as a list.
     *
     * @param filter value of the filter.
     * @return node tree.
     * @throws IdleAccountIdentificationClientException Error when validate filters.
     */
    private List<ExpressionNode> getExpressionNodes(String filter) throws IdleAccountIdentificationClientException {

        // Filter example : isDisabled eq true.
        List<ExpressionNode> expressionNodes = new ArrayList<>();
        FilterTreeBuilder filterTreeBuilder;
        if (StringUtils.isNotBlank(filter)) {
            try {
                filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();
                setExpressionNodeList(rootNode, expressionNodes);
            } catch (IOException | IdentityException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Failed to parse filter: %s", filter));
                }
                ErrorMessage error = ErrorMessage.ERROR_INVALID_FILTER;
                throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                        String.format(error.getDescription()));
            }
        }
        return expressionNodes;
    }

    /**
     * Set the node values as list of expression.
     *
     * @param node       filter node.
     * @param expression list of expression.
     * @throws IdleAccountIdentificationClientException Error when passing invalid filter.
     */
    private void setExpressionNodeList(Node node, List<ExpressionNode> expression)
            throws IdleAccountIdentificationClientException {

        if (node instanceof ExpressionNode && StringUtils.isNotBlank(((ExpressionNode) node).getAttributeValue())) {
            if (((ExpressionNode) node).getAttributeValue().contains(IS_DISABLED)) {
                if (TRUE_VALUE.contains(((ExpressionNode) node).getValue())) {
                    ((ExpressionNode) node).setValue(TRUE_VALUE);
                } else if (FALSE_VALUE.contains(((ExpressionNode) node).getValue())) {
                    ((ExpressionNode) node).setValue(FALSE_VALUE);
                } else {
                    String message = "Invalid value: " + ((ExpressionNode) node).getValue() + "is passed for '" +
                            IS_DISABLED + "' attribute in the filter. It should be '" + TRUE_VALUE + "' or '" +
                            FALSE_VALUE + "'";
                    ErrorMessage error = ErrorMessage.ERROR_INVALID_FILTER;
                    throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                            message);
                }
            }
            expression.add((ExpressionNode) node);
        }
    }

    /**
     * Validate the expression nodes.
     *
     * @param expressionNodes List of expression nodes.
     * @return boolean.
     * @throws IdleAccountIdentificationClientException Error when validate filters.
     */
    private boolean validateExpressionNodes(List<ExpressionNode> expressionNodes)
            throws IdleAccountIdentificationClientException {

        if (IS_DISABLED.equals(expressionNodes.get(0).getAttributeValue())) {
            return true;
        }

        ErrorMessage error = ErrorMessage.ERROR_INVALID_FILTER;
        throw new IdleAccountIdentificationClientException(error.getCode(), error.getMessage(),
                String.format(error.getDescription()));
    }
}

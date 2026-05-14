/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.consent.management.v2.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementClientException;
import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementException;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CANNOT_DELETE_LATEST_PURPOSE_VERSION;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_INVALID_STATE_FOR_AUTHORIZE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_INVALID_STATE_FOR_REVOKE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_CONSENT_USER_NOT_IN_AUTHORIZATION_LIST;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_NO_USER_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PII_CATEGORY_ALREADY_EXIST;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PII_CATEGORY_ID_INVALID;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PII_CATEGORY_IS_ASSOCIATED;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_ALREADY_EXIST;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_CATEGORY_ALREADY_EXIST;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_CATEGORY_ID_INVALID;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_HAS_VERSIONS_WITH_CONSENTS;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_ID_INVALID;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_IS_ASSOCIATED;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_UUID_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_ALREADY_EXISTS;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_ID_INVALID;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_LABEL_ALREADY_EXISTS;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_MISMATCH;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_PURPOSE_VERSION_NOT_FOUND;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_RECEIPT_ID_INVALID;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_USER_NOT_AUTHORIZED;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.STATUS_BAD_REQUEST_MESSAGE_DEFAULT;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.IDENTITY_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.STATUS_CONFLICT_MESSAGE_DEFAULT;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.STATUS_FORBIDDEN_MESSAGE_DEFAULT;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.STATUS_NOT_FOUND_MESSAGE_DEFAULT;
import static org.wso2.carbon.identity.api.server.consent.management.common.ConsentManagementConstants.STATUS_UNAUTHORIZED_MESSAGE_DEFAULT;

/**
 * Utility class for the V2 Consent Management REST API endpoints.
 */
public class ConsentMgtEndpointUtil {

    private static final Log LOG = LogFactory.getLog(ConsentMgtEndpointUtil.class);

    private static final Set<String> NOT_FOUND_CODES = new HashSet<>(Arrays.asList(
            ERROR_CODE_PURPOSE_ID_INVALID.getCode(),
            ERROR_CODE_PURPOSE_CATEGORY_ID_INVALID.getCode(),
            ERROR_CODE_PII_CATEGORY_ID_INVALID.getCode(),
            ERROR_CODE_RECEIPT_ID_INVALID.getCode(),
            ERROR_CODE_PURPOSE_VERSION_ID_INVALID.getCode(),
            ERROR_CODE_PURPOSE_VERSION_NOT_FOUND.getCode(),
            ERROR_CODE_PURPOSE_VERSION_MISMATCH.getCode(),
            ERROR_CODE_PURPOSE_UUID_NOT_FOUND.getCode(),
            ERROR_CODE_ELEMENT_UUID_NOT_FOUND.getCode()
    ));

    private static final Set<String> CONFLICT_CODES = new HashSet<>(Arrays.asList(
            ERROR_CODE_PURPOSE_ALREADY_EXIST.getCode(),
            ERROR_CODE_PII_CATEGORY_ALREADY_EXIST.getCode(),
            ERROR_CODE_PURPOSE_CATEGORY_ALREADY_EXIST.getCode(),
            ERROR_CODE_PURPOSE_VERSION_ALREADY_EXISTS.getCode(),
            ERROR_CODE_PURPOSE_VERSION_LABEL_ALREADY_EXISTS.getCode(),
            ERROR_CODE_PURPOSE_IS_ASSOCIATED.getCode(),
            ERROR_CODE_PII_CATEGORY_IS_ASSOCIATED.getCode(),
            ERROR_CODE_CONSENT_INVALID_STATE_FOR_REVOKE.getCode(),
            ERROR_CODE_CONSENT_INVALID_STATE_FOR_AUTHORIZE.getCode(),
            ERROR_CODE_CANNOT_DELETE_LATEST_PURPOSE_VERSION.getCode(),
            ERROR_CODE_PURPOSE_HAS_VERSIONS_WITH_CONSENTS.getCode()
    ));

    private static final Set<String> UNAUTHORIZED_CODES = new HashSet<>(Arrays.asList(
            ERROR_CODE_NO_USER_FOUND.getCode()
    ));

    private static final Set<String> FORBIDDEN_CODES = new HashSet<>(Arrays.asList(
            ERROR_CODE_USER_NOT_AUTHORIZED.getCode(),
            ERROR_CODE_CONSENT_USER_NOT_IN_AUTHORIZATION_LIST.getCode()
    ));

    private ConsentMgtEndpointUtil() {

    }

    public static URI buildURIForHeader(String endpoint) {

        try {
            String url = ServiceURLBuilder.create().addPath(IDENTITY_API_PATH_COMPONENT + endpoint)
                    .build().getAbsolutePublicURL();
            return URI.create(url);
        } catch (URLBuilderException e) {
            ErrorResponse errorResponse = new ErrorResponse.Builder()
                    .withMessage("Error while building response.")
                    .withDescription("Server encountered an error while building URL for response header.")
                    .build(LOG, e, e.getMessage());
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }
    }

    public static APIError handleConsentManagementException(ConsentManagementException e) {

        if (e instanceof ConsentManagementClientException) {
            return handleConsentManagementClientException((ConsentManagementClientException) e);
        }
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT)
                .withDescription(STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT)
                .build(LOG, e, e.getMessage());
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    public static APIError handleConsentManagementClientException(ConsentManagementClientException e) {

        String code = e.getErrorCode();
        Response.Status status;
        String defaultMessage;

        if (NOT_FOUND_CODES.contains(code)) {
            status = Response.Status.NOT_FOUND;
            defaultMessage = STATUS_NOT_FOUND_MESSAGE_DEFAULT;
        } else if (CONFLICT_CODES.contains(code)) {
            status = Response.Status.CONFLICT;
            defaultMessage = STATUS_CONFLICT_MESSAGE_DEFAULT;
        } else if (UNAUTHORIZED_CODES.contains(code)) {
            status = Response.Status.UNAUTHORIZED;
            defaultMessage = STATUS_UNAUTHORIZED_MESSAGE_DEFAULT;
        } else if (FORBIDDEN_CODES.contains(code)) {
            status = Response.Status.FORBIDDEN;
            defaultMessage = STATUS_FORBIDDEN_MESSAGE_DEFAULT;
        } else {
            status = Response.Status.BAD_REQUEST;
            defaultMessage = STATUS_BAD_REQUEST_MESSAGE_DEFAULT;
        }

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(code)
                .withMessage(defaultMessage)
                .withDescription(e.getMessage())
                .build(LOG, e.getMessage());
        return new APIError(status, errorResponse);
    }
}

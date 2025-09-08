/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.idp.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idp.debug.common.Constants;
import org.wso2.carbon.identity.api.server.idp.debug.common.DFDPApiErrorFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.IdpDebugApi;
import org.wso2.carbon.identity.api.server.idp.debug.v1.core.DFDPService;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DFDPTestRequest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Implementation of IdP Debug Flow Data Provider API.
 */
public class IdpDebugApiServiceImpl implements IdpDebugApi {

    private static final Log LOG = LogFactory.getLog(IdpDebugApiServiceImpl.class);
    private final DFDPService dfdpService;

    public IdpDebugApiServiceImpl() {
        this.dfdpService = new DFDPService();
    }

    @Override
    public Response debugIdpAuthentication(String idpName, String authenticatorName, String format, 
                                         DFDPTestRequest testRequest) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Testing IdP authentication for: " + idpName + 
                         " with authenticator: " + authenticatorName + 
                         " format: " + format);
            }

            // Set default format if not provided
            if (format == null || format.trim().isEmpty()) {
                format = Constants.Defaults.DEFAULT_RESPONSE_FORMAT;
            }

            // Validate the format
            if (!isValidFormat(format)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(DFDPApiErrorFactory.buildError(
                                Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FORMAT))
                        .build();
            }

            // Validate required parameters
            if (idpName == null || idpName.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(DFDPApiErrorFactory.buildError(
                                Constants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST))
                        .build();
            }

            // Call the service to test authentication
            Object result = dfdpService.testIdpAuthentication(idpName, authenticatorName, format);
            
            return Response.ok(result).build();

        } catch (Exception e) {
            LOG.error("Error testing IdP authentication for IdP: " + idpName, e);
            ErrorResponse errorResponse = new ErrorResponse.Builder()
                    .withCode("ISV-60001")
                    .withMessage("Internal Server Error")
                    .withDescription("An error occurred while testing IdP authentication.")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @Override
    public Response debugIdpOperations(String action, String idpName) {
        try {
            // Default action is health check
            if (action == null || action.trim().isEmpty()) {
                action = "health";
            }

            switch (action.toLowerCase()) {
                case "list-idps":
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Retrieving available identity providers for testing");
                    }
                    Object idpList = dfdpService.getAvailableIdps();
                    return Response.ok(idpList).build();

                case "list-authenticators":
                    if (idpName == null || idpName.trim().isEmpty()) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity(DFDPApiErrorFactory.buildError(
                                        Constants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST))
                                .build();
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Retrieving authenticators for IdP: " + idpName);
                    }
                    Object authenticators = dfdpService.getIdpAuthenticators(idpName);
                    return Response.ok(authenticators).build();

                case "health":
                default:
                    Map<String, Object> healthStatus = new HashMap<>();
                    healthStatus.put("status", "UP");
                    healthStatus.put("timestamp", System.currentTimeMillis());
                    healthStatus.put("service", "IdP Debug Flow Data Provider API");
                    healthStatus.put("version", "v1");
                    return Response.ok(healthStatus).build();
            }

        } catch (Exception e) {
            LOG.error("Error processing debug operation: " + action, e);
            ErrorResponse errorResponse = new ErrorResponse.Builder()
                    .withCode("ISV-60001")
                    .withMessage("Internal Server Error")
                    .withDescription("An error occurred while processing the debug operation.")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @Override
    public Response getAvailableIdps() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving available identity providers for testing");
            }

            Object idpList = dfdpService.getAvailableIdps();
            return Response.ok(idpList).build();

        } catch (Exception e) {
            LOG.error("Error retrieving available identity providers", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(DFDPApiErrorFactory.buildError(
                            Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_IDPS))
                    .build();
        }
    }

    @Override
    public Response getIdpAuthenticators(String idpName) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving authenticators for IdP: " + idpName);
            }

            // Validate required parameters
            if (idpName == null || idpName.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(DFDPApiErrorFactory.buildError(
                                Constants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST))
                        .build();
            }

            Object authenticators = dfdpService.getIdpAuthenticators(idpName);
            return Response.ok(authenticators).build();

        } catch (Exception e) {
            LOG.error("Error retrieving authenticators for IdP: " + idpName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(DFDPApiErrorFactory.buildError(
                            Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATORS))
                    .build();
        }
    }

    @Override
    public Response getHealthStatus() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("service", "IdP Debug Flow Data Provider");
            health.put("timestamp", System.currentTimeMillis());
            health.put("version", "v1");

            return Response.ok(health).build();

        } catch (Exception e) {
            LOG.error("Error getting health status", e);
            Map<String, Object> health = new HashMap<>();
            health.put("status", "DOWN");
            health.put("service", "IdP Debug Flow Data Provider");
            health.put("timestamp", System.currentTimeMillis());
            health.put("error", e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(health)
                    .build();
        }
    }

    /**
     * Validates if the provided format is supported.
     *
     * @param format Response format to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidFormat(String format) {
        return Constants.ResponseFormat.JSON.equals(format) ||
               Constants.ResponseFormat.HTML.equals(format) ||
               Constants.ResponseFormat.TEXT.equals(format) ||
               Constants.ResponseFormat.SUMMARY.equals(format);
    }
}

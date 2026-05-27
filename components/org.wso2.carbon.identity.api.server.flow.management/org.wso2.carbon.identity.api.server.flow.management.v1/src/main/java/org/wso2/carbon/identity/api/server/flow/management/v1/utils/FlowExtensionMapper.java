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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.model.Authentication;
import org.wso2.carbon.identity.action.management.api.model.EndpointConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.AccessConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.AuthenticationTypeResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.ContextPath;
import org.wso2.carbon.identity.api.server.flow.management.v1.Encryption;
import org.wso2.carbon.identity.api.server.flow.management.v1.Endpoint;
import org.wso2.carbon.identity.api.server.flow.management.v1.EndpointResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.EndpointUpdateModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionUpdateModel;
import org.wso2.carbon.identity.certificate.management.model.Certificate;
import org.wso2.carbon.identity.flow.extension.model.FlowExtensionAction;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST_BODY;

/**
 * Mapper utility for translating between the flow-management API models and the
 * {@link FlowExtensionAction} domain model.
 * <p>
 * All methods are static; this class has no OSGi dependency.
 * </p>
 */
public final class FlowExtensionMapper {

    private FlowExtensionMapper() {

    }

    /**
     * Convert a create-request {@link FlowExtensionModel} to an {@link FlowExtensionAction}
     * ready to be passed to {@code ActionManagementService#addAction}.
     *
     * @param model the API create model.
     * @return domain action for persistence.
     * @throws FlowMgtClientException if endpoint authentication properties are invalid.
     */
    public static Action toFlowExtensionAction(FlowExtensionModel model)
            throws FlowMgtClientException {

        if (model == null) {
            throw new FlowMgtClientException(
                    ERROR_CODE_INVALID_REQUEST_BODY.getCode(),
                    ERROR_CODE_INVALID_REQUEST_BODY.getMessage(),
                    ERROR_CODE_INVALID_REQUEST_BODY.getDescription());
        }
        EndpointConfig endpointConfig = toEndpointConfig(model.getEndpoint());

        org.wso2.carbon.identity.flow.extension.model.AccessConfig accessConfig =
                toEngineAccessConfig(model.getAccessConfig());
        Certificate certificate = toCertificate(model.getEncryption());

        Action baseAction = new Action.ActionRequestBuilder()
                .name(model.getName())
                .description(model.getDescription())
                .endpoint(endpointConfig)
                .build();

        return new FlowExtensionAction.RequestBuilder(baseAction)
                .accessConfig(accessConfig)
                .certificate(certificate)
                .iconUrl(model.getIconUrl())
                .build();
    }

    /**
     * Convert an update-request {@link FlowExtensionUpdateModel} to an
     * {@link FlowExtensionAction} ready to be passed to
     * {@code ActionManagementService#updateAction}.
     * Only non-null fields are set; null fields are ignored (PATCH semantics).
     *
     * @param model the API update model.
     * @return domain action carrying only the fields that should be updated.
     * @throws FlowMgtClientException if endpoint authentication properties are invalid.
     */
    public static Action toFlowExtensionAction(FlowExtensionUpdateModel model)
            throws FlowMgtClientException {

        if (model == null) {
            throw new FlowMgtClientException(
                    ERROR_CODE_INVALID_REQUEST_BODY.getCode(),
                    ERROR_CODE_INVALID_REQUEST_BODY.getMessage(),
                    ERROR_CODE_INVALID_REQUEST_BODY.getDescription());
        }
        EndpointConfig endpointConfig = null;
        if (model.getEndpoint() != null) {
            endpointConfig = toEndpointConfigFromUpdate(model.getEndpoint());
        }

        Action baseAction = new Action.ActionRequestBuilder()
                .name(model.getName())
                .description(model.getDescription())
                .actionVersion(model.getVersion())
                .endpoint(endpointConfig)
                .build();

        org.wso2.carbon.identity.flow.extension.model.AccessConfig accessConfig =
                toEngineAccessConfig(model.getAccessConfig());
        Certificate certificate = toCertificate(model.getEncryption());

        return new FlowExtensionAction.RequestBuilder(baseAction)
                .accessConfig(accessConfig)
                .certificate(certificate)
                .iconUrl(model.getIconUrl())
                .build();
    }

    /**
     * Convert an {@link Action} (expected to be an {@link FlowExtensionAction}) to a full
     * {@link FlowExtensionResponse}.
     *
     * @param action the domain action returned by the service layer.
     * @return API response model.
     */
    public static FlowExtensionResponse toFlowExtensionResponse(Action action) {

        FlowExtensionAction ext = (action instanceof FlowExtensionAction)
                ? (FlowExtensionAction) action : null;

        FlowExtensionResponse response = new FlowExtensionResponse()
                .id(action.getId())
                .name(action.getName())
                .description(action.getDescription())
                .version(action.getActionVersion())
                .createdAt(action.getCreatedAt() != null
                        ? action.getCreatedAt().toInstant().toString() : null)
                .updatedAt(action.getUpdatedAt() != null
                        ? action.getUpdatedAt().toInstant().toString() : null)
                .endpoint(toEndpointResponse(action.getEndpoint()));

        if (ext != null) {
            response.accessConfig(toApiAccessConfig(ext.getAccessConfig()));
            if (ext.getCertificate() != null) {
                response.encryption(toApiEncryption(ext.getCertificate()));
            }
            response.iconUrl(ext.getIconUrl());
        }
        return response;
    }

    /**
     * Convert an {@link Action} to a lightweight {@link FlowExtensionBasicResponse} for list
     * responses.
     *
     * @param action the domain action.
     * @return API list-item model.
     */
    public static FlowExtensionBasicResponse toFlowExtensionBasicResponse(Action action) {

        FlowExtensionBasicResponse response = new FlowExtensionBasicResponse()
                .id(action.getId())
                .name(action.getName())
                .description(action.getDescription())
                .version(action.getActionVersion())
                .createdAt(action.getCreatedAt() != null
                        ? action.getCreatedAt().toInstant().toString() : null)
                .updatedAt(action.getUpdatedAt() != null
                        ? action.getUpdatedAt().toInstant().toString() : null);
        if (action instanceof FlowExtensionAction) {
            response.iconUrl(((FlowExtensionAction) action).getIconUrl());
        }
        return response;
    }

    private static EndpointConfig toEndpointConfig(Endpoint endpoint)
            throws FlowMgtClientException {

        Authentication authentication = buildAuthentication(
                Authentication.Type.valueOfName(endpoint.getAuthentication().getType().toString()),
                endpoint.getAuthentication().getProperties());

        return new EndpointConfig.EndpointConfigBuilder()
                .uri(endpoint.getUri())
                .authentication(authentication)
                .allowedHeaders(endpoint.getAllowedHeaders())
                .build();
    }

    private static EndpointConfig toEndpointConfigFromUpdate(EndpointUpdateModel endpoint)
            throws FlowMgtClientException {

        Authentication authentication = null;
        if (endpoint.getAuthentication() != null) {
            authentication = buildAuthentication(
                    Authentication.Type.valueOfName(
                            endpoint.getAuthentication().getType().toString()),
                    endpoint.getAuthentication().getProperties());
        }

        return new EndpointConfig.EndpointConfigBuilder()
                .uri(endpoint.getUri())
                .authentication(authentication)
                .allowedHeaders(endpoint.getAllowedHeaders())
                .build();
    }

    private static EndpointResponse toEndpointResponse(EndpointConfig endpointConfig) {

        if (endpointConfig == null) {
            return null;
        }
        return new EndpointResponse()
                .uri(endpointConfig.getUri())
                .authentication(new AuthenticationTypeResponse()
                        .type(AuthenticationTypeResponse.TypeEnum.valueOf(
                                endpointConfig.getAuthentication().getType().toString())))
                .allowedHeaders(endpointConfig.getAllowedHeaders());
    }

    private static Authentication buildAuthentication(Authentication.Type authType,
                                                      Map<String, Object> props)
            throws FlowMgtClientException {

        switch (authType) {
            case BASIC:
                String username = getRequiredStringProp(props,
                        Authentication.Property.USERNAME.getName());
                String password = getRequiredStringProp(props,
                        Authentication.Property.PASSWORD.getName());
                return new Authentication.BasicAuthBuilder(username, password).build();
            case BEARER:
                String token = getRequiredStringProp(props,
                        Authentication.Property.ACCESS_TOKEN.getName());
                return new Authentication.BearerAuthBuilder(token).build();
            case API_KEY:
                String header = getRequiredStringProp(props,
                        Authentication.Property.HEADER.getName());
                String value = getRequiredStringProp(props,
                        Authentication.Property.VALUE.getName());
                return new Authentication.APIKeyAuthBuilder(header, value).build();
            case CLIENT_CREDENTIAL:
                String clientId = getRequiredStringProp(props,
                        Authentication.Property.CLIENT_ID.getName());
                String clientSecret = getRequiredStringProp(props,
                        Authentication.Property.CLIENT_SECRET.getName());
                String tokenEndpoint = getRequiredStringProp(props,
                        Authentication.Property.TOKEN_ENDPOINT.getName());
                String scopes = getOptionalStringProp(props,
                        Authentication.Property.SCOPES.getName());
                return new Authentication.ClientCredentialAuthBuilder(
                        clientId, clientSecret, tokenEndpoint, scopes).build();
            case PASSWORD_CREDENTIAL:
                String pwdGrantClientId = getRequiredStringProp(props,
                        Authentication.Property.CLIENT_ID.getName());
                String pwdGrantClientSecret = getRequiredStringProp(props,
                        Authentication.Property.CLIENT_SECRET.getName());
                String pwdGrantTokenEndpoint = getRequiredStringProp(props,
                        Authentication.Property.TOKEN_ENDPOINT.getName());
                String pwdGrantUsername = getRequiredStringProp(props,
                        Authentication.Property.USERNAME.getName());
                String pwdGrantPassword = getRequiredStringProp(props,
                        Authentication.Property.PASSWORD.getName());
                String pwdGrantScopes = getOptionalStringProp(props,
                        Authentication.Property.SCOPES.getName());
                return new Authentication.PasswordCredentialAuthBuilder(
                        pwdGrantClientId, pwdGrantClientSecret, pwdGrantTokenEndpoint,
                        pwdGrantScopes, pwdGrantUsername, pwdGrantPassword).build();
            case NONE:
                return new Authentication.NoneAuthBuilder().build();
            default:
                throw new FlowMgtClientException(
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getCode(),
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getDescription());
        }
    }

    private static String getRequiredStringProp(Map<String, Object> props, String key)
            throws FlowMgtClientException {

        if (props == null || !props.containsKey(key)) {
            throw new FlowMgtClientException(
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getCode(),
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getDescription());
        }
        String val = (String) props.get(key);
        if (StringUtils.isEmpty(val)) {
            throw new FlowMgtClientException(
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getCode(),
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getDescription());
        }
        return val;
    }

    private static String getOptionalStringProp(Map<String, Object> props, String key) {

        if (props == null || !props.containsKey(key)) {
            return null;
        }
        Object val = props.get(key);
        return (val instanceof String) ? (String) val : null;
    }

    private static org.wso2.carbon.identity.flow.extension.model.AccessConfig
    toEngineAccessConfig(AccessConfig model) {

        if (model == null) {
            return null;
        }
        List<org.wso2.carbon.identity.flow.extension.model.ContextPath> expose =
                toEngineContextPaths(model.getExpose());
        List<org.wso2.carbon.identity.flow.extension.model.ContextPath> modify =
                toEngineContextPaths(model.getModify());

        return new org.wso2.carbon.identity.flow.extension.model
                .AccessConfig(expose, modify);
    }

    private static List<org.wso2.carbon.identity.flow.extension.model.ContextPath>
    toEngineContextPaths(List<ContextPath> apiPaths) {

        if (apiPaths == null) {
            return null;
        }
        return apiPaths.stream()
                .map(cp -> new org.wso2.carbon.identity.flow.extension.model
                        .ContextPath(cp.getPath(), Boolean.TRUE.equals(cp.getEncrypted())))
                .collect(Collectors.toList());
    }

    private static AccessConfig toApiAccessConfig(
            org.wso2.carbon.identity.flow.extension.model.AccessConfig engineConfig) {

        if (engineConfig == null) {
            return null;
        }
        AccessConfig model = new AccessConfig();
        if (engineConfig.getExpose() != null) {
            model.setExpose(engineConfig.getExpose().stream()
                    .map(cp -> new ContextPath().path(cp.getPath()).encrypted(cp.isEncrypted()))
                    .collect(Collectors.toList()));
        }
        if (engineConfig.getModify() != null) {
            model.setModify(engineConfig.getModify().stream()
                    .map(cp -> new ContextPath().path(cp.getPath()).encrypted(cp.isEncrypted()))
                    .collect(Collectors.toList()));
        }
        return model;
    }

    private static Certificate toCertificate(Encryption model) {

        if (model == null) {
            return null;
        }
        if (StringUtils.isBlank(model.getCertificate())) {
            return new Certificate.Builder().certificateContent(null).build();
        }
        return new Certificate.Builder()
                .certificateContent(model.getCertificate())
                .build();
    }

    private static Encryption toApiEncryption(Certificate certificate) {

        if (certificate == null) {
            return null;
        }
        return new Encryption();
    }
}

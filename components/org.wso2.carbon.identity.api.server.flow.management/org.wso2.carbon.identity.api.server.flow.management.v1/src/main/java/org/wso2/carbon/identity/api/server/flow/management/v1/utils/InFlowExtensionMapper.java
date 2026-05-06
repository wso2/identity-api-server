/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtClientException;
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
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionUpdateModel;
import org.wso2.carbon.identity.certificate.management.model.Certificate;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.InFlowExtensionAction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES;

/**
 * Mapper utility for translating between the flow-management API models and the
 * {@link InFlowExtensionAction} domain model.
 * <p>
 * All methods are static; this class has no OSGi dependency.
 * </p>
 */
public final class InFlowExtensionMapper {

    private InFlowExtensionMapper() {

    }

    // -------------------------------------------------------------------------
    // API model → Domain model (create)
    // -------------------------------------------------------------------------

    /**
     * Convert a create-request {@link InFlowExtensionModel} to an {@link InFlowExtensionAction}
     * ready to be passed to {@code ActionManagementService#addAction}.
     *
     * @param model the API create model.
     * @return domain action for persistence.
     * @throws ActionMgtClientException if endpoint authentication properties are invalid.
     */
    public static Action toInFlowExtensionAction(InFlowExtensionModel model)
            throws ActionMgtClientException {

        EndpointConfig endpointConfig = toEndpointConfig(model.getEndpoint());

        org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.AccessConfig accessConfig =
                toEngineAccessConfig(model.getAccessConfig());
        org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption encryption =
                toEngineEncryption(model.getEncryption());

        Action baseAction = new Action.ActionRequestBuilder()
                .name(model.getName())
                .description(model.getDescription())
                .endpoint(endpointConfig)
                .build();

        return new InFlowExtensionAction.RequestBuilder(baseAction)
                .accessConfig(accessConfig)
                .encryption(encryption)
                .iconUrl(model.getIconUrl())
                .build();
    }

    // -------------------------------------------------------------------------
    // API model → Domain model (update / PATCH)
    // -------------------------------------------------------------------------

    /**
     * Convert an update-request {@link InFlowExtensionUpdateModel} to an
     * {@link InFlowExtensionAction} ready to be passed to
     * {@code ActionManagementService#updateAction}.
     * Only non-null fields are set; null fields are ignored (PATCH semantics).
     *
     * @param model the API update model.
     * @return domain action carrying only the fields that should be updated.
     * @throws ActionMgtClientException if endpoint authentication properties are invalid.
     */
    public static Action toInFlowExtensionAction(InFlowExtensionUpdateModel model)
            throws ActionMgtClientException {

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

        org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.AccessConfig accessConfig =
                toEngineAccessConfig(model.getAccessConfig());
        org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption encryption =
                toEngineEncryption(model.getEncryption());

        return new InFlowExtensionAction.RequestBuilder(baseAction)
                .accessConfig(accessConfig)
                .encryption(encryption)
                .iconUrl(model.getIconUrl())
                .build();
    }

    // -------------------------------------------------------------------------
    // Domain model → API response (full)
    // -------------------------------------------------------------------------

    /**
     * Convert an {@link Action} (expected to be an {@link InFlowExtensionAction}) to a full
     * {@link InFlowExtensionResponse}.
     *
     * @param action the domain action returned by the service layer.
     * @return API response model.
     */
    public static InFlowExtensionResponse toInFlowExtensionResponse(Action action) {

        InFlowExtensionAction ext = (action instanceof InFlowExtensionAction)
                ? (InFlowExtensionAction) action : null;

        InFlowExtensionResponse response = new InFlowExtensionResponse()
                .id(action.getId())
                .name(action.getName())
                .description(action.getDescription())
                .status(InFlowExtensionResponse.StatusEnum.valueOf(action.getStatus().name()))
                .version(action.getActionVersion())
                .createdAt(action.getCreatedAt() != null
                        ? action.getCreatedAt().toInstant().toString() : null)
                .updatedAt(action.getUpdatedAt() != null
                        ? action.getUpdatedAt().toInstant().toString() : null)
                .endpoint(toEndpointResponse(action.getEndpoint()));

        if (ext != null) {
            response.accessConfig(toApiAccessConfig(ext.getAccessConfig()));
            if (ext.getEncryption() != null) {
                response.encryption(toApiEncryption(ext.getEncryption()));
            }
            response.iconUrl(ext.getIconUrl());
        }
        return response;
    }

    // -------------------------------------------------------------------------
    // Domain model → API response (list item)
    // -------------------------------------------------------------------------

    /**
     * Convert an {@link Action} to a lightweight {@link InFlowExtensionBasicResponse} for list
     * responses.
     *
     * @param action the domain action.
     * @return API list-item model.
     */
    public static InFlowExtensionBasicResponse toInFlowExtensionBasicResponse(Action action) {

        InFlowExtensionBasicResponse response = new InFlowExtensionBasicResponse()
                .id(action.getId())
                .name(action.getName())
                .description(action.getDescription())
                .status(InFlowExtensionBasicResponse.StatusEnum.valueOf(action.getStatus().name()))
                .version(action.getActionVersion())
                .createdAt(action.getCreatedAt() != null
                        ? action.getCreatedAt().toInstant().toString() : null)
                .updatedAt(action.getUpdatedAt() != null
                        ? action.getUpdatedAt().toInstant().toString() : null);
        if (action instanceof InFlowExtensionAction) {
            response.iconUrl(((InFlowExtensionAction) action).getIconUrl());
        }
        return response;
    }

    // -------------------------------------------------------------------------
    // Private helpers — Endpoint
    // -------------------------------------------------------------------------

    private static EndpointConfig toEndpointConfig(Endpoint endpoint)
            throws ActionMgtClientException {

        Authentication authentication = buildAuthentication(
                Authentication.Type.valueOfName(endpoint.getAuthentication().getType().toString()),
                endpoint.getAuthentication().getProperties());

        return new EndpointConfig.EndpointConfigBuilder()
                .uri(endpoint.getUri())
                .authentication(authentication)
                .allowedHeaders(endpoint.getAllowedHeaders())
                .allowedParameters(endpoint.getAllowedParameters())
                .build();
    }

    private static EndpointConfig toEndpointConfigFromUpdate(EndpointUpdateModel endpoint)
            throws ActionMgtClientException {

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
                .allowedParameters(endpoint.getAllowedParameters())
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
                .allowedHeaders(endpointConfig.getAllowedHeaders())
                .allowedParameters(endpointConfig.getAllowedParameters());
    }

    // -------------------------------------------------------------------------
    // Private helpers — Authentication
    // -------------------------------------------------------------------------

    private static Authentication buildAuthentication(Authentication.Type authType,
                                                      Map<String, Object> props)
            throws ActionMgtClientException {

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
            case NONE:
                return new Authentication.NoneAuthBuilder().build();
            default:
                throw new ActionMgtClientException(
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getDescription(),
                        ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getCode());
        }
    }

    private static String getRequiredStringProp(Map<String, Object> props, String key)
            throws ActionMgtClientException {

        if (props == null || !props.containsKey(key)) {
            throw new ActionMgtClientException(
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getDescription(),
                    ERROR_CODE_INVALID_ENDPOINT_AUTH_PROPERTIES.getCode());
        }
        String val = (String) props.get(key);
        if (StringUtils.isEmpty(val)) {
            throw new ActionMgtClientException(
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getMessage(),
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getDescription(),
                    ERROR_CODE_EMPTY_ENDPOINT_AUTH_PROPERTIES.getCode());
        }
        return val;
    }

    // -------------------------------------------------------------------------
    // Private helpers — AccessConfig
    // -------------------------------------------------------------------------

    private static org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.AccessConfig
    toEngineAccessConfig(AccessConfig model) {

        if (model == null) {
            return null;
        }
        List<org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.ContextPath> expose =
                toEngineContextPaths(model.getExpose());
        List<org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.ContextPath> modify =
                toEngineContextPaths(model.getModify());

        return new org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model
                .AccessConfig(expose, modify);
    }

    private static List<org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.ContextPath>
    toEngineContextPaths(List<ContextPath> apiPaths) {

        if (apiPaths == null) {
            return null;
        }
        return apiPaths.stream()
                .map(cp -> new org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model
                        .ContextPath(cp.getPath(), Boolean.TRUE.equals(cp.getEncrypted())))
                .collect(Collectors.toList());
    }

    private static AccessConfig toApiAccessConfig(
            org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.AccessConfig engineConfig) {

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

    // -------------------------------------------------------------------------
    // Private helpers — Encryption
    // -------------------------------------------------------------------------

    private static org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption
    toEngineEncryption(Encryption model) {

        if (model == null) {
            return null;
        }
        if (StringUtils.isBlank(model.getCertificate())) {
            // Blank/null certificate in the update request signals explicit removal.
            // Return Encryption with null certificate so buildActionDTO stores "" (String),
            // which handleCertificateUpdate recognises as isExplicitRemoval.
            return new org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption(null);
        }
        // Pass the raw PEM string to the framework; the DTOModelResolver (InFlowExtensionActionDTOModelResolver)
        // takes care of persisting it via CertificateManagementService and replacing with a UUID.
        Certificate cert = new Certificate.Builder()
                .certificateContent(model.getCertificate())
                .build();
        return new org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption(cert);
    }

    private static Encryption toApiEncryption(
            org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption engineEncryption) {

        // The certificate is resolved back to a Certificate object by the DTOModelResolver on GET.
        // We intentionally do not echo the certificate content back in the response.
        if (engineEncryption == null || engineEncryption.getCertificate() == null) {
            return null;
        }
        // Return an Encryption object without the certificate content (treat as opaque).
        return new Encryption();
    }
}

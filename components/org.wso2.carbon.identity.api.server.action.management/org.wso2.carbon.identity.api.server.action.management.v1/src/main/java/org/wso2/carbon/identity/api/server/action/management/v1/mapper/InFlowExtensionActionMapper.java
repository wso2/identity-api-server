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

package org.wso2.carbon.identity.api.server.action.management.v1.mapper;

import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.AccessConfigModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.EncryptionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.InFlowExtensionActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.InFlowExtensionActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.InFlowExtensionActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.InFlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMapperUtil;
import org.wso2.carbon.identity.certificate.management.model.Certificate;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.AccessConfig;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.ContextPath;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.Encryption;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.model.InFlowExtensionAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for In-Flow Extension Action.
 */
public class InFlowExtensionActionMapper implements ActionMapper {

    @Override
    public Action.ActionTypes getSupportedActionType() {

        return Action.ActionTypes.IN_FLOW_EXTENSION;
    }

    @Override
    public Action toAction(ActionModel actionModel) throws ActionMgtException {

        if (!(actionModel instanceof InFlowExtensionActionModel)) {
            // Fall back to basic action creation if no access config is provided.
            return ActionMapperUtil.buildActionRequest(getSupportedActionType(), actionModel);
        }

        InFlowExtensionActionModel extModel = (InFlowExtensionActionModel) actionModel;
        Action basicAction = ActionMapperUtil.buildActionRequest(getSupportedActionType(), actionModel);
        AccessConfig accessConfig = toAccessConfig(extModel.getAccessConfig());
        Encryption encryption = toEncryption(extModel.getEncryption());

        return new InFlowExtensionAction.RequestBuilder(basicAction)
                .accessConfig(accessConfig)
                .encryption(encryption)
                .iconUrl(extModel.getIconUrl())
                .build();
    }

    @Override
    public Action toAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException {

        if (!(actionUpdateModel instanceof InFlowExtensionActionUpdateModel)) {
            return ActionMapperUtil.buildUpdatingActionRequest(getSupportedActionType(), actionUpdateModel);
        }

        InFlowExtensionActionUpdateModel extUpdateModel = (InFlowExtensionActionUpdateModel) actionUpdateModel;
        Action basicUpdatingAction = ActionMapperUtil.buildUpdatingActionRequest(getSupportedActionType(),
                actionUpdateModel);
        AccessConfig accessConfig = toAccessConfig(extUpdateModel.getAccessConfig());
        Encryption encryption = toEncryption(extUpdateModel.getEncryption());

        Map<String, AccessConfig> flowTypeOverrides = null;
        if (extUpdateModel.getFlowTypeOverrides() != null) {
            flowTypeOverrides = new HashMap<>();
            for (Map.Entry<String, AccessConfigModel> entry : extUpdateModel.getFlowTypeOverrides().entrySet()) {
                flowTypeOverrides.put(entry.getKey(), toAccessConfig(entry.getValue()));
            }
        }

        return new InFlowExtensionAction.RequestBuilder(basicUpdatingAction)
                .accessConfig(accessConfig)
                .encryption(encryption)
                .iconUrl(extUpdateModel.getIconUrl())
                .flowTypeOverrides(flowTypeOverrides)
                .build();
    }

    @Override
    public ActionBasicResponse toActionBasicResponse(Action action) throws ActionMgtException {

        ActionBasicResponse basicResponse = ActionMapperUtil.buildActionBasicResponse(action);

        if (!(action instanceof InFlowExtensionAction)) {
            return basicResponse;
        }

        InFlowExtensionAction extAction = (InFlowExtensionAction) action;
        if (extAction.getIconUrl() != null) {
            return new InFlowExtensionBasicResponse(basicResponse)
                    .iconUrl(extAction.getIconUrl());
        }
        return basicResponse;
    }

    @Override
    public ActionResponse toActionResponse(Action action) throws ActionMgtException {

        ActionResponse actionResponse = ActionMapperUtil.buildActionResponse(action);

        if (!(action instanceof InFlowExtensionAction)) {
            return actionResponse;
        }

        InFlowExtensionAction extAction = (InFlowExtensionAction) action;
        AccessConfigModel configModel = toAccessConfigModel(extAction.getAccessConfig());
        EncryptionModel encryptionModel = toEncryptionModel(extAction.getEncryption());

        InFlowExtensionActionResponse response = new InFlowExtensionActionResponse(actionResponse)
                .accessConfig(configModel);
        if (encryptionModel != null) {
            response.encryption(encryptionModel);
        }
        if (extAction.getIconUrl() != null) {
            response.iconUrl(extAction.getIconUrl());
        }
        if (extAction.getFlowTypeOverrides() != null && !extAction.getFlowTypeOverrides().isEmpty()) {
            Map<String, AccessConfigModel> overridesModel = new HashMap<>();
            for (Map.Entry<String, AccessConfig> entry : extAction.getFlowTypeOverrides().entrySet()) {
                overridesModel.put(entry.getKey(), toAccessConfigModel(entry.getValue()));
            }
            response.flowTypeOverrides(overridesModel);
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private AccessConfig toAccessConfig(AccessConfigModel configModel) {

        if (configModel == null) {
            return null;
        }

        // Convert expose: List<Object> → List<ContextPath>.
        // Supports both plain strings (encrypted=false) and objects {path, encrypted}.
        List<ContextPath> exposePaths = null;
        if (configModel.getExpose() != null) {
            exposePaths = new ArrayList<>();
            for (Object item : configModel.getExpose()) {
                if (item instanceof String) {
                    exposePaths.add(new ContextPath((String) item, false));
                } else if (item instanceof Map) {
                    Map<?, ?> exposeMap = (Map<?, ?>) item;
                    String path = (String) exposeMap.get("path");
                    boolean encrypted = toBooleanSafe(exposeMap.get("encrypted"));
                    if (path != null) {
                        exposePaths.add(new ContextPath(path, encrypted));
                    }
                }
            }
        }

        // Convert modify: List<Object> → List<ContextPath>.
        // Same format as expose: supports both plain strings and objects {path, encrypted}.
        List<ContextPath> modifyPaths = null;
        if (configModel.getModify() != null) {
            modifyPaths = new ArrayList<>();
            for (Object item : configModel.getModify()) {
                if (item instanceof String) {
                    modifyPaths.add(new ContextPath((String) item, false));
                } else if (item instanceof Map) {
                    Map<?, ?> modifyMap = (Map<?, ?>) item;
                    String path = (String) modifyMap.get("path");
                    boolean encrypted = toBooleanSafe(modifyMap.get("encrypted"));
                    if (path != null) {
                        modifyPaths.add(new ContextPath(path, encrypted));
                    }
                }
            }
        }

        return new AccessConfig(exposePaths, modifyPaths);
    }

    private Encryption toEncryption(EncryptionModel encryptionModel) {

        if (encryptionModel == null || encryptionModel.getCertificate() == null) {
            return null;
        }

        // Empty certificate string signals explicit removal of the existing certificate.
        if (encryptionModel.getCertificate().isEmpty()) {
            return new Encryption(null);
        }

        Certificate certificate = new Certificate.Builder()
                .name("external-service-cert")
                .certificateContent(encryptionModel.getCertificate())
                .build();
        return new Encryption(certificate);
    }

    private AccessConfigModel toAccessConfigModel(AccessConfig accessConfig) {

        if (accessConfig == null) {
            return null;
        }

        // Convert expose: List<ContextPath> → List<Object>.
        // Always output as {path, encrypted} objects for consistency.
        List<Object> expose = null;
        if (accessConfig.getExpose() != null) {
            expose = new ArrayList<>();
            for (ContextPath ep : accessConfig.getExpose()) {
                Map<String, Object> exposeMap = new HashMap<>();
                exposeMap.put("path", ep.getPath());
                exposeMap.put("encrypted", ep.isEncrypted());
                expose.add(exposeMap);
            }
        }

        // Convert modify: List<ContextPath> → List<Object>.
        // Always output as {path, encrypted} objects for consistency.
        List<Object> modify = null;
        if (accessConfig.getModify() != null) {
            modify = new ArrayList<>();
            for (ContextPath mp : accessConfig.getModify()) {
                Map<String, Object> modifyMap = new HashMap<>();
                modifyMap.put("path", mp.getPath());
                modifyMap.put("encrypted", mp.isEncrypted());
                modify.add(modifyMap);
            }
        }

        return new AccessConfigModel()
                .expose(expose)
                .modify(modify);
    }

    private EncryptionModel toEncryptionModel(Encryption encryption) {

        if (encryption == null || encryption.getCertificate() == null) {
            return null;
        }

        return new EncryptionModel()
                .certificate(encryption.getCertificate().getCertificateContent());
    }

    /**
     * Safely converts a value to boolean, handling both {@link Boolean} and {@link String} types.
     * Jackson deserializes JSON {@code true} as {@link Boolean} but JSON {@code "true"} as {@link String}.
     *
     * @param value The value to convert.
     * @return {@code true} if the value is Boolean TRUE or the string "true" (case-insensitive).
     */
    private static boolean toBooleanSafe(Object value) {

        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return false;
    }
}

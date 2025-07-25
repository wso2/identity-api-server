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

package org.wso2.carbon.identity.api.server.action.management.v1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdateProfileActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdateProfileActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;

/**
 * Utility class for action deserialization.
 */
public class ActionDeserializer {

    private static final Log LOG = LogFactory.getLog(ActionDeserializer.class);

    /**
     * Deserialize the action model.
     *
     * @param actionType Action type.
     * @param jsonBody   JSON body.
     * @return Action model.
     */
    public static ActionModel deserializeActionModel(Action.ActionTypes actionType, String jsonBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deserializing action model for type: " + (actionType != null ? actionType : "null"));
        }
        ActionModel actionModel = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            switch (actionType) {
                case PRE_ISSUE_ACCESS_TOKEN:
                    actionModel = objectMapper.readValue(jsonBody, ActionModel.class);
                    validateActionModel(actionModel, ActionModel.class);
                    break;
                case PRE_UPDATE_PASSWORD:
                    PreUpdatePasswordActionModel preUpdatePasswordActionModel = objectMapper.readValue(jsonBody,
                            PreUpdatePasswordActionModel.class);
                    validateActionModel(preUpdatePasswordActionModel, PreUpdatePasswordActionModel.class);
                    actionModel = preUpdatePasswordActionModel;
                    break;
                case PRE_UPDATE_PROFILE:
                    PreUpdateProfileActionModel preUpdateProfileActionModel = objectMapper.readValue(jsonBody,
                            PreUpdateProfileActionModel.class);
                    validateActionModel(preUpdateProfileActionModel, PreUpdateProfileActionModel.class);
                    actionModel = preUpdateProfileActionModel;
                    break;
                default:
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No deserializer found for action type: " + actionType);
                    }
                    break;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Action model deserialized successfully for type: " + actionType);
            }
        } catch (JsonProcessingException e) {
            LOG.warn("Failed to deserialize action model for type: " + actionType + ". Invalid JSON payload.", e);
            throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                    ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_PAYLOAD);
        }

        return actionModel;
    }

    /**
     * Deserialize the action update model.
     *
     * @param actionType Action type.
     * @param jsonBody   JSON body.
     * @return Action update model.
     */
    public static ActionUpdateModel deserializeActionUpdateModel(Action.ActionTypes actionType, String jsonBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deserializing action update model for type: " + (actionType != null ? actionType : "null"));
        }
        ActionUpdateModel actionUpdateModel = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            switch (actionType) {
                case PRE_ISSUE_ACCESS_TOKEN:
                    actionUpdateModel = objectMapper.readValue(jsonBody, ActionUpdateModel.class);
                    validateActionModel(actionUpdateModel, ActionUpdateModel.class);
                    break;
                case PRE_UPDATE_PASSWORD:
                    PreUpdatePasswordActionUpdateModel preUpdatePasswordActionUpdateModel =
                            objectMapper.readValue(jsonBody, PreUpdatePasswordActionUpdateModel.class);
                    validateActionModel(preUpdatePasswordActionUpdateModel, PreUpdatePasswordActionUpdateModel.class);
                    actionUpdateModel = preUpdatePasswordActionUpdateModel;
                    break;
                case PRE_UPDATE_PROFILE:
                    PreUpdateProfileActionUpdateModel preUpdateProfileActionUpdateModel =
                            objectMapper.readValue(jsonBody, PreUpdateProfileActionUpdateModel.class);
                    validateActionModel(preUpdateProfileActionUpdateModel, PreUpdateProfileActionUpdateModel.class);
                    actionUpdateModel = preUpdateProfileActionUpdateModel;
                    break;
                default:
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No update model deserializer found for action type: " + actionType);
                    }
                    break;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Action update model deserialized successfully for type: " + actionType);
            }
        } catch (JsonProcessingException e) {
            String errorMessage = "Failed to deserialize action update model for type: " + actionType + 
                    ". Invalid JSON payload.";
            LOG.warn(errorMessage, e);
            throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                    ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_PAYLOAD);
        }

        return actionUpdateModel;
    }

    /**
     * Validate the action model.
     *
     * @param actionModel Action model to be validated.
     * @param modelClass  Class of the action model.
     * @param <T>         Type of the action model.
     */
    private static <T> void validateActionModel(T actionModel, Class<T> modelClass) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(actionModel);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

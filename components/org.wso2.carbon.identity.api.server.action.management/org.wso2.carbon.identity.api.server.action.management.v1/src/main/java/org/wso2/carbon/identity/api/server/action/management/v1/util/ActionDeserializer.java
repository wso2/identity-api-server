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

    /**
     * Deserialize the action model.
     *
     * @param actionType Action type.
     * @param jsonBody   JSON body.
     * @return Action model.
     */
    public static ActionModel deserializeActionModel(Action.ActionTypes actionType, String jsonBody) {

        ActionModel actionModel = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            switch (actionType) {
                case PRE_ISSUE_ACCESS_TOKEN:
                case PRE_ISSUE_ID_TOKEN:
                    actionModel = objectMapper.readValue(jsonBody, ActionModel.class);
                    // Validate the object
                    validateActionModel(actionModel, ActionModel.class);
                    break;
                case PRE_UPDATE_PASSWORD:
                    PreUpdatePasswordActionModel preUpdatePasswordActionModel = objectMapper.readValue(jsonBody,
                            PreUpdatePasswordActionModel.class);
                    // Validate the object
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
                    break;
            }
        } catch (JsonProcessingException e) {
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

        ActionUpdateModel actionUpdateModel = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            switch (actionType) {
                case PRE_ISSUE_ACCESS_TOKEN:
                case PRE_ISSUE_ID_TOKEN:
                    actionUpdateModel = objectMapper.readValue(jsonBody, ActionUpdateModel.class);
                    // Validate the object
                    validateActionModel(actionUpdateModel, ActionUpdateModel.class);
                    break;
                case PRE_UPDATE_PASSWORD:
                    PreUpdatePasswordActionUpdateModel preUpdatePasswordActionUpdateModel =
                            objectMapper.readValue(jsonBody, PreUpdatePasswordActionUpdateModel.class);
                    // Validate the object
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
                    break;
            }
        } catch (JsonProcessingException e) {
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

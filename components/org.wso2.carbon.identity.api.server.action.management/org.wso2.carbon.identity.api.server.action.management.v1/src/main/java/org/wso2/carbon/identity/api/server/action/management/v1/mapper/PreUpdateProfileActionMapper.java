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
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtServerException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdateProfileActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdateProfileActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdateProfileActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMapperUtil;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMgtEndpointUtil;
import org.wso2.carbon.identity.user.pre.update.profile.action.api.model.PreUpdateProfileAction;

import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NOT_SUPPORTED_ALLOWED_HEADERS;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NOT_SUPPORTED_ALLOWED_PARAMETERS;

/**
 * Pre Update Profile Action Mapper.
 */
public class PreUpdateProfileActionMapper implements ActionMapper {

    @Override
    public Action.ActionTypes getSupportedActionType() {

        return Action.ActionTypes.PRE_UPDATE_PROFILE;
    }

    @Override
    public Action toAction(ActionModel actionModel) throws ActionMgtException {

        if (!(actionModel instanceof PreUpdateProfileActionModel)) {
            throw new ActionMgtServerException("Unsupported action model for action type: " + getSupportedActionType());
        }

        validateAllowedHeadersAndParameters(actionModel.getEndpoint().getAllowedHeaders(),
                actionModel.getEndpoint().getAllowedParameters());
        Action basicAction = ActionMapperUtil.buildActionRequest(getSupportedActionType(), actionModel);
        return new PreUpdateProfileAction.RequestBuilder(basicAction)
                .attributes(((PreUpdateProfileActionModel) actionModel).getAttributes())
                .build();
    }

    @Override
    public Action toAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException {

        if (!(actionUpdateModel instanceof PreUpdateProfileActionUpdateModel)) {
            throw new ActionMgtServerException("Unsupported action update model for action type: " +
                    getSupportedActionType());
        }

        if (actionUpdateModel.getEndpoint() != null) {
            validateAllowedHeadersAndParameters(actionUpdateModel.getEndpoint().getAllowedHeaders(),
                    actionUpdateModel.getEndpoint().getAllowedParameters());
        }
        Action basicUpdatingAction = ActionMapperUtil.buildUpdatingActionRequest(getSupportedActionType(),
                actionUpdateModel);
        return new PreUpdateProfileAction.RequestBuilder(basicUpdatingAction)
                .attributes(((PreUpdateProfileActionUpdateModel) actionUpdateModel).getAttributes())
                .build();
    }

    @Override
    public ActionResponse toActionResponse(Action action) throws ActionMgtException {

        if (!(action instanceof PreUpdateProfileAction)) {
            throw new ActionMgtServerException("Unsupported action response for action type: " +
                    getSupportedActionType());
        }

        ActionResponse actionResponse = ActionMapperUtil.buildActionResponse(action);
        return new PreUpdateProfileActionResponse(actionResponse)
                .attributes(((PreUpdateProfileAction) action).getAttributes());
    }

    private void validateAllowedHeadersAndParameters(List<String> allowedHeaders, List<String> allowedParams) {

        if (allowedHeaders != null) {
            throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                    ERROR_NOT_SUPPORTED_ALLOWED_HEADERS, getSupportedActionType().name());
        }

        if (allowedParams != null) {
            throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                    ERROR_NOT_SUPPORTED_ALLOWED_PARAMETERS, getSupportedActionType().name());
        }
    }
}

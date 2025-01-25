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

import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMapperUtil;

/**
 * This interface defines the Action ActionConverter.
 * Action ActionConverter is the component that is responsible for the conversions between Action and ExtendedAction
 * objects.
 */
public interface ActionMapper {

    Action.ActionTypes getSupportedActionType();

    /**
     * Convert {@link ActionModel} object into {@link Action} object.
     *
     * @param actionModel ActionModel object.
     * @return Action object.
     */
    Action toAction(ActionModel actionModel) throws ActionMgtException;

    /**
     * Convert {@link ActionUpdateModel} object into {@link Action} object.
     *
     * @param actionUpdateModel ActionUpdateModel object.
     * @return Action object.
     */
    Action toAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException;

    /**
     * Convert {@link Action} object into {@link ActionResponse} object.
     *
     * @param action Action object.
     * @return ActionResponse object.
     */
    ActionResponse toActionResponse(Action action) throws ActionMgtException;

    /**
     * Convert {@link Action} object into {@link ActionBasicResponse} object.
     *
     * @param action Action object.
     * @return ActionBasicResponse object.
     */
    default ActionBasicResponse toActionBasicResponse(Action action) throws ActionMgtException {

        return ActionMapperUtil.buildActionBasicResponse(action);
    }
}

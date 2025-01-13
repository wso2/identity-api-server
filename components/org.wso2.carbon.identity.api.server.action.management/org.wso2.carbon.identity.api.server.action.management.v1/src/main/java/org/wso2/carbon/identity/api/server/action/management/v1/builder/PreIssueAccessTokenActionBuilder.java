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

package org.wso2.carbon.identity.api.server.action.management.v1.builder;

import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionBuilderUtil;

/**
 * Pre Issue Access Token Action Builder.
 */
public class PreIssueAccessTokenActionBuilder implements ActionBuilder {

    @Override
    public Action.ActionTypes getSupportedActionType() {

        return Action.ActionTypes.PRE_ISSUE_ACCESS_TOKEN;
    }

    @Override
    public Action buildAction(ActionModel actionModel) throws ActionMgtException {

        return ActionBuilderUtil.buildActionRequest(getSupportedActionType(), actionModel);
    }

    @Override
    public Action buildAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException {

        return ActionBuilderUtil.buildUpdatingActionRequest(getSupportedActionType(), actionUpdateModel);
    }

    @Override
    public ActionResponse buildActionResponse(Action action) throws ActionMgtException {

        return ActionBuilderUtil.buildActionResponse(action);
    }
}

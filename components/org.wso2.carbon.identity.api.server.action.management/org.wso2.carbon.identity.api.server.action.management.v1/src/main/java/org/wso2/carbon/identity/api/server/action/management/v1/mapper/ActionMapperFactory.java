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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtServerException;
import org.wso2.carbon.identity.action.management.api.model.Action;

/**
 * Factory class to get the ActionMapper based on the action type.
 */
public class ActionMapperFactory {

    private static final Log LOG = LogFactory.getLog(ActionMapperFactory.class);

    /**
     * Get ActionMapper object based on the action type.
     *
     * @param actionType Action type.
     * @return ActionMapper object.
     */
    public static ActionMapper getActionMapper(Action.ActionTypes actionType) throws ActionMgtServerException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting action mapper for type: " + (actionType != null ? actionType : "null"));
        }
        ActionMapper actionMapper = null;
        switch (actionType) {
            case PRE_ISSUE_ACCESS_TOKEN:
                actionMapper = new PreIssueAccessTokenActionMapper();
                break;
            case PRE_UPDATE_PASSWORD:
                actionMapper = new PreUpdatePasswordActionMapper();
                break;
            case PRE_UPDATE_PROFILE:
                actionMapper = new PreUpdateProfileActionMapper();
                break;
            default:
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No mapper available for action type: " + actionType);
                }
                break;
        }

        if (actionMapper == null) {
            LOG.error("No Action Mapper found for the given action type: " + 
                    (actionType != null ? actionType.getDisplayName() : "null"));
            throw new ActionMgtServerException("No Action Mapper found for the given action type: " +
                    (actionType != null ? actionType.getDisplayName() : "null"));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Action mapper retrieved successfully for type: " + actionType);
        }
        return actionMapper;
    }
}

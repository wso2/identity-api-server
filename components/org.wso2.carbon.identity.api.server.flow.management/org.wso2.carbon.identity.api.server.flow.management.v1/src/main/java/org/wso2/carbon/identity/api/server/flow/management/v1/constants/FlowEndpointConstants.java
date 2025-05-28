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

package org.wso2.carbon.identity.api.server.flow.management.v1.constants;

import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;

/**
 * Constants related to the flow endpoint.
 */
public class FlowEndpointConstants {

    private FlowEndpointConstants() {

    }

    public static final String FLOW_PREFIX = "FM-";
    public static final String ERROR_CODE_INVALID_FLOW_TYPE = "60013";
    public static final String ERROR_MESSAGE_INVALID_FLOW_TYPE = "Invalid action type.";
    public static final String ERROR_DESCRIPTION_INVALID_FLOW_TYPE =
            "The provided flow type is not supported.";

    /**
     * Supported flow types.
     */
    public enum FlowType {

        SELF_REGISTRATION,
        PASSWORD_RECOVERY,
        ASK_PASSWORD,;

        /**
         * Check if a given string is a valid flow type.
         *
         * @param value Flow type as string
         */
        public static void validateFlowType(String value) {

            for (FlowType type : FlowType.values()) {
                if (type.name().equals(value)) {
                    return;
                }
            }
            throw Utils.handleFlowMgtException(new FlowMgtClientException(ERROR_CODE_INVALID_FLOW_TYPE,
                    ERROR_MESSAGE_INVALID_FLOW_TYPE, ERROR_DESCRIPTION_INVALID_FLOW_TYPE));
        }
    }

    /**
     * Constants related to flow schema.
     */
    public static class Schema {

        public static final String IDP_NAME = "idpName";
    }

}

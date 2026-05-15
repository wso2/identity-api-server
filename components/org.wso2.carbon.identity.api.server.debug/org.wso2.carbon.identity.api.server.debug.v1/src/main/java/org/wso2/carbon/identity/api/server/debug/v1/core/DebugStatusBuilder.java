/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;

import java.util.Locale;

/**
 * Utility class for resolving API status enums from framework status strings.
 */
public final class DebugStatusBuilder {

    private static final Log LOG = LogFactory.getLog(DebugStatusBuilder.class);

    private DebugStatusBuilder() {
        // Utility class — prevent instantiation.
    }

    /**
     * Resolves the {@link DebugResponse.StatusEnum} from the framework status string.
     * Falls back to {@link DebugResponse.StatusEnum#SUCCESS_INCOMPLETE} if the status is
     * unrecognized or absent.
     *
     * @param status Status string from the framework response.
     * @return Resolved {@link DebugResponse.StatusEnum}.
     */
    public static DebugResponse.StatusEnum resolveDebugStatus(String status) {

        if (status != null) {
            String normalized = status.toUpperCase(Locale.ROOT);
            for (DebugResponse.StatusEnum value : DebugResponse.StatusEnum.values()) {
                if (value.name().equals(normalized)) {
                    return value;
                }
            }
            LOG.warn(String.format(
                    "Unrecognized debug status from framework: %s. Falling back to SUCCESS_INCOMPLETE.", status));
        }
        return DebugResponse.StatusEnum.SUCCESS_INCOMPLETE;
    }

    /**
     * Resolves the {@link DebugResult.StatusEnum} from the framework status string.
     * Falls back to {@link DebugResult.StatusEnum#FAILURE} if the status is
     * unrecognized or absent.
     *
     * @param status Status string from the framework response.
     * @return Resolved {@link DebugResult.StatusEnum}.
     */
    public static DebugResult.StatusEnum resolveResultStatus(String status) {

        if (status != null) {
            String normalized = status.toUpperCase(Locale.ROOT);
            for (DebugResult.StatusEnum value : DebugResult.StatusEnum.values()) {
                if (value.name().equals(normalized)) {
                    return value;
                }
            }
            LOG.warn(String.format(
                    "Unrecognized debug status from framework: %s. Falling back to FAILURE.", status));
        }
        return DebugResult.StatusEnum.FAILURE;
    }
}

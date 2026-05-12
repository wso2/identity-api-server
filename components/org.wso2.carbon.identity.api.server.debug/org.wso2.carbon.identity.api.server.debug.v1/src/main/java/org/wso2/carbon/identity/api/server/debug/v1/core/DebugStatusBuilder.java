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
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;

import java.util.Locale;

/**
 * Builds API status enums from framework status and success values.
 */
public class DebugStatusBuilder {

    private final Log log;

    public DebugStatusBuilder(Log log) {

        this.log = log;
    }

    public DebugResponse.StatusEnum buildDebugStatus(Object status, Boolean success) {

        DebugResponse.StatusEnum resolvedStatus =
                resolveStatus(status, DebugResponse.StatusEnum.values());
        if (resolvedStatus != null) {
            return resolvedStatus;
        }

        if (Boolean.FALSE.equals(success)) {
            return DebugResponse.StatusEnum.FAILURE;
        }

        return DebugResponse.StatusEnum.SUCCESS_INCOMPLETE;
    }

    public DebugResult.StatusEnum buildResultStatus(Object status, Object success) {

        DebugResult.StatusEnum resolvedStatus = resolveStatus(status, DebugResult.StatusEnum.values());
        if (resolvedStatus != null) {
            return resolvedStatus;
        }

        if (success instanceof Boolean) {
            return (Boolean) success ? DebugResult.StatusEnum.SUCCESS_COMPLETE : DebugResult.StatusEnum.FAILURE;
        }

        return DebugResult.StatusEnum.FAILURE;
    }

    private <T extends Enum<T>> T resolveStatus(Object status, T[] values) {

        if (status == null) {
            return null;
        }

        String statusValue = status.toString().toUpperCase(Locale.ROOT);
        for (T value : values) {
            if (value.name().equals(statusValue)) {
                return value;
            }
        }

        log.warn("Unrecognized debug status from framework: " + status + ". Falling back to derived defaults.");
        return null;
    }
}

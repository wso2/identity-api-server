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

package org.wso2.carbon.identity.api.server.debug.v1.model;

import java.util.Map;

/**
 * Internal DTO holding common fields extracted from a DebugFrameworkResponse,
 * shared between the start-session and get-result response builders.
 */
public class FrameworkResponseDTO {

    private final String debugId;
    private final String rawStatus;
    private final String rawMessage;
    private final Map<String, Object> metadata;

    public FrameworkResponseDTO(String debugId, String rawStatus, String rawMessage,
            Map<String, Object> metadata) {

        this.debugId = debugId;
        this.rawStatus = rawStatus;
        this.rawMessage = rawMessage;
        this.metadata = metadata;
    }

    public String getDebugId() {

        return debugId;
    }

    public String getRawStatus() {

        return rawStatus;
    }

    public String getRawMessage() {

        return rawMessage;
    }

    public Map<String, Object> getMetadata() {

        return metadata;
    }
}

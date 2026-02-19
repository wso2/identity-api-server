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

package org.wso2.carbon.identity.api.server.debug.v1;

import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import javax.ws.rs.core.Response;

/**
 * Abstract service class for handling debug API operations.
 * Defines the contract for starting debug sessions and retrieving debug results.
 * Implementations should provide the actual logic for these operations.
 */

public abstract class DebugApiService {

    /**
     * Starts a debug session for a given debug connection request.
     *
     * @param debugConnectionRequest The debug connection request.
     * @return The response containing debug session information.
     */
    public abstract Response startDebugSession(DebugConnectionRequest debugConnectionRequest);

    /**
     * Retrieves the debug result for a given session ID.
     *
     * @param sessionId The session ID.
     * @return The response containing the debug result.
     */
    public abstract Response getDebugResult(String sessionId);
}

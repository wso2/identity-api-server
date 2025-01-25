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

package org.wso2.carbon.identity.api.server.script.library.v1.factories;

import org.wso2.carbon.identity.api.server.script.library.common.ScriptLibraryServiceHolder;
import org.wso2.carbon.identity.api.server.script.library.v1.core.ServerScriptLibrariesService;
import org.wso2.carbon.identity.functions.library.mgt.FunctionLibraryManagementService;

/**
 * Factory class for Server Script Libraries API.
 */
public class ServerScriptLibrariesServiceFactory {

    private static final ServerScriptLibrariesService SERVICE;

    static {
        FunctionLibraryManagementService functionLibraryManagementService = ScriptLibraryServiceHolder
                .getScriptLibraryManagementService();

        if (functionLibraryManagementService == null) {
            throw new IllegalStateException("FunctionLibraryManagementService is not available from OSGi context.");
        }

        SERVICE = new ServerScriptLibrariesService(functionLibraryManagementService);
    }

    /**
     * Get Server Script Libraries API service.
     *
     * @return Server Script Libraries API service.
     */
    public static ServerScriptLibrariesService getServerScriptLibrariesService() {

        return SERVICE;
    }
}

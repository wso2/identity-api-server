/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.script.library.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.functions.library.mgt.FunctionLibraryManagementService;

/**
 * Service holder class for script library management.
 */
public class ScriptLibraryServiceHolder {

    private ScriptLibraryServiceHolder() {

    }

    private static class FunctionLibraryManagementServiceHolder {

        static final FunctionLibraryManagementService SERVICE =
                (FunctionLibraryManagementService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(FunctionLibraryManagementService.class, null);
    }

    /**
     * Get FunctionLibraryManagementService osgi service.
     *
     * @return FunctionLibraryManagementService
     */
    public static FunctionLibraryManagementService getScriptLibraryManagementService() {

        return FunctionLibraryManagementServiceHolder.SERVICE;
    }
}

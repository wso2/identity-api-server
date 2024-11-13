/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.AuthorizationDetailsTypeManager;

/**
 * Factory class for {@link AuthorizationDetailsTypeManager}.
 */
public class AuthorizationDetailsTypeMgtOSGiServiceFactory extends
        AbstractFactoryBean<AuthorizationDetailsTypeManager> {

    private AuthorizationDetailsTypeManager authorizationDetailsTypeManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected AuthorizationDetailsTypeManager createInstance() throws Exception {

        if (this.authorizationDetailsTypeManager == null) {
            this.authorizationDetailsTypeManager = (AuthorizationDetailsTypeManager) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(AuthorizationDetailsTypeManager.class, null);
            if (this.authorizationDetailsTypeManager == null) {
                throw new Exception("Unable to retrieve AuthorizationDetailsTypeManager service.");
            }
        }
        return this.authorizationDetailsTypeManager;
    }
}

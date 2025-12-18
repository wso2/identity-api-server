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

package org.wso2.carbon.identity.api.server.credential.management.v1.factories;

import org.wso2.carbon.identity.api.server.credential.management.common.CredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.core.PasskeyCredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.common.core.PushCredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.v1.impl.CredentialManagementServiceImpl;

import java.util.EnumMap;
import java.util.Map;

/**
 * Factory class for CredentialManagementServiceImpl.
 */
public class CredentialManagementServiceFactory {

    public static CredentialManagementServiceImpl getCredentialManagementService() {

        Map<CredentialTypes, CredentialHandler> handlerMap = new EnumMap<>(CredentialTypes.class);
        handlerMap.put(CredentialTypes.PASSKEY, new PasskeyCredentialHandler());
        handlerMap.put(CredentialTypes.PUSH_AUTH, new PushCredentialHandler());
        return new CredentialManagementServiceImpl(handlerMap);
    }
}

/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.notification.sender.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.email.mgt.SMSProviderPayloadTemplateManager;

/**
 * Factory Beans serve as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the SMSProviderPayloadTemplateManager type of object inside the container.
 */
public class SMSProviderPayloadTemplateMgtOSSGiServiceFactory extends
        AbstractFactoryBean<SMSProviderPayloadTemplateManager> {

    private SMSProviderPayloadTemplateManager smsProviderPayloadTemplateManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected SMSProviderPayloadTemplateManager createInstance() throws Exception {

        if (this.smsProviderPayloadTemplateManager == null) {
            SMSProviderPayloadTemplateManager taskOperationService =
                    (SMSProviderPayloadTemplateManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(SMSProviderPayloadTemplateManager.class, null);

            if (taskOperationService != null) {
                this.smsProviderPayloadTemplateManager = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve SMS provider payload template manager service.");
            }
        }
        return this.smsProviderPayloadTemplateManager;
    }
}

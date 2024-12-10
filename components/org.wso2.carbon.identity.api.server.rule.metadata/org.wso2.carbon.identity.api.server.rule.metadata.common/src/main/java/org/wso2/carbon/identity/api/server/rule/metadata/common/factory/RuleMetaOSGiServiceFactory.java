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

package org.wso2.carbon.identity.api.server.rule.metadata.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.rule.metadata.service.RuleMetadataService;

/**
 * Factory bean used to instantiate the RuleMetadataService type of object inside the container.
 */
public class RuleMetaOSGiServiceFactory extends AbstractFactoryBean<RuleMetadataService> {

    private RuleMetadataService ruleMetadataService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected RuleMetadataService createInstance() throws Exception {

        if (this.ruleMetadataService == null) {
            ruleMetadataService = (RuleMetadataService) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(RuleMetadataService.class, null);
            if (ruleMetadataService == null) {
                throw new Exception("Rule Metadata Service is not available.");
            }
        }
        return this.ruleMetadataService;
    }
}

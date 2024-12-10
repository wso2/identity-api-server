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

package org.wso2.carbon.identity.api.server.rule.metadata.common;

import org.wso2.carbon.identity.rule.metadata.service.RuleMetadataService;

/**
 * Service holder class for Rule Metadata Service.
 */
public class RuleMetadataServiceHolder {

    private static RuleMetadataService ruleMetadataService;

    public static RuleMetadataService getRuleMetadataService() {

        return ruleMetadataService;
    }

    public static void setRuleMetadataService(RuleMetadataService ruleMetadataService) {

        RuleMetadataServiceHolder.ruleMetadataService = ruleMetadataService;
    }
}

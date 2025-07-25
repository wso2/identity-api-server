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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.rule.metadata.common.RuleMetadataServiceHolder;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.core.ServerRuleMetadataService;
import org.wso2.carbon.identity.rule.metadata.api.service.RuleMetadataService;

/**
 * Factory class for ServerRuleMetadataService.
 */
public class ServerRuleMetadataServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerRuleMetadataServiceFactory.class);
    private static final ServerRuleMetadataService SERVICE;

    static {
        LOG.info("Initializing ServerRuleMetadataService");
        RuleMetadataService ruleMetadataService = RuleMetadataServiceHolder.getRuleMetadataService();
        if (ruleMetadataService == null) {
            LOG.error("RuleMetadataService is not available from OSGi context");
            throw new IllegalStateException("RuleMetadataService is not available from OSGi context.");
        }
        SERVICE = new ServerRuleMetadataService(ruleMetadataService);
        LOG.info("ServerRuleMetadataService initialized successfully");
    }

    /**
     * Get ServerRuleMetadataService service.
     *
     * @return ServerRuleMetadataService service.
     */
    public static ServerRuleMetadataService getServerRuleMetadataService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving ServerRuleMetadataService instance");
        }
        return SERVICE;
    }
}

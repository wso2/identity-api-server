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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.RulesApiService;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.core.ServerRuleMetadataService;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.factories.ServerRuleMetadataServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Rules Metadata REST API.
 */
public class RulesApiServiceImpl implements RulesApiService {

    private static final Log LOG = LogFactory.getLog(RulesApiServiceImpl.class);
    private final ServerRuleMetadataService serverRuleMetadataService;

    public RulesApiServiceImpl() {

        LOG.info("Initializing RulesApiServiceImpl");
        try {
            serverRuleMetadataService = ServerRuleMetadataServiceFactory.getServerRuleMetadataService();
            LOG.info("RulesApiServiceImpl initialized successfully");
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while retrieving RuleMetadataService", e);
            throw new RuntimeException("Error occurred while retrieving RuleMetadataService.", e);
        }
    }

    @Override
    public Response getExpressionMeta(String flow) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received request to get expression metadata for flow: " + flow);
        }
        
        if (flow == null || flow.trim().isEmpty()) {
            LOG.warn("Received null or empty flow parameter for expression metadata request");
        }

        return Response.ok().entity(serverRuleMetadataService.getExpressionMeta(flow)).build();
    }
}

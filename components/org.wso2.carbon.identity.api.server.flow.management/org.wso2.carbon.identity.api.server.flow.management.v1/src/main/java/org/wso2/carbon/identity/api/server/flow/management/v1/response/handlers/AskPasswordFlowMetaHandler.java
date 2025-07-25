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

package org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.management.v1.BaseConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.SelfRegistrationConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.flow.mgt.Constants.FlowTypes.INVITED_USER_REGISTRATION;

/**
 * Handler for managing the ask password flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for ask password flow.
 */
public class AskPasswordFlowMetaHandler extends AbstractMetaResponseHandler {

    private static final Log LOG = LogFactory.getLog(AskPasswordFlowMetaHandler.class);

    @Override
    public String getFlowType() {

        return String.valueOf(INVITED_USER_REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return SELF_REGISTRATION_ATTRIBUTE_PROFILE;
    }

    @Override
    public SelfRegistrationConnectorConfigs getConnectorConfigs() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving connector configs for ask password flow.");
        }
        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        SelfRegistrationConnectorConfigs connectorConfigs = new SelfRegistrationConnectorConfigs();
        BaseConnectorConfigs baseConfigs = super.getConnectorConfigs();
        connectorConfigs.setMultiAttributeLoginEnabled(baseConfigs.getMultiAttributeLoginEnabled());
        boolean selfRegEnabled = utils.isFlowConfigEnabled(tenantDomain,
                IdentityRecoveryConstants.ConnectorConfig.ENABLE_SELF_SIGNUP);
        connectorConfigs.setSelfRegistrationEnabled(selfRegEnabled);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved connector configs for ask password flow in tenant: " + tenantDomain +
                    " with selfRegistration: " + selfRegEnabled);
        }
        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>();
    }

    @Override
    public List<String> getSupportedExecutors() {

        List<String> supportedExecutors = new ArrayList<>(super.getSupportedExecutors());
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        return supportedExecutors;
    }

}

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
import org.wso2.carbon.identity.api.server.flow.management.v1.BaseFlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.multi.attribute.login.constants.MultiAttributeLoginConstants;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ABSTRACT_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.APPLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.CONFIRMATION_CODE_VALIDATION_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FACEBOOK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FIDO2_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.GOOGLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.MAGIC_LINK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.OFFICE365_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.OPENID_CONNECT_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_ONBOARD_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SMS_OTP_EXECUTOR;

/**
 * Abstract class for handling meta responses for different flows.
 * This class provides methods to retrieve flow type, attribute profile,
 * supported executors, connector configurations, and connection meta information.
 */
public abstract class AbstractMetaResponseHandler {

    private static final Log LOG = LogFactory.getLog(AbstractMetaResponseHandler.class);

    /**
     * Get the flow type.
     *
     * @return Flow type.
     */
    public abstract String getFlowType();

    /**
     * Get the attribute profile.
     *
     * @return Attribute profile.
     */
    public abstract String getAttributeProfile();

    /**
     * Get the supported executors for the flow.
     *
     * @return List of supported executors.
     */
    public List<String> getSupportedExecutors() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving supported executors for flow type: " + getFlowType());
        }
        ArrayList<String> supportedExecutors = new ArrayList<>();
        supportedExecutors.add(OPENID_CONNECT_EXECUTOR);
        supportedExecutors.add(GOOGLE_EXECUTOR);
        supportedExecutors.add(FACEBOOK_EXECUTOR);
        supportedExecutors.add(OFFICE365_EXECUTOR);
        supportedExecutors.add(APPLE_EXECUTOR);
        supportedExecutors.add(FIDO2_EXECUTOR);
        supportedExecutors.add(PASSWORD_PROVISIONING_EXECUTOR);
        supportedExecutors.add(PASSWORD_ONBOARD_EXECUTOR);
        supportedExecutors.add(ABSTRACT_OTP_EXECUTOR);
        supportedExecutors.add(EMAIL_OTP_EXECUTOR);
        supportedExecutors.add(SMS_OTP_EXECUTOR);
        supportedExecutors.add(MAGIC_LINK_EXECUTOR);
        supportedExecutors.add(CONFIRMATION_CODE_VALIDATION_EXECUTOR);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved " + supportedExecutors.size() + " supported executors for flow type: "
                    + getFlowType());
        }
        return supportedExecutors;
    };

    /**
     * Get the connector configurations for the flow.
     *
     * @return Connector configurations.
     */
    public BaseConnectorConfigs getConnectorConfigs() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving connector configs for flow type: " + getFlowType());
        }
        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        BaseConnectorConfigs connectorConfigs = new BaseConnectorConfigs();
        boolean multiAttributeLogin = utils.isFlowConfigEnabled(tenantDomain,
                MultiAttributeLoginConstants.MULTI_ATTRIBUTE_LOGIN_PROPERTY);
        connectorConfigs.setMultiAttributeLoginEnabled(multiAttributeLogin);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved connector configs for flow type: " + getFlowType() + " in tenant: " + tenantDomain +
                    " with multiAttributeLogin: " + multiAttributeLogin);
        }
        return connectorConfigs;
    }

    /**
     * Get the required input fields for the flow.
     *
     * @return List of required input fields.
     */
    public abstract List<String> getRequiredInputFields();

    /**
     * Create the meta response object populated with common values.
     *
     * @return BaseFlowMetaResponse instance.
     */
    public BaseFlowMetaResponse createResponse() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating meta response for flow type: " + getFlowType());
        }
        BaseFlowMetaResponse response = new BaseFlowMetaResponse();
        response.setFlowType(getFlowType());
        response.setAttributeProfile(getAttributeProfile());
        response.setSupportedExecutors(getSupportedExecutors());
        response.setConnectorConfigs(getConnectorConfigs());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully created meta response for flow type: " + getFlowType());
        }
        return response;
    }

    /**
     * Return login related input fields common across flows.
     */
    protected List<String> getLoginInputFields() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving login input fields for flow type: " + getFlowType());
        }
        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        List<String> fields = new java.util.ArrayList<>();
        boolean multiAttributeEnabled = utils.isFlowConfigEnabled(tenantDomain,
                MultiAttributeLoginConstants.MULTI_ATTRIBUTE_LOGIN_PROPERTY);
        if (multiAttributeEnabled) {
            fields.add(FlowEndpointConstants.USER_IDENTIFIER);
            fields.add(FlowEndpointConstants.USERNAME_IDENTIFIER);
        } else {
            fields.add(FlowEndpointConstants.USERNAME_IDENTIFIER);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved " + fields.size() + " login input fields for flow type: " + getFlowType() +
                    " with multiAttribute: " + multiAttributeEnabled);
        }
        return fields;
    }
}

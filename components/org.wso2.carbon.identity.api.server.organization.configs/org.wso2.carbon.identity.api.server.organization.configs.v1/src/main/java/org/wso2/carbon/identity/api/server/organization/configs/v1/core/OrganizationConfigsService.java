/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.configs.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.organization.configs.v1.model.Config;
import org.wso2.carbon.identity.api.server.organization.configs.v1.model.Properties;
import org.wso2.carbon.identity.organization.config.service.OrganizationConfigManager;
import org.wso2.carbon.identity.organization.config.service.exception.OrganizationConfigClientException;
import org.wso2.carbon.identity.organization.config.service.exception.OrganizationConfigException;
import org.wso2.carbon.identity.organization.config.service.model.ConfigProperty;
import org.wso2.carbon.identity.organization.config.service.model.DiscoveryConfig;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.organization.config.service.constant.OrganizationConfigConstants.ErrorMessages.ERROR_CODE_DISCOVERY_CONFIG_CONFLICT;
import static org.wso2.carbon.identity.organization.config.service.constant.OrganizationConfigConstants.ErrorMessages.ERROR_CODE_DISCOVERY_CONFIG_NOT_EXIST;

/**
 * Perform organization configuration management related operations.
 */
public class OrganizationConfigsService {

    private final OrganizationConfigManager organizationConfigManager;

    private static final Log LOG = LogFactory.getLog(OrganizationConfigsService.class);

    public OrganizationConfigsService(OrganizationConfigManager organizationConfigManager) {

        this.organizationConfigManager = organizationConfigManager;
    }

    /**
     * Add the organization discovery configuration in the primary organization.
     *
     * @param config The organization discovery configuration.
     */
    public void addDiscoveryConfiguration(Config config) {

        if (config == null) {
            LOG.error("Configuration object is null. Cannot add discovery configuration.");
            throw new IllegalArgumentException("Configuration object cannot be null.");
        }
        
        if (config.getProperties() == null) {
            LOG.error("Configuration properties are null. Cannot add discovery configuration.");
            throw new IllegalArgumentException("Configuration properties cannot be null.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding organization discovery configuration with " + config.getProperties().size() + 
                    " properties.");
        }

        List<ConfigProperty> configProperties = config.getProperties().stream()
                .map(property -> new ConfigProperty(property.getKey(), property.getValue()))
                .collect(Collectors.toList());
        try {
            organizationConfigManager.addDiscoveryConfiguration(new DiscoveryConfig(configProperties));
            LOG.info("Successfully added organization discovery configuration.");
        } catch (OrganizationConfigException e) {
            throw handleException(e);
        }
    }

    /**
     * Update the organization discovery configuration in the primary organization.
     *
     * @param config The organization discovery configuration.
     */
    public void updateDiscoveryConfiguration(Config config) {

        if (config == null) {
            LOG.error("Configuration object is null. Cannot update discovery configuration.");
            throw new IllegalArgumentException("Configuration object cannot be null.");
        }
        
        if (config.getProperties() == null) {
            LOG.error("Configuration properties are null. Cannot update discovery configuration.");
            throw new IllegalArgumentException("Configuration properties cannot be null.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating organization discovery configuration with " + config.getProperties().size() + 
                    " properties.");
        }

        List<ConfigProperty> configProperties = config.getProperties().stream()
                .map(property -> new ConfigProperty(property.getKey(), property.getValue()))
                .collect(Collectors.toList());
        try {
            organizationConfigManager.updateDiscoveryConfiguration
                    (new DiscoveryConfig(configProperties));
            LOG.info("Successfully updated organization discovery configuration.");
        } catch (OrganizationConfigException e) {
            throw handleException(e);
        }
    }

    /**
     * Fetch organization discovery configuration.
     *
     * @return The organization discovery configuration.
     */
    public Config getDiscoveryConfiguration() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving organization discovery configuration.");
        }

        try {
            DiscoveryConfig discoveryConfig = organizationConfigManager.getDiscoveryConfiguration();

            List<Properties> properties = discoveryConfig.getConfigProperties().stream()
                    .map(configProperty -> {
                        Properties prop = new Properties();
                        prop.setKey(configProperty.getKey());
                        prop.setValue(configProperty.getValue());
                        return prop;
                    }).collect(Collectors.toList());
            Config config = new Config();
            config.setProperties(properties);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieved organization discovery configuration with " + properties.size() + " properties.");
            }
            
            return config;
        } catch (OrganizationConfigException e) {
            throw handleException(e);
        }
    }

    /**
     * Delete the organization discovery configuration.
     */
    public void deleteDiscoveryConfiguration() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting organization discovery configuration.");
        }

        try {
            organizationConfigManager.deleteDiscoveryConfiguration();
            LOG.info("Successfully deleted organization discovery configuration.");
        } catch (OrganizationConfigException e) {
            throw handleException(e);
        }
    }

    private APIError handleException(OrganizationConfigException e) {

        if (e instanceof OrganizationConfigClientException) {
            throw buildClientError(e);
        }
        throw buildServerError(e);
    }

    private APIError buildClientError(OrganizationConfigException e) {

        String errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse.Builder().withCode(e.getErrorCode()).withMessage(e.getMessage())
                .withDescription(e.getDescription()).build(LOG, e.getMessage());

        Response.Status status = Response.Status.BAD_REQUEST;
        if (ERROR_CODE_DISCOVERY_CONFIG_CONFLICT.getCode().equals(errorCode)) {
            status = Response.Status.CONFLICT;
        } else if (ERROR_CODE_DISCOVERY_CONFIG_NOT_EXIST.getCode().equals(errorCode)) {
            status = Response.Status.NOT_FOUND;
        }
        return new APIError(status, errorResponse);
    }

    private APIError buildServerError(OrganizationConfigException e) {

        String errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse.Builder().withCode(errorCode).withMessage(e.getMessage())
                .withDescription(e.getDescription()).build(LOG, e, e.getMessage());

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }
}

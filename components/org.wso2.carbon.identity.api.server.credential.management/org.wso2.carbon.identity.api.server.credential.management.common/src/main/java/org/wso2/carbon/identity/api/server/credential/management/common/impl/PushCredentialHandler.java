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

package org.wso2.carbon.identity.api.server.credential.management.common.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;
import org.wso2.carbon.identity.notification.push.device.handler.model.Device;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.ErrorMessages.ERROR_CODE_PUSH_AUTH_DEVICE_NOT_FOUND;

/**
 * Credential handler implementation for Push Authentication.
 */
public class PushCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(PushCredentialHandler.class);

    private final DeviceHandlerService deviceHandler;

    public PushCredentialHandler() {

        this.deviceHandler = CredentialManagementServiceDataHolder.getPushDeviceHandler();
    }

    @Override
    public List<CredentialDTO> getCredentialsForUser(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving push authentication credential for user: " + userId);
        }
        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            List<Device> pushCredentials = Collections.singletonList(deviceHandler
                    .getDeviceByUserId(userId, tenantDomain));

            if (LOG.isDebugEnabled()) {
                LOG.info("Successfully retrieved push authentication credential for user: " + userId);
            }
            return pushCredentials.stream().map(this::mapPushToCredentialDTO).collect(Collectors.toList());
        } catch (PushDeviceHandlerException e) {
            if (ERROR_CODE_PUSH_AUTH_DEVICE_NOT_FOUND.equals(e.getErrorCode())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No push authentication devices found for user: " + userId);
                }
                return Collections.emptyList();
            }
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PUSH_AUTH_DEVICE, e, userId);
        }
    }

    @Override
    public void deleteCredentialForUser(String userId, String credentialId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting push authentication credential for user: " + userId);
        }
        try {
            deviceHandler.unregisterDevice(credentialId);
            if (LOG.isDebugEnabled()) {
                LOG.info("Successfully deleted push authentication credential for user: " + userId);
            }
        } catch (PushDeviceHandlerClientException e) {
            throw CredentialManagementUtils.handleClientException(CredentialManagementConstants.ErrorMessages
                    .ERROR_CODE_DELETE_PUSH_AUTH_CREDENTIAL, e, credentialId);
        } catch (PushDeviceHandlerException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PUSH_AUTH_DEVICE, e, userId);
        }
    }

    /**
     * Map Push Authentication device to CredentialDTO.
     *
     * @param credential Push Authentication device.
     * @return CredentialDTO.
     */
    private CredentialDTO mapPushToCredentialDTO(Device credential) {

        return new CredentialDTO.Builder()
            .credentialId(credential.getDeviceId())
            .displayName(credential.getDeviceName())
            .type(CredentialTypes.PUSH_AUTH.getApiValue())
            .build();
    }
}

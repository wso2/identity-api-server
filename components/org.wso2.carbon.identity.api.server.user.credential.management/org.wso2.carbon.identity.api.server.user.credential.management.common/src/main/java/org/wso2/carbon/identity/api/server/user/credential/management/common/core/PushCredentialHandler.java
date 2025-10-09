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

package org.wso2.carbon.identity.api.server.user.credential.management.common.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialHandler;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.user.credential.management.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.notification.push.device.handler.exception.PushDeviceHandlerException;
import org.wso2.carbon.identity.notification.push.device.handler.model.Device;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_PUSH_AUTH_DEVICE_NOT_FOUND;

/**
 * Credential handler implementation for Push Authentication.
 */
public class PushCredentialHandler implements UserCredentialHandler {

    private static final Log LOG = LogFactory.getLog(PushCredentialHandler.class);

    private final DeviceHandlerService deviceHandler;

    public PushCredentialHandler() {

        this.deviceHandler = UserCredentialManagementServiceDataHolder.getPushDeviceHandler();
    }

    @Override
    public List<UserCredentialDTO> getCredentialsForUser(String userId) throws CredentialMgtException {

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
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PUSH_AUTH_DEVICE, e, userId);
        }
    }

    @Override
    public void deleteCredentialForUser(UserCredentialDeletionRequestDTO userCredentialDeletionRequest)
            throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting push authentication credential for user: "
                    + userCredentialDeletionRequest.getUserId());
        }
        try {
            deviceHandler.unregisterDevice(userCredentialDeletionRequest.getCredentialId());
            if (LOG.isDebugEnabled()) {
                LOG.info("Successfully deleted push authentication credential for user: "
                        + userCredentialDeletionRequest.getUserId());
            }
        } catch (PushDeviceHandlerClientException e) {
            throw CredentialManagementUtils.handleClientException(UserCredentialManagementConstants.ErrorMessages
                    .ERROR_CODE_DELETE_PUSH_AUTH_CREDENTIAL, e, userCredentialDeletionRequest.getCredentialId());
        } catch (PushDeviceHandlerException e) {
            throw CredentialManagementUtils.handleServerException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PUSH_AUTH_DEVICE, e,
                    userCredentialDeletionRequest.getUserId());
        }
    }

    /**
     * Map Push Authentication device to UserCredentialDTO.
     *
     * @param credential Push Authentication device.
     * @return UserCredentialDTO.
     */
    private UserCredentialDTO mapPushToCredentialDTO(Device credential) {

        return new UserCredentialDTO.Builder()
            .credentialId(credential.getDeviceId())
            .displayName(credential.getDeviceName())
            .type(CredentialTypes.PUSH_AUTH.getApiValue())
            .build();
    }
}

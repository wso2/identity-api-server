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

package org.wso2.carbon.identity.api.server.credential.management.common.internal;

import org.wso2.carbon.identity.api.server.credential.management.common.AdminCredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtClientException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtServerException;
import org.wso2.carbon.identity.api.server.credential.management.common.service.CredentialHandler;
import org.wso2.carbon.identity.application.authenticator.push.device.handler.DeviceHandler;
import org.wso2.carbon.identity.application.authenticator.push.device.handler.exception.PushDeviceHandlerClientException;
import org.wso2.carbon.identity.application.authenticator.push.device.handler.exception.PushDeviceHandlerServerException;
import org.wso2.carbon.identity.application.authenticator.push.device.handler.model.Device;

import java.util.List;
import java.util.stream.Collectors;

public class PushCredentialHandler implements CredentialHandler {

    private final DeviceHandler deviceHandler;

    public PushCredentialHandler() {
        this.deviceHandler = AdminCredentialManagementServiceDataHolder.getPushDeviceHandler();
    }

    @Override
    public List<CredentialDTO> getCredentialsForUser(String userId) throws AdminCredentialMgtException {
        try {
            List<Device> pushCredentials = deviceHandler.listDevices(userId);
            return pushCredentials.stream().map(this::mapPushToCredentialDTO).collect(Collectors.toList());
        } catch (PushDeviceHandlerServerException e) {
            throw new AdminCredentialMgtServerException(
                    "CM-65006",
                    "Server error retrieving push credentials for user: " + userId,
                    "The server encountered an error while fetching push credentials.",
                    e
            );
        }
    }

    @Override
    public void deleteCredentialForUser(String userId, String credentialId) throws AdminCredentialMgtException {
        try {
            deviceHandler.unregisterDevice(credentialId);
        } catch (PushDeviceHandlerClientException e) {
            throw new AdminCredentialMgtClientException(
                    "CM-65007",
                    "Client error deleting push credential: " + credentialId,
                    "The request to delete the credential was invalid.",
                    e
            );
        } catch (PushDeviceHandlerServerException e) {
            throw new AdminCredentialMgtServerException(
                    "CM-65008",
                    "Server error deleting push credential: " + credentialId,
                    "The server encountered an error while deleting the push credential.",
                    e
            );
        }
    }

    private CredentialDTO mapPushToCredentialDTO(Device credential) {
        CredentialDTO credentialDTO = new CredentialDTO();
        credentialDTO.setCredentialId(credential.getDeviceId());
        credentialDTO.setDisplayName(credential.getDeviceName());
        credentialDTO.setType(CredentialTypes.PUSH_AUTH.getApiValue());

        return credentialDTO;
    }
}

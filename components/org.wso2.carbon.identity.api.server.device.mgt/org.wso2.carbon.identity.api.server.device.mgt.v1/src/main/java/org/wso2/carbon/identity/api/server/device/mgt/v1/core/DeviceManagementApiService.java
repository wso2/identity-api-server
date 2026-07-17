/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.device.mgt.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.device.mgt.common.Constants;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceListLink;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceListResponse;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DevicePatchRequest;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceResponse;
import org.wso2.carbon.identity.device.mgt.api.constant.ErrorMessage;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtClientException;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtException;
import org.wso2.carbon.identity.device.mgt.api.model.Device;
import org.wso2.carbon.identity.device.mgt.api.service.DeviceManagementService;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for Device Management API — handles listing, renaming and deleting devices.
 */
public class DeviceManagementApiService {

    private static final Log LOG = LogFactory.getLog(DeviceManagementApiService.class);
    private static final int DEFAULT_LIMIT = 30;
    private static final int DEFAULT_OFFSET = 0;
    private static final int DEVICE_NAME_MAX_LENGTH = 255;

    private final DeviceManagementService deviceManagementService;

    public DeviceManagementApiService(DeviceManagementService deviceManagementService) {

        this.deviceManagementService = deviceManagementService;
    }

    /**
     * Returns a paginated set of devices registered in the tenant, optionally filtered to a
     * single user's devices. Returns devices of any status (ACTIVE or INACTIVE).
     *
     * @param limit  Maximum number of records to return (defaults to 30 when null).
     * @param offset Number of records to skip (defaults to 0 when null).
     * @param userId ID of the user to filter devices by, or {@code null}/blank for no filtering.
     * @return Paginated device list response.
     */
    public DeviceListResponse listDevices(Integer limit, Integer offset, String userId) {

        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        String resolvedUserId = StringUtils.isNotBlank(userId) ? userId : null;
        validatePaginationParameters(resolvedLimit, resolvedOffset);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            int totalResults = deviceManagementService.getDeviceCount(tenantDomain, resolvedUserId);
            List<Device> devices = deviceManagementService.getDevices(
                    tenantDomain, resolvedOffset, resolvedLimit, resolvedUserId);

            List<DeviceResponse> items = devices.stream()
                    .map(this::toDeviceResponse)
                    .collect(Collectors.toList());

            List<DeviceListLink> links = Util.buildPaginationLinks(
                            resolvedLimit, resolvedOffset, totalResults, Constants.DEVICE_PATH_COMPONENT)
                    .entrySet().stream()
                    .map(link -> new DeviceListLink().rel(link.getKey()).href(link.getValue()))
                    .collect(Collectors.toList());

            return new DeviceListResponse()
                    .totalResults(totalResults)
                    .startIndex(resolvedOffset + 1)
                    .count(items.size())
                    .devices(items)
                    .links(links);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_DEVICES, null);
        }
    }

    private void validatePaginationParameters(int limit, int offset) {

        if (limit < 1 || offset < 0) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.code())
                    .withMessage(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.message())
                    .withDescription(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.description())
                    .build(LOG, "Invalid pagination parameters."));
        }
    }

    /**
     * Returns a single device by ID.
     *
     * @param deviceId Device UUID.
     * @return DeviceResponse.
     */
    public DeviceResponse getDevice(String deviceId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Device device = deviceManagementService.getDeviceById(deviceId, tenantDomain);
            if (device == null) {
                throw new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                        .withCode(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.code())
                        .withMessage(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.message())
                        .withDescription(String.format(
                                Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.description(), deviceId))
                        .build(LOG, "Device not found for id: " + deviceId));
            }
            return toDeviceResponse(device);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEVICE, deviceId);
        }
    }

    /**
     * Updates the display name of a device.
     *
     * @param deviceId     Device UUID.
     * @param patchRequest Request body containing the new name.
     * @return Updated DeviceResponse.
     */
    public DeviceResponse updateDeviceName(String deviceId, DevicePatchRequest patchRequest) {

        validateDeviceName(patchRequest);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Device updated = deviceManagementService.updateDeviceName(
                    deviceId, patchRequest.getDeviceName(), tenantDomain);
            return toDeviceResponse(updated);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEVICE, deviceId);
        }
    }

    private void validateDeviceName(DevicePatchRequest patchRequest) {

        String deviceName = patchRequest != null ? patchRequest.getDeviceName() : null;
        if (StringUtils.isBlank(deviceName) || deviceName.length() > DEVICE_NAME_MAX_LENGTH) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(Constants.ErrorMessage.ERROR_CODE_INVALID_DEVICE_NAME.code())
                    .withMessage(Constants.ErrorMessage.ERROR_CODE_INVALID_DEVICE_NAME.message())
                    .withDescription(Constants.ErrorMessage.ERROR_CODE_INVALID_DEVICE_NAME.description())
                    .build(LOG, "Invalid device name."));
        }
    }

    /**
     * Deletes a device by ID.
     *
     * @param deviceId Device UUID.
     */
    public void deleteDevice(String deviceId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            deviceManagementService.deleteDevice(deviceId, tenantDomain);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_DEVICE, deviceId);
        }
    }

    private DeviceResponse toDeviceResponse(Device device) {

        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setUserId(device.getUserId());
        response.setDeviceName(device.getDeviceName());
        response.setDeviceModel(device.getDeviceModel());
        if (device.getStatus() != null) {
            response.setStatus(device.getStatus().name());
        }
        if (device.getRegisteredAt() != null) {
            response.setRegisteredAt(device.getRegisteredAt().toInstant().toString());
        }
        response.setMetadata(device.getMetadata());
        return response;
    }

    private APIError handleException(DeviceMgtException e, Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (e instanceof DeviceMgtClientException) {
            String backendErrorCode = e.getErrorCode();
            String errorCode;
            if (backendErrorCode == null) {
                errorCode = errorEnum.code();
            } else if (backendErrorCode.startsWith(Constants.DEVICE_MGT_ERROR_PREFIX)) {
                errorCode = backendErrorCode;
            } else {
                errorCode = Constants.DEVICE_MGT_ERROR_PREFIX + backendErrorCode;
            }
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorCode)
                    .withMessage(e.getMessage())
                    .withDescription(e.getDescription())
                    .build(LOG, e.getMessage());
            status = ErrorMessage.ERROR_DEVICE_NOT_FOUND.getCode().equals(e.getErrorCode())
                    ? Response.Status.NOT_FOUND
                    : Response.Status.BAD_REQUEST;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorEnum.code())
                    .withMessage(errorEnum.message())
                    .withDescription(errorEnum.description())
                    .build(LOG, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }
}

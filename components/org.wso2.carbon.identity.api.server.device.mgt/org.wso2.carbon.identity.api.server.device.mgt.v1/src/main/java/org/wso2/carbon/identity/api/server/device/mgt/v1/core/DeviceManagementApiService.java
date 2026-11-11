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
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.device.mgt.common.Constants;
import org.wso2.carbon.identity.api.server.device.mgt.v1.function.DeviceResponseBuilder;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceListLink;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceListResponse;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DevicePatchRequest;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceResponse;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.device.mgt.api.constant.ErrorMessage;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtClientException;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtException;
import org.wso2.carbon.identity.device.mgt.api.model.Device;
import org.wso2.carbon.identity.device.mgt.api.service.DeviceManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for Device Management API — handles listing, renaming, activating, deactivating and deleting
 * devices.
 */
public class DeviceManagementApiService {

    private static final Log LOG = LogFactory.getLog(DeviceManagementApiService.class);
    private static final int DEFAULT_LIMIT = 30;
    private static final int DEFAULT_OFFSET = 0;
    private static final int MAXIMUM_LIMIT = 100;
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

        validateApiEnabled();
        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        validatePaginationParameters(resolvedLimit, resolvedOffset);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            int totalResults;
            List<Device> devices;
            if (StringUtils.isNotBlank(userId)) {
                totalResults = deviceManagementService.getDeviceCountByUserId(userId, tenantDomain);
                devices = deviceManagementService.getDevicesByUserId(
                        userId, tenantDomain, resolvedOffset, resolvedLimit);
            } else {
                totalResults = deviceManagementService.getDeviceCount(tenantDomain);
                devices = deviceManagementService.getDevices(tenantDomain, resolvedOffset, resolvedLimit);
            }

            List<DeviceResponse> items = devices.stream()
                    .map(DeviceResponseBuilder::buildDeviceResponse)
                    .collect(Collectors.toList());

            List<DeviceListLink> links = buildPaginationLinks(resolvedLimit, resolvedOffset, totalResults, userId);

            return new DeviceListResponse()
                    .totalResults(totalResults)
                    .startIndex(resolvedOffset + 1)
                    .count(items.size())
                    .devices(items)
                    .links(links);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_DEVICES);
        }
    }

    /**
     * Rejects the request when the device management APIs are disabled via the
     * {@value Constants#DISABLE_DEVICE_MANAGEMENT_APIS} config. The APIs stay disabled unless the config is
     * explicitly set to false.
     */
    private void validateApiEnabled() {

        String disableApis = IdentityUtil.getProperty(Constants.DISABLE_DEVICE_MANAGEMENT_APIS);
        if (StringUtils.isBlank(disableApis) || Boolean.parseBoolean(disableApis)) {
            throw new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                    .withCode(Constants.ErrorMessage.ERROR_CODE_API_DISABLED.code())
                    .withMessage(Constants.ErrorMessage.ERROR_CODE_API_DISABLED.message())
                    .withDescription(Constants.ErrorMessage.ERROR_CODE_API_DISABLED.description())
                    .build(LOG, "Device management API is disabled."));
        }
    }

    private void validatePaginationParameters(int limit, int offset) {

        if (limit < 1 || limit > MAXIMUM_LIMIT || offset < 0) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.code())
                    .withMessage(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.message())
                    .withDescription(Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION.description())
                    .build(LOG, "Invalid pagination parameters."));
        }
    }

    /**
     * Builds 'next' and 'previous' pagination links for the device list, carrying the {@code userId}
     * filter (when present) on every emitted link so that following a link does not widen the result set.
     *
     * @param limit        Value of the 'limit' parameter.
     * @param offset       Value of the 'offset' parameter.
     * @param totalResults Total number of matching devices.
     * @param userId       ID of the user devices are filtered by, or {@code null}/blank for no filtering.
     * @return List of pagination links.
     */
    private List<DeviceListLink> buildPaginationLinks(int limit, int offset, int totalResults, String userId) {

        List<DeviceListLink> links = new ArrayList<>();
        String baseUrl = Constants.V1_API_PATH_COMPONENT + Constants.DEVICE_PATH_COMPONENT;

        // Next link. Widened to long so that a large 'offset' cannot overflow into a false positive.
        if (((long) offset + limit) < totalResults) {
            links.add(buildPageLink(baseUrl, Constants.PAGE_LINK_REL_NEXT, offset + limit, limit, userId));
        }

        /*
        Previous link.
        Previous link matters only if offset is greater than 0.
        */
        if (offset > 0) {
            if ((offset - limit) >= 0) { // A previous page of size 'limit' exists.
                links.add(buildPageLink(baseUrl, Constants.PAGE_LINK_REL_PREVIOUS,
                        calculateOffsetForPreviousLink(offset, limit, totalResults), limit, userId));
            } else { // A previous page exists but it's size is less than the specified limit.
                links.add(buildPageLink(baseUrl, Constants.PAGE_LINK_REL_PREVIOUS, 0, offset, userId));
            }
        }

        return links;
    }

    private DeviceListLink buildPageLink(String baseUrl, String rel, int offset, int limit, String userId) {

        String query = StringUtils.isNotBlank(userId)
                ? String.format(Constants.PAGINATION_WITH_USER_ID_LINK_FORMAT, offset, limit, userId)
                : String.format(Constants.PAGINATION_LINK_FORMAT, offset, limit);
        return new DeviceListLink().rel(rel).href(ContextLoader.buildURIForBody(baseUrl + query).toString());
    }

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = offset - limit;
        if (newOffset < total) {
            return newOffset;
        }

        /*
        The offset points past the end of the result set, so step back by chunks of 'limit' until a
        page that overlaps the results is found. The number of chunks is computed directly, since
        stepping one page at a time is unbounded for a large 'offset'. The result is floored at 0
        because a negative offset is not a valid page start.
        */
        long chunks = ((long) newOffset - total) / limit + 1;
        return (int) Math.max(newOffset - chunks * limit, 0);
    }

    /**
     * Returns a single device by ID.
     *
     * @param deviceId Device UUID.
     * @return DeviceResponse.
     */
    public DeviceResponse getDevice(String deviceId) {

        validateApiEnabled();
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Device device = deviceManagementService.getDeviceById(deviceId, tenantDomain);
            if (device == null) {
                throw buildDeviceNotFoundError(deviceId);
            }
            return DeviceResponseBuilder.buildDeviceResponse(device);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEVICE);
        }
    }

    private APIError buildDeviceNotFoundError(String deviceId) {

        return new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                .withCode(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.code())
                .withMessage(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.message())
                .withDescription(String.format(
                        Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.description(), deviceId))
                .build(LOG, "Device not found for id: " + deviceId));
    }

    /**
     * Updates a device. Renames the device when a device name is given and activates or deactivates it when a
     * status is given. Both can be applied in the same request.
     *
     * @param deviceId     Device UUID.
     * @param patchRequest Fields to update.
     * @return DeviceResponse reflecting the updated device.
     */
    public DeviceResponse updateDevice(String deviceId, DevicePatchRequest patchRequest) {

        validateApiEnabled();
        validatePatchRequest(patchRequest);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Device device = deviceManagementService.getDeviceById(deviceId, tenantDomain);
            if (device == null) {
                throw buildDeviceNotFoundError(deviceId);
            }

            if (patchRequest.getDeviceName() != null) {
                device = deviceManagementService.updateDeviceName(
                        deviceId, patchRequest.getDeviceName(), tenantDomain);
            }
            if (patchRequest.getStatus() != null) {
                device = updateDeviceStatus(device, patchRequest.getStatus(), tenantDomain);
            }
            return DeviceResponseBuilder.buildDeviceResponse(device);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEVICE);
        }
    }

    /**
     * Activates or deactivates the device. Nothing is changed when the device already has the requested status.
     *
     * @param device       Device to update.
     * @param status       Requested status.
     * @param tenantDomain Tenant domain.
     * @return Device reflecting the requested status.
     * @throws DeviceMgtException If the status update fails.
     */
    private Device updateDeviceStatus(Device device, DevicePatchRequest.StatusEnum status, String tenantDomain)
            throws DeviceMgtException {

        Device.Status requestedStatus = Device.Status.valueOf(status.value());
        if (requestedStatus == device.getStatus()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("The device: " + device.getId() + " is already " + requestedStatus
                        + ". Skipping the status update.");
            }
            return device;
        }
        if (requestedStatus == Device.Status.ACTIVE) {
            return deviceManagementService.activateDevice(device.getId(), tenantDomain);
        }
        return deviceManagementService.deactivateDevice(device.getId(), tenantDomain);
    }

    private void validatePatchRequest(DevicePatchRequest patchRequest) {

        if (patchRequest == null || (patchRequest.getDeviceName() == null && patchRequest.getStatus() == null)) {
            throw buildBadRequestError(Constants.ErrorMessage.ERROR_CODE_INVALID_PATCH_REQUEST);
        }
        String deviceName = patchRequest.getDeviceName();
        if (deviceName != null && (StringUtils.isBlank(deviceName) || deviceName.length() > DEVICE_NAME_MAX_LENGTH)) {
            throw buildBadRequestError(Constants.ErrorMessage.ERROR_CODE_INVALID_DEVICE_NAME);
        }
    }

    private APIError buildBadRequestError(Constants.ErrorMessage error) {

        return new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                .withCode(error.code())
                .withMessage(error.message())
                .withDescription(error.description())
                .build(LOG, error.message()));
    }

    /**
     * Deletes a device by ID.
     *
     * @param deviceId Device UUID.
     */
    public void deleteDevice(String deviceId) {

        validateApiEnabled();
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            deviceManagementService.deleteDevice(deviceId, tenantDomain);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_DEVICE);
        }
    }

    private APIError handleException(DeviceMgtException e, Constants.ErrorMessage errorEnum) {

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

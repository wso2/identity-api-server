/*
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com) All Rights Reserved.
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.configs.v1.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.v1.model.CompatibilitySettings;
import org.wso2.carbon.identity.api.server.configs.v1.model.CompatibilitySettingsGroup;
import org.wso2.carbon.identity.compatibility.settings.core.exception.CompatibilitySettingClientException;
import org.wso2.carbon.identity.compatibility.settings.core.exception.CompatibilitySettingServerException;
import org.wso2.carbon.identity.compatibility.settings.core.model.CompatibilitySetting;
import org.wso2.carbon.identity.compatibility.settings.core.model.CompatibilitySettingGroup;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Utility class for compatibility settings related functions.
 */
public class CompatibilitySettingUtil {

    private static final Log LOG = LogFactory.getLog(CompatibilitySettingUtil.class);

    /**
     * Convert CompatibilitySetting to CompatibilitySettings API model.
     *
     * @param setting CompatibilitySetting from service layer.
     * @return CompatibilitySettings API model.
     */
    public static CompatibilitySettings toCompatibilitySettings(CompatibilitySetting setting) {

        CompatibilitySettings compatibilitySettings = new CompatibilitySettings();
        if (setting == null || setting.getCompatibilitySettings() == null) {
            return compatibilitySettings;
        }

        Map<String, Map<String, Object>> settingsMap = new HashMap<>();
        for (Map.Entry<String, CompatibilitySettingGroup> entry : setting.getCompatibilitySettings().entrySet()) {
            CompatibilitySettingGroup group = entry.getValue();
            if (group.getSettingGroup() != null) {
                Map<String, Object> groupSettings = new HashMap<>();
                if (group.getSettings() != null) {
                    groupSettings.putAll(group.getSettings());
                }
                settingsMap.put(group.getSettingGroup(), groupSettings);
            }
        }
        compatibilitySettings.setAllSettings(settingsMap);
        return compatibilitySettings;
    }

    /**
     * Convert CompatibilitySetting for a specific setting group to CompatibilitySettingsGroup API model.
     *
     * @param setting CompatibilitySetting from service layer.
     * @param settingGroupName Setting group name to extract settings for.
     * @return CompatibilitySettingsGroup API model.
     */
    public static CompatibilitySettingsGroup toCompatibilitySettingsGroup(CompatibilitySetting setting,
                                                                           String settingGroupName) {

        CompatibilitySettingsGroup settingsGroup = new CompatibilitySettingsGroup();
        if (setting == null) {
            return settingsGroup;
        }

        CompatibilitySettingGroup group = setting.getCompatibilitySetting(settingGroupName);
        if (group != null && group.getSettings() != null) {
            Map<String, Object> settings = new HashMap<>(group.getSettings());
            settingsGroup.setAllSettings(settings);
        }

        return settingsGroup;
    }

    /**
     * Convert CompatibilitySettings API model to CompatibilitySetting (backend model).
     *
     * @param compatibilitySettings CompatibilitySettings API model.
     * @return CompatibilitySetting for service layer.
     */
    public static CompatibilitySetting toBackendModel(CompatibilitySettings compatibilitySettings) {

        CompatibilitySetting setting = new CompatibilitySetting();
        if (compatibilitySettings == null || compatibilitySettings.getAdditionalProperties() == null) {
            return setting;
        }

        for (Map.Entry<String, Map<String, Object>> entry :
                compatibilitySettings.getAdditionalProperties().entrySet()) {
            CompatibilitySettingGroup group = new CompatibilitySettingGroup();
            group.setSettingGroup(entry.getKey());

            if (entry.getValue() != null) {
                Map<String, String> stringSettings = new HashMap<>();
                for (Map.Entry<String, Object> settingEntry : entry.getValue().entrySet()) {
                    stringSettings.put(settingEntry.getKey(),
                            settingEntry.getValue() != null ? String.valueOf(settingEntry.getValue()) : null);
                }
                group.addSettings(stringSettings);
            }
            setting.addCompatibilitySetting(group);
        }
        return setting;
    }

    /**
     * Validate setting group name.
     *
     * @param settingGroup Setting group name.
     * @return True if valid, false otherwise.
     */
    public static boolean isValidSettingGroupName(String settingGroup) {

        return StringUtils.isNotBlank(settingGroup) && settingGroup.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Check if CompatibilitySetting has settings for the specified setting group.
     *
     * @param setting CompatibilitySetting to check.
     * @param settingGroupName Setting group name to look for.
     * @return True if setting group exists, false otherwise.
     */
    public static boolean hasSettingGroup(CompatibilitySetting setting, String settingGroupName) {

        if (setting == null) {
            return false;
        }
        return setting.getCompatibilitySetting(settingGroupName) != null;
    }

    /**
     * Handle Compatibility Settings exceptions and return corresponding APIError.
     *
     * @param e         Exception.
     * @param errorEnum Error Message enum.
     * @param data      Extra data.
     * @return APIError.
     */
    public static APIError handleCompatibilitySettingsException(Exception e, Constants.ErrorMessage errorEnum,
                                                                 String data) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (e instanceof CompatibilitySettingClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e.getMessage());
            if (((CompatibilitySettingClientException) e).getErrorCode() != null) {
                String errorCode = ((CompatibilitySettingClientException) e).getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                        errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof CompatibilitySettingServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, errorEnum.description());
            if (((CompatibilitySettingServerException) e).getErrorCode() != null) {
                String errorCode = ((CompatibilitySettingServerException) e).getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                        errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else if (e instanceof IllegalArgumentException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new APIError(status, errorResponse);
    }

    /**
     * Return error builder with given error message enum and data.
     *
     * @param errorEnum Error Message enum.
     * @param data      Error data.
     * @return ErrorResponse.Builder.
     */
    private static ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorEnum, String data) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.code())
                .withMessage(errorEnum.message())
                .withDescription(includeData(errorEnum, data));
    }

    /**
     * Include data in the error description.
     *
     * @param error Error message.
     * @param data  Data.
     * @return Error description with data.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.description(), data);
        } else {
            message = error.description();
        }
        return message;
    }
}

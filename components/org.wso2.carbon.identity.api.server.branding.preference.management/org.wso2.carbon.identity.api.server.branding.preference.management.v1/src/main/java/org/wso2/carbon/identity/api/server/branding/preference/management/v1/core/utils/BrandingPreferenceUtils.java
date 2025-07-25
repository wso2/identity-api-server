/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Util class for branding preference management.
 */
public class BrandingPreferenceUtils {

    private static final Log log = LogFactory.getLog(BrandingPreferenceUtils.class);

    /**
     * Check whether the given string is a valid JSON or not.
     *
     * @param stringJSON Input String.
     * @return True if the input string is a valid JSON.
     */
    public static boolean isValidJSONString(String stringJSON) {

        if (StringUtils.isBlank(stringJSON)) {
            if (log.isDebugEnabled()) {
                log.debug("JSON string validation failed: Input string is blank or null");
            }
            return false;
        }
        try {
            JSONObject objectJSON = new JSONObject(stringJSON);
            if (objectJSON.length() == 0) {
                if (log.isDebugEnabled()) {
                    log.debug("JSON string validation failed: JSON object is empty");
                }
                return false;
            }
        } catch (JSONException exception) {
            if (log.isDebugEnabled()) {
                log.debug("JSON string validation failed: Invalid JSON format", exception);
            }
            return false;
        }
        return true;
    }
}

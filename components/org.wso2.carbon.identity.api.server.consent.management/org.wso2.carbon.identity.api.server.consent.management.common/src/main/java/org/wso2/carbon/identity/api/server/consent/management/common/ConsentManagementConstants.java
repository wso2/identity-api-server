/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.consent.management.common;

/**
 * Constants for Consent API server.
 */
public class ConsentManagementConstants {

    private ConsentManagementConstants() {

    }

    // API Path Components
    public static final String V2_API_PATH_COMPONENT = "/consent-mgt/v2.0";

    // Resource Paths
    public static final String CONSENTS_PATH = "/consents";
    public static final String PURPOSES_PATH = "/purposes";
    public static final String ELEMENTS_PATH = "/elements";
    public static final String VERSIONS_PATH = "/versions";

    // HTTP Status Messages
    public static final String STATUS_NOT_FOUND_MESSAGE_DEFAULT = "Not Found";
    public static final String STATUS_CONFLICT_MESSAGE_DEFAULT = "Conflict";
    public static final String STATUS_UNAUTHORIZED_MESSAGE_DEFAULT = "Unauthorized";
    public static final String STATUS_FORBIDDEN_MESSAGE_DEFAULT = "Forbidden";
}

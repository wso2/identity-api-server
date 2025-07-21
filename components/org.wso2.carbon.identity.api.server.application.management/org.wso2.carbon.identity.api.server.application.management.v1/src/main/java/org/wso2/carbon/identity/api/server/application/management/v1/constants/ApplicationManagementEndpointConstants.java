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

package org.wso2.carbon.identity.api.server.application.management.v1.constants;

/**
 * Constants for Application Management Endpoint.
 */
public class ApplicationManagementEndpointConstants {

    public static final String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String FILTER_PARAM = "filter";
    public static final String LIMIT_PARAM = "limit";
    public static final String RECURSIVE_PARAM = "recursive";
    public static final String EXCLUDED_ATTRIBUTES_PARAM = "excludedAttributes";
    public static final String INCLUDED_ATTRIBUTES_PARAM = "attributes";

    public static final String ASYNC_OPERATION_RESPONSE_STATUS = "Processing";

    public static final String ROLE_AUDIENCE_KEY = "audience";
    public static final String ROLE_DISPLAY_NAME_KEY = "displayName";
    public static final String ROLE_AUDIENCE_DISPLAY_KEY = "display";
    public static final String ROLE_AUDIENCE_TYPE_KEY = "type";

    public static final String APPLICATION_SHARE_TRIGGER_SUCCESS =
            "Application sharing process triggered successfully.";
    public static final String APPLICATION_SHARE_UPDATE_TRIGGER_SUCCESS
            = "Application sharing update process triggered successfully.";
    public static final String APPLICATION_UNSHARE_TRIGGER_SUCCESS
            = "Application unsharing process triggered successfully.";
}

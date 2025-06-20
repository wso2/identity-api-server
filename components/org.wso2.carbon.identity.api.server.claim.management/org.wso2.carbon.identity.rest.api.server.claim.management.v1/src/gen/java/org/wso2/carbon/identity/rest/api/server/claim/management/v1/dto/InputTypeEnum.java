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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the input types for user attributes in a UI form.
 */
public enum InputTypeEnum {

    DROPDOWN("dropdown"),
    RADIO_GROUP("radio_group"),

    MULTI_SELECT_DROPDOWN("multi_select_dropdown"),
    CHECKBOX_GROUP("checkbox_group"),

    TEXT_INPUT("text_input"),
    TEXTAREA("textarea"),

    DATE_PICKER("date_picker"),
    NUMBER_INPUT("number_input"),

    CHECKBOX("checkbox"),
    TOGGLE("toggle");

    private final String value;

    InputTypeEnum(String value) {

        this.value = value;
    }

    public String getInputTypeName() {

        return value;
    }

    @JsonValue
    public String getValue() {

        return value;
    }
}

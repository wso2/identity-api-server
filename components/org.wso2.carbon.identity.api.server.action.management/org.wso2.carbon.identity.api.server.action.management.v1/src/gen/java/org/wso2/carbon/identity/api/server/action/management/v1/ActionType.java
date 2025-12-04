/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1;

import javax.validation.constraints.*;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlType(name="")
@XmlEnum(String.class)
public enum ActionType {

    @XmlEnumValue("PRE_ISSUE_ACCESS_TOKEN") PRE_ISSUE_ACCESS_TOKEN(String.valueOf("PRE_ISSUE_ACCESS_TOKEN")), @XmlEnumValue("PRE_UPDATE_PASSWORD") PRE_UPDATE_PASSWORD(String.valueOf("PRE_UPDATE_PASSWORD")), @XmlEnumValue("PRE_UPDATE_PROFILE") PRE_UPDATE_PROFILE(String.valueOf("PRE_UPDATE_PROFILE")), @XmlEnumValue("PRE_REGISTRATION") PRE_REGISTRATION(String.valueOf("PRE_REGISTRATION")), @XmlEnumValue("PRE_ISSUE_ID_TOKEN") PRE_ISSUE_ID_TOKEN(String.valueOf("PRE_ISSUE_ID_TOKEN"));


    private String value;

    ActionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ActionType fromValue(String value) {
        for (ActionType b : ActionType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}




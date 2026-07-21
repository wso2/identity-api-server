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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;

/**
 * Channel used to deliver a device registration notification. - &#x60;EMAIL&#x60;: Deliver the notification over email. - &#x60;PUSH_NOTIFICATION&#x60;: Deliver the notification to the user&#39;s existing devices. 
 **/
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlType(name="")
@XmlEnum(String.class)
public enum DeviceRegistrationNotificationChannel {

    @XmlEnumValue("EMAIL") EMAIL(String.valueOf("EMAIL")), @XmlEnumValue("PUSH_NOTIFICATION") PUSH_NOTIFICATION(String.valueOf("PUSH_NOTIFICATION"));


    private String value;

    DeviceRegistrationNotificationChannel(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static DeviceRegistrationNotificationChannel fromValue(String value) {
        for (DeviceRegistrationNotificationChannel b : DeviceRegistrationNotificationChannel.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}




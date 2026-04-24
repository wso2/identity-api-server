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

package org.wso2.carbon.identity.api.server.application.management.v1;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;

/**
 * FAPI security profile. - &#x60;FAPI1_ADVANCED&#x60;: FAPI 1.0 Advanced Security Profile. - &#x60;FAPI2_SECURITY&#x60;: FAPI 2.0 Security Profile. 
 **/
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlType(name="")
@XmlEnum(String.class)
public enum FapiProfile {

    @XmlEnumValue("FAPI1_ADVANCED") FAPI1_ADVANCED(String.valueOf("FAPI1_ADVANCED")), @XmlEnumValue("FAPI2_SECURITY") FAPI2_SECURITY(String.valueOf("FAPI2_SECURITY"));


    private String value;

    FapiProfile(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static FapiProfile fromValue(String value) {
        for (FapiProfile b : FapiProfile.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}




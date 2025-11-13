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

package org.wso2.carbon.identity.api.server.notification.sender.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;

/**
 * The type of authentication required by the action&#39;s endpoint. The following options are supported:   - BASIC: Basic authentication with a username and password.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;CLIENT_CREDENTIAL\&quot;,     \&quot;properties\&quot;: {       \&quot;username\&quot;: \&quot;auth_username\&quot;,       \&quot;password\&quot;: \&quot;auth_password\&quot;     }   }&#x60;&#x60;   - API_CREDENTIAL: API key secret based authentication.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;API_CREDENTIAL\&quot;,     \&quot;properties\&quot;: {       \&quot;key\&quot;: \&quot;123ert45\&quot;,       \&quot;secret\&quot;: \&quot;12345-abcde-67890\&quot;     }   }&#x60;&#x60; 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The type of authentication required by the action's endpoint. The following options are supported:   - BASIC: Basic authentication with a username and password.<br>   ``{     \"type\": \"CLIENT_CREDENTIAL\",     \"properties\": {       \"username\": \"auth_username\",       \"password\": \"auth_password\"     }   }``   - API_CREDENTIAL: API key secret based authentication.<br>   ``{     \"type\": \"API_CREDENTIAL\",     \"properties\": {       \"key\": \"123ert45\",       \"secret\": \"12345-abcde-67890\"     }   }`` ")
public class Authentication  {
  

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("CLIENT_CREDENTIAL") CLIENT_CREDENTIAL(String.valueOf("CLIENT_CREDENTIAL")), @XmlEnumValue("BASIC") BASIC(String.valueOf("BASIC")), @XmlEnumValue("API_KEY") API_KEY(String.valueOf("API_KEY")), @XmlEnumValue("BEARER") BEARER(String.valueOf("BEARER"));


    private String value;

    TypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
        for (TypeEnum b : TypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private TypeEnum type;
    private Map<String, Object> properties = null;


    /**
    **/
    public Authentication type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "CLIENT_CREDENTIAL", required = true, value = "")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    * Authentication properties specific to the selected type.
    **/
    public Authentication properties(Map<String, Object> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Authentication properties specific to the selected type.")
    @JsonProperty("properties")
    @Valid
    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }


    public Authentication putPropertiesItem(String key, Object propertiesItem) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, propertiesItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authentication authentication = (Authentication) o;
        return Objects.equals(this.type, authentication.type) &&
            Objects.equals(this.properties, authentication.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Authentication {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}


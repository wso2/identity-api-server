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
 * The type of authentication required by the action&#39;s endpoint. The following options are supported:   - CLIENT_CREDENTIAL: OAuth 2.0 client credentials authentication.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;CLIENT_CREDENTIAL\&quot;,     \&quot;properties\&quot;: {       \&quot;clientId\&quot;: \&quot;3e172dd2-901b-43a9-a26a-728466795f01\&quot;,       \&quot;clientSecret\&quot;: \&quot;83cdc120-ccf6-4163-a4a8-c1ba3e872daa\&quot;,       \&quot;tokenEndpoint\&quot;: \&quot;https://custom.idp.com/todos\&quot;,       \&quot;scopes\&quot;: \&quot;send_scope\&quot;     }   }&#x60;&#x60;  - BASIC: Username and password based authentication.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;BASIC\&quot;,     \&quot;properties\&quot;: {       \&quot;username\&quot;: \&quot;admin\&quot;,       \&quot;password\&quot;: \&quot;admin123\&quot;     }   }&#x60;&#x60; - BEARER: Token based based authentication.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;BEARER\&quot;,     \&quot;properties\&quot;: {       \&quot;token\&quot;: \&quot;12345-abcde-67890\&quot;     }   }&#x60;&#x60; - API_KEY: API key secret based authentication.&lt;br&gt;   &#x60;&#x60;{     \&quot;type\&quot;: \&quot;API_KEY\&quot;,     \&quot;properties\&quot;: {       \&quot;header\&quot;: \&quot;auth\&quot;,       \&quot;value\&quot;: \&quot;123ert45\&quot;     }   }&#x60;&#x60; 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The type of authentication required by the action's endpoint. The following options are supported:   - CLIENT_CREDENTIAL: OAuth 2.0 client credentials authentication.<br>   ``{     \"type\": \"CLIENT_CREDENTIAL\",     \"properties\": {       \"clientId\": \"3e172dd2-901b-43a9-a26a-728466795f01\",       \"clientSecret\": \"83cdc120-ccf6-4163-a4a8-c1ba3e872daa\",       \"tokenEndpoint\": \"https://custom.idp.com/todos\",       \"scopes\": \"send_scope\"     }   }``  - BASIC: Username and password based authentication.<br>   ``{     \"type\": \"BASIC\",     \"properties\": {       \"username\": \"admin\",       \"password\": \"admin123\"     }   }`` - BEARER: Token based based authentication.<br>   ``{     \"type\": \"BEARER\",     \"properties\": {       \"token\": \"12345-abcde-67890\"     }   }`` - API_KEY: API key secret based authentication.<br>   ``{     \"type\": \"API_KEY\",     \"properties\": {       \"header\": \"auth\",       \"value\": \"123ert45\"     }   }`` ")
public class Authentication  {
  

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("CLIENT_CREDENTIAL") CLIENT_CREDENTIAL(String.valueOf("CLIENT_CREDENTIAL")), @XmlEnumValue("BASIC") BASIC(String.valueOf("BASIC")), @XmlEnumValue("API_KEY") API_KEY(String.valueOf("API_KEY")), @XmlEnumValue("BEARER") BEARER(String.valueOf("BEARER")), @XmlEnumValue("NONE") NONE(String.valueOf("NONE"));


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


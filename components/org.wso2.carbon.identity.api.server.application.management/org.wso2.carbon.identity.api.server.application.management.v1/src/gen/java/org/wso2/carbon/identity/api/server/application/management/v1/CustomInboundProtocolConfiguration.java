/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CustomInboundProtocolConfiguration  {
  
    private String name;
    private String inboundKey;
    private List<PropertyModel> properties = null;


    /**
    **/
    public CustomInboundProtocolConfiguration name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "custom-wso2-inbound", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public CustomInboundProtocolConfiguration inboundKey(String inboundKey) {

        this.inboundKey = inboundKey;
        return this;
    }
    
    @ApiModelProperty(example = "custom-wso2-inbound-id", required = true, value = "")
    @JsonProperty("inboundKey")
    @Valid
    @NotNull(message = "Property inboundKey cannot be null.")

    public String getInboundKey() {
        return inboundKey;
    }
    public void setInboundKey(String inboundKey) {
        this.inboundKey = inboundKey;
    }

    /**
    **/
    public CustomInboundProtocolConfiguration properties(List<PropertyModel> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<PropertyModel> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyModel> properties) {
        this.properties = properties;
    }

    public CustomInboundProtocolConfiguration addPropertiesItem(PropertyModel propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
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
        CustomInboundProtocolConfiguration customInboundProtocolConfiguration = (CustomInboundProtocolConfiguration) o;
        return Objects.equals(this.name, customInboundProtocolConfiguration.name) &&
            Objects.equals(this.inboundKey, customInboundProtocolConfiguration.inboundKey) &&
            Objects.equals(this.properties, customInboundProtocolConfiguration.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inboundKey, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CustomInboundProtocolConfiguration {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    inboundKey: ").append(toIndentedString(inboundKey)).append("\n");
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


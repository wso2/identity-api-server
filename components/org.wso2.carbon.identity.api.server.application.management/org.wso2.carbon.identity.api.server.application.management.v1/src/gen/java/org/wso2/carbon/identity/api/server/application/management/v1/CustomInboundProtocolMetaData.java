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
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CustomInboundProtocolMetaData  {
  
    private String displayName;
    private String configName;
    private List<CustomInboundProtocolProperty> properties = null;


    /**
    **/
    public CustomInboundProtocolMetaData displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "My Custom Protocol", value = "")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public CustomInboundProtocolMetaData configName(String configName) {

        this.configName = configName;
        return this;
    }
    
    @ApiModelProperty(example = "Custom Protocol", value = "")
    @JsonProperty("configName")
    @Valid
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
    **/
    public CustomInboundProtocolMetaData properties(List<CustomInboundProtocolProperty> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<CustomInboundProtocolProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<CustomInboundProtocolProperty> properties) {
        this.properties = properties;
    }

    public CustomInboundProtocolMetaData addPropertiesItem(CustomInboundProtocolProperty propertiesItem) {
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
        CustomInboundProtocolMetaData customInboundProtocolMetaData = (CustomInboundProtocolMetaData) o;
        return Objects.equals(this.displayName, customInboundProtocolMetaData.displayName) &&
            Objects.equals(this.configName, customInboundProtocolMetaData.configName) &&
            Objects.equals(this.properties, customInboundProtocolMetaData.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, configName, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CustomInboundProtocolMetaData {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    configName: ").append(toIndentedString(configName)).append("\n");
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


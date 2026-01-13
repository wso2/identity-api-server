/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class EventProperty  {
  
    private String propertyKey;
    private String propertyValue;

    /**
    * key of the event property
    **/
    public EventProperty propertyKey(String propertyKey) {

        this.propertyKey = propertyKey;
        return this;
    }
    
    @ApiModelProperty(example = "propertyKey1", value = "key of the event property")
    @JsonProperty("propertyKey")
    @Valid
    public String getPropertyKey() {
        return propertyKey;
    }
    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
    * value of the event property
    **/
    public EventProperty propertyValue(String propertyValue) {

        this.propertyValue = propertyValue;
        return this;
    }
    
    @ApiModelProperty(example = "propertyValue1", value = "value of the event property")
    @JsonProperty("propertyValue")
    @Valid
    public String getPropertyValue() {
        return propertyValue;
    }
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventProperty eventProperty = (EventProperty) o;
        return Objects.equals(this.propertyKey, eventProperty.propertyKey) &&
            Objects.equals(this.propertyValue, eventProperty.propertyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyKey, propertyValue);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EventProperty {\n");
        
        sb.append("    propertyKey: ").append(toIndentedString(propertyKey)).append("\n");
        sb.append("    propertyValue: ").append(toIndentedString(propertyValue)).append("\n");
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


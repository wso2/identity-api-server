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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * In-flow extension connection info for flow metadata
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "In-flow extension connection info for flow metadata")
public class FlowExtensionConnectionInfo  {
  
    private String actionId;
    private String name;
    private String iconUrl;

    /**
    * The unique identifier of the flow extension action.
    **/
    public FlowExtensionConnectionInfo actionId(String actionId) {

        this.actionId = actionId;
        return this;
    }
    
    @ApiModelProperty(value = "The unique identifier of the flow extension action.")
    @JsonProperty("actionId")
    @Valid
    public String getActionId() {
        return actionId;
    }
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
    * The name of the flow extension.
    **/
    public FlowExtensionConnectionInfo name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "The name of the flow extension.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * The URL of the icon associated with the flow extension.
    **/
    public FlowExtensionConnectionInfo iconUrl(String iconUrl) {

        this.iconUrl = iconUrl;
        return this;
    }
    
    @ApiModelProperty(value = "The URL of the icon associated with the flow extension.")
    @JsonProperty("iconUrl")
    @Valid
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowExtensionConnectionInfo flowExtensionConnectionInfo = (FlowExtensionConnectionInfo) o;
        return Objects.equals(this.actionId, flowExtensionConnectionInfo.actionId) &&
            Objects.equals(this.name, flowExtensionConnectionInfo.name) &&
            Objects.equals(this.iconUrl, flowExtensionConnectionInfo.iconUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionId, name, iconUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowExtensionConnectionInfo {\n");
        
        sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
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


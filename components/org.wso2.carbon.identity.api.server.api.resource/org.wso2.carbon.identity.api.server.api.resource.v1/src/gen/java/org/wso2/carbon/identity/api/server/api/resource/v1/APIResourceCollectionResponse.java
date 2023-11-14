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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCollectionResponseApiResources;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class APIResourceCollectionResponse  {
  
    private String id;
    private String name;
    private String displayName;
    private String type;
    private APIResourceMap apiResources;

    /**
    **/
    public APIResourceCollectionResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "gh43-jk34-vb34-df67", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public APIResourceCollectionResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "bulkUserImport", required = true, value = "")
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
    public APIResourceCollectionResponse displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Bulk User Import", required = true, value = "")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public APIResourceCollectionResponse type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "SYSTEM", value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public APIResourceCollectionResponse apiResources(APIResourceMap apiResources) {

        this.apiResources = apiResources;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("apiResources")
    @Valid
    public APIResourceMap getApiResources() {
        return apiResources;
    }
    public void setApiResources(APIResourceMap apiResources) {
        this.apiResources = apiResources;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        APIResourceCollectionResponse apIResourceCollectionResponse = (APIResourceCollectionResponse) o;
        return Objects.equals(this.id, apIResourceCollectionResponse.id) &&
            Objects.equals(this.name, apIResourceCollectionResponse.name) &&
            Objects.equals(this.displayName, apIResourceCollectionResponse.displayName) &&
            Objects.equals(this.type, apIResourceCollectionResponse.type) &&
            Objects.equals(this.apiResources, apIResourceCollectionResponse.apiResources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, type, apiResources);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class APIResourceCollectionResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    apiResources: ").append(toIndentedString(apiResources)).append("\n");
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


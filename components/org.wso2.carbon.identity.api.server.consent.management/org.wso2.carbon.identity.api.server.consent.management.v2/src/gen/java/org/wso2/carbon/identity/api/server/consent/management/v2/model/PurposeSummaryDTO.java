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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTOLatestVersion;
import javax.validation.constraints.*;

/**
 * Minimal purpose information for list responses
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Minimal purpose information for list responses")
public class PurposeSummaryDTO  {
  
    private String id;
    private String name;
    private String description;
    private String type;
    private PurposeDTOLatestVersion latestVersion;

    /**
    **/
    public PurposeSummaryDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public PurposeSummaryDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Privacy Policy", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public PurposeSummaryDTO description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Collection of user data for privacy policy compliance and consent management", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public PurposeSummaryDTO type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "Policy", value = "")
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
    public PurposeSummaryDTO latestVersion(PurposeDTOLatestVersion latestVersion) {

        this.latestVersion = latestVersion;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("latestVersion")
    @Valid
    public PurposeDTOLatestVersion getLatestVersion() {
        return latestVersion;
    }
    public void setLatestVersion(PurposeDTOLatestVersion latestVersion) {
        this.latestVersion = latestVersion;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurposeSummaryDTO purposeSummaryDTO = (PurposeSummaryDTO) o;
        return Objects.equals(this.id, purposeSummaryDTO.id) &&
            Objects.equals(this.name, purposeSummaryDTO.name) &&
            Objects.equals(this.description, purposeSummaryDTO.description) &&
            Objects.equals(this.type, purposeSummaryDTO.type) &&
            Objects.equals(this.latestVersion, purposeSummaryDTO.latestVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, latestVersion);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeSummaryDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    latestVersion: ").append(toIndentedString(latestVersion)).append("\n");
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


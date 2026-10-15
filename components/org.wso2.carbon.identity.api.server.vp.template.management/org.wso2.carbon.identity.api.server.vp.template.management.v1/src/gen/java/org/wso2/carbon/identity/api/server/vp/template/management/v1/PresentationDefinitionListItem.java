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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Summary representation of a presentation definition returned in list responses.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Summary representation of a presentation definition returned in list responses.")
public class PresentationDefinitionListItem  {
  
    private String id;
    private String identifier;
    private String displayName;
    private String description;

    /**
    * Server-assigned UUID of the presentation definition.
    **/
    public PresentationDefinitionListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "a9b3c21d-84f0-4e1b-b9e7-123456789abc", value = "Server-assigned UUID of the presentation definition.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * User-facing identifier of the presentation definition.
    **/
    public PresentationDefinitionListItem identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "employee-id-verification", value = "User-facing identifier of the presentation definition.")
    @JsonProperty("identifier")
    @Valid
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    * Display label of the presentation definition.
    **/
    public PresentationDefinitionListItem displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Employee ID Verification", value = "Display label of the presentation definition.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Description of the presentation definition.
    **/
    public PresentationDefinitionListItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Requests an employee ID credential from the wallet.", value = "Description of the presentation definition.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PresentationDefinitionListItem presentationDefinitionListItem = (PresentationDefinitionListItem) o;
        return Objects.equals(this.id, presentationDefinitionListItem.id) &&
            Objects.equals(this.identifier, presentationDefinitionListItem.identifier) &&
            Objects.equals(this.displayName, presentationDefinitionListItem.displayName) &&
            Objects.equals(this.description, presentationDefinitionListItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, displayName, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PresentationDefinitionListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.RequestedCredentialModel;
import javax.validation.constraints.*;

/**
 * Request body for creating a new presentation definition.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for creating a new presentation definition.")
public class PresentationDefinitionCreationModel  {
  
    private String identifier;
    private String displayName;
    private String description;
    private List<RequestedCredentialModel> credentials = new ArrayList<>();


    /**
    * Unique user-facing identifier for this definition within the tenant. Used in API paths. Only alphanumeric characters, underscores, and hyphens are allowed. 
    **/
    public PresentationDefinitionCreationModel identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "employee-id-verification", required = true, value = "Unique user-facing identifier for this definition within the tenant. Used in API paths. Only alphanumeric characters, underscores, and hyphens are allowed. ")
    @JsonProperty("identifier")
    @Valid
    @NotNull(message = "Property identifier cannot be null.")
 @Pattern(regexp="^[A-Za-z0-9_-]+$")
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    * Human-readable display label for the presentation definition.
    **/
    public PresentationDefinitionCreationModel displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Employee ID Verification", required = true, value = "Human-readable display label for the presentation definition.")
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
    * Human-readable description of the presentation definition.
    **/
    public PresentationDefinitionCreationModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Requests an employee ID credential from the wallet.", value = "Human-readable description of the presentation definition.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * The requested credentials that constitute this presentation definition.
    **/
    public PresentationDefinitionCreationModel credentials(List<RequestedCredentialModel> credentials) {

        this.credentials = credentials;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The requested credentials that constitute this presentation definition.")
    @JsonProperty("credentials")
    @Valid
    @NotNull(message = "Property credentials cannot be null.")
 @Size(min=1)
    public List<RequestedCredentialModel> getCredentials() {
        return credentials;
    }
    public void setCredentials(List<RequestedCredentialModel> credentials) {
        this.credentials = credentials;
    }

    public PresentationDefinitionCreationModel addCredentialsItem(RequestedCredentialModel credentialsItem) {
        this.credentials.add(credentialsItem);
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
        PresentationDefinitionCreationModel presentationDefinitionCreationModel = (PresentationDefinitionCreationModel) o;
        return Objects.equals(this.identifier, presentationDefinitionCreationModel.identifier) &&
            Objects.equals(this.displayName, presentationDefinitionCreationModel.displayName) &&
            Objects.equals(this.description, presentationDefinitionCreationModel.description) &&
            Objects.equals(this.credentials, presentationDefinitionCreationModel.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, displayName, description, credentials);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PresentationDefinitionCreationModel {\n");
        
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    credentials: ").append(toIndentedString(credentials)).append("\n");
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


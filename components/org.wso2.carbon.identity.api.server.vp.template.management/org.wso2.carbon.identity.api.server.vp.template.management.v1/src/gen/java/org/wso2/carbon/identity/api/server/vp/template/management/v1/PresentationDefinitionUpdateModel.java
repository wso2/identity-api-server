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
 * Request body for updating an existing presentation definition. All fields are optional.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for updating an existing presentation definition. All fields are optional.")
public class PresentationDefinitionUpdateModel  {
  
    private String displayName;
    private String description;
    private List<RequestedCredentialModel> credentials = null;


    /**
    * New display label for the presentation definition.
    **/
    public PresentationDefinitionUpdateModel displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Updated Employee ID Verification", value = "New display label for the presentation definition.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * New description for the presentation definition.
    **/
    public PresentationDefinitionUpdateModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Updated employee ID credential request.", value = "New description for the presentation definition.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Replacement set of requested credentials. When provided, replaces the entire list.
    **/
    public PresentationDefinitionUpdateModel credentials(List<RequestedCredentialModel> credentials) {

        this.credentials = credentials;
        return this;
    }
    
    @ApiModelProperty(value = "Replacement set of requested credentials. When provided, replaces the entire list.")
    @JsonProperty("credentials")
    @Valid
    public List<RequestedCredentialModel> getCredentials() {
        return credentials;
    }
    public void setCredentials(List<RequestedCredentialModel> credentials) {
        this.credentials = credentials;
    }

    public PresentationDefinitionUpdateModel addCredentialsItem(RequestedCredentialModel credentialsItem) {
        if (this.credentials == null) {
            this.credentials = new ArrayList<>();
        }
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
        PresentationDefinitionUpdateModel presentationDefinitionUpdateModel = (PresentationDefinitionUpdateModel) o;
        return Objects.equals(this.displayName, presentationDefinitionUpdateModel.displayName) &&
            Objects.equals(this.description, presentationDefinitionUpdateModel.description) &&
            Objects.equals(this.credentials, presentationDefinitionUpdateModel.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, description, credentials);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PresentationDefinitionUpdateModel {\n");
        
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


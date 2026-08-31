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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Presentation definition response model.
 */
@ApiModel(description = "Presentation definition response")
public class PresentationDefinitionResponse {

    private String id;
    private String identifier;
    private String displayName;
    private String description;
    private java.util.List<RequestedCredentialModel> credentials;

    @ApiModelProperty(value = "Server-generated UUID of the presentation definition.")
    @JsonProperty("id")
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    @ApiModelProperty(value = "User-facing identifier of the presentation definition.")
    @JsonProperty("identifier")
    public String getIdentifier() {

        return identifier;
    }

    public void setIdentifier(String identifier) {

        this.identifier = identifier;
    }

    @ApiModelProperty(value = "Display label of the presentation definition.")
    @JsonProperty("displayName")
    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }

    @ApiModelProperty(value = "Description of the presentation definition.")
    @JsonProperty("description")
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @ApiModelProperty(value = "The list of requested credentials in this presentation definition.")
    @JsonProperty("credentials")
    public java.util.List<RequestedCredentialModel> getCredentials() {

        return credentials;
    }

    public void setCredentials(java.util.List<RequestedCredentialModel> credentials) {

        this.credentials = credentials;
    }

}

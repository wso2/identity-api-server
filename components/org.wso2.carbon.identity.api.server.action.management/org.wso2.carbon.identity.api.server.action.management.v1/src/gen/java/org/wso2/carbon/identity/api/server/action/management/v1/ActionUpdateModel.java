/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.action.management.v1.EndpointUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ORRule;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ActionUpdateModel  {
  
    private String name;
    private String description;
    private String version;
    private EndpointUpdateModel endpoint;
    private ORRule rule;

    /**
    * Updating name of the action.
    **/
    public ActionUpdateModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Pre Issue Access Token Action", value = "Updating name of the action.")
    @JsonProperty("name")
    @Valid @Size(min=1,max=255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Updating description of the action.
    **/
    public ActionUpdateModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "This action invokes before issuing an access token.", value = "Updating description of the action.")
    @JsonProperty("description")
    @Valid @Size(max=255)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Version of the action.
    **/
    public ActionUpdateModel version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "v2", value = "Version of the action.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    **/
    public ActionUpdateModel endpoint(EndpointUpdateModel endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("endpoint")
    @Valid
    public EndpointUpdateModel getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(EndpointUpdateModel endpoint) {
        this.endpoint = endpoint;
    }

    /**
    **/
    public ActionUpdateModel rule(ORRule rule) {

        this.rule = rule;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("rule")
    @Valid
    public ORRule getRule() {
        return rule;
    }
    public void setRule(ORRule rule) {
        this.rule = rule;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionUpdateModel actionUpdateModel = (ActionUpdateModel) o;
        return Objects.equals(this.name, actionUpdateModel.name) &&
            Objects.equals(this.description, actionUpdateModel.description) &&
            Objects.equals(this.version, actionUpdateModel.version) &&
            Objects.equals(this.endpoint, actionUpdateModel.endpoint) &&
            Objects.equals(this.rule, actionUpdateModel.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, version, endpoint, rule);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ActionUpdateModel {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
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


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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.flow.management.v1.AccessConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.Encryption;
import org.wso2.carbon.identity.api.server.flow.management.v1.EndpointUpdateModel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InFlowExtensionUpdateModel  {
  
    private String name;
    private String description;
    private String iconUrl;
    private String version;
    private EndpointUpdateModel endpoint;
    private AccessConfig accessConfig;
    private Encryption encryption;

    /**
    **/
    public InFlowExtensionUpdateModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public InFlowExtensionUpdateModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public InFlowExtensionUpdateModel iconUrl(String iconUrl) {

        this.iconUrl = iconUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("iconUrl")
    @Valid
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
    **/
    public InFlowExtensionUpdateModel version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public InFlowExtensionUpdateModel endpoint(EndpointUpdateModel endpoint) {

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
    public InFlowExtensionUpdateModel accessConfig(AccessConfig accessConfig) {

        this.accessConfig = accessConfig;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("accessConfig")
    @Valid
    public AccessConfig getAccessConfig() {
        return accessConfig;
    }
    public void setAccessConfig(AccessConfig accessConfig) {
        this.accessConfig = accessConfig;
    }

    /**
    **/
    public InFlowExtensionUpdateModel encryption(Encryption encryption) {

        this.encryption = encryption;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("encryption")
    @Valid
    public Encryption getEncryption() {
        return encryption;
    }
    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InFlowExtensionUpdateModel inFlowExtensionUpdateModel = (InFlowExtensionUpdateModel) o;
        return Objects.equals(this.name, inFlowExtensionUpdateModel.name) &&
            Objects.equals(this.description, inFlowExtensionUpdateModel.description) &&
            Objects.equals(this.iconUrl, inFlowExtensionUpdateModel.iconUrl) &&
            Objects.equals(this.version, inFlowExtensionUpdateModel.version) &&
            Objects.equals(this.endpoint, inFlowExtensionUpdateModel.endpoint) &&
            Objects.equals(this.accessConfig, inFlowExtensionUpdateModel.accessConfig) &&
            Objects.equals(this.encryption, inFlowExtensionUpdateModel.encryption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, iconUrl, version, endpoint, accessConfig, encryption);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionUpdateModel {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    accessConfig: ").append(toIndentedString(accessConfig)).append("\n");
        sb.append("    encryption: ").append(toIndentedString(encryption)).append("\n");
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


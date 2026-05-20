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
import org.wso2.carbon.identity.api.server.flow.management.v1.AccessConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.Encryption;
import org.wso2.carbon.identity.api.server.flow.management.v1.EndpointResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InFlowExtensionResponse  {
  
    private String id;
    private String name;
    private String description;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("INACTIVE") INACTIVE(String.valueOf("INACTIVE"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;
    private String version;
    private String createdAt;
    private String updatedAt;
    private String iconUrl;
    private EndpointResponse endpoint;
    private AccessConfig accessConfig;
    private Encryption encryption;

    /**
    **/
    public InFlowExtensionResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public InFlowExtensionResponse name(String name) {

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
    public InFlowExtensionResponse description(String description) {

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
    public InFlowExtensionResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    **/
    public InFlowExtensionResponse version(String version) {

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
    public InFlowExtensionResponse createdAt(String createdAt) {

        this.createdAt = createdAt;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("createdAt")
    @Valid
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
    **/
    public InFlowExtensionResponse updatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("updatedAt")
    @Valid
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
    **/
    public InFlowExtensionResponse iconUrl(String iconUrl) {

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
    public InFlowExtensionResponse endpoint(EndpointResponse endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("endpoint")
    @Valid
    public EndpointResponse getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(EndpointResponse endpoint) {
        this.endpoint = endpoint;
    }

    /**
    **/
    public InFlowExtensionResponse accessConfig(AccessConfig accessConfig) {

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
    public InFlowExtensionResponse encryption(Encryption encryption) {

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
        InFlowExtensionResponse inFlowExtensionResponse = (InFlowExtensionResponse) o;
        return Objects.equals(this.id, inFlowExtensionResponse.id) &&
            Objects.equals(this.name, inFlowExtensionResponse.name) &&
            Objects.equals(this.description, inFlowExtensionResponse.description) &&
            Objects.equals(this.status, inFlowExtensionResponse.status) &&
            Objects.equals(this.version, inFlowExtensionResponse.version) &&
            Objects.equals(this.createdAt, inFlowExtensionResponse.createdAt) &&
            Objects.equals(this.updatedAt, inFlowExtensionResponse.updatedAt) &&
            Objects.equals(this.iconUrl, inFlowExtensionResponse.iconUrl) &&
            Objects.equals(this.endpoint, inFlowExtensionResponse.endpoint) &&
            Objects.equals(this.accessConfig, inFlowExtensionResponse.accessConfig) &&
            Objects.equals(this.encryption, inFlowExtensionResponse.encryption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, version, createdAt, updatedAt, iconUrl, endpoint, accessConfig, encryption);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
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


/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationResponseModel  {
  
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String accessUrl;
    private String templateId;
    private ClaimConfiguration claimConfiguration;
    private List<InboundProtocolListItem> inboundProtocols = null;

    private AuthenticationSequence authenticationSequence;
    private AdvancedApplicationConfiguration advancedConfigurations;
    private ProvisioningConfiguration provisioningConfigurations;

@XmlType(name="AccessEnum")
@XmlEnum(String.class)
public enum AccessEnum {

    @XmlEnumValue("READ") READ(String.valueOf("READ")), @XmlEnumValue("WRITE") WRITE(String.valueOf("WRITE"));


    private String value;

    AccessEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static AccessEnum fromValue(String value) {
        for (AccessEnum b : AccessEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private AccessEnum access = AccessEnum.READ;

    /**
    **/
    public ApplicationResponseModel id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "394b8adcce24c64a8a09a0d80abf8c337bd253de", value = "")
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
    public ApplicationResponseModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "pickup", required = true, value = "")
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
    public ApplicationResponseModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "This is the configuration for Pickup application.", value = "")
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
    public ApplicationResponseModel imageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/logo/my-logo.png", value = "")
    @JsonProperty("imageUrl")
    @Valid
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
    **/
    public ApplicationResponseModel accessUrl(String accessUrl) {

        this.accessUrl = accessUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/login", value = "")
    @JsonProperty("accessUrl")
    @Valid
    public String getAccessUrl() {
        return accessUrl;
    }
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    /**
    **/
    public ApplicationResponseModel templateId(String templateId) {

        this.templateId = templateId;
        return this;
    }
    
    @ApiModelProperty(example = "adwefi2429asdfdf94444rraf44", value = "")
    @JsonProperty("templateId")
    @Valid
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
    **/
    public ApplicationResponseModel claimConfiguration(ClaimConfiguration claimConfiguration) {

        this.claimConfiguration = claimConfiguration;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claimConfiguration")
    @Valid
    public ClaimConfiguration getClaimConfiguration() {
        return claimConfiguration;
    }
    public void setClaimConfiguration(ClaimConfiguration claimConfiguration) {
        this.claimConfiguration = claimConfiguration;
    }

    /**
    **/
    public ApplicationResponseModel inboundProtocols(List<InboundProtocolListItem> inboundProtocols) {

        this.inboundProtocols = inboundProtocols;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("inboundProtocols")
    @Valid
    public List<InboundProtocolListItem> getInboundProtocols() {
        return inboundProtocols;
    }
    public void setInboundProtocols(List<InboundProtocolListItem> inboundProtocols) {
        this.inboundProtocols = inboundProtocols;
    }

    public ApplicationResponseModel addInboundProtocolsItem(InboundProtocolListItem inboundProtocolsItem) {
        if (this.inboundProtocols == null) {
            this.inboundProtocols = new ArrayList<>();
        }
        this.inboundProtocols.add(inboundProtocolsItem);
        return this;
    }

        /**
    **/
    public ApplicationResponseModel authenticationSequence(AuthenticationSequence authenticationSequence) {

        this.authenticationSequence = authenticationSequence;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authenticationSequence")
    @Valid
    public AuthenticationSequence getAuthenticationSequence() {
        return authenticationSequence;
    }
    public void setAuthenticationSequence(AuthenticationSequence authenticationSequence) {
        this.authenticationSequence = authenticationSequence;
    }

    /**
    **/
    public ApplicationResponseModel advancedConfigurations(AdvancedApplicationConfiguration advancedConfigurations) {

        this.advancedConfigurations = advancedConfigurations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("advancedConfigurations")
    @Valid
    public AdvancedApplicationConfiguration getAdvancedConfigurations() {
        return advancedConfigurations;
    }
    public void setAdvancedConfigurations(AdvancedApplicationConfiguration advancedConfigurations) {
        this.advancedConfigurations = advancedConfigurations;
    }

    /**
    **/
    public ApplicationResponseModel provisioningConfigurations(ProvisioningConfiguration provisioningConfigurations) {

        this.provisioningConfigurations = provisioningConfigurations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("provisioningConfigurations")
    @Valid
    public ProvisioningConfiguration getProvisioningConfigurations() {
        return provisioningConfigurations;
    }
    public void setProvisioningConfigurations(ProvisioningConfiguration provisioningConfigurations) {
        this.provisioningConfigurations = provisioningConfigurations;
    }

    /**
    **/
    public ApplicationResponseModel access(AccessEnum access) {

        this.access = access;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("access")
    @Valid
    public AccessEnum getAccess() {
        return access;
    }
    public void setAccess(AccessEnum access) {
        this.access = access;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationResponseModel applicationResponseModel = (ApplicationResponseModel) o;
        return Objects.equals(this.id, applicationResponseModel.id) &&
            Objects.equals(this.name, applicationResponseModel.name) &&
            Objects.equals(this.description, applicationResponseModel.description) &&
            Objects.equals(this.imageUrl, applicationResponseModel.imageUrl) &&
            Objects.equals(this.accessUrl, applicationResponseModel.accessUrl) &&
            Objects.equals(this.templateId, applicationResponseModel.templateId) &&
            Objects.equals(this.claimConfiguration, applicationResponseModel.claimConfiguration) &&
            Objects.equals(this.inboundProtocols, applicationResponseModel.inboundProtocols) &&
            Objects.equals(this.authenticationSequence, applicationResponseModel.authenticationSequence) &&
            Objects.equals(this.advancedConfigurations, applicationResponseModel.advancedConfigurations) &&
            Objects.equals(this.provisioningConfigurations, applicationResponseModel.provisioningConfigurations) &&
            Objects.equals(this.access, applicationResponseModel.access);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imageUrl, accessUrl, templateId, claimConfiguration, inboundProtocols, authenticationSequence, advancedConfigurations, provisioningConfigurations, access);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationResponseModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
        sb.append("    accessUrl: ").append(toIndentedString(accessUrl)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    claimConfiguration: ").append(toIndentedString(claimConfiguration)).append("\n");
        sb.append("    inboundProtocols: ").append(toIndentedString(inboundProtocols)).append("\n");
        sb.append("    authenticationSequence: ").append(toIndentedString(authenticationSequence)).append("\n");
        sb.append("    advancedConfigurations: ").append(toIndentedString(advancedConfigurations)).append("\n");
        sb.append("    provisioningConfigurations: ").append(toIndentedString(provisioningConfigurations)).append("\n");
        sb.append("    access: ").append(toIndentedString(access)).append("\n");
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


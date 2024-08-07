/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.AssociatedRolesConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationModel  {
  
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String accessUrl;
    private String logoutReturnUrl;
    private String templateId;
    private String templateVersion;
    private Boolean isManagementApp = false;
    private Boolean isB2BSelfServiceApp = false;
    private Boolean applicationEnabled = true;
    private AssociatedRolesConfig associatedRoles;
    private ClaimConfiguration claimConfiguration;
    private InboundProtocols inboundProtocolConfiguration;
    private AuthenticationSequence authenticationSequence;
    private AdvancedApplicationConfiguration advancedConfigurations;
    private ProvisioningConfiguration provisioningConfigurations;

    /**
    **/
    public ApplicationModel id(String id) {

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
    public ApplicationModel name(String name) {

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
    public ApplicationModel description(String description) {

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
    public ApplicationModel imageUrl(String imageUrl) {

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
    public ApplicationModel accessUrl(String accessUrl) {

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
    public ApplicationModel logoutReturnUrl(String logoutReturnUrl) {

        this.logoutReturnUrl = logoutReturnUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/app/logout", value = "")
    @JsonProperty("logoutReturnUrl")
    @Valid
    public String getLogoutReturnUrl() {
        return logoutReturnUrl;
    }
    public void setLogoutReturnUrl(String logoutReturnUrl) {
        this.logoutReturnUrl = logoutReturnUrl;
    }

    /**
    **/
    public ApplicationModel templateId(String templateId) {

        this.templateId = templateId;
        return this;
    }
    
    @ApiModelProperty(example = "980b8tester24c64a8a09a0d80abf8c337bd2555", value = "")
    @JsonProperty("templateId")
    @Valid
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
    * Version of the template used to create the application.
    **/
    public ApplicationModel templateVersion(String templateVersion) {

        this.templateVersion = templateVersion;
        return this;
    }
    
    @ApiModelProperty(example = "v1.0.0", value = "Version of the template used to create the application.")
    @JsonProperty("templateVersion")
    @Valid
    public String getTemplateVersion() {
        return templateVersion;
    }
    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    /**
    * Decides whether the application used to access System APIs
    **/
    public ApplicationModel isManagementApp(Boolean isManagementApp) {

        this.isManagementApp = isManagementApp;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the application used to access System APIs")
    @JsonProperty("isManagementApp")
    @Valid
    public Boolean getIsManagementApp() {
        return isManagementApp;
    }
    public void setIsManagementApp(Boolean isManagementApp) {
        this.isManagementApp = isManagementApp;
    }

    /**
    * Decides whether the application used to for B2B self service
    **/
    public ApplicationModel isB2BSelfServiceApp(Boolean isB2BSelfServiceApp) {

        this.isB2BSelfServiceApp = isB2BSelfServiceApp;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the application used to for B2B self service")
    @JsonProperty("isB2BSelfServiceApp")
    @Valid
    public Boolean getIsB2BSelfServiceApp() {
        return isB2BSelfServiceApp;
    }
    public void setIsB2BSelfServiceApp(Boolean isB2BSelfServiceApp) {
        this.isB2BSelfServiceApp = isB2BSelfServiceApp;
    }

    /**
    * Decides whether the application is enabled.
    **/
    public ApplicationModel applicationEnabled(Boolean applicationEnabled) {

        this.applicationEnabled = applicationEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Decides whether the application is enabled.")
    @JsonProperty("applicationEnabled")
    @Valid
    public Boolean getApplicationEnabled() {
        return applicationEnabled;
    }
    public void setApplicationEnabled(Boolean applicationEnabled) {
        this.applicationEnabled = applicationEnabled;
    }

    /**
    **/
    public ApplicationModel associatedRoles(AssociatedRolesConfig associatedRoles) {

        this.associatedRoles = associatedRoles;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("associatedRoles")
    @Valid
    public AssociatedRolesConfig getAssociatedRoles() {
        return associatedRoles;
    }
    public void setAssociatedRoles(AssociatedRolesConfig associatedRoles) {
        this.associatedRoles = associatedRoles;
    }

    /**
    **/
    public ApplicationModel claimConfiguration(ClaimConfiguration claimConfiguration) {

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
    public ApplicationModel inboundProtocolConfiguration(InboundProtocols inboundProtocolConfiguration) {

        this.inboundProtocolConfiguration = inboundProtocolConfiguration;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("inboundProtocolConfiguration")
    @Valid
    public InboundProtocols getInboundProtocolConfiguration() {
        return inboundProtocolConfiguration;
    }
    public void setInboundProtocolConfiguration(InboundProtocols inboundProtocolConfiguration) {
        this.inboundProtocolConfiguration = inboundProtocolConfiguration;
    }

    /**
    **/
    public ApplicationModel authenticationSequence(AuthenticationSequence authenticationSequence) {

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
    public ApplicationModel advancedConfigurations(AdvancedApplicationConfiguration advancedConfigurations) {

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
    public ApplicationModel provisioningConfigurations(ProvisioningConfiguration provisioningConfigurations) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationModel applicationModel = (ApplicationModel) o;
        return Objects.equals(this.id, applicationModel.id) &&
            Objects.equals(this.name, applicationModel.name) &&
            Objects.equals(this.description, applicationModel.description) &&
            Objects.equals(this.imageUrl, applicationModel.imageUrl) &&
            Objects.equals(this.accessUrl, applicationModel.accessUrl) &&
            Objects.equals(this.logoutReturnUrl, applicationModel.logoutReturnUrl) &&
            Objects.equals(this.templateId, applicationModel.templateId) &&
            Objects.equals(this.templateVersion, applicationModel.templateVersion) &&
            Objects.equals(this.isManagementApp, applicationModel.isManagementApp) &&
            Objects.equals(this.isB2BSelfServiceApp, applicationModel.isB2BSelfServiceApp) &&
            Objects.equals(this.applicationEnabled, applicationModel.applicationEnabled) &&
            Objects.equals(this.associatedRoles, applicationModel.associatedRoles) &&
            Objects.equals(this.claimConfiguration, applicationModel.claimConfiguration) &&
            Objects.equals(this.inboundProtocolConfiguration, applicationModel.inboundProtocolConfiguration) &&
            Objects.equals(this.authenticationSequence, applicationModel.authenticationSequence) &&
            Objects.equals(this.advancedConfigurations, applicationModel.advancedConfigurations) &&
            Objects.equals(this.provisioningConfigurations, applicationModel.provisioningConfigurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imageUrl, accessUrl, logoutReturnUrl, templateId, templateVersion, isManagementApp, isB2BSelfServiceApp, applicationEnabled, associatedRoles, claimConfiguration, inboundProtocolConfiguration, authenticationSequence, advancedConfigurations, provisioningConfigurations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
        sb.append("    accessUrl: ").append(toIndentedString(accessUrl)).append("\n");
        sb.append("    logoutReturnUrl: ").append(toIndentedString(logoutReturnUrl)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    templateVersion: ").append(toIndentedString(templateVersion)).append("\n");
        sb.append("    isManagementApp: ").append(toIndentedString(isManagementApp)).append("\n");
        sb.append("    isB2BSelfServiceApp: ").append(toIndentedString(isB2BSelfServiceApp)).append("\n");
        sb.append("    applicationEnabled: ").append(toIndentedString(applicationEnabled)).append("\n");
        sb.append("    associatedRoles: ").append(toIndentedString(associatedRoles)).append("\n");
        sb.append("    claimConfiguration: ").append(toIndentedString(claimConfiguration)).append("\n");
        sb.append("    inboundProtocolConfiguration: ").append(toIndentedString(inboundProtocolConfiguration)).append("\n");
        sb.append("    authenticationSequence: ").append(toIndentedString(authenticationSequence)).append("\n");
        sb.append("    advancedConfigurations: ").append(toIndentedString(advancedConfigurations)).append("\n");
        sb.append("    provisioningConfigurations: ").append(toIndentedString(provisioningConfigurations)).append("\n");
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


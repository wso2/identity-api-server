/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AdditionalSpProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AdvancedApplicationConfiguration  {
  
    private Boolean saas;
    private Boolean discoverableByEndUsers;
    private Certificate certificate;
    private Boolean skipLoginConsent;
    private Boolean skipLogoutConsent;
    private Boolean useExternalConsentManagement;
    private String externalConsentURL;
    private Boolean returnAuthenticatedIdpList;
    private Boolean enableAuthorization;
    private Boolean fragment;
    private List<AdditionalSpProperty> additionalSpProperties = null;


    /**
    * Decides whether the application is accessible across tenants.
    **/
    public AdvancedApplicationConfiguration saas(Boolean saas) {

        this.saas = saas;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the application is accessible across tenants.")
    @JsonProperty("saas")
    @Valid
    public Boolean getSaas() {
        return saas;
    }
    public void setSaas(Boolean saas) {
        this.saas = saas;
    }

    /**
    * Decides whether the application is visible for end users.
    **/
    public AdvancedApplicationConfiguration discoverableByEndUsers(Boolean discoverableByEndUsers) {

        this.discoverableByEndUsers = discoverableByEndUsers;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the application is visible for end users.")
    @JsonProperty("discoverableByEndUsers")
    @Valid
    public Boolean getDiscoverableByEndUsers() {
        return discoverableByEndUsers;
    }
    public void setDiscoverableByEndUsers(Boolean discoverableByEndUsers) {
        this.discoverableByEndUsers = discoverableByEndUsers;
    }

    /**
    **/
    public AdvancedApplicationConfiguration certificate(Certificate certificate) {

        this.certificate = certificate;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("certificate")
    @Valid
    public Certificate getCertificate() {
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    /**
    * Decides whether user consent needs to be skipped during login flows.
    **/
    public AdvancedApplicationConfiguration skipLoginConsent(Boolean skipLoginConsent) {

        this.skipLoginConsent = skipLoginConsent;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether user consent needs to be skipped during login flows.")
    @JsonProperty("skipLoginConsent")
    @Valid
    public Boolean getSkipLoginConsent() {
        return skipLoginConsent;
    }
    public void setSkipLoginConsent(Boolean skipLoginConsent) {
        this.skipLoginConsent = skipLoginConsent;
    }

    /**
    * Decides whether user consent needs to be skipped during logout flows.
    **/
    public AdvancedApplicationConfiguration skipLogoutConsent(Boolean skipLogoutConsent) {

        this.skipLogoutConsent = skipLogoutConsent;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether user consent needs to be skipped during logout flows.")
    @JsonProperty("skipLogoutConsent")
    @Valid
    public Boolean getSkipLogoutConsent() {
        return skipLogoutConsent;
    }
    public void setSkipLogoutConsent(Boolean skipLogoutConsent) {
        this.skipLogoutConsent = skipLogoutConsent;
    }

    /**
     * Decides whether user consent needs to be handles externally.
     **/
    public AdvancedApplicationConfiguration useExternalConsentManagement(Boolean useExternalConsentManagement) {

        this.useExternalConsentManagement = useExternalConsentManagement;
        return this;
    }

    @ApiModelProperty(example = "false", value = "Decides whether user consent needs to be handled externally.")
    @JsonProperty("useExternalConsentManagement")
    @Valid
    public Boolean getUseExternalConsentManagement() {
        return useExternalConsentManagement;
    }
    public void setUseExternalConsentManagement(Boolean useExternalConsentManagement) {
        this.useExternalConsentManagement = useExternalConsentManagement;
    }

    /**
     * Decides whether user consent needs to be handles externally.
     **/
    public AdvancedApplicationConfiguration externalConsentURL(String externalConsentURL) {

        this.externalConsentURL = externalConsentURL;
        return this;
    }

    @ApiModelProperty(example = "", value = "Decides whether user consent needs to be handled externally.")
    @JsonProperty("externalConsentURL")
    @Valid
    public String getExternalConsentURL() {
        return externalConsentURL;
    }
    public void setExternalConsentURL(String externalConsentURL) {
        this.externalConsentURL = externalConsentURL;
    }

    /**
    * Decides whether the list of authenticated identity providers need to be returned in the authentication response.
    **/
    public AdvancedApplicationConfiguration returnAuthenticatedIdpList(Boolean returnAuthenticatedIdpList) {

        this.returnAuthenticatedIdpList = returnAuthenticatedIdpList;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the list of authenticated identity providers need to be returned in the authentication response.")
    @JsonProperty("returnAuthenticatedIdpList")
    @Valid
    public Boolean getReturnAuthenticatedIdpList() {
        return returnAuthenticatedIdpList;
    }
    public void setReturnAuthenticatedIdpList(Boolean returnAuthenticatedIdpList) {
        this.returnAuthenticatedIdpList = returnAuthenticatedIdpList;
    }

    /**
    * Decides whether authorization policies needs to be engaged during authentication flows.
    **/
    public AdvancedApplicationConfiguration enableAuthorization(Boolean enableAuthorization) {

        this.enableAuthorization = enableAuthorization;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Decides whether authorization policies needs to be engaged during authentication flows.")
    @JsonProperty("enableAuthorization")
    @Valid
    public Boolean getEnableAuthorization() {
        return enableAuthorization;
    }
    public void setEnableAuthorization(Boolean enableAuthorization) {
        this.enableAuthorization = enableAuthorization;
    }

    /**
    * Decides whether application is a fragment application.
    **/
    public AdvancedApplicationConfiguration fragment(Boolean fragment) {

        this.fragment = fragment;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether application is a fragment application.")
    @JsonProperty("fragment")
    @Valid
    public Boolean getFragment() {
        return fragment;
    }
    public void setFragment(Boolean fragment) {
        this.fragment = fragment;
    }

    /**
    **/
    public AdvancedApplicationConfiguration additionalSpProperties(List<AdditionalSpProperty> additionalSpProperties) {

        this.additionalSpProperties = additionalSpProperties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("additionalSpProperties")
    @Valid
    public List<AdditionalSpProperty> getAdditionalSpProperties() {
        return additionalSpProperties;
    }
    public void setAdditionalSpProperties(List<AdditionalSpProperty> additionalSpProperties) {
        this.additionalSpProperties = additionalSpProperties;
    }

    public AdvancedApplicationConfiguration addAdditionalSpPropertiesItem(AdditionalSpProperty additionalSpPropertiesItem) {
        if (this.additionalSpProperties == null) {
            this.additionalSpProperties = new ArrayList<>();
        }
        this.additionalSpProperties.add(additionalSpPropertiesItem);
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
        AdvancedApplicationConfiguration advancedApplicationConfiguration = (AdvancedApplicationConfiguration) o;
        return Objects.equals(this.saas, advancedApplicationConfiguration.saas) &&
            Objects.equals(this.discoverableByEndUsers, advancedApplicationConfiguration.discoverableByEndUsers) &&
            Objects.equals(this.certificate, advancedApplicationConfiguration.certificate) &&
            Objects.equals(this.skipLoginConsent, advancedApplicationConfiguration.skipLoginConsent) &&
            Objects.equals(this.skipLogoutConsent, advancedApplicationConfiguration.skipLogoutConsent) &&
            Objects.equals(this.useExternalConsentManagement, advancedApplicationConfiguration.useExternalConsentManagement) &&
            Objects.equals(this.externalConsentURL, advancedApplicationConfiguration.externalConsentURL) &&
            Objects.equals(this.returnAuthenticatedIdpList, advancedApplicationConfiguration.returnAuthenticatedIdpList) &&
            Objects.equals(this.enableAuthorization, advancedApplicationConfiguration.enableAuthorization) &&
            Objects.equals(this.fragment, advancedApplicationConfiguration.fragment) &&
            Objects.equals(this.additionalSpProperties, advancedApplicationConfiguration.additionalSpProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saas, discoverableByEndUsers, certificate, skipLoginConsent, skipLogoutConsent, useExternalConsentManagement, externalConsentURL, returnAuthenticatedIdpList, enableAuthorization, fragment, additionalSpProperties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AdvancedApplicationConfiguration {\n");
        
        sb.append("    saas: ").append(toIndentedString(saas)).append("\n");
        sb.append("    discoverableByEndUsers: ").append(toIndentedString(discoverableByEndUsers)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("    skipLoginConsent: ").append(toIndentedString(skipLoginConsent)).append("\n");
        sb.append("    skipLogoutConsent: ").append(toIndentedString(skipLogoutConsent)).append("\n");
        sb.append("    useExternalConsentManagement: ").append(toIndentedString(useExternalConsentManagement)).append("\n");
        sb.append("    externalConsentURL: ").append(toIndentedString(externalConsentURL)).append("\n");
        sb.append("    returnAuthenticatedIdpList: ").append(toIndentedString(returnAuthenticatedIdpList)).append("\n");
        sb.append("    enableAuthorization: ").append(toIndentedString(enableAuthorization)).append("\n");
        sb.append("    fragment: ").append(toIndentedString(fragment)).append("\n");
        sb.append("    additionalSpProperties: ").append(toIndentedString(additionalSpProperties)).append("\n");
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


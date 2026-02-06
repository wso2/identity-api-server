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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.ProvisioningConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RealmConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ServerConfig  {
  
    private List<String> homeRealmIdentifiers = null;

    private RealmConfig realmConfig;
    private String idleSessionTimeoutPeriod = "15";
    private String rememberMePeriod = "20160";
    private String enableMaximumSessionTimeoutPeriod = "false";
    private String maximumSessionTimeoutPeriod = "43200";
    private ProvisioningConfig provisioning;
    private List<AuthenticatorListItem> authenticators = null;

    private CORSConfig cors;
    private DCRConfig dcr;

    /**
    * The home realm identifier for the resident identity provider
    **/
    public ServerConfig homeRealmIdentifiers(List<String> homeRealmIdentifiers) {

        this.homeRealmIdentifiers = homeRealmIdentifiers;
        return this;
    }
    
    @ApiModelProperty(value = "The home realm identifier for the resident identity provider")
    @JsonProperty("homeRealmIdentifiers")
    @Valid
    public List<String> getHomeRealmIdentifiers() {
        return homeRealmIdentifiers;
    }
    public void setHomeRealmIdentifiers(List<String> homeRealmIdentifiers) {
        this.homeRealmIdentifiers = homeRealmIdentifiers;
    }

    public ServerConfig addHomeRealmIdentifiersItem(String homeRealmIdentifiersItem) {
        if (this.homeRealmIdentifiers == null) {
            this.homeRealmIdentifiers = new ArrayList<>();
        }
        this.homeRealmIdentifiers.add(homeRealmIdentifiersItem);
        return this;
    }

        /**
    **/
    public ServerConfig realmConfig(RealmConfig realmConfig) {

        this.realmConfig = realmConfig;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("realmConfig")
    @Valid
    public RealmConfig getRealmConfig() {
        return realmConfig;
    }
    public void setRealmConfig(RealmConfig realmConfig) {
        this.realmConfig = realmConfig;
    }

    /**
    * The idle session timeout in minutes
    **/
    public ServerConfig idleSessionTimeoutPeriod(String idleSessionTimeoutPeriod) {

        this.idleSessionTimeoutPeriod = idleSessionTimeoutPeriod;
        return this;
    }
    
    @ApiModelProperty(example = "15", value = "The idle session timeout in minutes")
    @JsonProperty("idleSessionTimeoutPeriod")
    @Valid
    public String getIdleSessionTimeoutPeriod() {
        return idleSessionTimeoutPeriod;
    }
    public void setIdleSessionTimeoutPeriod(String idleSessionTimeoutPeriod) {
        this.idleSessionTimeoutPeriod = idleSessionTimeoutPeriod;
    }

    /**
    * The remember me period in minutes
    **/
    public ServerConfig rememberMePeriod(String rememberMePeriod) {

        this.rememberMePeriod = rememberMePeriod;
        return this;
    }
    
    @ApiModelProperty(example = "20160", value = "The remember me period in minutes")
    @JsonProperty("rememberMePeriod")
    @Valid
    public String getRememberMePeriod() {
        return rememberMePeriod;
    }
    public void setRememberMePeriod(String rememberMePeriod) {
        this.rememberMePeriod = rememberMePeriod;
    }

    /**
    * Whether to enable maximum session timeout
    **/
    public ServerConfig enableMaximumSessionTimeoutPeriod(String enableMaximumSessionTimeoutPeriod) {

        this.enableMaximumSessionTimeoutPeriod = enableMaximumSessionTimeoutPeriod;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether to enable maximum session timeout")
    @JsonProperty("enableMaximumSessionTimeoutPeriod")
    @Valid
    public String getEnableMaximumSessionTimeoutPeriod() {
        return enableMaximumSessionTimeoutPeriod;
    }
    public void setEnableMaximumSessionTimeoutPeriod(String enableMaximumSessionTimeoutPeriod) {
        this.enableMaximumSessionTimeoutPeriod = enableMaximumSessionTimeoutPeriod;
    }

    /**
    * The maximum session timeout in minutes. This property becomes only applicable if the enableMaximumSessionTimeoutPeriod config is set to true.
    **/
    public ServerConfig maximumSessionTimeoutPeriod(String maximumSessionTimeoutPeriod) {

        this.maximumSessionTimeoutPeriod = maximumSessionTimeoutPeriod;
        return this;
    }
    
    @ApiModelProperty(example = "20160", value = "The maximum session timeout in minutes. This property becomes only applicable if the enableMaximumSessionTimeoutPeriod config is set to true.")
    @JsonProperty("maximumSessionTimeoutPeriod")
    @Valid
    public String getMaximumSessionTimeoutPeriod() {
        return maximumSessionTimeoutPeriod;
    }
    public void setMaximumSessionTimeoutPeriod(String maximumSessionTimeoutPeriod) {
        this.maximumSessionTimeoutPeriod = maximumSessionTimeoutPeriod;
    }

    /**
    **/
    public ServerConfig provisioning(ProvisioningConfig provisioning) {

        this.provisioning = provisioning;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("provisioning")
    @Valid
    public ProvisioningConfig getProvisioning() {
        return provisioning;
    }
    public void setProvisioning(ProvisioningConfig provisioning) {
        this.provisioning = provisioning;
    }

    /**
    **/
    public ServerConfig authenticators(List<AuthenticatorListItem> authenticators) {

        this.authenticators = authenticators;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authenticators")
    @Valid
    public List<AuthenticatorListItem> getAuthenticators() {
        return authenticators;
    }
    public void setAuthenticators(List<AuthenticatorListItem> authenticators) {
        this.authenticators = authenticators;
    }

    public ServerConfig addAuthenticatorsItem(AuthenticatorListItem authenticatorsItem) {
        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }
        this.authenticators.add(authenticatorsItem);
        return this;
    }

        /**
    **/
    public ServerConfig cors(CORSConfig cors) {

        this.cors = cors;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("cors")
    @Valid
    public CORSConfig getCors() {
        return cors;
    }
    public void setCors(CORSConfig cors) {
        this.cors = cors;
    }

    /** DCR Configurations
     **/
    public ServerConfig dcrConfig(DCRConfig dcr) {

        this.dcr = dcr;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("dcr")
    @Valid
    public DCRConfig getDcr() {
        return dcr;
    }
    public void setDcr(DCRConfig dcr) {
        this.dcr = dcr;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerConfig serverConfig = (ServerConfig) o;
        return Objects.equals(this.homeRealmIdentifiers, serverConfig.homeRealmIdentifiers) &&
            Objects.equals(this.realmConfig, serverConfig.realmConfig) &&
            Objects.equals(this.idleSessionTimeoutPeriod, serverConfig.idleSessionTimeoutPeriod) &&
            Objects.equals(this.rememberMePeriod, serverConfig.rememberMePeriod) &&
            Objects.equals(this.enableMaximumSessionTimeoutPeriod, serverConfig.enableMaximumSessionTimeoutPeriod) &&
            Objects.equals(this.maximumSessionTimeoutPeriod, serverConfig.maximumSessionTimeoutPeriod) &&
            Objects.equals(this.provisioning, serverConfig.provisioning) &&
            Objects.equals(this.authenticators, serverConfig.authenticators) &&
            Objects.equals(this.cors, serverConfig.cors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeRealmIdentifiers, realmConfig, idleSessionTimeoutPeriod, rememberMePeriod, enableMaximumSessionTimeoutPeriod, maximumSessionTimeoutPeriod, provisioning, authenticators, cors);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ServerConfig {\n");
        
        sb.append("    homeRealmIdentifiers: ").append(toIndentedString(homeRealmIdentifiers)).append("\n");
        sb.append("    realmConfig: ").append(toIndentedString(realmConfig)).append("\n");
        sb.append("    idleSessionTimeoutPeriod: ").append(toIndentedString(idleSessionTimeoutPeriod)).append("\n");
        sb.append("    rememberMePeriod: ").append(toIndentedString(rememberMePeriod)).append("\n");
        sb.append("    enableMaximumSessionTimeoutPeriod: ").append(toIndentedString(enableMaximumSessionTimeoutPeriod)).append("\n");
        sb.append("    maximumSessionTimeoutPeriod: ").append(toIndentedString(maximumSessionTimeoutPeriod)).append("\n");
        sb.append("    provisioning: ").append(toIndentedString(provisioning)).append("\n");
        sb.append("    authenticators: ").append(toIndentedString(authenticators)).append("\n");
        sb.append("    cors: ").append(toIndentedString(cors)).append("\n");
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


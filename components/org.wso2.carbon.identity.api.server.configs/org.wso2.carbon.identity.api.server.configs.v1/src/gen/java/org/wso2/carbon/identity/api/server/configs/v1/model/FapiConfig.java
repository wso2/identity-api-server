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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.FapiProfile;
import javax.validation.constraints.*;

/**
 * Financial-grade API (FAPI) configuration for the tenant.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Financial-grade API (FAPI) configuration for the tenant.")
public class FapiConfig  {
  
    private Boolean enabled = false;
    private List<FapiProfile> supportedProfiles = null;


    /**
    * Indicates whether FAPI compliance enforcement is enabled for the tenant.
    **/
    public FapiConfig enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Indicates whether FAPI compliance enforcement is enabled for the tenant.")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    * List of FAPI security profiles supported by the tenant. - &#x60;FAPI1_ADVANCED&#x60;: FAPI 1.0 Advanced Security Profile (Read and Write API Security Profile). - &#x60;FAPI2_SECURITY&#x60;: FAPI 2.0 Security Profile (baseline profile for high-security OAuth 2.0 APIs). 
    **/
    public FapiConfig supportedProfiles(List<FapiProfile> supportedProfiles) {

        this.supportedProfiles = supportedProfiles;
        return this;
    }
    
    @ApiModelProperty(example = "[\"FAPI1_ADVANCED\",\"FAPI2_SECURITY\"]", value = "List of FAPI security profiles supported by the tenant. - `FAPI1_ADVANCED`: FAPI 1.0 Advanced Security Profile (Read and Write API Security Profile). - `FAPI2_SECURITY`: FAPI 2.0 Security Profile (baseline profile for high-security OAuth 2.0 APIs). ")
    @JsonProperty("supportedProfiles")
    @Valid
    public List<FapiProfile> getSupportedProfiles() {
        return supportedProfiles;
    }
    public void setSupportedProfiles(List<FapiProfile> supportedProfiles) {
        this.supportedProfiles = supportedProfiles;
    }

    public FapiConfig addSupportedProfilesItem(FapiProfile supportedProfilesItem) {
        if (this.supportedProfiles == null) {
            this.supportedProfiles = new ArrayList<>();
        }
        this.supportedProfiles.add(supportedProfilesItem);
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
        FapiConfig fapiConfig = (FapiConfig) o;
        return Objects.equals(this.enabled, fapiConfig.enabled) &&
            Objects.equals(this.supportedProfiles, fapiConfig.supportedProfiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, supportedProfiles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FapiConfig {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    supportedProfiles: ").append(toIndentedString(supportedProfiles)).append("\n");
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


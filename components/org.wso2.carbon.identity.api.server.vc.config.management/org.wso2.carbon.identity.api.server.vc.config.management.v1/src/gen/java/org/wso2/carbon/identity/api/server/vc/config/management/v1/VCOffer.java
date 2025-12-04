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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCOffer  {
  
    private String id;
    private String displayName;
    private List<String> credentialConfigurationIds = new ArrayList<String>();


    /**
    **/
    public VCOffer id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "7e5f9d2a-6b5e-4df6-9b87-8a3d1a4a0c31", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public VCOffer displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Offer 1", required = true, value = "")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public VCOffer credentialConfigurationIds(List<String> credentialConfigurationIds) {

        this.credentialConfigurationIds = credentialConfigurationIds;
        return this;
    }
    
    @ApiModelProperty(example = "[\"EmployeeBadge\"]", required = true, value = "")
    @JsonProperty("credentialConfigurationIds")
    @Valid
    @NotNull(message = "Property credentialConfigurationIds cannot be null.")

    public List<String> getCredentialConfigurationIds() {
        return credentialConfigurationIds;
    }
    public void setCredentialConfigurationIds(List<String> credentialConfigurationIds) {
        this.credentialConfigurationIds = credentialConfigurationIds;
    }

    public VCOffer addCredentialConfigurationIdsItem(String credentialConfigurationIdsItem) {
        this.credentialConfigurationIds.add(credentialConfigurationIdsItem);
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
        VCOffer vcOffer = (VCOffer) o;
        return Objects.equals(this.id, vcOffer.id) &&
            Objects.equals(this.displayName, vcOffer.displayName) &&
            Objects.equals(this.credentialConfigurationIds, vcOffer.credentialConfigurationIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, credentialConfigurationIds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCOffer {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    credentialConfigurationIds: ").append(toIndentedString(credentialConfigurationIds)).append("\n");
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


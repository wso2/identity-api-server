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

package org.wso2.carbon.identity.api.server.idp.v1.model;

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

public class IdentityProviderUnshareSelectedRequestBody  {
  
    private String identityProviderId;
    private List<String> orgIds = new ArrayList<>();


    /**
    **/
    public IdentityProviderUnshareSelectedRequestBody identityProviderId(String identityProviderId) {

        this.identityProviderId = identityProviderId;
        return this;
    }
    
    @ApiModelProperty(example = "c75e27f9-98c7-4518-a968-c6cd59f0ac6b", required = true, value = "")
    @JsonProperty("identityProviderId")
    @Valid
    @NotNull(message = "Property identityProviderId cannot be null.")

    public String getIdentityProviderId() {
        return identityProviderId;
    }
    public void setIdentityProviderId(String identityProviderId) {
        this.identityProviderId = identityProviderId;
    }

    /**
    **/
    public IdentityProviderUnshareSelectedRequestBody orgIds(List<String> orgIds) {

        this.orgIds = orgIds;
        return this;
    }
    
    @ApiModelProperty(example = "[\"682edf68-4835-4bb8-961f-0a16bc6cc866\",\"ghfbctgf-4318-46d4-8ee1-7t3s38e23098\"]", required = true, value = "")
    @JsonProperty("orgIds")
    @Valid
    @NotNull(message = "Property orgIds cannot be null.")

    public List<String> getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public IdentityProviderUnshareSelectedRequestBody addOrgIdsItem(String orgIdsItem) {
        this.orgIds.add(orgIdsItem);
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
        IdentityProviderUnshareSelectedRequestBody identityProviderUnshareSelectedRequestBody = (IdentityProviderUnshareSelectedRequestBody) o;
        return Objects.equals(this.identityProviderId, identityProviderUnshareSelectedRequestBody.identityProviderId) &&
            Objects.equals(this.orgIds, identityProviderUnshareSelectedRequestBody.orgIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityProviderId, orgIds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderUnshareSelectedRequestBody {\n");
        
        sb.append("    identityProviderId: ").append(toIndentedString(identityProviderId)).append("\n");
        sb.append("    orgIds: ").append(toIndentedString(orgIds)).append("\n");
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


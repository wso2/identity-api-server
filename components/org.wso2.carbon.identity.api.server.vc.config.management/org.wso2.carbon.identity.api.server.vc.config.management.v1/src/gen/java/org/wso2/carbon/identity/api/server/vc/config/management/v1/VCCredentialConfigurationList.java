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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCCredentialConfigurationList  {
  
    private Integer totalResults;
    private List<VCCredentialConfigurationListItem> vcCredentialConfigurations = null;


    /**
    **/
    public VCCredentialConfigurationList totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    **/
    public VCCredentialConfigurationList vcCredentialConfigurations(List<VCCredentialConfigurationListItem> vcCredentialConfigurations) {

        this.vcCredentialConfigurations = vcCredentialConfigurations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("VCCredentialConfigurations")
    @Valid
    public List<VCCredentialConfigurationListItem> getVcCredentialConfigurations() {
        return vcCredentialConfigurations;
    }
    public void setVcCredentialConfigurations(List<VCCredentialConfigurationListItem> vcCredentialConfigurations) {
        this.vcCredentialConfigurations = vcCredentialConfigurations;
    }

    public VCCredentialConfigurationList addVcCredentialConfigurationsItem(VCCredentialConfigurationListItem vcCredentialConfigurationsItem) {
        if (this.vcCredentialConfigurations == null) {
            this.vcCredentialConfigurations = new ArrayList<VCCredentialConfigurationListItem>();
        }
        this.vcCredentialConfigurations.add(vcCredentialConfigurationsItem);
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
        VCCredentialConfigurationList vcCredentialConfigurationList = (VCCredentialConfigurationList) o;
        return Objects.equals(this.totalResults, vcCredentialConfigurationList.totalResults) &&
            Objects.equals(this.vcCredentialConfigurations, vcCredentialConfigurationList.vcCredentialConfigurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, vcCredentialConfigurations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfigurationList {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    vcCredentialConfigurations: ").append(toIndentedString(vcCredentialConfigurations)).append("\n");
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


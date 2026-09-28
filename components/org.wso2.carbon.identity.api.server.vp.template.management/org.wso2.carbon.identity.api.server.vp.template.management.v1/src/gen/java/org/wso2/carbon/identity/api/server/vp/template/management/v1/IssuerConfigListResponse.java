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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.IssuerConfigModel;
import javax.validation.constraints.*;

/**
 * List of trusted issuer configurations for a credential.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "List of trusted issuer configurations for a credential.")
public class IssuerConfigListResponse  {
  
    private List<IssuerConfigModel> issuerConfigs = null;


    /**
    * The issuer configurations.
    **/
    public IssuerConfigListResponse issuerConfigs(List<IssuerConfigModel> issuerConfigs) {

        this.issuerConfigs = issuerConfigs;
        return this;
    }
    
    @ApiModelProperty(value = "The issuer configurations.")
    @JsonProperty("issuerConfigs")
    @Valid
    public List<IssuerConfigModel> getIssuerConfigs() {
        return issuerConfigs;
    }
    public void setIssuerConfigs(List<IssuerConfigModel> issuerConfigs) {
        this.issuerConfigs = issuerConfigs;
    }

    public IssuerConfigListResponse addIssuerConfigsItem(IssuerConfigModel issuerConfigsItem) {
        if (this.issuerConfigs == null) {
            this.issuerConfigs = new ArrayList<>();
        }
        this.issuerConfigs.add(issuerConfigsItem);
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
        IssuerConfigListResponse issuerConfigListResponse = (IssuerConfigListResponse) o;
        return Objects.equals(this.issuerConfigs, issuerConfigListResponse.issuerConfigs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuerConfigs);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IssuerConfigListResponse {\n");
        
        sb.append("    issuerConfigs: ").append(toIndentedString(issuerConfigs)).append("\n");
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


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

package org.wso2.carbon.identity.api.server.idv.provider.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdVProviderListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<IdVProviderResponse> identityVerificationProviders = null;


    /**
    **/
    public IdVProviderListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
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
    public IdVProviderListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    **/
    public IdVProviderListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    **/
    public IdVProviderListResponse identityVerificationProviders(List<IdVProviderResponse> identityVerificationProviders) {

        this.identityVerificationProviders = identityVerificationProviders;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("identityVerificationProviders")
    @Valid
    public List<IdVProviderResponse> getIdentityVerificationProviders() {
        return identityVerificationProviders;
    }
    public void setIdentityVerificationProviders(List<IdVProviderResponse> identityVerificationProviders) {
        this.identityVerificationProviders = identityVerificationProviders;
    }

    public IdVProviderListResponse addIdentityVerificationProvidersItem(IdVProviderResponse identityVerificationProvidersItem) {
        if (this.identityVerificationProviders == null) {
            this.identityVerificationProviders = new ArrayList<>();
        }
        this.identityVerificationProviders.add(identityVerificationProvidersItem);
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
        IdVProviderListResponse idVProviderListResponse = (IdVProviderListResponse) o;
        return Objects.equals(this.totalResults, idVProviderListResponse.totalResults) &&
            Objects.equals(this.startIndex, idVProviderListResponse.startIndex) &&
            Objects.equals(this.count, idVProviderListResponse.count) &&
            Objects.equals(this.identityVerificationProviders, idVProviderListResponse.identityVerificationProviders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, identityVerificationProviders);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdVProviderListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    identityVerificationProviders: ").append(toIndentedString(identityVerificationProviders)).append("\n");
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


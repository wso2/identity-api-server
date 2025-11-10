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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCOfferList  {
  
    private Integer totalResults;
    private List<VCOfferListItem> vcOffers = null;


    /**
    **/
    public VCOfferList totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public VCOfferList vcOffers(List<VCOfferListItem> vcOffers) {

        this.vcOffers = vcOffers;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("VCOffers")
    @Valid
    public List<VCOfferListItem> getVcOffers() {
        return vcOffers;
    }
    public void setVcOffers(List<VCOfferListItem> vcOffers) {
        this.vcOffers = vcOffers;
    }

    public VCOfferList addVcOffersItem(VCOfferListItem vcOffersItem) {
        if (this.vcOffers == null) {
            this.vcOffers = new ArrayList<VCOfferListItem>();
        }
        this.vcOffers.add(vcOffersItem);
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
        VCOfferList vcOfferList = (VCOfferList) o;
        return Objects.equals(this.totalResults, vcOfferList.totalResults) &&
            Objects.equals(this.vcOffers, vcOfferList.vcOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, vcOffers);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCOfferList {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    vcOffers: ").append(toIndentedString(vcOffers)).append("\n");
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


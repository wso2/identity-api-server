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

package org.wso2.carbon.identity.api.server.vc.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCTemplateList  {
  
    private Integer totalResults;
    private List<PaginationLink> links = null;

    private List<VCTemplateListItem> vcTemplates = null;


    /**
    **/
    public VCTemplateList totalResults(Integer totalResults) {

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
    public VCTemplateList links(List<PaginationLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("links")
    @Valid
    public List<PaginationLink> getLinks() {
        return links;
    }
    public void setLinks(List<PaginationLink> links) {
        this.links = links;
    }

    public VCTemplateList addLinksItem(PaginationLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<PaginationLink>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public VCTemplateList vcTemplates(List<VCTemplateListItem> vcTemplates) {

        this.vcTemplates = vcTemplates;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("VCTemplates")
    @Valid
    public List<VCTemplateListItem> getVcTemplates() {
        return vcTemplates;
    }
    public void setVcTemplates(List<VCTemplateListItem> vcTemplates) {
        this.vcTemplates = vcTemplates;
    }

    public VCTemplateList addVcTemplatesItem(VCTemplateListItem vcTemplatesItem) {
        if (this.vcTemplates == null) {
            this.vcTemplates = new ArrayList<VCTemplateListItem>();
        }
        this.vcTemplates.add(vcTemplatesItem);
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
        VCTemplateList vcTemplateList = (VCTemplateList) o;
        return Objects.equals(this.totalResults, vcTemplateList.totalResults) &&
            Objects.equals(this.links, vcTemplateList.links) &&
            Objects.equals(this.vcTemplates, vcTemplateList.vcTemplates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, links, vcTemplates);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCTemplateList {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    vcTemplates: ").append(toIndentedString(vcTemplates)).append("\n");
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


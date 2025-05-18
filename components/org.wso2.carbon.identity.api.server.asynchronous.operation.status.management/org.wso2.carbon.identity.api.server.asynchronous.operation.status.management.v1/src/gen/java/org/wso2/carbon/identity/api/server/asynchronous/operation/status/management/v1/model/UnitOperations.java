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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperation;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UnitOperations  {
  
    private List<Link> links = null;

    private List<UnitOperation> unitOperations = null;


    /**
    **/
    public UnitOperations links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/api/server/v1/async-operations/f4485783-20c9-4acb-9598-a61d5206ece7/unit-operations?limit=1&after=MjAyNS0wNC0yMSAxMDo1MDoyMi41MTU=\",\"rel\":\"next\"},{\"href\":\"/api/server/v1/async-operations/f4485783-20c9-4acb-9598-a61d5206ece7/unit-operations?limit=1&after=MjAyNS0wNC0yMSAxMDo1MDoyMi41MTU=\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public UnitOperations addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public UnitOperations unitOperations(List<UnitOperation> unitOperations) {

        this.unitOperations = unitOperations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("unitOperations")
    @Valid
    public List<UnitOperation> getUnitOperations() {
        return unitOperations;
    }
    public void setUnitOperations(List<UnitOperation> unitOperations) {
        this.unitOperations = unitOperations;
    }

    public UnitOperations addUnitOperationsItem(UnitOperation unitOperationsItem) {
        if (this.unitOperations == null) {
            this.unitOperations = new ArrayList<>();
        }
        this.unitOperations.add(unitOperationsItem);
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
        UnitOperations unitOperations = (UnitOperations) o;
        return Objects.equals(this.links, unitOperations.links) &&
            Objects.equals(this.unitOperations, unitOperations.unitOperations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, unitOperations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UnitOperations {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    unitOperations: ").append(toIndentedString(unitOperations)).append("\n");
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


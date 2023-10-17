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

public class AssociationResponse  {
  
    private Boolean isEnabled;
    private List<String> lookupAttribute = null;


    /**
    **/
    public AssociationResponse isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
    **/
    public AssociationResponse lookupAttribute(List<String> lookupAttribute) {

        this.lookupAttribute = lookupAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "[\"email\"]", value = "")
    @JsonProperty("lookupAttribute")
    @Valid
    public List<String> getLookupAttribute() {
        return lookupAttribute;
    }
    public void setLookupAttribute(List<String> lookupAttribute) {
        this.lookupAttribute = lookupAttribute;
    }

    public AssociationResponse addLookupAttributeItem(String lookupAttributeItem) {
        if (this.lookupAttribute == null) {
            this.lookupAttribute = new ArrayList<>();
        }
        this.lookupAttribute.add(lookupAttributeItem);
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
        AssociationResponse associationResponse = (AssociationResponse) o;
        return Objects.equals(this.isEnabled, associationResponse.isEnabled) &&
            Objects.equals(this.lookupAttribute, associationResponse.lookupAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEnabled, lookupAttribute);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AssociationResponse {\n");
        
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    lookupAttribute: ").append(toIndentedString(lookupAttribute)).append("\n");
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


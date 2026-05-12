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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementTerminationInfo;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentPurposeBinding  {
  
    private String id;
    private List<ElementTerminationInfo> elements = new ArrayList<>();


    /**
    * ID of the purpose (will use latest version)
    **/
    public ConsentPurposeBinding id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", required = true, value = "ID of the purpose (will use latest version)")
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
    * Consent elements for this purpose
    **/
    public ConsentPurposeBinding elements(List<ElementTerminationInfo> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Consent elements for this purpose")
    @JsonProperty("elements")
    @Valid
    @NotNull(message = "Property elements cannot be null.")
 @Size(min=1)
    public List<ElementTerminationInfo> getElements() {
        return elements;
    }
    public void setElements(List<ElementTerminationInfo> elements) {
        this.elements = elements;
    }

    public ConsentPurposeBinding addElementsItem(ElementTerminationInfo elementsItem) {
        this.elements.add(elementsItem);
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
        ConsentPurposeBinding consentPurposeBinding = (ConsentPurposeBinding) o;
        return Objects.equals(this.id, consentPurposeBinding.id) &&
            Objects.equals(this.elements, consentPurposeBinding.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, elements);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentPurposeBinding {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
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


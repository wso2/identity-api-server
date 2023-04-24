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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * The authentication attributes required by a user registrant.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The authentication attributes required by a user registrant.")
public class AuthAttribute  {
  
    private String attribute;
    private Boolean isClaim;
    private Boolean isConfidential;
    private String attributeType;

    /**
    * Name of the attribute.
    **/
    public AuthAttribute attribute(String attribute) {

        this.attribute = attribute;
        return this;
    }
    
    @ApiModelProperty(example = "password", value = "Name of the attribute.")
    @JsonProperty("attribute")
    @Valid
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
    * Defines whether the attribute is a claim.
    **/
    public AuthAttribute isClaim(Boolean isClaim) {

        this.isClaim = isClaim;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Defines whether the attribute is a claim.")
    @JsonProperty("isClaim")
    @Valid
    public Boolean getIsClaim() {
        return isClaim;
    }
    public void setIsClaim(Boolean isClaim) {
        this.isClaim = isClaim;
    }

    /**
    * Defines whether the attribute contains confidential data.
    **/
    public AuthAttribute isConfidential(Boolean isConfidential) {

        this.isConfidential = isConfidential;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Defines whether the attribute contains confidential data.")
    @JsonProperty("isConfidential")
    @Valid
    public Boolean getIsConfidential() {
        return isConfidential;
    }
    public void setIsConfidential(Boolean isConfidential) {
        this.isConfidential = isConfidential;
    }

    /**
    * Data type of value of the attribute.
    **/
    public AuthAttribute attributeType(String attributeType) {

        this.attributeType = attributeType;
        return this;
    }
    
    @ApiModelProperty(example = "STRING", value = "Data type of value of the attribute.")
    @JsonProperty("attributeType")
    @Valid
    public String getAttributeType() {
        return attributeType;
    }
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthAttribute authAttribute = (AuthAttribute) o;
        return Objects.equals(this.attribute, authAttribute.attribute) &&
            Objects.equals(this.isClaim, authAttribute.isClaim) &&
            Objects.equals(this.isConfidential, authAttribute.isConfidential) &&
            Objects.equals(this.attributeType, authAttribute.attributeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, isClaim, isConfidential, attributeType);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthAttribute {\n");
        
        sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
        sb.append("    isClaim: ").append(toIndentedString(isClaim)).append("\n");
        sb.append("    isConfidential: ").append(toIndentedString(isConfidential)).append("\n");
        sb.append("    attributeType: ").append(toIndentedString(attributeType)).append("\n");
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


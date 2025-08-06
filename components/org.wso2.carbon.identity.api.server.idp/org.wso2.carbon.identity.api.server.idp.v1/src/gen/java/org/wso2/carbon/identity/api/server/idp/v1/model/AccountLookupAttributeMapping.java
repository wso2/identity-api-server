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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AccountLookupAttributeMapping  {
  
    private String localAttribute;
    private String federatedAttribute;

    /**
    **/
    public AccountLookupAttributeMapping localAttribute(String localAttribute) {

        this.localAttribute = localAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/emailaddress", value = "")
    @JsonProperty("localAttribute")
    @Valid
    public String getLocalAttribute() {
        return localAttribute;
    }
    public void setLocalAttribute(String localAttribute) {
        this.localAttribute = localAttribute;
    }

    /**
    **/
    public AccountLookupAttributeMapping federatedAttribute(String federatedAttribute) {

        this.federatedAttribute = federatedAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "email", value = "")
    @JsonProperty("federatedAttribute")
    @Valid
    public String getFederatedAttribute() {
        return federatedAttribute;
    }
    public void setFederatedAttribute(String federatedAttribute) {
        this.federatedAttribute = federatedAttribute;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountLookupAttributeMapping accountLookupAttributeMapping = (AccountLookupAttributeMapping) o;
        return Objects.equals(this.localAttribute, accountLookupAttributeMapping.localAttribute) &&
            Objects.equals(this.federatedAttribute, accountLookupAttributeMapping.federatedAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localAttribute, federatedAttribute);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccountLookupAttributeMapping {\n");
        
        sb.append("    localAttribute: ").append(toIndentedString(localAttribute)).append("\n");
        sb.append("    federatedAttribute: ").append(toIndentedString(federatedAttribute)).append("\n");
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

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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleGetResponseUser  {
  
    private String $ref;
    private String display;
    private String value;
    private String orgId;
    private String orgName;

    /**
    **/
    public RoleGetResponseUser $ref(String $ref) {

        this.$ref = $ref;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/o/e5198bbf-7448-49c9-b5f1-44ae2fa39bb6/scim2/Users/f78b1b05-dea5-4572-99c8-4dc649887c20", value = "")
    @JsonProperty("$ref")
    @Valid
    public String get$Ref() {
        return $ref;
    }
    public void set$Ref(String $ref) {
        this.$ref = $ref;
    }

    /**
    **/
    public RoleGetResponseUser display(String display) {

        this.display = display;
        return this;
    }
    
    @ApiModelProperty(example = "kim", value = "")
    @JsonProperty("display")
    @Valid
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
    **/
    public RoleGetResponseUser value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "3a12bae9-4386-44be-befd-caf349297f45", value = "")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /**
    **/
    public RoleGetResponseUser orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "48e31bc5-1669-4de1-bb22-c71e443aeb8b", value = "")
    @JsonProperty("orgId")
    @Valid
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    **/
    public RoleGetResponseUser orgName(String orgName) {

        this.orgName = orgName;
        return this;
    }
    
    @ApiModelProperty(example = "SubOrganization", value = "")
    @JsonProperty("orgName")
    @Valid
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleGetResponseUser roleGetResponseUser = (RoleGetResponseUser) o;
        return Objects.equals(this.$ref, roleGetResponseUser.$ref) &&
            Objects.equals(this.display, roleGetResponseUser.display) &&
            Objects.equals(this.value, roleGetResponseUser.value) &&
            Objects.equals(this.orgId, roleGetResponseUser.orgId) &&
            Objects.equals(this.orgName, roleGetResponseUser.orgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash($ref, display, value, orgId, orgName);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleGetResponseUser {\n");
        
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("    display: ").append(toIndentedString(display)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
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


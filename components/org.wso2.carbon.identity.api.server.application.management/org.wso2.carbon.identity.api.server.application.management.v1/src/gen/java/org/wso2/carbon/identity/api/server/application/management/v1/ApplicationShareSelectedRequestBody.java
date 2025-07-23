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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.OrgShareConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationShareSelectedRequestBody  {
  
    private String applicationId;
    private List<OrgShareConfig> organizations = new ArrayList<>();


    /**
    **/
    public ApplicationShareSelectedRequestBody applicationId(String applicationId) {

        this.applicationId = applicationId;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("applicationId")
    @Valid
    @NotNull(message = "Property applicationId cannot be null.")

    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
    **/
    public ApplicationShareSelectedRequestBody organizations(List<OrgShareConfig> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("organizations")
    @Valid
    @NotNull(message = "Property organizations cannot be null.")

    public List<OrgShareConfig> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<OrgShareConfig> organizations) {
        this.organizations = organizations;
    }

    public ApplicationShareSelectedRequestBody addOrganizationsItem(OrgShareConfig organizationsItem) {
        this.organizations.add(organizationsItem);
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
        ApplicationShareSelectedRequestBody applicationShareSelectedRequestBody = (ApplicationShareSelectedRequestBody) o;
        return Objects.equals(this.applicationId, applicationShareSelectedRequestBody.applicationId) &&
            Objects.equals(this.organizations, applicationShareSelectedRequestBody.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationShareSelectedRequestBody {\n");
        
        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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


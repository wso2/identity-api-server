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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationUnshareSelectedRequestBody  {
  
    private String applicationId;
    private List<String> orgIds = new ArrayList<>();


    /**
    **/
    public ApplicationUnshareSelectedRequestBody applicationId(String applicationId) {

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
    public ApplicationUnshareSelectedRequestBody orgIds(List<String> orgIds) {

        this.orgIds = orgIds;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("orgIds")
    @Valid
    @NotNull(message = "Property orgIds cannot be null.")

    public List<String> getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public ApplicationUnshareSelectedRequestBody addOrgIdsItem(String orgIdsItem) {
        this.orgIds.add(orgIdsItem);
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
        ApplicationUnshareSelectedRequestBody applicationUnshareSelectedRequestBody = (ApplicationUnshareSelectedRequestBody) o;
        return Objects.equals(this.applicationId, applicationUnshareSelectedRequestBody.applicationId) &&
            Objects.equals(this.orgIds, applicationUnshareSelectedRequestBody.orgIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, orgIds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationUnshareSelectedRequestBody {\n");
        
        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    orgIds: ").append(toIndentedString(orgIds)).append("\n");
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


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

package org.wso2.carbon.identity.api.server.organization.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SharedApplicationResponse  {
  
    private String applicationId;
    private String organizationId;

    /**
    * Shared application&#39;s id.
    **/
    public SharedApplicationResponse applicationId(String applicationId) {

        this.applicationId = applicationId;
        return this;
    }
    
    @ApiModelProperty(example = "ca322554-fe79-4c04-9c94-492855ef92a3", value = "Shared application's id.")
    @JsonProperty("applicationId")
    @Valid
    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
    * Shared application residing organization id.
    **/
    public SharedApplicationResponse organizationId(String organizationId) {

        this.organizationId = organizationId;
        return this;
    }
    
    @ApiModelProperty(example = "682edf68-4835-4bb8-961f-0a16bc6cc866", value = "Shared application residing organization id.")
    @JsonProperty("organizationId")
    @Valid
    public String getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SharedApplicationResponse sharedApplicationResponse = (SharedApplicationResponse) o;
        return Objects.equals(this.applicationId, sharedApplicationResponse.applicationId) &&
            Objects.equals(this.organizationId, sharedApplicationResponse.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, organizationId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SharedApplicationResponse {\n");
        
        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
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


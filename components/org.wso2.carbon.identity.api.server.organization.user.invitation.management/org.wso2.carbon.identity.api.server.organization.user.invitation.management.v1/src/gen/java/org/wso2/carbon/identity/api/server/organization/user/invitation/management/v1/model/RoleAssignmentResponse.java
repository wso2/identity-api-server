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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.Audience;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleAssignmentResponse  {
  
    private String displayName;
    private String id;
    private List<Audience> audience = null;


    /**
    **/
    public RoleAssignmentResponse displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "loginRole", value = "")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public RoleAssignmentResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "4645709c-ea8c-4495-8590-e1fa0efe3de0", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public RoleAssignmentResponse audience(List<Audience> audience) {

        this.audience = audience;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("audience")
    @Valid
    public List<Audience> getAudience() {
        return audience;
    }
    public void setAudience(List<Audience> audience) {
        this.audience = audience;
    }

    public RoleAssignmentResponse addAudienceItem(Audience audienceItem) {
        if (this.audience == null) {
            this.audience = new ArrayList<>();
        }
        this.audience.add(audienceItem);
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
        RoleAssignmentResponse roleAssignmentResponse = (RoleAssignmentResponse) o;
        return Objects.equals(this.displayName, roleAssignmentResponse.displayName) &&
            Objects.equals(this.id, roleAssignmentResponse.id) &&
            Objects.equals(this.audience, roleAssignmentResponse.audience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, id, audience);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleAssignmentResponse {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
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


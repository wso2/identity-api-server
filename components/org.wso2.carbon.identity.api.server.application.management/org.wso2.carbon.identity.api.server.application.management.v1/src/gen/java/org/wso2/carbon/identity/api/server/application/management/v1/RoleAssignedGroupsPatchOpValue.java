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


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleAssignedGroupsPatchOpValue  {
  
    private String idpId;
    private String groupId;

    /**
    **/
    public RoleAssignedGroupsPatchOpValue idpId(String idpId) {

        this.idpId = idpId;
        return this;
    }
    
    @ApiModelProperty(example = "e44dbc52-dcc3-443d-96f5-fe9dc208e9d8", value = "")
    @JsonProperty("idpId")
    @Valid
    public String getIdpId() {
        return idpId;
    }
    public void setIdpId(String idpId) {
        this.idpId = idpId;
    }

    /**
    **/
    public RoleAssignedGroupsPatchOpValue groupId(String groupId) {

        this.groupId = groupId;
        return this;
    }
    
    @ApiModelProperty(example = "e44dbc52-dcc3-443d-96f5-fe9dc208e9d8", value = "")
    @JsonProperty("groupId")
    @Valid
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleAssignedGroupsPatchOpValue roleAssignedGroupsPatchOpValue = (RoleAssignedGroupsPatchOpValue) o;
        return Objects.equals(this.idpId, roleAssignedGroupsPatchOpValue.idpId) &&
            Objects.equals(this.groupId, roleAssignedGroupsPatchOpValue.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idpId, groupId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleAssignedGroupsPatchOpValue {\n");
        
        sb.append("    idpId: ").append(toIndentedString(idpId)).append("\n");
        sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
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


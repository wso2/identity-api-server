/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

public class DiscoverableGroupModel  {
  
    private String userStore;
    private List<String> groups = null;


    /**
    * ID of the user store the groups belong to
    **/
    public DiscoverableGroupModel userStore(String userStore) {

        this.userStore = userStore;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the user store the groups belong to")
    @JsonProperty("userStore")
    @Valid
    public String getUserStore() {
        return userStore;
    }
    public void setUserStore(String userStore) {
        this.userStore = userStore;
    }

    /**
    * List of group IDs configured for discoverability
    **/
    public DiscoverableGroupModel groups(List<String> groups) {

        this.groups = groups;
        return this;
    }
    
    @ApiModelProperty(value = "List of group IDs configured for discoverability")
    @JsonProperty("groups")
    @Valid
    public List<String> getGroups() {
        return groups;
    }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public DiscoverableGroupModel addGroupsItem(String groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(groupsItem);
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
        DiscoverableGroupModel discoverableGroupModel = (DiscoverableGroupModel) o;
        return Objects.equals(this.userStore, discoverableGroupModel.userStore) &&
            Objects.equals(this.groups, discoverableGroupModel.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userStore, groups);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DiscoverableGroupModel {\n");
        
        sb.append("    userStore: ").append(toIndentedString(userStore)).append("\n");
        sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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


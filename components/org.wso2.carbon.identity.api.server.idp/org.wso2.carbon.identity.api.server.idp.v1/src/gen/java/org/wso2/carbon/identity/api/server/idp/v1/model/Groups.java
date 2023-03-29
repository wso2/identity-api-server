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

package org.wso2.carbon.identity.api.server.idp.v1.model;

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

public class Groups  {
  
    private List<String> groupsList = null;


    /**
    **/
    public Groups groupsList(List<String> groupsList) {

        this.groupsList = groupsList;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("groupsList")
    @Valid
    public List<String> getGroupsList() {
        return groupsList;
    }
    public void setGroupsList(List<String> groupsList) {
        this.groupsList = groupsList;
    }

    public Groups addGroupsListItem(String groupsListItem) {
        if (this.groupsList == null) {
            this.groupsList = new ArrayList<>();
        }
        this.groupsList.add(groupsListItem);
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
        Groups groups = (Groups) o;
        return Objects.equals(this.groupsList, groups.groupsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupsList);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Groups {\n");
        
        sb.append("    groupsList: ").append(toIndentedString(groupsList)).append("\n");
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


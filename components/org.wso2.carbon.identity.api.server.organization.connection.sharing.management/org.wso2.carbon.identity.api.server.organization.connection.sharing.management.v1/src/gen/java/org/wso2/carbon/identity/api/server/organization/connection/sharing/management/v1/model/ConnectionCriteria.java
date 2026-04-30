/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * Criteria for selecting the connections to share or unshare.  At least one of &#x60;connectionIds&#x60; or &#x60;connectionNames&#x60; must be provided. Both can be supplied simultaneously, in which case the union of matched connections is used. Validation that at least one field is populated is enforced at the service layer.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Criteria for selecting the connections to share or unshare.  At least one of `connectionIds` or `connectionNames` must be provided. Both can be supplied simultaneously, in which case the union of matched connections is used. Validation that at least one field is populated is enforced at the service layer.")
public class ConnectionCriteria  {
  
    private List<String> connectionIds = null;

    private List<String> connectionNames = null;


    /**
    * List of connection UUIDs.
    **/
    public ConnectionCriteria connectionIds(List<String> connectionIds) {

        this.connectionIds = connectionIds;
        return this;
    }
    
    @ApiModelProperty(example = "[\"7a1b7d63-8cfc-4dc9-9332-3f84641b72d8\",\"5d2a1c84-9f7a-43cd-b12e-6e52d7f87e16\"]", value = "List of connection UUIDs.")
    @JsonProperty("connectionIds")
    @Valid
    public List<String> getConnectionIds() {
        return connectionIds;
    }
    public void setConnectionIds(List<String> connectionIds) {
        this.connectionIds = connectionIds;
    }

    public ConnectionCriteria addConnectionIdsItem(String connectionIdsItem) {
        if (this.connectionIds == null) {
            this.connectionIds = new ArrayList<>();
        }
        this.connectionIds.add(connectionIdsItem);
        return this;
    }

        /**
    * List of connection names.
    **/
    public ConnectionCriteria connectionNames(List<String> connectionNames) {

        this.connectionNames = connectionNames;
        return this;
    }
    
    @ApiModelProperty(example = "[\"Google\",\"Facebook\"]", value = "List of connection names.")
    @JsonProperty("connectionNames")
    @Valid
    public List<String> getConnectionNames() {
        return connectionNames;
    }
    public void setConnectionNames(List<String> connectionNames) {
        this.connectionNames = connectionNames;
    }

    public ConnectionCriteria addConnectionNamesItem(String connectionNamesItem) {
        if (this.connectionNames == null) {
            this.connectionNames = new ArrayList<>();
        }
        this.connectionNames.add(connectionNamesItem);
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
        ConnectionCriteria connectionCriteria = (ConnectionCriteria) o;
        return Objects.equals(this.connectionIds, connectionCriteria.connectionIds) &&
            Objects.equals(this.connectionNames, connectionCriteria.connectionNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionIds, connectionNames);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionCriteria {\n");
        
        sb.append("    connectionIds: ").append(toIndentedString(connectionIds)).append("\n");
        sb.append("    connectionNames: ").append(toIndentedString(connectionNames)).append("\n");
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


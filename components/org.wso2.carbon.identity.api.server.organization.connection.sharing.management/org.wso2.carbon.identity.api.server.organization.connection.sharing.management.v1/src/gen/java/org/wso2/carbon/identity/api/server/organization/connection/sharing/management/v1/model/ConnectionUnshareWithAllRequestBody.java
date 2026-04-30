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
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionCriteria;
import javax.validation.constraints.*;

/**
 * Request body for unsharing connections from **all child organizations**.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for unsharing connections from **all child organizations**.")
public class ConnectionUnshareWithAllRequestBody  {
  
    private ConnectionCriteria connectionCriteria;

    /**
    **/
    public ConnectionUnshareWithAllRequestBody connectionCriteria(ConnectionCriteria connectionCriteria) {

        this.connectionCriteria = connectionCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("connectionCriteria")
    @Valid
    @NotNull(message = "Property connectionCriteria cannot be null.")

    public ConnectionCriteria getConnectionCriteria() {
        return connectionCriteria;
    }
    public void setConnectionCriteria(ConnectionCriteria connectionCriteria) {
        this.connectionCriteria = connectionCriteria;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectionUnshareWithAllRequestBody connectionUnshareWithAllRequestBody = (ConnectionUnshareWithAllRequestBody) o;
        return Objects.equals(this.connectionCriteria, connectionUnshareWithAllRequestBody.connectionCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionCriteria);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionUnshareWithAllRequestBody {\n");
        
        sb.append("    connectionCriteria: ").append(toIndentedString(connectionCriteria)).append("\n");
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


/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConnectionEstablishedResponse  {
  
    private Boolean connection;

    /**
    **/
    public ConnectionEstablishedResponse connection(Boolean connection) {

        this.connection = connection;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("connection")
    @Valid
    public Boolean getConnection() {
        return connection;
    }
    public void setConnection(Boolean connection) {
        this.connection = connection;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectionEstablishedResponse connectionEstablishedResponse = (ConnectionEstablishedResponse) o;
        return Objects.equals(this.connection, connectionEstablishedResponse.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionEstablishedResponse {\n");
        
        sb.append("    connection: ").append(toIndentedString(connection)).append("\n");
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


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

/**
 * RDBMS Connection Request.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "RDBMS Connection Request.")
public class RDBMSConnectionReq  {
  
    private String domain;
    private String driverName;
    private String connectionURL;
    private String username;
    private String connectionPassword;

    /**
    * User store domain name.
    **/
    public RDBMSConnectionReq domain(String domain) {

        this.domain = domain;
        return this;
    }
    
    @ApiModelProperty(example = "PRIMARY", value = "User store domain name.")
    @JsonProperty("domain")
    @Valid
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
    * Driver Name.
    **/
    public RDBMSConnectionReq driverName(String driverName) {

        this.driverName = driverName;
        return this;
    }
    
    @ApiModelProperty(example = "com.mysql.jdbc.Driver", required = true, value = "Driver Name.")
    @JsonProperty("driverName")
    @Valid
    @NotNull(message = "Property driverName cannot be null.")

    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
    * The Connection URL.
    **/
    public RDBMSConnectionReq connectionURL(String connectionURL) {

        this.connectionURL = connectionURL;
        return this;
    }
    
    @ApiModelProperty(example = "jdbc:mysql://192.168.48.154:3306/test", required = true, value = "The Connection URL.")
    @JsonProperty("connectionURL")
    @Valid
    @NotNull(message = "Property connectionURL cannot be null.")

    public String getConnectionURL() {
        return connectionURL;
    }
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    /**
    * The username.
    **/
    public RDBMSConnectionReq username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "root", required = true, value = "The username.")
    @JsonProperty("username")
    @Valid
    @NotNull(message = "Property username cannot be null.")

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * The password.
    **/
    public RDBMSConnectionReq connectionPassword(String connectionPassword) {

        this.connectionPassword = connectionPassword;
        return this;
    }
    
    @ApiModelProperty(example = "root", required = true, value = "The password.")
    @JsonProperty("connectionPassword")
    @Valid
    @NotNull(message = "Property connectionPassword cannot be null.")

    public String getConnectionPassword() {
        return connectionPassword;
    }
    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RDBMSConnectionReq rdBMSConnectionReq = (RDBMSConnectionReq) o;
        return Objects.equals(this.domain, rdBMSConnectionReq.domain) &&
            Objects.equals(this.driverName, rdBMSConnectionReq.driverName) &&
            Objects.equals(this.connectionURL, rdBMSConnectionReq.connectionURL) &&
            Objects.equals(this.username, rdBMSConnectionReq.username) &&
            Objects.equals(this.connectionPassword, rdBMSConnectionReq.connectionPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, driverName, connectionURL, username, connectionPassword);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RDBMSConnectionReq {\n");
        
        sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
        sb.append("    driverName: ").append(toIndentedString(driverName)).append("\n");
        sb.append("    connectionURL: ").append(toIndentedString(connectionURL)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    connectionPassword: ").append(toIndentedString(connectionPassword)).append("\n");
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


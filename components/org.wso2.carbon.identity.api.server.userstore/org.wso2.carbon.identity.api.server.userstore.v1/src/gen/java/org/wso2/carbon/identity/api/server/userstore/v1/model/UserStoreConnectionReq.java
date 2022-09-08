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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Property;
import javax.validation.constraints.*;

/**
 * User Store Connection Request.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "User Store Connection Request.")
public class UserStoreConnectionReq  {
  
    private String typeId;
    private String domain;
    private String driverName;
    private String connectionURL;
    private String username;
    private String connectionPassword;
    private List<Property> properties = null;


    /**
    * The id of the user store manager class type.
    **/
    public UserStoreConnectionReq typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }
    
    @ApiModelProperty(example = "VW5pcXVlSURKREJDVXNlclN0b3JlTWFuYWdlcg", value = "The id of the user store manager class type.")
    @JsonProperty("typeId")
    @Valid
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
    * User store domain name.
    **/
    public UserStoreConnectionReq domain(String domain) {

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
    public UserStoreConnectionReq driverName(String driverName) {

        this.driverName = driverName;
        return this;
    }
    
    @ApiModelProperty(example = "com.mysql.jdbc.Driver", value = "Driver Name.")
    @JsonProperty("driverName")
    @Valid
    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
    * The Connection URL.
    **/
    public UserStoreConnectionReq connectionURL(String connectionURL) {

        this.connectionURL = connectionURL;
        return this;
    }
    
    @ApiModelProperty(example = "jdbc:mysql://192.168.48.154:3306/test", value = "The Connection URL.")
    @JsonProperty("connectionURL")
    @Valid
    public String getConnectionURL() {
        return connectionURL;
    }
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    /**
    * The username.
    **/
    public UserStoreConnectionReq username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "root", value = "The username.")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * The password.
    **/
    public UserStoreConnectionReq connectionPassword(String connectionPassword) {

        this.connectionPassword = connectionPassword;
        return this;
    }
    
    @ApiModelProperty(example = "root", value = "The password.")
    @JsonProperty("connectionPassword")
    @Valid
    public String getConnectionPassword() {
        return connectionPassword;
    }
    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }

    /**
    * Properties related to the user store.
    **/
    public UserStoreConnectionReq properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Properties related to the user store.")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public UserStoreConnectionReq addPropertiesItem(Property propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
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
        UserStoreConnectionReq userStoreConnectionReq = (UserStoreConnectionReq) o;
        return Objects.equals(this.typeId, userStoreConnectionReq.typeId) &&
            Objects.equals(this.domain, userStoreConnectionReq.domain) &&
            Objects.equals(this.driverName, userStoreConnectionReq.driverName) &&
            Objects.equals(this.connectionURL, userStoreConnectionReq.connectionURL) &&
            Objects.equals(this.username, userStoreConnectionReq.username) &&
            Objects.equals(this.connectionPassword, userStoreConnectionReq.connectionPassword) &&
            Objects.equals(this.properties, userStoreConnectionReq.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, domain, driverName, connectionURL, username, connectionPassword, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreConnectionReq {\n");
        
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
        sb.append("    driverName: ").append(toIndentedString(driverName)).append("\n");
        sb.append("    connectionURL: ").append(toIndentedString(connectionURL)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    connectionPassword: ").append(toIndentedString(connectionPassword)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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


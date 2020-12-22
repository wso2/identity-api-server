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

package org.wso2.carbon.identity.api.server.notification.sender.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Properties;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class EmailSenderAdd  {
  
    private String name;
    private String smtpServerHost;
    private Integer smtpPort;
    private String fromAddress;
    private String userName;
    private String password;
    private List<Properties> properties = null;


    /**
    **/
    public EmailSenderAdd name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public EmailSenderAdd smtpServerHost(String smtpServerHost) {

        this.smtpServerHost = smtpServerHost;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("smtpServerHost")
    @Valid
    public String getSmtpServerHost() {
        return smtpServerHost;
    }
    public void setSmtpServerHost(String smtpServerHost) {
        this.smtpServerHost = smtpServerHost;
    }

    /**
    **/
    public EmailSenderAdd smtpPort(Integer smtpPort) {

        this.smtpPort = smtpPort;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("smtpPort")
    @Valid
    public Integer getSmtpPort() {
        return smtpPort;
    }
    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
    **/
    public EmailSenderAdd fromAddress(String fromAddress) {

        this.fromAddress = fromAddress;
        return this;
    }
    
    @ApiModelProperty(example = "iam@gmail.com", required = true, value = "")
    @JsonProperty("fromAddress")
    @Valid
    @NotNull(message = "Property fromAddress cannot be null.")

    public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
    **/
    public EmailSenderAdd userName(String userName) {

        this.userName = userName;
        return this;
    }
    
    @ApiModelProperty(example = "iam", required = true, value = "")
    @JsonProperty("userName")
    @Valid
    @NotNull(message = "Property userName cannot be null.")

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
    **/
    public EmailSenderAdd password(String password) {

        this.password = password;
        return this;
    }
    
    @ApiModelProperty(example = "iam123", required = true, value = "")
    @JsonProperty("password")
    @Valid
    @NotNull(message = "Property password cannot be null.")

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    **/
    public EmailSenderAdd properties(List<Properties> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"key\":\"body.scope\",\"value\":\"true\"},{\"key\":\"mail.smtp.starttls.enable\",\"value\":true}]", value = "")
    @JsonProperty("properties")
    @Valid
    public List<Properties> getProperties() {
        return properties;
    }
    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

    public EmailSenderAdd addPropertiesItem(Properties propertiesItem) {
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
        EmailSenderAdd emailSenderAdd = (EmailSenderAdd) o;
        return Objects.equals(this.name, emailSenderAdd.name) &&
            Objects.equals(this.smtpServerHost, emailSenderAdd.smtpServerHost) &&
            Objects.equals(this.smtpPort, emailSenderAdd.smtpPort) &&
            Objects.equals(this.fromAddress, emailSenderAdd.fromAddress) &&
            Objects.equals(this.userName, emailSenderAdd.userName) &&
            Objects.equals(this.password, emailSenderAdd.password) &&
            Objects.equals(this.properties, emailSenderAdd.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, smtpServerHost, smtpPort, fromAddress, userName, password, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EmailSenderAdd {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    smtpServerHost: ").append(toIndentedString(smtpServerHost)).append("\n");
        sb.append("    smtpPort: ").append(toIndentedString(smtpPort)).append("\n");
        sb.append("    fromAddress: ").append(toIndentedString(fromAddress)).append("\n");
        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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


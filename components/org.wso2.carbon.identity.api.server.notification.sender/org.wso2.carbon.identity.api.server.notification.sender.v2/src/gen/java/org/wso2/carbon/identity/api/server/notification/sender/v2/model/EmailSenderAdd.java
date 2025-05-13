/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.Properties;
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
    private String authType;
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
    public EmailSenderAdd authType(String authType) {

        this.authType = authType;
        return this;
    }
    
    @ApiModelProperty(example = "BASIC", value = "")
    @JsonProperty("authType")
    @Valid
    public String getAuthType() {
        return authType;
    }
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
    **/
    public EmailSenderAdd properties(List<Properties> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"key\":\"body.scope\",\"value\":\"true\"},{\"key\":\"mail.smtp.starttls.enable\",\"value\":true},{\"key\":\"userName\",\"value\":\"iam\"},{\"key\":\"password\",\"value\":\"iam123\"}]", value = "")
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
            Objects.equals(this.authType, emailSenderAdd.authType) &&
            Objects.equals(this.properties, emailSenderAdd.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, smtpServerHost, smtpPort, fromAddress, authType, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EmailSenderAdd {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    smtpServerHost: ").append(toIndentedString(smtpServerHost)).append("\n");
        sb.append("    smtpPort: ").append(toIndentedString(smtpPort)).append("\n");
        sb.append("    fromAddress: ").append(toIndentedString(fromAddress)).append("\n");
        sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
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


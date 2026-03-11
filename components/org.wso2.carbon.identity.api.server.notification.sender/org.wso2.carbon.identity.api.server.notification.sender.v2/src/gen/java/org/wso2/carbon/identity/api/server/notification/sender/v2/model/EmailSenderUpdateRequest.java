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

public class EmailSenderUpdateRequest  {
  

@XmlType(name="ProviderEnum")
@XmlEnum(String.class)
public enum ProviderEnum {

    @XmlEnumValue("SMTP") SMTP(String.valueOf("SMTP")), @XmlEnumValue("HTTP") HTTP(String.valueOf("HTTP"));


    private String value;

    ProviderEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ProviderEnum fromValue(String value) {
        for (ProviderEnum b : ProviderEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ProviderEnum provider = ProviderEnum.SMTP;
    private String providerURL;
    private String smtpServerHost;
    private Integer smtpPort;
    private String fromAddress;
    private String authType;
    private List<Properties> properties = null;


    /**
    **/
    public EmailSenderUpdateRequest provider(ProviderEnum provider) {

        this.provider = provider;
        return this;
    }
    
    @ApiModelProperty(example = "SMTP", value = "")
    @JsonProperty("provider")
    @Valid
    public ProviderEnum getProvider() {
        return provider;
    }
    public void setProvider(ProviderEnum provider) {
        this.provider = provider;
    }

    /**
    * URL of the email provider. Required when the provider is HTTP.
    **/
    public EmailSenderUpdateRequest providerURL(String providerURL) {

        this.providerURL = providerURL;
        return this;
    }
    
    @ApiModelProperty(value = "URL of the email provider. Required when the provider is HTTP.")
    @JsonProperty("providerURL")
    @Valid
    public String getProviderURL() {
        return providerURL;
    }
    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    /**
    **/
    public EmailSenderUpdateRequest smtpServerHost(String smtpServerHost) {

        this.smtpServerHost = smtpServerHost;
        return this;
    }
    
    @ApiModelProperty(example = "smtp.gmail.com", value = "")
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
    public EmailSenderUpdateRequest smtpPort(Integer smtpPort) {

        this.smtpPort = smtpPort;
        return this;
    }
    
    @ApiModelProperty(example = "587", value = "")
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
    public EmailSenderUpdateRequest fromAddress(String fromAddress) {

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
    public EmailSenderUpdateRequest authType(String authType) {

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
    public EmailSenderUpdateRequest properties(List<Properties> properties) {

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

    public EmailSenderUpdateRequest addPropertiesItem(Properties propertiesItem) {
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
        EmailSenderUpdateRequest emailSenderUpdateRequest = (EmailSenderUpdateRequest) o;
        return Objects.equals(this.provider, emailSenderUpdateRequest.provider) &&
            Objects.equals(this.providerURL, emailSenderUpdateRequest.providerURL) &&
            Objects.equals(this.smtpServerHost, emailSenderUpdateRequest.smtpServerHost) &&
            Objects.equals(this.smtpPort, emailSenderUpdateRequest.smtpPort) &&
            Objects.equals(this.fromAddress, emailSenderUpdateRequest.fromAddress) &&
            Objects.equals(this.authType, emailSenderUpdateRequest.authType) &&
            Objects.equals(this.properties, emailSenderUpdateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, providerURL, smtpServerHost, smtpPort, fromAddress, authType, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EmailSenderUpdateRequest {\n");
        
        sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
        sb.append("    providerURL: ").append(toIndentedString(providerURL)).append("\n");
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


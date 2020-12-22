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

public class SMSSenderUpdateRequest  {
  
    private String provider;
    private String providerURL;
    private String key;
    private String secret;
    private String sender;
    private List<Properties> properties = null;


    /**
    **/
    public SMSSenderUpdateRequest provider(String provider) {

        this.provider = provider;
        return this;
    }
    
    @ApiModelProperty(example = "NEXMO", required = true, value = "")
    @JsonProperty("provider")
    @Valid
    @NotNull(message = "Property provider cannot be null.")

    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
    **/
    public SMSSenderUpdateRequest providerURL(String providerURL) {

        this.providerURL = providerURL;
        return this;
    }
    
    @ApiModelProperty(example = "https://rest.nexmo.com/sms/json", required = true, value = "")
    @JsonProperty("providerURL")
    @Valid
    @NotNull(message = "Property providerURL cannot be null.")

    public String getProviderURL() {
        return providerURL;
    }
    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    /**
    **/
    public SMSSenderUpdateRequest key(String key) {

        this.key = key;
        return this;
    }
    
    @ApiModelProperty(example = "123**45", value = "")
    @JsonProperty("key")
    @Valid
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    /**
    **/
    public SMSSenderUpdateRequest secret(String secret) {

        this.secret = secret;
        return this;
    }
    
    @ApiModelProperty(example = "5tg**ssd", value = "")
    @JsonProperty("secret")
    @Valid
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
    **/
    public SMSSenderUpdateRequest sender(String sender) {

        this.sender = sender;
        return this;
    }
    
    @ApiModelProperty(example = "+94 775563324", value = "")
    @JsonProperty("sender")
    @Valid
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
    **/
    public SMSSenderUpdateRequest properties(List<Properties> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"key\":\"http.headers\",\"value\":\"X-Version: 1, Authorization: bearer ,Accept: application/json ,Content-Type: application/json\"}]", value = "")
    @JsonProperty("properties")
    @Valid
    public List<Properties> getProperties() {
        return properties;
    }
    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

    public SMSSenderUpdateRequest addPropertiesItem(Properties propertiesItem) {
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
        SMSSenderUpdateRequest smSSenderUpdateRequest = (SMSSenderUpdateRequest) o;
        return Objects.equals(this.provider, smSSenderUpdateRequest.provider) &&
            Objects.equals(this.providerURL, smSSenderUpdateRequest.providerURL) &&
            Objects.equals(this.key, smSSenderUpdateRequest.key) &&
            Objects.equals(this.secret, smSSenderUpdateRequest.secret) &&
            Objects.equals(this.sender, smSSenderUpdateRequest.sender) &&
            Objects.equals(this.properties, smSSenderUpdateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, providerURL, key, secret, sender, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SMSSenderUpdateRequest {\n");
        
        sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
        sb.append("    providerURL: ").append(toIndentedString(providerURL)).append("\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
        sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
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


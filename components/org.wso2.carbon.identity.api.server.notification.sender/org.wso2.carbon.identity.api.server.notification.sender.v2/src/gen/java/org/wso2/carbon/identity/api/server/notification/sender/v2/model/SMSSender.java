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
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.Authentication;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.Properties;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SMSSender  {
  
    private String name;
    private String provider;
    private String providerURL;
    private String key;
    private String secret;
    private String sender;
    private Authentication authentication;

@XmlType(name="ContentTypeEnum")
@XmlEnum(String.class)
public enum ContentTypeEnum {

    @XmlEnumValue("JSON") JSON(String.valueOf("JSON")), @XmlEnumValue("FORM") FORM(String.valueOf("FORM"));


    private String value;

    ContentTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ContentTypeEnum fromValue(String value) {
        for (ContentTypeEnum b : ContentTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ContentTypeEnum contentType;
    private List<Properties> properties = null;


    /**
    **/
    public SMSSender name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "SMSPublisher", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public SMSSender provider(String provider) {

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
    public SMSSender providerURL(String providerURL) {

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
    public SMSSender key(String key) {

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
    public SMSSender secret(String secret) {

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
    public SMSSender sender(String sender) {

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
    public SMSSender authentication(Authentication authentication) {

        this.authentication = authentication;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authentication")
    @Valid
    public Authentication getAuthentication() {
        return authentication;
    }
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
    **/
    public SMSSender contentType(ContentTypeEnum contentType) {

        this.contentType = contentType;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("contentType")
    @Valid
    @NotNull(message = "Property contentType cannot be null.")

    public ContentTypeEnum getContentType() {
        return contentType;
    }
    public void setContentType(ContentTypeEnum contentType) {
        this.contentType = contentType;
    }

    /**
    **/
    public SMSSender properties(List<Properties> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"key\":\"body.scope\",\"value\":\"internal\"},{\"key\":\"http.headers\",\"value\":\"X-Version: 1, Authorization: bearer ,Accept: application/json ,Content-Type: application/json\"}]", value = "")
    @JsonProperty("properties")
    @Valid
    public List<Properties> getProperties() {
        return properties;
    }
    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

    public SMSSender addPropertiesItem(Properties propertiesItem) {
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
        SMSSender smSSender = (SMSSender) o;
        return Objects.equals(this.name, smSSender.name) &&
            Objects.equals(this.provider, smSSender.provider) &&
            Objects.equals(this.providerURL, smSSender.providerURL) &&
            Objects.equals(this.key, smSSender.key) &&
            Objects.equals(this.secret, smSSender.secret) &&
            Objects.equals(this.sender, smSSender.sender) &&
            Objects.equals(this.authentication, smSSender.authentication) &&
            Objects.equals(this.contentType, smSSender.contentType) &&
            Objects.equals(this.properties, smSSender.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, provider, providerURL, key, secret, sender, authentication, contentType, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SMSSender {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
        sb.append("    providerURL: ").append(toIndentedString(providerURL)).append("\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
        sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
        sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
        sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
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


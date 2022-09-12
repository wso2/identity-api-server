/*
 * Copyright (c) 2020, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AccessTokenConfiguration  {
  
    private String type;
    private List<String> audience = null;

    private Long userAccessTokenExpiryInSeconds;
    private Long applicationAccessTokenExpiryInSeconds;
    private String bindingType = "None";
    private Boolean revokeTokensWhenIDPSessionTerminated;
    private Boolean validateTokenBinding;

    /**
    **/
    public AccessTokenConfiguration type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "JWT", value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public AccessTokenConfiguration audience(List<String> audience) {

        this.audience = audience;
        return this;
    }
    
    @ApiModelProperty(example = "[\"http://idp.xyz.com\",\"http://idp.abc.com\"]", value = "")
    @JsonProperty("audience")
    @Valid
    public List<String> getAudience() {
        return audience;
    }
    public void setAudience(List<String> audience) {
        this.audience = audience;
    }

    public AccessTokenConfiguration addAudienceItem(String audienceItem) {
        if (this.audience == null) {
            this.audience = new ArrayList<>();
        }
        this.audience.add(audienceItem);
        return this;
    }

        /**
    **/
    public AccessTokenConfiguration userAccessTokenExpiryInSeconds(Long userAccessTokenExpiryInSeconds) {

        this.userAccessTokenExpiryInSeconds = userAccessTokenExpiryInSeconds;
        return this;
    }
    
    @ApiModelProperty(example = "3600", value = "")
    @JsonProperty("userAccessTokenExpiryInSeconds")
    @Valid
    public Long getUserAccessTokenExpiryInSeconds() {
        return userAccessTokenExpiryInSeconds;
    }
    public void setUserAccessTokenExpiryInSeconds(Long userAccessTokenExpiryInSeconds) {
        this.userAccessTokenExpiryInSeconds = userAccessTokenExpiryInSeconds;
    }

    /**
    **/
    public AccessTokenConfiguration applicationAccessTokenExpiryInSeconds(Long applicationAccessTokenExpiryInSeconds) {

        this.applicationAccessTokenExpiryInSeconds = applicationAccessTokenExpiryInSeconds;
        return this;
    }
    
    @ApiModelProperty(example = "3600", value = "")
    @JsonProperty("applicationAccessTokenExpiryInSeconds")
    @Valid
    public Long getApplicationAccessTokenExpiryInSeconds() {
        return applicationAccessTokenExpiryInSeconds;
    }
    public void setApplicationAccessTokenExpiryInSeconds(Long applicationAccessTokenExpiryInSeconds) {
        this.applicationAccessTokenExpiryInSeconds = applicationAccessTokenExpiryInSeconds;
    }

    /**
    * OAuth2 access token and refresh token can be bound to an external attribute during the token generation so that it can be optionally validated during the API invocation.
    **/
    public AccessTokenConfiguration bindingType(String bindingType) {

        this.bindingType = bindingType;
        return this;
    }
    
    @ApiModelProperty(example = "cookie", value = "OAuth2 access token and refresh token can be bound to an external attribute during the token generation so that it can be optionally validated during the API invocation.")
    @JsonProperty("bindingType")
    @Valid
    public String getBindingType() {
        return bindingType;
    }
    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }

    /**
    * If enabled, when the IDP session is terminated, all the access tokens bound to the session will get revoked.
    **/
    public AccessTokenConfiguration revokeTokensWhenIDPSessionTerminated(Boolean revokeTokensWhenIDPSessionTerminated) {

        this.revokeTokensWhenIDPSessionTerminated = revokeTokensWhenIDPSessionTerminated;
        return this;
    }
    
    @ApiModelProperty(value = "If enabled, when the IDP session is terminated, all the access tokens bound to the session will get revoked.")
    @JsonProperty("revokeTokensWhenIDPSessionTerminated")
    @Valid
    public Boolean getRevokeTokensWhenIDPSessionTerminated() {
        return revokeTokensWhenIDPSessionTerminated;
    }
    public void setRevokeTokensWhenIDPSessionTerminated(Boolean revokeTokensWhenIDPSessionTerminated) {
        this.revokeTokensWhenIDPSessionTerminated = revokeTokensWhenIDPSessionTerminated;
    }

    /**
    * If enabled, both access token and the token binding needs to be present for a successful API invocation.
    **/
    public AccessTokenConfiguration validateTokenBinding(Boolean validateTokenBinding) {

        this.validateTokenBinding = validateTokenBinding;
        return this;
    }
    
    @ApiModelProperty(value = "If enabled, both access token and the token binding needs to be present for a successful API invocation.")
    @JsonProperty("validateTokenBinding")
    @Valid
    public Boolean getValidateTokenBinding() {
        return validateTokenBinding;
    }
    public void setValidateTokenBinding(Boolean validateTokenBinding) {
        this.validateTokenBinding = validateTokenBinding;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenConfiguration accessTokenConfiguration = (AccessTokenConfiguration) o;
        return Objects.equals(this.type, accessTokenConfiguration.type) &&
            Objects.equals(this.audience, accessTokenConfiguration.audience) &&
            Objects.equals(this.userAccessTokenExpiryInSeconds, accessTokenConfiguration.userAccessTokenExpiryInSeconds) &&
            Objects.equals(this.applicationAccessTokenExpiryInSeconds, accessTokenConfiguration.applicationAccessTokenExpiryInSeconds) &&
            Objects.equals(this.bindingType, accessTokenConfiguration.bindingType) &&
            Objects.equals(this.revokeTokensWhenIDPSessionTerminated, accessTokenConfiguration.revokeTokensWhenIDPSessionTerminated) &&
            Objects.equals(this.validateTokenBinding, accessTokenConfiguration.validateTokenBinding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, audience, userAccessTokenExpiryInSeconds, applicationAccessTokenExpiryInSeconds, bindingType, revokeTokensWhenIDPSessionTerminated, validateTokenBinding);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccessTokenConfiguration {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
        sb.append("    userAccessTokenExpiryInSeconds: ").append(toIndentedString(userAccessTokenExpiryInSeconds)).append("\n");
        sb.append("    applicationAccessTokenExpiryInSeconds: ").append(toIndentedString(applicationAccessTokenExpiryInSeconds)).append("\n");
        sb.append("    bindingType: ").append(toIndentedString(bindingType)).append("\n");
        sb.append("    revokeTokensWhenIDPSessionTerminated: ").append(toIndentedString(revokeTokensWhenIDPSessionTerminated)).append("\n");
        sb.append("    validateTokenBinding: ").append(toIndentedString(validateTokenBinding)).append("\n");
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


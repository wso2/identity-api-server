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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.Secret;

import javax.validation.constraints.*;

import io.swagger.annotations.*;

import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class EndpointConfiguration {

    private String referenceName;
    private String url;
    private String authType;
    private Secret secret;

    /**
     *
     **/
    public EndpointConfiguration referenceName(String referenceName) {

        this.referenceName = referenceName;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    @JsonProperty("referenceName")
    @Valid
    @NotNull(message = "Property referenceName cannot be null.")

    public String getReferenceName() {

        return referenceName;
    }

    public void setReferenceName(String referenceName) {

        this.referenceName = referenceName;
    }

    /**
     *
     **/
    public EndpointConfiguration url(String url) {

        this.url = url;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    @JsonProperty("url")
    @Valid
    @NotNull(message = "Property url cannot be null.")

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    /**
     *
     **/
    public EndpointConfiguration authType(String authType) {

        this.authType = authType;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    @JsonProperty("auth-type")
    @Valid
    @NotNull(message = "Property authType cannot be null.")

    public String getAuthType() {

        return authType;
    }

    public void setAuthType(String authType) {

        this.authType = authType;
    }

    /**
     *
     **/
    public EndpointConfiguration secret(Secret secret) {

        this.secret = secret;
        return this;
    }

    @ApiModelProperty(required = true, value = "")
    @JsonProperty("secret")
    @Valid
    @NotNull(message = "Property secret cannot be null.")

    public Secret getSecret() {

        return secret;
    }

    public void setSecret(Secret secret) {

        this.secret = secret;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointConfiguration endpointConfiguration = (EndpointConfiguration) o;
        return Objects.equals(this.referenceName, endpointConfiguration.referenceName) &&
                Objects.equals(this.url, endpointConfiguration.url) &&
                Objects.equals(this.authType, endpointConfiguration.authType) &&
                Objects.equals(this.secret, endpointConfiguration.secret);
    }

    @Override
    public int hashCode() {

        return Objects.hash(referenceName, url, authType, secret);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EndpointConfiguration {\n");

        sb.append("    referenceName: ").append(toIndentedString(referenceName)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
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

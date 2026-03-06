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

package org.wso2.carbon.identity.api.server.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Resource-specific metadata. For IDP OAuth debugging, includes &#39;authorizationUrl&#39;.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Resource-specific metadata. For IDP OAuth debugging, includes 'authorizationUrl'.")
public class DebugConnectionResponseMetadata  {
  
    private String authorizationUrl;

    /**
    * OAuth 2.0 authorization URL for IDP debugging.
    **/
    public DebugConnectionResponseMetadata authorizationUrl(String authorizationUrl) {

        this.authorizationUrl = authorizationUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://api.asgardeo.io/t/linuka/oauth2/authorize?response_type=code&client_id=lfrTEyDGHBEUbeBKoiaosz1y8Aca&redirect_uri=https%3A%2F%2Flocalhost%3A9443%2Fcommonauth", value = "OAuth 2.0 authorization URL for IDP debugging.")
    @JsonProperty("authorizationUrl")
    @Valid
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DebugConnectionResponseMetadata debugConnectionResponseMetadata = (DebugConnectionResponseMetadata) o;
        return Objects.equals(this.authorizationUrl, debugConnectionResponseMetadata.authorizationUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DebugConnectionResponseMetadata {\n");
        
        sb.append("    authorizationUrl: ").append(toIndentedString(authorizationUrl)).append("\n");
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


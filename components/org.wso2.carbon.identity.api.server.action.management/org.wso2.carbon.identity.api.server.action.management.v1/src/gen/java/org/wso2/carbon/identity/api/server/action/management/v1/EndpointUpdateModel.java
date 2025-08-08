/*
 * Copyright (c) 2024-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.action.management.v1.AuthenticationType;
import javax.validation.constraints.*;

/**
 * Updating endpoint configurations of the action.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Updating endpoint configurations of the action.")
public class EndpointUpdateModel  {
  
    private String uri;
    private AuthenticationType authentication;
    private List<String> allowedHeaders = null;

    private List<String> allowedParameters = null;


    /**
    * Updating HTTPS URI of the endpoint.
    **/
    public EndpointUpdateModel uri(String uri) {

        this.uri = uri;
        return this;
    }
    
    @ApiModelProperty(example = "https://myextension.com/token", value = "Updating HTTPS URI of the endpoint.")
    @JsonProperty("uri")
    @Valid @Pattern(regexp="^https?://.+")
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    **/
    public EndpointUpdateModel authentication(AuthenticationType authentication) {

        this.authentication = authentication;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authentication")
    @Valid
    public AuthenticationType getAuthentication() {
        return authentication;
    }
    public void setAuthentication(AuthenticationType authentication) {
        this.authentication = authentication;
    }

    /**
    * List of HTTP headers to forward to the extension.
    **/
    public EndpointUpdateModel allowedHeaders(List<String> allowedHeaders) {

        this.allowedHeaders = allowedHeaders;
        return this;
    }
    
    @ApiModelProperty(example = "[\"x-geo-location\",\"host\"]", value = "List of HTTP headers to forward to the extension.")
    @JsonProperty("allowedHeaders")
    @Valid
    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }
    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public EndpointUpdateModel addAllowedHeadersItem(String allowedHeadersItem) {
        if (this.allowedHeaders == null) {
            this.allowedHeaders = new ArrayList<String>();
        }
        this.allowedHeaders.add(allowedHeadersItem);
        return this;
    }

        /**
    * List of parameters to forward to the extension.
    **/
    public EndpointUpdateModel allowedParameters(List<String> allowedParameters) {

        this.allowedParameters = allowedParameters;
        return this;
    }
    
    @ApiModelProperty(example = "[\"device-id\"]", value = "List of parameters to forward to the extension.")
    @JsonProperty("allowedParameters")
    @Valid
    public List<String> getAllowedParameters() {
        return allowedParameters;
    }
    public void setAllowedParameters(List<String> allowedParameters) {
        this.allowedParameters = allowedParameters;
    }

    public EndpointUpdateModel addAllowedParametersItem(String allowedParametersItem) {
        if (this.allowedParameters == null) {
            this.allowedParameters = new ArrayList<String>();
        }
        this.allowedParameters.add(allowedParametersItem);
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
        EndpointUpdateModel endpointUpdateModel = (EndpointUpdateModel) o;
        return Objects.equals(this.uri, endpointUpdateModel.uri) &&
            Objects.equals(this.authentication, endpointUpdateModel.authentication) &&
            Objects.equals(this.allowedHeaders, endpointUpdateModel.allowedHeaders) &&
            Objects.equals(this.allowedParameters, endpointUpdateModel.allowedParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, authentication, allowedHeaders, allowedParameters);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EndpointUpdateModel {\n");
        
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
        sb.append("    allowedHeaders: ").append(toIndentedString(allowedHeaders)).append("\n");
        sb.append("    allowedParameters: ").append(toIndentedString(allowedParameters)).append("\n");
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


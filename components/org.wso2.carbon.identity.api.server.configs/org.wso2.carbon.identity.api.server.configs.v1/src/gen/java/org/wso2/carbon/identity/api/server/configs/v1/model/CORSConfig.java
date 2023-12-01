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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CORSConfig  {
  
    private Boolean allowGenericHttpRequests;
    private Boolean allowAnyOrigin;
    private Boolean allowSubdomains;
    private List<String> supportedMethods = null;

    private Boolean supportAnyHeader;
    private List<String> supportedHeaders = null;

    private List<String> exposedHeaders = null;

    private Boolean supportsCredentials;
    private BigDecimal maxAge;

    /**
    * If true, generic HTTP requests must be allowed to pass through the filter. Else, only valid and accepted CORS must be allowed (strict CORS filtering).
    **/
    public CORSConfig allowGenericHttpRequests(Boolean allowGenericHttpRequests) {

        this.allowGenericHttpRequests = allowGenericHttpRequests;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, generic HTTP requests must be allowed to pass through the filter. Else, only valid and accepted CORS must be allowed (strict CORS filtering).")
    @JsonProperty("allowGenericHttpRequests")
    @Valid
    public Boolean getAllowGenericHttpRequests() {
        return allowGenericHttpRequests;
    }
    public void setAllowGenericHttpRequests(Boolean allowGenericHttpRequests) {
        this.allowGenericHttpRequests = allowGenericHttpRequests;
    }

    /**
    * If true the CORS valve must allow requests from any origin, else the origin whitelist must be consulted.
    **/
    public CORSConfig allowAnyOrigin(Boolean allowAnyOrigin) {

        this.allowAnyOrigin = allowAnyOrigin;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "If true the CORS valve must allow requests from any origin, else the origin whitelist must be consulted.")
    @JsonProperty("allowAnyOrigin")
    @Valid
    public Boolean getAllowAnyOrigin() {
        return allowAnyOrigin;
    }
    public void setAllowAnyOrigin(Boolean allowAnyOrigin) {
        this.allowAnyOrigin = allowAnyOrigin;
    }

    /**
    * If true, the CORS valve must allow requests from any origin which is a subdomain origin of the allowed origins.
    **/
    public CORSConfig allowSubdomains(Boolean allowSubdomains) {

        this.allowSubdomains = allowSubdomains;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "If true, the CORS valve must allow requests from any origin which is a subdomain origin of the allowed origins.")
    @JsonProperty("allowSubdomains")
    @Valid
    public Boolean getAllowSubdomains() {
        return allowSubdomains;
    }
    public void setAllowSubdomains(Boolean allowSubdomains) {
        this.allowSubdomains = allowSubdomains;
    }

    /**
    * The supported HTTP methods. Requests for methods not included here must be refused by the CORS filter with an HTTP 405 \\\&quot;Method not allowed\\\&quot; response.
    **/
    public CORSConfig supportedMethods(List<String> supportedMethods) {

        this.supportedMethods = supportedMethods;
        return this;
    }
    
    @ApiModelProperty(value = "The supported HTTP methods. Requests for methods not included here must be refused by the CORS filter with an HTTP 405 \\\"Method not allowed\\\" response.")
    @JsonProperty("supportedMethods")
    @Valid
    public List<String> getSupportedMethods() {
        return supportedMethods;
    }
    public void setSupportedMethods(List<String> supportedMethods) {
        this.supportedMethods = supportedMethods;
    }

    public CORSConfig addSupportedMethodsItem(String supportedMethodsItem) {
        if (this.supportedMethods == null) {
            this.supportedMethods = new ArrayList<>();
        }
        this.supportedMethods.add(supportedMethodsItem);
        return this;
    }

        /**
    * If true the CORS valve must support any requested header, else the supported headers list must be consulted.
    **/
    public CORSConfig supportAnyHeader(Boolean supportAnyHeader) {

        this.supportAnyHeader = supportAnyHeader;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "If true the CORS valve must support any requested header, else the supported headers list must be consulted.")
    @JsonProperty("supportAnyHeader")
    @Valid
    public Boolean getSupportAnyHeader() {
        return supportAnyHeader;
    }
    public void setSupportAnyHeader(Boolean supportAnyHeader) {
        this.supportAnyHeader = supportAnyHeader;
    }

    /**
    * The names of the supported author request headers.
    **/
    public CORSConfig supportedHeaders(List<String> supportedHeaders) {

        this.supportedHeaders = supportedHeaders;
        return this;
    }
    
    @ApiModelProperty(value = "The names of the supported author request headers.")
    @JsonProperty("supportedHeaders")
    @Valid
    public List<String> getSupportedHeaders() {
        return supportedHeaders;
    }
    public void setSupportedHeaders(List<String> supportedHeaders) {
        this.supportedHeaders = supportedHeaders;
    }

    public CORSConfig addSupportedHeadersItem(String supportedHeadersItem) {
        if (this.supportedHeaders == null) {
            this.supportedHeaders = new ArrayList<>();
        }
        this.supportedHeaders.add(supportedHeadersItem);
        return this;
    }

        /**
    * The non-simple response headers that the web browser should expose to the author of the CORS request.
    **/
    public CORSConfig exposedHeaders(List<String> exposedHeaders) {

        this.exposedHeaders = exposedHeaders;
        return this;
    }
    
    @ApiModelProperty(value = "The non-simple response headers that the web browser should expose to the author of the CORS request.")
    @JsonProperty("exposedHeaders")
    @Valid
    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }
    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public CORSConfig addExposedHeadersItem(String exposedHeadersItem) {
        if (this.exposedHeaders == null) {
            this.exposedHeaders = new ArrayList<>();
        }
        this.exposedHeaders.add(exposedHeadersItem);
        return this;
    }

        /**
    * Whether user credentials, such as cookies, HTTP authentication or client-side certificates, are supported.
    **/
    public CORSConfig supportsCredentials(Boolean supportsCredentials) {

        this.supportsCredentials = supportsCredentials;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether user credentials, such as cookies, HTTP authentication or client-side certificates, are supported.")
    @JsonProperty("supportsCredentials")
    @Valid
    public Boolean getSupportsCredentials() {
        return supportsCredentials;
    }
    public void setSupportsCredentials(Boolean supportsCredentials) {
        this.supportsCredentials = supportsCredentials;
    }

    /**
    * Indicates how long the results of a preflight request can be cached by the web client, in seconds. If -1 then unspecified.
    **/
    public CORSConfig maxAge(BigDecimal maxAge) {

        this.maxAge = maxAge;
        return this;
    }
    
    @ApiModelProperty(example = "3600", value = "Indicates how long the results of a preflight request can be cached by the web client, in seconds. If -1 then unspecified.")
    @JsonProperty("maxAge")
    @Valid
    public BigDecimal getMaxAge() {
        return maxAge;
    }
    public void setMaxAge(BigDecimal maxAge) {
        this.maxAge = maxAge;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CORSConfig coRSConfig = (CORSConfig) o;
        return Objects.equals(this.allowGenericHttpRequests, coRSConfig.allowGenericHttpRequests) &&
            Objects.equals(this.allowAnyOrigin, coRSConfig.allowAnyOrigin) &&
            Objects.equals(this.allowSubdomains, coRSConfig.allowSubdomains) &&
            Objects.equals(this.supportedMethods, coRSConfig.supportedMethods) &&
            Objects.equals(this.supportAnyHeader, coRSConfig.supportAnyHeader) &&
            Objects.equals(this.supportedHeaders, coRSConfig.supportedHeaders) &&
            Objects.equals(this.exposedHeaders, coRSConfig.exposedHeaders) &&
            Objects.equals(this.supportsCredentials, coRSConfig.supportsCredentials) &&
            Objects.equals(this.maxAge, coRSConfig.maxAge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowGenericHttpRequests, allowAnyOrigin, allowSubdomains, supportedMethods, supportAnyHeader, supportedHeaders, exposedHeaders, supportsCredentials, maxAge);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CORSConfig {\n");
        
        sb.append("    allowGenericHttpRequests: ").append(toIndentedString(allowGenericHttpRequests)).append("\n");
        sb.append("    allowAnyOrigin: ").append(toIndentedString(allowAnyOrigin)).append("\n");
        sb.append("    allowSubdomains: ").append(toIndentedString(allowSubdomains)).append("\n");
        sb.append("    supportedMethods: ").append(toIndentedString(supportedMethods)).append("\n");
        sb.append("    supportAnyHeader: ").append(toIndentedString(supportAnyHeader)).append("\n");
        sb.append("    supportedHeaders: ").append(toIndentedString(supportedHeaders)).append("\n");
        sb.append("    exposedHeaders: ").append(toIndentedString(exposedHeaders)).append("\n");
        sb.append("    supportsCredentials: ").append(toIndentedString(supportsCredentials)).append("\n");
        sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
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


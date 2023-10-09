/*
 * Copyright (c) 2023, WSO2 LLC. (https://www.wso2.com).
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

public class InboundAuthSAML2Config  {
  
    private List<String> destinationURLs = null;

    private BigDecimal metadataValidityPeriod;
    private Boolean enableMetadataSigning;
    private String metadataEndpoint;

    /**
    * List of destination URLs
    **/
    public InboundAuthSAML2Config destinationURLs(List<String> destinationURLs) {

        this.destinationURLs = destinationURLs;
        return this;
    }
    
    @ApiModelProperty(example = "[\"https://localhost:9443/samlsso\"]", value = "List of destination URLs")
    @JsonProperty("destinationURLs")
    @Valid
    public List<String> getDestinationURLs() {
        return destinationURLs;
    }
    public void setDestinationURLs(List<String> destinationURLs) {
        this.destinationURLs = destinationURLs;
    }

    public InboundAuthSAML2Config addDestinationURLsItem(String destinationURLsItem) {
        if (this.destinationURLs == null) {
            this.destinationURLs = new ArrayList<>();
        }
        this.destinationURLs.add(destinationURLsItem);
        return this;
    }

        /**
    * SAML metadata validity period in minutes
    **/
    public InboundAuthSAML2Config metadataValidityPeriod(BigDecimal metadataValidityPeriod) {

        this.metadataValidityPeriod = metadataValidityPeriod;
        return this;
    }
    
    @ApiModelProperty(example = "60", value = "SAML metadata validity period in minutes")
    @JsonProperty("metadataValidityPeriod")
    @Valid
    public BigDecimal getMetadataValidityPeriod() {
        return metadataValidityPeriod;
    }
    public void setMetadataValidityPeriod(BigDecimal metadataValidityPeriod) {
        this.metadataValidityPeriod = metadataValidityPeriod;
    }

    /**
    * Enable SAML metadata signing
    **/
    public InboundAuthSAML2Config enableMetadataSigning(Boolean enableMetadataSigning) {

        this.enableMetadataSigning = enableMetadataSigning;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Enable SAML metadata signing")
    @JsonProperty("enableMetadataSigning")
    @Valid
    public Boolean getEnableMetadataSigning() {
        return enableMetadataSigning;
    }
    public void setEnableMetadataSigning(Boolean enableMetadataSigning) {
        this.enableMetadataSigning = enableMetadataSigning;
    }

    /**
    * SAML metadata endpoint
    **/
    public InboundAuthSAML2Config metadataEndpoint(String metadataEndpoint) {

        this.metadataEndpoint = metadataEndpoint;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/identity/metadata/saml2", value = "SAML metadata endpoint")
    @JsonProperty("metadataEndpoint")
    @Valid
    public String getMetadataEndpoint() {
        return metadataEndpoint;
    }
    public void setMetadataEndpoint(String metadataEndpoint) {
        this.metadataEndpoint = metadataEndpoint;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InboundAuthSAML2Config inboundAuthSAML2Config = (InboundAuthSAML2Config) o;
        return Objects.equals(this.destinationURLs, inboundAuthSAML2Config.destinationURLs) &&
            Objects.equals(this.metadataValidityPeriod, inboundAuthSAML2Config.metadataValidityPeriod) &&
            Objects.equals(this.enableMetadataSigning, inboundAuthSAML2Config.enableMetadataSigning) &&
            Objects.equals(this.metadataEndpoint, inboundAuthSAML2Config.metadataEndpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationURLs, metadataValidityPeriod, enableMetadataSigning, metadataEndpoint);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InboundAuthSAML2Config {\n");
        
        sb.append("    destinationURLs: ").append(toIndentedString(destinationURLs)).append("\n");
        sb.append("    metadataValidityPeriod: ").append(toIndentedString(metadataValidityPeriod)).append("\n");
        sb.append("    enableMetadataSigning: ").append(toIndentedString(enableMetadataSigning)).append("\n");
        sb.append("    metadataEndpoint: ").append(toIndentedString(metadataEndpoint)).append("\n");
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


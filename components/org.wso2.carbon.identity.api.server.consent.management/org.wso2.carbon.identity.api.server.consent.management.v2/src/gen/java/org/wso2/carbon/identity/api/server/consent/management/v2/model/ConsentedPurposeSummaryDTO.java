/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Minimal consented purpose information for list responses
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Minimal consented purpose information for list responses")
public class ConsentedPurposeSummaryDTO  {
  
    private String name;
    private String id;
    private String type;
    private String versionId;
    private String version;

    /**
    **/
    public ConsentedPurposeSummaryDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Privacy Policy", value = "")
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
    public ConsentedPurposeSummaryDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Purpose type classification
    **/
    public ConsentedPurposeSummaryDTO type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "Policy", value = "Purpose type classification")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * UUID of the purpose version that was consented
    **/
    public ConsentedPurposeSummaryDTO versionId(String versionId) {

        this.versionId = versionId;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "UUID of the purpose version that was consented")
    @JsonProperty("versionId")
    @Valid
    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
    * Human-readable version label (e.g. \&quot;2\&quot;). Null for pre-versioning consents.
    **/
    public ConsentedPurposeSummaryDTO version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Human-readable version label (e.g. \"2\"). Null for pre-versioning consents.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsentedPurposeSummaryDTO consentedPurposeSummaryDTO = (ConsentedPurposeSummaryDTO) o;
        return Objects.equals(this.name, consentedPurposeSummaryDTO.name) &&
            Objects.equals(this.id, consentedPurposeSummaryDTO.id) &&
            Objects.equals(this.type, consentedPurposeSummaryDTO.type) &&
            Objects.equals(this.versionId, consentedPurposeSummaryDTO.versionId) &&
            Objects.equals(this.version, consentedPurposeSummaryDTO.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, type, versionId, version);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentedPurposeSummaryDTO {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    versionId: ").append(toIndentedString(versionId)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
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


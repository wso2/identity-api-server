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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

@ApiModel(description = "Attribute metadata")
public class AttributeMetadata {

    private String name;
    private String claimURI;
    private Boolean required;
    private Boolean readOnly;
    private List<String> validators = null;


    /**
     *
     **/
    public AttributeMetadata name(String name) {

        this.name = name;
        return this;
    }

    @ApiModelProperty(example = "Username", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    /**
     *
     **/
    public AttributeMetadata claimURI(String claimURI) {

        this.claimURI = claimURI;
        return this;
    }

    @ApiModelProperty(example = "http://wso2.org/claims/username", value = "")
    @JsonProperty("claimURI")
    @Valid
    public String getClaimURI() {

        return claimURI;
    }

    public void setClaimURI(String claimURI) {

        this.claimURI = claimURI;
    }

    /**
     *
     **/
    public AttributeMetadata required(Boolean required) {

        this.required = required;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("required")
    @Valid
    public Boolean getRequired() {

        return required;
    }

    public void setRequired(Boolean required) {

        this.required = required;
    }

    /**
     *
     **/
    public AttributeMetadata readOnly(Boolean readOnly) {

        this.readOnly = readOnly;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("readOnly")
    @Valid
    public Boolean getReadOnly() {

        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {

        this.readOnly = readOnly;
    }

    /**
     *
     **/
    public AttributeMetadata validators(List<String> validators) {

        this.validators = validators;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("validators")
    @Valid
    public List<String> getValidators() {

        return validators;
    }

    public void setValidators(List<String> validators) {

        this.validators = validators;
    }

    public AttributeMetadata addValidatorsItem(String validatorsItem) {

        if (this.validators == null) {
            this.validators = new ArrayList<String>();
        }
        this.validators.add(validatorsItem);
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
        AttributeMetadata attributeMetadata = (AttributeMetadata) o;
        return Objects.equals(this.name, attributeMetadata.name) &&
                Objects.equals(this.claimURI, attributeMetadata.claimURI) &&
                Objects.equals(this.required, attributeMetadata.required) &&
                Objects.equals(this.readOnly, attributeMetadata.readOnly) &&
                Objects.equals(this.validators, attributeMetadata.validators);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, claimURI, required, readOnly, validators);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeMetadata {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    claimURI: ").append(toIndentedString(claimURI)).append("\n");
        sb.append("    required: ").append(toIndentedString(required)).append("\n");
        sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
        sb.append("    validators: ").append(toIndentedString(validators)).append("\n");
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


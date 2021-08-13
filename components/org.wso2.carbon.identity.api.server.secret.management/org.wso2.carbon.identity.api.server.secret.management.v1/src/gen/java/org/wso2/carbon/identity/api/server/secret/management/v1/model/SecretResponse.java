/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org).
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

package org.wso2.carbon.identity.api.server.secret.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

import java.util.Objects;
import javax.validation.Valid;

public class SecretResponse {

    private String secretId;
    private String secretName;
    private String created;
    private String lastModified;
    private String type;
    private String description;

    /**
     *
     **/
    public SecretResponse secretId(String secretId) {

        this.secretId = secretId;
        return this;
    }

    @ApiModelProperty(example = "30103923-923c-485f-a8f9-606398", required = true, value = "")
    @JsonProperty("secretId")
    @Valid
    @NotNull(message = "Property secretId cannot be null.")

    public String getSecretId() {

        return secretId;
    }

    public void setSecretId(String secretId) {

        this.secretId = secretId;
    }

    /**
     *
     **/
    public SecretResponse secretName(String secretName) {

        this.secretName = secretName;
        return this;
    }

    @ApiModelProperty(example = "choreo-riskScore", required = true, value = "")
    @JsonProperty("secretName")
    @Valid
    @NotNull(message = "Property secretName cannot be null.")

    public String getSecretName() {

        return secretName;
    }

    public void setSecretName(String secretName) {

        this.secretName = secretName;
    }

    /**
     *
     **/
    public SecretResponse created(String created) {

        this.created = created;
        return this;
    }

    @ApiModelProperty(example = "2021.07.22", required = true, value = "")
    @JsonProperty("created")
    @Valid
    @NotNull(message = "Property created cannot be null.")

    public String getCreated() {

        return created;
    }

    public void setCreated(String created) {

        this.created = created;
    }

    /**
     *
     **/
    public SecretResponse lastModified(String lastModified) {

        this.lastModified = lastModified;
        return this;
    }

    @ApiModelProperty(example = "2021.07.22", required = true, value = "")
    @JsonProperty("lastModified")
    @Valid
    @NotNull(message = "Property lastModified cannot be null.")

    public String getLastModified() {

        return lastModified;
    }

    public void setLastModified(String lastModified) {

        this.lastModified = lastModified;
    }

    /**
     *
     **/
    public SecretResponse type(String type) {

        this.type = type;
        return this;
    }

    @ApiModelProperty(example = "adaptive", required = true, value = "")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    /**
     *
     **/
    public SecretResponse description(String description) {

        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "sample_description", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecretResponse secretResponse = (SecretResponse) o;
        return Objects.equals(this.secretId, secretResponse.secretId) &&
                Objects.equals(this.secretName, secretResponse.secretName) &&
                Objects.equals(this.created, secretResponse.created) &&
                Objects.equals(this.lastModified, secretResponse.lastModified) &&
                Objects.equals(this.type, secretResponse.type) &&
                Objects.equals(this.description, secretResponse.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(secretId, secretName, created, lastModified, type, description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SecretResponse {\n");

        sb.append("    secretId: ").append(toIndentedString(secretId)).append("\n");
        sb.append("    secretName: ").append(toIndentedString(secretName)).append("\n");
        sb.append("    created: ").append(toIndentedString(created)).append("\n");
        sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

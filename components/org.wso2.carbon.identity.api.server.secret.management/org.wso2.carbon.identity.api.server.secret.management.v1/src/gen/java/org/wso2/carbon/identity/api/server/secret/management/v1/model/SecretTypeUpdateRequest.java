/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

import java.util.Objects;
import javax.validation.Valid;

public class SecretTypeUpdateRequest {

    private String description;

    /**
     *
     **/
    public SecretTypeUpdateRequest description(String description) {

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
        SecretTypeUpdateRequest secretTypeUpdateRequest = (SecretTypeUpdateRequest) o;
        return Objects.equals(this.description, secretTypeUpdateRequest.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(description);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SecretTypeUpdateRequest {\n");

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

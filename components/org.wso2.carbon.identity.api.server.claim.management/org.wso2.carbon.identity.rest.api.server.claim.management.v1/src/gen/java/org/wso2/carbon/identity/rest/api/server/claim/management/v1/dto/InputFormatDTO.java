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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

@ApiModel(description = "Input format of the attribute.")
public class InputFormatDTO {

    @Valid 
    private String inputType = null;

    /**
     * Specifies the format of the input type.
     * @return inputType
     */
    @ApiModelProperty(value = "The format of the input type.")
    @JsonProperty("inputType")
    public String getInputType() {

        return inputType;
    }

    public void setInputType(String inputType) {

        this.inputType = inputType;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InputFormatDTO {\n");
        sb.append("    inputType: ").append(inputType).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputFormatDTO profilesDTO = (InputFormatDTO) o;
        return Objects.equals(this.inputType, profilesDTO.inputType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(inputType);
    }
}

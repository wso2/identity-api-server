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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InboundAuthOAuth2Config  {
  
    private Boolean preserveSessionAtPasswordUpdate;

    /**
    * Preserve logged in session after password update
    **/
    public InboundAuthOAuth2Config preserveSessionAtPasswordUpdate(Boolean preserveSessionAtPasswordUpdate) {

        this.preserveSessionAtPasswordUpdate = preserveSessionAtPasswordUpdate;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Preserve logged in session after password update")
    @JsonProperty("preserveSessionAtPasswordUpdate")
    @Valid
    public Boolean getPreserveSessionAtPasswordUpdate() {
        return preserveSessionAtPasswordUpdate;
    }
    public void setPreserveSessionAtPasswordUpdate(Boolean preserveSessionAtPasswordUpdate) {
        this.preserveSessionAtPasswordUpdate = preserveSessionAtPasswordUpdate;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InboundAuthOAuth2Config inboundAuthOAuth2Config = (InboundAuthOAuth2Config) o;
        return Objects.equals(this.preserveSessionAtPasswordUpdate, inboundAuthOAuth2Config.preserveSessionAtPasswordUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preserveSessionAtPasswordUpdate);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InboundAuthOAuth2Config {\n");
        
        sb.append("    preserveSessionAtPasswordUpdate: ").append(toIndentedString(preserveSessionAtPasswordUpdate)).append("\n");
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


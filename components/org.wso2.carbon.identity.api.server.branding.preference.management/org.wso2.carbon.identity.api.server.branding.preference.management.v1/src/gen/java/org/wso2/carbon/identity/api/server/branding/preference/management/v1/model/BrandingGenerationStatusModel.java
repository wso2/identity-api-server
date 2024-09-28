/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class BrandingGenerationStatusModel  {
  
    private Object status;

    /**
    * This is the JSON structured branding preference
    **/
    public BrandingGenerationStatusModel status(Object status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "{\"render_webpage\":true,\"extract_webpage_content\":true,\"webpage_extraction_completed\":true,\"generate_branding\":true,\"branding_generation_status\":{\"color_palette\":true,\"style_properties\":true},\"branding_generation_completed_status\":{\"color_palette\":false,\"style_properties\":false},\"create_branding_theme\":false,\"branding_generation_completed\":false}", value = "This is the JSON structured branding preference")
    @JsonProperty("status")
    @Valid
    public Object getStatus() {
        return status;
    }
    public void setStatus(Object status) {
        this.status = status;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandingGenerationStatusModel brandingGenerationStatusModel = (BrandingGenerationStatusModel) o;
        return Objects.equals(this.status, brandingGenerationStatusModel.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BrandingGenerationStatusModel {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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


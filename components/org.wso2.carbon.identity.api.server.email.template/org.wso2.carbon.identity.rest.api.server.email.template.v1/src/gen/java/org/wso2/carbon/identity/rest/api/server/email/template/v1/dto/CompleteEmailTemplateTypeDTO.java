/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.email.template.v1.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class CompleteEmailTemplateTypeDTO {

    @Valid 
    @NotNull(message = "Property displayName cannot be null.") 
    private String displayName = null;

    @Valid 
    @NotNull(message = "Property items cannot be null.") 
    private List<CompleteEmailTemplateDTO> items = new ArrayList<CompleteEmailTemplateDTO>();

    /**
    * Display name of the email template type.
    **/
    @ApiModelProperty(required = true, value = "Display name of the email template type.")
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Email templates for the template type.
    **/
    @ApiModelProperty(required = true, value = "Email templates for the template type.")
    @JsonProperty("items")
    public List<CompleteEmailTemplateDTO> getItems() {
        return items;
    }
    public void setItems(List<CompleteEmailTemplateDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CompleteEmailTemplateTypeDTO {\n");
        
        sb.append("    displayName: ").append(displayName).append("\n");
        sb.append("    items: ").append(items).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}

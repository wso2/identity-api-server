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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SimpleTemplateTypeID  {
  
    private String templateTypeId;
    private String channel;

    /**
    * Unique ID of the template type.
    **/
    public SimpleTemplateTypeID templateTypeId(String templateTypeId) {

        this.templateTypeId = templateTypeId;
        return this;
    }
    
    @ApiModelProperty(example = "YWNjb3VudGNvbmZpcm1hdGlvbg", required = true, value = "Unique ID of the template type.")
    @JsonProperty("templateTypeId")
    @Valid
    @NotNull(message = "Property templateTypeId cannot be null.")

    public String getTemplateTypeId() {
        return templateTypeId;
    }
    public void setTemplateTypeId(String templateTypeId) {
        this.templateTypeId = templateTypeId;
    }

    /**
    * Notification channel of the template type (SMS or EMAIL).
    **/
    public SimpleTemplateTypeID channel(String channel) {

        this.channel = channel;
        return this;
    }
    
    @ApiModelProperty(example = "EMAIL", required = true, value = "Notification channel of the template type (SMS or EMAIL).")
    @JsonProperty("channel")
    @Valid
    @NotNull(message = "Property channel cannot be null.")

    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleTemplateTypeID simpleTemplateTypeID = (SimpleTemplateTypeID) o;
        return Objects.equals(this.templateTypeId, simpleTemplateTypeID.templateTypeId) &&
            Objects.equals(this.channel, simpleTemplateTypeID.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateTypeId, channel);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SimpleTemplateTypeID {\n");
        
        sb.append("    templateTypeId: ").append(toIndentedString(templateTypeId)).append("\n");
        sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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


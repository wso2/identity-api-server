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

package org.wso2.carbon.identity.api.server.image.service.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class DownloadResponse  {
  
    private String id;
    private String link;
    private Integer height;
    private Integer width;

    /**
    **/
    public DownloadResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public DownloadResponse link(String link) {

        this.link = link;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("link")
    @Valid
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    /**
    **/
    public DownloadResponse height(Integer height) {

        this.height = height;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("height")
    @Valid
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
    **/
    public DownloadResponse width(Integer width) {

        this.width = width;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("width")
    @Valid
    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DownloadResponse downloadResponse = (DownloadResponse) o;
        return Objects.equals(this.id, downloadResponse.id) &&
            Objects.equals(this.link, downloadResponse.link) &&
            Objects.equals(this.height, downloadResponse.height) &&
            Objects.equals(this.width, downloadResponse.width);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, height, width);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DownloadResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    link: ").append(toIndentedString(link)).append("\n");
        sb.append("    height: ").append(toIndentedString(height)).append("\n");
        sb.append("    width: ").append(toIndentedString(width)).append("\n");
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


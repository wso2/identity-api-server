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
import java.io.File;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ImageFile  {
  
    private File file;
    private String type;

    /**
    * file to upload
    **/
    public ImageFile file(File file) {

        this.file = file;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "file to upload")
    @JsonProperty("file")
    @Valid
    @NotNull(message = "Property file cannot be null.")

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    /**
    * Type of image uploading. It can be &#39;idp&#39;, &#39;app&#39; or &#39;user&#39;.
    **/
    public ImageFile type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "Type of image uploading. It can be 'idp', 'app' or 'user'.")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageFile imageFile = (ImageFile) o;
        return Objects.equals(this.file, imageFile.file) &&
            Objects.equals(this.type, imageFile.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, type);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ImageFile {\n");
        
        sb.append("    file: ").append(toIndentedString(file)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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


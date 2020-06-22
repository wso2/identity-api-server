/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.media.service.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MyResourceFiles  {
  
    private List<File> files = new ArrayList<>();

    private MyResourceFilesMetadata metadata;

    /**
    **/
    public MyResourceFiles files(List<File> files) {

        this.files = files;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("files")
    @Valid
    @NotNull(message = "Property files cannot be null.")

    public List<File> getFiles() {
        return files;
    }
    public void setFiles(List<File> files) {
        this.files = files;
    }

    public MyResourceFiles addFilesItem(File filesItem) {
        this.files.add(filesItem);
        return this;
    }

        /**
    **/
    public MyResourceFiles metadata(MyResourceFilesMetadata metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("metadata")
    @Valid
    public MyResourceFilesMetadata getMetadata() {
        return metadata;
    }
    public void setMetadata(MyResourceFilesMetadata metadata) {
        this.metadata = metadata;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyResourceFiles myResourceFiles = (MyResourceFiles) o;
        return Objects.equals(this.files, myResourceFiles.files) &&
            Objects.equals(this.metadata, myResourceFiles.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(files, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MyResourceFiles {\n");
        
        sb.append("    files: ").append(toIndentedString(files)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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


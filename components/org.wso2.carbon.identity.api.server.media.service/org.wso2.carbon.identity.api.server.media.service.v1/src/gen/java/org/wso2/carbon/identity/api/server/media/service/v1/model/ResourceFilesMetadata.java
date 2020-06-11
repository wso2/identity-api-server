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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadataFileSecurity;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ResourceFilesMetadata  {
  
    private String fileTag;
    private ResourceFilesMetadataFileSecurity fileSecurity;
    private List<String> fileIdentifiers = null;


    /**
    * The file tag.
    **/
    public ResourceFilesMetadata fileTag(String fileTag) {

        this.fileTag = fileTag;
        return this;
    }
    
    @ApiModelProperty(example = "user", value = "The file tag.")
    @JsonProperty("fileTag")
    @Valid
    public String getFileTag() {
        return fileTag;
    }
    public void setFileTag(String fileTag) {
        this.fileTag = fileTag;
    }

    /**
    **/
    public ResourceFilesMetadata fileSecurity(ResourceFilesMetadataFileSecurity fileSecurity) {

        this.fileSecurity = fileSecurity;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("fileSecurity")
    @Valid
    public ResourceFilesMetadataFileSecurity getFileSecurity() {
        return fileSecurity;
    }
    public void setFileSecurity(ResourceFilesMetadataFileSecurity fileSecurity) {
        this.fileSecurity = fileSecurity;
    }

    /**
    **/
    public ResourceFilesMetadata fileIdentifiers(List<String> fileIdentifiers) {

        this.fileIdentifiers = fileIdentifiers;
        return this;
    }
    
    @ApiModelProperty(example = "[\"large\",\"medium\",\"small\"]", value = "")
    @JsonProperty("fileIdentifiers")
    @Valid
    public List<String> getFileIdentifiers() {
        return fileIdentifiers;
    }
    public void setFileIdentifiers(List<String> fileIdentifiers) {
        this.fileIdentifiers = fileIdentifiers;
    }

    public ResourceFilesMetadata addFileIdentifiersItem(String fileIdentifiersItem) {
        if (this.fileIdentifiers == null) {
            this.fileIdentifiers = new ArrayList<>();
        }
        this.fileIdentifiers.add(fileIdentifiersItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceFilesMetadata resourceFilesMetadata = (ResourceFilesMetadata) o;
        return Objects.equals(this.fileTag, resourceFilesMetadata.fileTag) &&
            Objects.equals(this.fileSecurity, resourceFilesMetadata.fileSecurity) &&
            Objects.equals(this.fileIdentifiers, resourceFilesMetadata.fileIdentifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileTag, fileSecurity, fileIdentifiers);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceFilesMetadata {\n");
        
        sb.append("    fileTag: ").append(toIndentedString(fileTag)).append("\n");
        sb.append("    fileSecurity: ").append(toIndentedString(fileSecurity)).append("\n");
        sb.append("    fileIdentifiers: ").append(toIndentedString(fileIdentifiers)).append("\n");
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


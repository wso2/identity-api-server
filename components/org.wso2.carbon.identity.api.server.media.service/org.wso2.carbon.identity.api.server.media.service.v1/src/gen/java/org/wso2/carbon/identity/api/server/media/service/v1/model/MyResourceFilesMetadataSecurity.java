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

import java.util.Objects;
import javax.validation.Valid;

public class MyResourceFilesMetadataSecurity  {
  
    private Boolean allowedAll;

    /**
    * Defines whether the file is publically available for access or has restricted access.
    **/
    public MyResourceFilesMetadataSecurity allowedAll(Boolean allowedAll) {

        this.allowedAll = allowedAll;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Defines whether the file is publically available for access or has restricted access.")
    @JsonProperty("allowedAll")
    @Valid
    public Boolean getAllowedAll() {
        return allowedAll;
    }
    public void setAllowedAll(Boolean allowedAll) {
        this.allowedAll = allowedAll;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyResourceFilesMetadataSecurity myResourceFilesMetadataSecurity = (MyResourceFilesMetadataSecurity) o;
        return Objects.equals(this.allowedAll, myResourceFilesMetadataSecurity.allowedAll);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedAll);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MyResourceFilesMetadataSecurity {\n");
        
        sb.append("    allowedAll: ").append(toIndentedString(allowedAll)).append("\n");
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


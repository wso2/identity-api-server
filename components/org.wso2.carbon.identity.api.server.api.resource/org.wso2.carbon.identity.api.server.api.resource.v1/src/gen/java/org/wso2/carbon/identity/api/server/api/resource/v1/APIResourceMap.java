/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCollectionItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class APIResourceMap  {
  
    private List<APIResourceCollectionItem> read = null;

    private List<APIResourceCollectionItem> write = null;


    /**
    **/
    public APIResourceMap read(List<APIResourceCollectionItem> read) {

        this.read = read;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("read")
    @Valid
    public List<APIResourceCollectionItem> getRead() {
        return read;
    }
    public void setRead(List<APIResourceCollectionItem> read) {
        this.read = read;
    }

    public APIResourceMap addReadItem(APIResourceCollectionItem readItem) {
        if (this.read == null) {
            this.read = new ArrayList<APIResourceCollectionItem>();
        }
        this.read.add(readItem);
        return this;
    }

        /**
    **/
    public APIResourceMap write(List<APIResourceCollectionItem> write) {

        this.write = write;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("write")
    @Valid
    public List<APIResourceCollectionItem> getWrite() {
        return write;
    }
    public void setWrite(List<APIResourceCollectionItem> write) {
        this.write = write;
    }

    public APIResourceMap addWriteItem(APIResourceCollectionItem writeItem) {
        if (this.write == null) {
            this.write = new ArrayList<APIResourceCollectionItem>();
        }
        this.write.add(writeItem);
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
        APIResourceMap apIResourceMap = (APIResourceMap) o;
        return Objects.equals(this.read, apIResourceMap.read) &&
            Objects.equals(this.write, apIResourceMap.write);
    }

    @Override
    public int hashCode() {
        return Objects.hash(read, write);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class APIResourceMap {\n");
        
        sb.append("    read: ").append(toIndentedString(read)).append("\n");
        sb.append("    write: ").append(toIndentedString(write)).append("\n");
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


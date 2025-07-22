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

package org.wso2.carbon.identity.api.server.organization.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ParentOrganization  {
  
    private String id;
    private String ref;

    /**
    **/
    public ParentOrganization id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "b4526d91-a8bf-43d2-8b14-c548cf73065b", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public ParentOrganization ref(String ref) {

        this.ref = ref;
        return this;
    }
    
    @ApiModelProperty(example = "/api/server/v1/organizations/b4526d91-a8bf-43d2-8b14-c548cf73065b", required = true, value = "")
    @JsonProperty("ref")
    @Valid
    @NotNull(message = "Property ref cannot be null.")

    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParentOrganization parentOrganization = (ParentOrganization) o;
        return Objects.equals(this.id, parentOrganization.id) &&
            Objects.equals(this.ref, parentOrganization.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ref);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ParentOrganization {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
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


/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.ContextPath;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AccessConfig  {
  
    private List<ContextPath> expose = null;

    private List<ContextPath> modify = null;


    /**
    **/
    public AccessConfig expose(List<ContextPath> expose) {

        this.expose = expose;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("expose")
    @Valid
    public List<ContextPath> getExpose() {
        return expose;
    }
    public void setExpose(List<ContextPath> expose) {
        this.expose = expose;
    }

    public AccessConfig addExposeItem(ContextPath exposeItem) {
        if (this.expose == null) {
            this.expose = new ArrayList<ContextPath>();
        }
        this.expose.add(exposeItem);
        return this;
    }

        /**
    **/
    public AccessConfig modify(List<ContextPath> modify) {

        this.modify = modify;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("modify")
    @Valid
    public List<ContextPath> getModify() {
        return modify;
    }
    public void setModify(List<ContextPath> modify) {
        this.modify = modify;
    }

    public AccessConfig addModifyItem(ContextPath modifyItem) {
        if (this.modify == null) {
            this.modify = new ArrayList<ContextPath>();
        }
        this.modify.add(modifyItem);
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
        AccessConfig accessConfig = (AccessConfig) o;
        return Objects.equals(this.expose, accessConfig.expose) &&
            Objects.equals(this.modify, accessConfig.modify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expose, modify);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccessConfig {\n");
        
        sb.append("    expose: ").append(toIndentedString(expose)).append("\n");
        sb.append("    modify: ").append(toIndentedString(modify)).append("\n");
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


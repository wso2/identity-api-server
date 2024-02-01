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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthorizedAPIPatchModel  {
  
    private List<String> addedScopes = null;

    private List<String> removedScopes = null;


    /**
    **/
    public AuthorizedAPIPatchModel addedScopes(List<String> addedScopes) {

        this.addedScopes = addedScopes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("addedScopes")
    @Valid
    public List<String> getAddedScopes() {
        return addedScopes;
    }
    public void setAddedScopes(List<String> addedScopes) {
        this.addedScopes = addedScopes;
    }

    public AuthorizedAPIPatchModel addAddedScopesItem(String addedScopesItem) {
        if (this.addedScopes == null) {
            this.addedScopes = new ArrayList<>();
        }
        this.addedScopes.add(addedScopesItem);
        return this;
    }

        /**
    **/
    public AuthorizedAPIPatchModel removedScopes(List<String> removedScopes) {

        this.removedScopes = removedScopes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("removedScopes")
    @Valid
    public List<String> getRemovedScopes() {
        return removedScopes;
    }
    public void setRemovedScopes(List<String> removedScopes) {
        this.removedScopes = removedScopes;
    }

    public AuthorizedAPIPatchModel addRemovedScopesItem(String removedScopesItem) {
        if (this.removedScopes == null) {
            this.removedScopes = new ArrayList<>();
        }
        this.removedScopes.add(removedScopesItem);
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
        AuthorizedAPIPatchModel authorizedAPIPatchModel = (AuthorizedAPIPatchModel) o;
        return Objects.equals(this.addedScopes, authorizedAPIPatchModel.addedScopes) &&
            Objects.equals(this.removedScopes, authorizedAPIPatchModel.removedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addedScopes, removedScopes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizedAPIPatchModel {\n");
        
        sb.append("    addedScopes: ").append(toIndentedString(addedScopes)).append("\n");
        sb.append("    removedScopes: ").append(toIndentedString(removedScopes)).append("\n");
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


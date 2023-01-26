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
import org.wso2.carbon.identity.api.server.application.management.v1.Authenticator;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthenticationStepModel  {
  
    private Integer id;
    private List<Authenticator> options = new ArrayList<>();


    /**
    * minimum: 1
    **/
    public AuthenticationStepModel id(Integer id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "1", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")
 @Min(1)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /**
    **/
    public AuthenticationStepModel options(List<Authenticator> options) {

        this.options = options;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("options")
    @Valid
    @NotNull(message = "Property options cannot be null.")
 @Size(min=1)
    public List<Authenticator> getOptions() {
        return options;
    }
    public void setOptions(List<Authenticator> options) {
        this.options = options;
    }

    public AuthenticationStepModel addOptionsItem(Authenticator optionsItem) {
        this.options.add(optionsItem);
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
        AuthenticationStepModel authenticationStepModel = (AuthenticationStepModel) o;
        return Objects.equals(this.id, authenticationStepModel.id) &&
            Objects.equals(this.options, authenticationStepModel.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, options);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationStepModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    options: ").append(toIndentedString(options)).append("\n");
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


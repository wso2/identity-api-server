/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * A single identity provider connection that references a presentation definition.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A single identity provider connection that references a presentation definition.")
public class ConnectedIdpItem  {
  
    private String idpId;
    private String name;
    private String self;

    /**
    * UUID of the identity provider.
    **/
    public ConnectedIdpItem idpId(String idpId) {

        this.idpId = idpId;
        return this;
    }
    
    @ApiModelProperty(example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", value = "UUID of the identity provider.")
    @JsonProperty("idpId")
    @Valid
    public String getIdpId() {
        return idpId;
    }
    public void setIdpId(String idpId) {
        this.idpId = idpId;
    }

    /**
    * Display name of the connection.
    **/
    public ConnectedIdpItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Corporate IDP", value = "Display name of the connection.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * URI of the connection resource in the Identity Provider API.
    **/
    public ConnectedIdpItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/api/server/v1/identity-providers/f47ac10b-58cc-4372-a567-0e02b2c3d479", value = "URI of the connection resource in the Identity Provider API.")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectedIdpItem connectedIdpItem = (ConnectedIdpItem) o;
        return Objects.equals(this.idpId, connectedIdpItem.idpId) &&
            Objects.equals(this.name, connectedIdpItem.name) &&
            Objects.equals(this.self, connectedIdpItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idpId, name, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectedIdpItem {\n");
        
        sb.append("    idpId: ").append(toIndentedString(idpId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
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


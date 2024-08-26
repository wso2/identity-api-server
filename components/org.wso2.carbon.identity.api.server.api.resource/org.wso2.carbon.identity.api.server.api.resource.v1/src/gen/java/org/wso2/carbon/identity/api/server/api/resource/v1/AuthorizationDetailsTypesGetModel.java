/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModelAllOf;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthorizationDetailsTypesGetModel  {
  
    private String id;
    private String type;
    private String name;
    private String description;
    private Map<String, Object> schema = new HashMap<String, Object>();


    /**
    * an unique id of the registered authorization details type
    **/
    public AuthorizationDetailsTypesGetModel id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "a9403470-dd11-46b4-8db9-aaa31f1d4423", required = true, value = "an unique id of the registered authorization details type")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")
 @Size(min=36,max=36)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * an unique type of the authorization details type
    **/
    public AuthorizationDetailsTypesGetModel type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "payment_initiation", required = true, value = "an unique type of the authorization details type")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")
 @Size(min=1,max=255)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * display name of the authorization details type
    **/
    public AuthorizationDetailsTypesGetModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Payment Initiation", required = true, value = "display name of the authorization details type")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")
 @Size(min=3,max=255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * description of the authorization details type
    **/
    public AuthorizationDetailsTypesGetModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Payment initiation authorization details type", required = true, value = "description of the authorization details type")
    @JsonProperty("description")
    @Valid
    @NotNull(message = "Property description cannot be null.")

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Accepts the [JSON Schema document](https://json-schema.org/draft/2020-12/json-schema-core#name-json-schema-documents) of the authorization details type
    **/
    public AuthorizationDetailsTypesGetModel schema(Map<String, Object> schema) {

        this.schema = schema;
        return this;
    }
    
    @ApiModelProperty(example = "{\"type\":\"object\",\"required\":[\"type\",\"actions\",\"locations\",\"instructedAmount\"],\"properties\":{\"type\":{\"type\":\"string\",\"enum\":[\"payment_initiation\"]},\"actions\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"enum\":[\"initiate\",\"cancel\"]}},\"locations\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"format\":\"uri\"}},\"instructedAmount\":{\"type\":\"object\",\"properties\":{\"currency\":{\"type\":\"string\",\"minLength\":3},\"amount\":{\"type\":\"string\"}}},\"creditorName\":\"string\",\"creditorAccount\":{\"type\":\"object\"}}}", required = true, value = "Accepts the [JSON Schema document](https://json-schema.org/draft/2020-12/json-schema-core#name-json-schema-documents) of the authorization details type")
    @JsonProperty("schema")
    @Valid
    @NotNull(message = "Property schema cannot be null.")

    public Map<String, Object> getSchema() {
        return schema;
    }
    public void setSchema(Map<String, Object> schema) {
        this.schema = schema;
    }


    public AuthorizationDetailsTypesGetModel putSchemaItem(String key, Object schemaItem) {
        this.schema.put(key, schemaItem);
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
        AuthorizationDetailsTypesGetModel authorizationDetailsTypesGetModel = (AuthorizationDetailsTypesGetModel) o;
        return Objects.equals(this.id, authorizationDetailsTypesGetModel.id) &&
            Objects.equals(this.type, authorizationDetailsTypesGetModel.type) &&
            Objects.equals(this.name, authorizationDetailsTypesGetModel.name) &&
            Objects.equals(this.description, authorizationDetailsTypesGetModel.description) &&
            Objects.equals(this.schema, authorizationDetailsTypesGetModel.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, schema);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizationDetailsTypesGetModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
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


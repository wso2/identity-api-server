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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationTemplateModel  {
  
    private String id;
    private String name;
    private String description;
    private String image;
    private String authenticationProtocol;
    private List<String> types = null;


@XmlType(name="CategoryEnum")
@XmlEnum(String.class)
public enum CategoryEnum {

    @XmlEnumValue("DEFAULT") DEFAULT(String.valueOf("DEFAULT")), @XmlEnumValue("VENDOR") VENDOR(String.valueOf("VENDOR"));


    private String value;

    CategoryEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static CategoryEnum fromValue(String value) {
        for (CategoryEnum b : CategoryEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private CategoryEnum category;
    private String templateGroup;
    private Integer displayOrder;
    private ApplicationModel application;

    /**
    **/
    public ApplicationTemplateModel id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "85e3f4b8-0d22-4181-b1e3-1651f71b88bd", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public ApplicationTemplateModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "OIDC Protocol Template", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public ApplicationTemplateModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Template to be used for Single Page Applications", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public ApplicationTemplateModel image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/logo/my-logo.png", value = "")
    @JsonProperty("image")
    @Valid
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    /**
    **/
    public ApplicationTemplateModel authenticationProtocol(String authenticationProtocol) {

        this.authenticationProtocol = authenticationProtocol;
        return this;
    }
    
    @ApiModelProperty(example = "oidc", value = "")
    @JsonProperty("authenticationProtocol")
    @Valid
    public String getAuthenticationProtocol() {
        return authenticationProtocol;
    }
    public void setAuthenticationProtocol(String authenticationProtocol) {
        this.authenticationProtocol = authenticationProtocol;
    }

    /**
    **/
    public ApplicationTemplateModel types(List<String> types) {

        this.types = types;
        return this;
    }
    
    @ApiModelProperty(example = "[\"react\",\"angular\"]", value = "")
    @JsonProperty("types")
    @Valid
    public List<String> getTypes() {
        return types;
    }
    public void setTypes(List<String> types) {
        this.types = types;
    }

    public ApplicationTemplateModel addTypesItem(String typesItem) {
        if (this.types == null) {
            this.types = new ArrayList<>();
        }
        this.types.add(typesItem);
        return this;
    }

        /**
    **/
    public ApplicationTemplateModel category(CategoryEnum category) {

        this.category = category;
        return this;
    }
    
    @ApiModelProperty(example = "DEFAULT", value = "")
    @JsonProperty("category")
    @Valid
    public CategoryEnum getCategory() {
        return category;
    }
    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    /**
    **/
    public ApplicationTemplateModel templateGroup(String templateGroup) {

        this.templateGroup = templateGroup;
        return this;
    }
    
    @ApiModelProperty(example = "web-application", value = "")
    @JsonProperty("templateGroup")
    @Valid
    public String getTemplateGroup() {
        return templateGroup;
    }
    public void setTemplateGroup(String templateGroup) {
        this.templateGroup = templateGroup;
    }

    /**
    **/
    public ApplicationTemplateModel displayOrder(Integer displayOrder) {

        this.displayOrder = displayOrder;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "")
    @JsonProperty("displayOrder")
    @Valid
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
    **/
    public ApplicationTemplateModel application(ApplicationModel application) {

        this.application = application;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("application")
    @Valid
    @NotNull(message = "Property application cannot be null.")

    public ApplicationModel getApplication() {
        return application;
    }
    public void setApplication(ApplicationModel application) {
        this.application = application;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationTemplateModel applicationTemplateModel = (ApplicationTemplateModel) o;
        return Objects.equals(this.id, applicationTemplateModel.id) &&
            Objects.equals(this.name, applicationTemplateModel.name) &&
            Objects.equals(this.description, applicationTemplateModel.description) &&
            Objects.equals(this.image, applicationTemplateModel.image) &&
            Objects.equals(this.authenticationProtocol, applicationTemplateModel.authenticationProtocol) &&
            Objects.equals(this.types, applicationTemplateModel.types) &&
            Objects.equals(this.category, applicationTemplateModel.category) &&
            Objects.equals(this.templateGroup, applicationTemplateModel.templateGroup) &&
            Objects.equals(this.displayOrder, applicationTemplateModel.displayOrder) &&
            Objects.equals(this.application, applicationTemplateModel.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, authenticationProtocol, types, category, templateGroup, displayOrder, application);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationTemplateModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    authenticationProtocol: ").append(toIndentedString(authenticationProtocol)).append("\n");
        sb.append("    types: ").append(toIndentedString(types)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    templateGroup: ").append(toIndentedString(templateGroup)).append("\n");
        sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
        sb.append("    application: ").append(toIndentedString(application)).append("\n");
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


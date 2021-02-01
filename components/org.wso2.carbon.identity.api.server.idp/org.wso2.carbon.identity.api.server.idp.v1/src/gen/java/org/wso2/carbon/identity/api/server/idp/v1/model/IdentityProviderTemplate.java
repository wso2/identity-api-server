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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdentityProviderTemplate  {
  
    private String id;
    private String name;
    private String description;
    private String image;

@XmlType(name="CategoryEnum")
@XmlEnum(String.class)
public enum CategoryEnum {

    @XmlEnumValue("DEFAULT") DEFAULT(String.valueOf("DEFAULT")), @XmlEnumValue("CUSTOM") CUSTOM(String.valueOf("CUSTOM"));

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
    private Integer displayOrder;
    private IdentityProviderPOSTRequest idp;

    /**
     * Set ID of the IDP in the identity provider template.
     *
     * @param id ID of the IDP.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "123e4567-e89b-12d3-a456-556642440000", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set IDP name in IDP template object.
     *
     * @param name IDP name.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "google", required = true, value = "")
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
     * Set IDP description in IDP template object.
     *
     * @param description A brief description about the IDP.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Google federated connector", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set IDP image in the IDP template.
     *
     * @param image IDP image.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "google-logo-url", value = "")
    @JsonProperty("image")
    @Valid
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Set category attribute in the IDP template.
     *
     * @param category Category of the IDP.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate category(CategoryEnum category) {

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
     * Set display order of the Identity provider in the template.
     *
     * @param displayOrder Display order of the IDP
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate displayOrder(Integer displayOrder) {

        this.displayOrder = displayOrder;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("displayOrder")
    @Valid
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * Set IDP to the IdentityProviderTemplate object.
     *
     * @param idp {@link IdentityProviderPOSTRequest} object.
     * @return IdentityProviderTemplate.
     */
    public IdentityProviderTemplate idp(IdentityProviderPOSTRequest idp) {

        this.idp = idp;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("idp")
    @Valid
    @NotNull(message = "Property idp cannot be null.")

    public IdentityProviderPOSTRequest getIdp() {
        return idp;
    }
    public void setIdp(IdentityProviderPOSTRequest idp) {
        this.idp = idp;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProviderTemplate identityProviderTemplate = (IdentityProviderTemplate) o;
        return Objects.equals(this.id, identityProviderTemplate.id) &&
            Objects.equals(this.name, identityProviderTemplate.name) &&
            Objects.equals(this.description, identityProviderTemplate.description) &&
            Objects.equals(this.image, identityProviderTemplate.image) &&
            Objects.equals(this.category, identityProviderTemplate.category) &&
            Objects.equals(this.displayOrder, identityProviderTemplate.displayOrder) &&
            Objects.equals(this.idp, identityProviderTemplate.idp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, category, displayOrder, idp);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderTemplate {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
        sb.append("    idp: ").append(toIndentedString(idp)).append("\n");
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


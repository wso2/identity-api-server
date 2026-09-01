/*
 * Copyright (c) 2025-2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ResolvedBrandingPreferenceModelResolvedFrom  {
  

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("ORG") ORG(String.valueOf("ORG")), @XmlEnumValue("APP") APP(String.valueOf("APP")), @XmlEnumValue("CUSTOM") CUSTOM(String.valueOf("CUSTOM"));


    private String value;

    TypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
        for (TypeEnum b : TypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private TypeEnum type;
    private String name;
    private String organization;
    private String application;

    /**
    **/
    public ResolvedBrandingPreferenceModelResolvedFrom type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "ORG", required = true, value = "")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    * Deprecated. Use `organization` and `application` instead.
    **/
    public ResolvedBrandingPreferenceModelResolvedFrom name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "WSO2", required = true, value = "Deprecated. Use `organization` and `application` instead.")
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
    * The organization the preference belongs to. This can be an ancestor organization, when the organization inherits its preference.
    **/
    public ResolvedBrandingPreferenceModelResolvedFrom organization(String organization) {

        this.organization = organization;
        return this;
    }
    
    @ApiModelProperty(example = "WSO2", value = "The organization the preference belongs to. This can be an ancestor organization, when the organization inherits its preference.")
    @JsonProperty("organization")
    @Valid
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
    * The application the preference belongs to. Returned only when the type is APP.
    **/
    public ResolvedBrandingPreferenceModelResolvedFrom application(String application) {

        this.application = application;
        return this;
    }
    
    @ApiModelProperty(example = "fa9b9ac5-a429-49e2-9c51-4259c7ebe45e", value = "The application the preference belongs to. Returned only when the type is APP.")
    @JsonProperty("application")
    @Valid
    public String getApplication() {
        return application;
    }
    public void setApplication(String application) {
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
        ResolvedBrandingPreferenceModelResolvedFrom resolvedBrandingPreferenceModelResolvedFrom = (ResolvedBrandingPreferenceModelResolvedFrom) o;
        return Objects.equals(this.type, resolvedBrandingPreferenceModelResolvedFrom.type) &&
            Objects.equals(this.name, resolvedBrandingPreferenceModelResolvedFrom.name) &&
            Objects.equals(this.organization, resolvedBrandingPreferenceModelResolvedFrom.organization) &&
            Objects.equals(this.application, resolvedBrandingPreferenceModelResolvedFrom.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, organization, application);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResolvedBrandingPreferenceModelResolvedFrom {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
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


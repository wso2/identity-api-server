/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

public class BrandingPreferenceModel  {
  

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
    private String locale = "en-US";
    private Object preference;

    /**
    **/
    public BrandingPreferenceModel type(TypeEnum type) {

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
    **/
    public BrandingPreferenceModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "WSO2", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public BrandingPreferenceModel locale(String locale) {

        this.locale = locale;
        return this;
    }
    
    @ApiModelProperty(example = "en-US", value = "")
    @JsonProperty("locale")
    @Valid
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
    * This is the JSON structured branding preference
    **/
    public BrandingPreferenceModel preference(Object preference) {

        this.preference = preference;
        return this;
    }
    
    @ApiModelProperty(example = "{\"organizationDetails\":{\"displayName\":\"Ballerina.io\",\"siteTitle\":\"Login - Ballerina\",\"copyrightText\":\"© 2021 WSO2\",\"supportEmail\":\"support@ballerina.io\"},\"images\":{\"logo\":{\"imgURL\":\"https://ballerina.io/img/ballerina-logo.svg\",\"altText\":\"Ballerina.io Logo\"},\"favicon\":{\"imgURL\":\"https://central.ballerina.io/favicon.ico\"}},\"urls\":{\"privacyPolicyURL\":\"https://ballerina.io/privacy-policy\",\"termsOfUseURL\":\"https://ballerina.io/terms-of-service/\",\"cookiePolicyURL\":\"https://ballerina.io/privacy-policy/#cookie-policy\"},\"stylesheets\":{\"accountApp\":\"https://firebasestorage.googleapis.com/v0/b/asgardeo-branding.appspot.com/o/ballerina%2Flogin-portal.overrides.css?alt=media&token=0315462e-534e-4f33-83f9-e4c092d0273d\",\"myAccountApp\":\"https://asgardeo-branding/user-portal.css\"},\"configs\":{\"isBrandingEnabled\":true,\"removeDefaultBranding\":false,\"selfSignUpEnabled\":true}}", required = true, value = "This is the JSON structured branding preference")
    @JsonProperty("preference")
    @Valid
    @NotNull(message = "Property preference cannot be null.")

    public Object getPreference() {
        return preference;
    }
    public void setPreference(Object preference) {
        this.preference = preference;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandingPreferenceModel brandingPreferenceModel = (BrandingPreferenceModel) o;
        return Objects.equals(this.type, brandingPreferenceModel.type) &&
            Objects.equals(this.name, brandingPreferenceModel.name) &&
            Objects.equals(this.locale, brandingPreferenceModel.locale) &&
            Objects.equals(this.preference, brandingPreferenceModel.preference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, locale, preference);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BrandingPreferenceModel {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
        sb.append("    preference: ").append(toIndentedString(preference)).append("\n");
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


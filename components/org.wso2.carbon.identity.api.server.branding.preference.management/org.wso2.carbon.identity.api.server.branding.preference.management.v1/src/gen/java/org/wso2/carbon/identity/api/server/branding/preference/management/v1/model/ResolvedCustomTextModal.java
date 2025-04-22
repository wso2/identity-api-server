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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.ResolvedBrandingPreferenceModelResolvedFrom;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ResolvedCustomTextModal  {
  

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
    private String screen;
    private ResolvedBrandingPreferenceModelResolvedFrom resolvedFrom;
    private Object preference;

    /**
    **/
    public ResolvedCustomTextModal type(TypeEnum type) {

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
    public ResolvedCustomTextModal name(String name) {

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
    public ResolvedCustomTextModal locale(String locale) {

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
    **/
    public ResolvedCustomTextModal screen(String screen) {

        this.screen = screen;
        return this;
    }
    
    @ApiModelProperty(example = "login", required = true, value = "")
    @JsonProperty("screen")
    @Valid
    @NotNull(message = "Property screen cannot be null.")

    public String getScreen() {
        return screen;
    }
    public void setScreen(String screen) {
        this.screen = screen;
    }

    /**
    **/
    public ResolvedCustomTextModal resolvedFrom(ResolvedBrandingPreferenceModelResolvedFrom resolvedFrom) {

        this.resolvedFrom = resolvedFrom;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("resolvedFrom")
    @Valid
    public ResolvedBrandingPreferenceModelResolvedFrom getResolvedFrom() {
        return resolvedFrom;
    }
    public void setResolvedFrom(ResolvedBrandingPreferenceModelResolvedFrom resolvedFrom) {
        this.resolvedFrom = resolvedFrom;
    }

    /**
    * This is the JSON structured branding preference
    **/
    public ResolvedCustomTextModal preference(Object preference) {

        this.preference = preference;
        return this;
    }
    
    @ApiModelProperty(example = "{\"login\":\"Sign In\",\"welcome\":\"Welcome\",\"account.linking\":\"Account Linking\",\"username\":\"Username\",\"email.username\":\"Email address\",\"back.to.sign.in\":\"Back to Sign In\",\"or\":\"Or\",\"dont.have.an.account\":\"Don't have an account?\"}", required = true, value = "This is the JSON structured branding preference")
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
        ResolvedCustomTextModal resolvedCustomTextModal = (ResolvedCustomTextModal) o;
        return Objects.equals(this.type, resolvedCustomTextModal.type) &&
            Objects.equals(this.name, resolvedCustomTextModal.name) &&
            Objects.equals(this.locale, resolvedCustomTextModal.locale) &&
            Objects.equals(this.screen, resolvedCustomTextModal.screen) &&
            Objects.equals(this.resolvedFrom, resolvedCustomTextModal.resolvedFrom) &&
            Objects.equals(this.preference, resolvedCustomTextModal.preference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, locale, screen, resolvedFrom, preference);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResolvedCustomTextModal {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
        sb.append("    screen: ").append(toIndentedString(screen)).append("\n");
        sb.append("    resolvedFrom: ").append(toIndentedString(resolvedFrom)).append("\n");
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


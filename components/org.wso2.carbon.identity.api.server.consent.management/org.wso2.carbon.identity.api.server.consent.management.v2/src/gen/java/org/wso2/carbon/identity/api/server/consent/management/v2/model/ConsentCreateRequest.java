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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentPurposeBinding;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentCreateRequest  {
  
    private String subjectId;
    private String serviceId;
    private String language;
    private List<ConsentPurposeBinding> purposes = new ArrayList<>();


@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED"));


    private String value;

    StateEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StateEnum fromValue(String value) {
        for (StateEnum b : StateEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StateEnum state = StateEnum.ACTIVE;
    private Long validityTime;
    private List<String> authorizations = null;

    private Map<String, String> properties = null;


    /**
    * Username of the user giving consent. If omitted, defaults to the authenticated caller. Required when the subject differs from the caller (delegated consent). 
    **/
    public ConsentCreateRequest subjectId(String subjectId) {

        this.subjectId = subjectId;
        return this;
    }
    
    @ApiModelProperty(example = "alice@wso2.com", value = "Username of the user giving consent. If omitted, defaults to the authenticated caller. Required when the subject differs from the caller (delegated consent). ")
    @JsonProperty("subjectId")
    @Valid @Size(min=1,max=255)
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
    * Service requesting consent
    **/
    public ConsentCreateRequest serviceId(String serviceId) {

        this.serviceId = serviceId;
        return this;
    }
    
    @ApiModelProperty(example = "admin-dashboard", required = true, value = "Service requesting consent")
    @JsonProperty("serviceId")
    @Valid
    @NotNull(message = "Property serviceId cannot be null.")
 @Size(min=1,max=255)
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
    * Language code for the consent
    **/
    public ConsentCreateRequest language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(example = "en", value = "Language code for the consent")
    @JsonProperty("language")
    @Valid @Size(min=1,max=20)
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
    * List of purposes and associated elements
    **/
    public ConsentCreateRequest purposes(List<ConsentPurposeBinding> purposes) {

        this.purposes = purposes;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of purposes and associated elements")
    @JsonProperty("purposes")
    @Valid
    @NotNull(message = "Property purposes cannot be null.")
 @Size(min=1)
    public List<ConsentPurposeBinding> getPurposes() {
        return purposes;
    }
    public void setPurposes(List<ConsentPurposeBinding> purposes) {
        this.purposes = purposes;
    }

    public ConsentCreateRequest addPurposesItem(ConsentPurposeBinding purposesItem) {
        this.purposes.add(purposesItem);
        return this;
    }

        /**
    * Initial state of the consent. Use ACTIVE when the user accepts (default). Use REJECTED when the user explicitly declines an optional policy — this records that the user saw and skipped this version so it is not shown again until a new version is published. PENDING is set automatically when authorizations are provided and cannot be specified here. 
    **/
    public ConsentCreateRequest state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Initial state of the consent. Use ACTIVE when the user accepts (default). Use REJECTED when the user explicitly declines an optional policy — this records that the user saw and skipped this version so it is not shown again until a new version is published. PENDING is set automatically when authorizations are provided and cannot be specified here. ")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
    * Milliseconds since epoch until which the consent is valid. If omitted, the consent does not expire.
    **/
    public ConsentCreateRequest validityTime(Long validityTime) {

        this.validityTime = validityTime;
        return this;
    }
    
    @ApiModelProperty(example = "1766383796000", value = "Milliseconds since epoch until which the consent is valid. If omitted, the consent does not expire.")
    @JsonProperty("validityTime")
    @Valid
    public Long getValidityTime() {
        return validityTime;
    }
    public void setValidityTime(Long validityTime) {
        this.validityTime = validityTime;
    }

    /**
    * Optional list of user IDs who are expected to authorize this consent. Each user will use the /authorize endpoint to give their actual consent.
    **/
    public ConsentCreateRequest authorizations(List<String> authorizations) {

        this.authorizations = authorizations;
        return this;
    }
    
    @ApiModelProperty(example = "[\"a1b2c3d4-1234-5678-abcd-ef1234567890\",\"b2c3d4e5-2345-6789-bcde-f01234567891\"]", value = "Optional list of user IDs who are expected to authorize this consent. Each user will use the /authorize endpoint to give their actual consent.")
    @JsonProperty("authorizations")
    @Valid
    public List<String> getAuthorizations() {
        return authorizations;
    }
    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }

    public ConsentCreateRequest addAuthorizationsItem(String authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }
        this.authorizations.add(authorizationsItem);
        return this;
    }

        /**
    * Optional properties as key-value pairs
    **/
    public ConsentCreateRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Optional properties as key-value pairs")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ConsentCreateRequest putPropertiesItem(String key, String propertiesItem) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, propertiesItem);
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
        ConsentCreateRequest consentCreateRequest = (ConsentCreateRequest) o;
        return Objects.equals(this.subjectId, consentCreateRequest.subjectId) &&
            Objects.equals(this.serviceId, consentCreateRequest.serviceId) &&
            Objects.equals(this.language, consentCreateRequest.language) &&
            Objects.equals(this.purposes, consentCreateRequest.purposes) &&
            Objects.equals(this.state, consentCreateRequest.state) &&
            Objects.equals(this.validityTime, consentCreateRequest.validityTime) &&
            Objects.equals(this.authorizations, consentCreateRequest.authorizations) &&
            Objects.equals(this.properties, consentCreateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, serviceId, language, purposes, state, validityTime, authorizations, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentCreateRequest {\n");
        
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    purposes: ").append(toIndentedString(purposes)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
        sb.append("    authorizations: ").append(toIndentedString(authorizations)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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


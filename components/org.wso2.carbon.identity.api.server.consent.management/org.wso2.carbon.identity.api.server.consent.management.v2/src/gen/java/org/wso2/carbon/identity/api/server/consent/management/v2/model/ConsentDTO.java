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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentedPurposeDTO;

import javax.validation.constraints.*;

/**
 * Complete consent details
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Complete consent details")
public class ConsentDTO  {
  
    private Long timestamp;
    private String id;
    private String language;
    private String subjectId;
    private String serviceId;

@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("PENDING") PENDING(String.valueOf("PENDING")), @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED")), @XmlEnumValue("REVOKED") REVOKED(String.valueOf("REVOKED")), @XmlEnumValue("EXPIRED") EXPIRED(String.valueOf("EXPIRED"));


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

    private StateEnum state;
    private Long expiryTime;
    private ConsentedPurposeDTO purpose;

    private List<AuthorizationDTO> authorizations = null;

    private Map<String, String> properties = null;


    /**
    * Timestamp of consent (milliseconds since epoch)
    **/
    public ConsentDTO timestamp(Long timestamp) {

        this.timestamp = timestamp;
        return this;
    }
    
    @ApiModelProperty(example = "1677686400000", value = "Timestamp of consent (milliseconds since epoch)")
    @JsonProperty("timestamp")
    @Valid
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
    * Unique identifier of the consent
    **/
    public ConsentDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "Unique identifier of the consent")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Language code
    **/
    public ConsentDTO language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(example = "en", value = "Language code")
    @JsonProperty("language")
    @Valid
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
    * Username of the user giving consent
    **/
    public ConsentDTO subjectId(String subjectId) {

        this.subjectId = subjectId;
        return this;
    }
    
    @ApiModelProperty(example = "alice@wso2.com", value = "Username of the user giving consent")
    @JsonProperty("subjectId")
    @Valid
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
    * Service name
    **/
    public ConsentDTO serviceId(String serviceId) {

        this.serviceId = serviceId;
        return this;
    }
    
    @ApiModelProperty(example = "admin-dashboard", value = "Service name")
    @JsonProperty("serviceId")
    @Valid
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
    * PENDING if awaiting approvals, ACTIVE if all accepted, REJECTED if any rejected before activation, REVOKED if any user revoked after activation, EXPIRED if expiryTime has passed.
    **/
    public ConsentDTO state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "PENDING if awaiting approvals, ACTIVE if all accepted, REJECTED if any rejected before activation, REVOKED if any user revoked after activation, EXPIRED if expiryTime has passed.")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
    * Milliseconds since epoch until which the consent is valid. Null if no expiry.
    **/
    public ConsentDTO expiryTime(Long expiryTime) {

        this.expiryTime = expiryTime;
        return this;
    }
    
    @ApiModelProperty(example = "1766383796000", value = "Milliseconds since epoch until which the consent is valid. Null if no expiry.")
    @JsonProperty("expiryTime")
    @Valid
    public Long getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
    * Purpose and elements in this consent
    **/
    public ConsentDTO purpose(ConsentedPurposeDTO purpose) {

        this.purpose = purpose;
        return this;
    }

    @ApiModelProperty(value = "Purpose and elements in this consent")
    @JsonProperty("purpose")
    @Valid
    public ConsentedPurposeDTO getPurpose() {
        return purpose;
    }
    public void setPurpose(ConsentedPurposeDTO purpose) {
        this.purpose = purpose;
    }

        /**
    * Authorization records associated with this consent
    **/
    public ConsentDTO authorizations(List<AuthorizationDTO> authorizations) {

        this.authorizations = authorizations;
        return this;
    }
    
    @ApiModelProperty(value = "Authorization records associated with this consent")
    @JsonProperty("authorizations")
    @Valid
    public List<AuthorizationDTO> getAuthorizations() {
        return authorizations;
    }
    public void setAuthorizations(List<AuthorizationDTO> authorizations) {
        this.authorizations = authorizations;
    }

    public ConsentDTO addAuthorizationsItem(AuthorizationDTO authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }
        this.authorizations.add(authorizationsItem);
        return this;
    }

        /**
    * Properties associated with this consent
    **/
    public ConsentDTO properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Properties associated with this consent")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ConsentDTO putPropertiesItem(String key, String propertiesItem) {
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
        ConsentDTO consentDTO = (ConsentDTO) o;
        return Objects.equals(this.timestamp, consentDTO.timestamp) &&
            Objects.equals(this.id, consentDTO.id) &&
            Objects.equals(this.language, consentDTO.language) &&
            Objects.equals(this.subjectId, consentDTO.subjectId) &&
            Objects.equals(this.serviceId, consentDTO.serviceId) &&
            Objects.equals(this.state, consentDTO.state) &&
            Objects.equals(this.expiryTime, consentDTO.expiryTime) &&
            Objects.equals(this.purpose, consentDTO.purpose) &&
            Objects.equals(this.authorizations, consentDTO.authorizations) &&
            Objects.equals(this.properties, consentDTO.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, id, language, subjectId, serviceId, state, expiryTime, purpose, authorizations, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentDTO {\n");
        
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    expiryTime: ").append(toIndentedString(expiryTime)).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
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


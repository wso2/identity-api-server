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
import javax.validation.constraints.*;

/**
 * Response returned when consent is recorded
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Response returned when consent is recorded")
public class ConsentResponseDTO  {
  
    private String id;
    private String language;
    private String subjectId;
    private String tenantDomain;

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

    /**
    * Unique identifier for this consent
    **/
    public ConsentResponseDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "Unique identifier for this consent")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Language code of the consent
    **/
    public ConsentResponseDTO language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(example = "en", value = "Language code of the consent")
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
    public ConsentResponseDTO subjectId(String subjectId) {

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
    * Tenant domain
    **/
    public ConsentResponseDTO tenantDomain(String tenantDomain) {

        this.tenantDomain = tenantDomain;
        return this;
    }
    
    @ApiModelProperty(example = "carbon.super", value = "Tenant domain")
    @JsonProperty("tenantDomain")
    @Valid
    public String getTenantDomain() {
        return tenantDomain;
    }
    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    /**
    * PENDING if awaiting approvals, ACTIVE if all accepted, REJECTED if any rejected before activation, REVOKED if any user revoked after activation, EXPIRED if expiryTime has passed.
    **/
    public ConsentResponseDTO state(StateEnum state) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsentResponseDTO consentResponseDTO = (ConsentResponseDTO) o;
        return Objects.equals(this.id, consentResponseDTO.id) &&
            Objects.equals(this.language, consentResponseDTO.language) &&
            Objects.equals(this.subjectId, consentResponseDTO.subjectId) &&
            Objects.equals(this.tenantDomain, consentResponseDTO.tenantDomain) &&
            Objects.equals(this.state, consentResponseDTO.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language, subjectId, tenantDomain, state);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentResponseDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    tenantDomain: ").append(toIndentedString(tenantDomain)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
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


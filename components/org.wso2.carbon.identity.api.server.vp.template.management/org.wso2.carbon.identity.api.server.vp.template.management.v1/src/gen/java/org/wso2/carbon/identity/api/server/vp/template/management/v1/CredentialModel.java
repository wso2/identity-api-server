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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationClaimModel;
import javax.validation.constraints.*;

/**
 * Specifies a single credential query within a presentation definition.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Specifies a single credential query within a presentation definition.")
public class CredentialModel  {

    private String id;
    private String type;

@XmlType(name="FormatEnum")
@XmlEnum(String.class)
public enum FormatEnum {

    @XmlEnumValue("dc+sd-jwt") DC_SD_JWT(String.valueOf("dc+sd-jwt")), @XmlEnumValue("mso_mdoc") MSO_MDOC(String.valueOf("mso_mdoc")), @XmlEnumValue("jwt_vc_json") JWT_VC_JSON(String.valueOf("jwt_vc_json"));


    private String value;

    FormatEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FormatEnum fromValue(String value) {
        for (FormatEnum b : FormatEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private FormatEnum format = FormatEnum.DC_SD_JWT;
    private List<PresentationClaimModel> claims = null;


    /**
    * User-defined identifier for this credential query within the definition. Used to correlate the wallet&#39;s response to this specific request.
    **/
    public CredentialModel id(String id) {

        this.id = id;
        return this;
    }

    @ApiModelProperty(example = "employee_id", required = true, value = "User-defined identifier for this credential query within the definition. Used to correlate the wallet's response to this specific request. ")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * The credential type (vct) to request from the wallet.
    **/
    public CredentialModel type(String type) {

        this.type = type;
        return this;
    }

    @ApiModelProperty(example = "EmployeeIDCredential", required = true, value = "The credential type (vct) to request from the wallet.")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Credential format as defined by OID4VP §6.1. Defaults to dc+sd-jwt when not specified.
    **/
    public CredentialModel format(FormatEnum format) {

        this.format = format;
        return this;
    }

    @ApiModelProperty(example = "dc+sd-jwt", value = "Credential format as defined by OID4VP §6.1. Defaults to dc+sd-jwt when not specified. ")
    @JsonProperty("format")
    @Valid
    public FormatEnum getFormat() {
        return format;
    }
    public void setFormat(FormatEnum format) {
        this.format = format;
    }

    /**
    * Claim constraints that must be satisfied by the presented credential.
    **/
    public CredentialModel claims(List<PresentationClaimModel> claims) {

        this.claims = claims;
        return this;
    }

    @ApiModelProperty(value = "Claim constraints that must be satisfied by the presented credential.")
    @JsonProperty("claims")
    @Valid
    public List<PresentationClaimModel> getClaims() {
        return claims;
    }
    public void setClaims(List<PresentationClaimModel> claims) {
        this.claims = claims;
    }

    public CredentialModel addClaimsItem(PresentationClaimModel claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<>();
        }
        this.claims.add(claimsItem);
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
        CredentialModel credentialModel = (CredentialModel) o;
        return Objects.equals(this.id, credentialModel.id) &&
            Objects.equals(this.type, credentialModel.type) &&
            Objects.equals(this.format, credentialModel.format) &&
            Objects.equals(this.claims, credentialModel.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, format, claims);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CredentialModel {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
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


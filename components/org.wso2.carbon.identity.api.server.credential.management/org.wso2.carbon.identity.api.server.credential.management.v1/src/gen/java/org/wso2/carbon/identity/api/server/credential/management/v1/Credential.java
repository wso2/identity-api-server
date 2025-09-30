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

package org.wso2.carbon.identity.api.server.credential.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Credential  {
  
    private String credentialId;
    private String displayName;

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("passkey") PASSKEY(String.valueOf("passkey")), @XmlEnumValue("push-auth") PUSH_AUTH(String.valueOf("push-auth"));


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

    /**
    * The unique identifier for the credential.
    **/
    public Credential credentialId(String credentialId) {

        this.credentialId = credentialId;
        return this;
    }
    
    @ApiModelProperty(example = "a5a81c76-27a3-42d4-82a8-55285d82a4a1", value = "The unique identifier for the credential.")
    @JsonProperty("credentialId")
    @Valid
    public String getCredentialId() {
        return credentialId;
    }
    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    /**
    * A user-friendly name for the credential (e.g., \&quot;John&#39;s iPhone\&quot; or \&quot;YubiKey 5\&quot;).
    **/
    public Credential displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "YubiKey 5C", value = "A user-friendly name for the credential (e.g., \"John's iPhone\" or \"YubiKey 5\").")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * The type of the credential.
    **/
    public Credential type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "The type of the credential.")
    @JsonProperty("type")
    @Valid
    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Credential credential = (Credential) o;
        return Objects.equals(this.credentialId, credential.credentialId) &&
            Objects.equals(this.displayName, credential.displayName) &&
            Objects.equals(this.type, credential.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialId, displayName, type);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Credential {\n");
        
        sb.append("    credentialId: ").append(toIndentedString(credentialId)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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


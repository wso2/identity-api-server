/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLAssertionConfiguration;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SingleSignOnProfile  {
  

@XmlType(name="BindingsEnum")
@XmlEnum(String.class)
public enum BindingsEnum {

    @XmlEnumValue("HTTP_POST") HTTP_POST(String.valueOf("HTTP_POST")), @XmlEnumValue("HTTP_REDIRECT") HTTP_REDIRECT(String.valueOf("HTTP_REDIRECT")), @XmlEnumValue("ARTIFACT") ARTIFACT(String.valueOf("ARTIFACT"));


    private String value;

    BindingsEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static BindingsEnum fromValue(String value) {
        for (BindingsEnum b : BindingsEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private List<BindingsEnum> bindings = null;

    private Boolean enableSignatureValidationForArtifactBinding = false;
    private String attributeConsumingServiceIndex;
    private Boolean enableIdpInitiatedSingleSignOn = false;
    private SAMLAssertionConfiguration assertion;

    /**
    **/
    public SingleSignOnProfile bindings(List<BindingsEnum> bindings) {

        this.bindings = bindings;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("bindings")
    @Valid
    public List<BindingsEnum> getBindings() {
        return bindings;
    }
    public void setBindings(List<BindingsEnum> bindings) {
        this.bindings = bindings;
    }

    public SingleSignOnProfile addBindingsItem(BindingsEnum bindingsItem) {
        if (this.bindings == null) {
            this.bindings = new ArrayList<>();
        }
        this.bindings.add(bindingsItem);
        return this;
    }

        /**
    * Enables Signature validation for SAML Artifact Binding. Applicable only if SAML Artifact binding is enabled through the bindings option.
    **/
    public SingleSignOnProfile enableSignatureValidationForArtifactBinding(Boolean enableSignatureValidationForArtifactBinding) {

        this.enableSignatureValidationForArtifactBinding = enableSignatureValidationForArtifactBinding;
        return this;
    }
    
    @ApiModelProperty(value = "Enables Signature validation for SAML Artifact Binding. Applicable only if SAML Artifact binding is enabled through the bindings option.")
    @JsonProperty("enableSignatureValidationForArtifactBinding")
    @Valid
    public Boolean getEnableSignatureValidationForArtifactBinding() {
        return enableSignatureValidationForArtifactBinding;
    }
    public void setEnableSignatureValidationForArtifactBinding(Boolean enableSignatureValidationForArtifactBinding) {
        this.enableSignatureValidationForArtifactBinding = enableSignatureValidationForArtifactBinding;
    }

    /**
    **/
    public SingleSignOnProfile attributeConsumingServiceIndex(String attributeConsumingServiceIndex) {

        this.attributeConsumingServiceIndex = attributeConsumingServiceIndex;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("attributeConsumingServiceIndex")
    @Valid
    public String getAttributeConsumingServiceIndex() {
        return attributeConsumingServiceIndex;
    }
    public void setAttributeConsumingServiceIndex(String attributeConsumingServiceIndex) {
        this.attributeConsumingServiceIndex = attributeConsumingServiceIndex;
    }

    /**
    **/
    public SingleSignOnProfile enableIdpInitiatedSingleSignOn(Boolean enableIdpInitiatedSingleSignOn) {

        this.enableIdpInitiatedSingleSignOn = enableIdpInitiatedSingleSignOn;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableIdpInitiatedSingleSignOn")
    @Valid
    public Boolean getEnableIdpInitiatedSingleSignOn() {
        return enableIdpInitiatedSingleSignOn;
    }
    public void setEnableIdpInitiatedSingleSignOn(Boolean enableIdpInitiatedSingleSignOn) {
        this.enableIdpInitiatedSingleSignOn = enableIdpInitiatedSingleSignOn;
    }

    /**
    **/
    public SingleSignOnProfile assertion(SAMLAssertionConfiguration assertion) {

        this.assertion = assertion;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("assertion")
    @Valid
    public SAMLAssertionConfiguration getAssertion() {
        return assertion;
    }
    public void setAssertion(SAMLAssertionConfiguration assertion) {
        this.assertion = assertion;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingleSignOnProfile singleSignOnProfile = (SingleSignOnProfile) o;
        return Objects.equals(this.bindings, singleSignOnProfile.bindings) &&
            Objects.equals(this.enableSignatureValidationForArtifactBinding, singleSignOnProfile.enableSignatureValidationForArtifactBinding) &&
            Objects.equals(this.attributeConsumingServiceIndex, singleSignOnProfile.attributeConsumingServiceIndex) &&
            Objects.equals(this.enableIdpInitiatedSingleSignOn, singleSignOnProfile.enableIdpInitiatedSingleSignOn) &&
            Objects.equals(this.assertion, singleSignOnProfile.assertion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindings, enableSignatureValidationForArtifactBinding, attributeConsumingServiceIndex, enableIdpInitiatedSingleSignOn, assertion);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SingleSignOnProfile {\n");
        
        sb.append("    bindings: ").append(toIndentedString(bindings)).append("\n");
        sb.append("    enableSignatureValidationForArtifactBinding: ").append(toIndentedString(enableSignatureValidationForArtifactBinding)).append("\n");
        sb.append("    attributeConsumingServiceIndex: ").append(toIndentedString(attributeConsumingServiceIndex)).append("\n");
        sb.append("    enableIdpInitiatedSingleSignOn: ").append(toIndentedString(enableIdpInitiatedSingleSignOn)).append("\n");
        sb.append("    assertion: ").append(toIndentedString(assertion)).append("\n");
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


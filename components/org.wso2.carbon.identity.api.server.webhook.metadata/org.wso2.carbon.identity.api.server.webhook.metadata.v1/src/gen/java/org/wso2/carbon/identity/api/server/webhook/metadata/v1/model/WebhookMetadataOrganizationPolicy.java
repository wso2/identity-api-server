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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WebhookMetadataOrganizationPolicy  {
  

@XmlType(name="PolicyNameEnum")
@XmlEnum(String.class)
public enum PolicyNameEnum {

    @XmlEnumValue("IMMEDIATE_EXISTING_AND_FUTURE_ORGS") IMMEDIATE_EXISTING_AND_FUTURE_ORGS(String.valueOf("IMMEDIATE_EXISTING_AND_FUTURE_ORGS")), @XmlEnumValue("NO_SHARING") NO_SHARING(String.valueOf("NO_SHARING"));


    private String value;

    PolicyNameEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static PolicyNameEnum fromValue(String value) {
        for (PolicyNameEnum b : PolicyNameEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private PolicyNameEnum policyName;

    /**
    * Webhook consuming organization policy name.
    **/
    public WebhookMetadataOrganizationPolicy policyName(PolicyNameEnum policyName) {

        this.policyName = policyName;
        return this;
    }
    
    @ApiModelProperty(example = "IMMEDIATE_EXISTING_AND_FUTURE_ORGS", value = "Webhook consuming organization policy name.")
    @JsonProperty("policyName")
    @Valid
    public PolicyNameEnum getPolicyName() {
        return policyName;
    }
    public void setPolicyName(PolicyNameEnum policyName) {
        this.policyName = policyName;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebhookMetadataOrganizationPolicy webhookMetadataOrganizationPolicy = (WebhookMetadataOrganizationPolicy) o;
        return Objects.equals(this.policyName, webhookMetadataOrganizationPolicy.policyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyName);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookMetadataOrganizationPolicy {\n");
        
        sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
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


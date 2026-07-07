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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleRequest;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PolicyResourceRequest  {
  
    private String target;

@XmlType(name="ResourceTypeEnum")
@XmlEnum(String.class)
public enum ResourceTypeEnum {

    @XmlEnumValue("RULE") RULE(String.valueOf("RULE")), @XmlEnumValue("ACTION") ACTION(String.valueOf("ACTION"));


    private String value;

    ResourceTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ResourceTypeEnum fromValue(String value) {
        for (ResourceTypeEnum b : ResourceTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ResourceTypeEnum resourceType = ResourceTypeEnum.RULE;
    private RuleRequest rule;

    /**
    * The selector value this resource applies to (e.g. platform - android, ios, macos, windows).
    **/
    public PolicyResourceRequest target(String target) {

        this.target = target;
        return this;
    }
    
    @ApiModelProperty(example = "ios", required = true, value = "The selector value this resource applies to (e.g. platform - android, ios, macos, windows).")
    @JsonProperty("target")
    @Valid
    @NotNull(message = "Property target cannot be null.")

    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }

    /**
    * The type of resource. Defaults to RULE. Only RULE is currently supported.
    **/
    public PolicyResourceRequest resourceType(ResourceTypeEnum resourceType) {

        this.resourceType = resourceType;
        return this;
    }
    
    @ApiModelProperty(example = "RULE", value = "The type of resource. Defaults to RULE. Only RULE is currently supported.")
    @JsonProperty("resourceType")
    @Valid
    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }
    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    /**
    **/
    public PolicyResourceRequest rule(RuleRequest rule) {

        this.rule = rule;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("rule")
    @Valid
    @NotNull(message = "Property rule cannot be null.")

    public RuleRequest getRule() {
        return rule;
    }
    public void setRule(RuleRequest rule) {
        this.rule = rule;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyResourceRequest policyResourceRequest = (PolicyResourceRequest) o;
        return Objects.equals(this.target, policyResourceRequest.target) &&
            Objects.equals(this.resourceType, policyResourceRequest.resourceType) &&
            Objects.equals(this.rule, policyResourceRequest.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, resourceType, rule);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyResourceRequest {\n");
        
        sb.append("    target: ").append(toIndentedString(target)).append("\n");
        sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
        sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
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


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
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PolicyResourceResponse  {
  
    private String id;
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

    private ResourceTypeEnum resourceType;
    private String resourceId;
    private RuleResponse rule;

    /**
    * The policy-resource association ID.
    **/
    public PolicyResourceResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "pr-456", value = "The policy-resource association ID.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * The selector value this resource applies to (e.g. device platform).
    **/
    public PolicyResourceResponse target(String target) {

        this.target = target;
        return this;
    }
    
    @ApiModelProperty(example = "ios", value = "The selector value this resource applies to (e.g. device platform).")
    @JsonProperty("target")
    @Valid
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }

    /**
    * The type of resource.
    **/
    public PolicyResourceResponse resourceType(ResourceTypeEnum resourceType) {

        this.resourceType = resourceType;
        return this;
    }
    
    @ApiModelProperty(example = "RULE", value = "The type of resource.")
    @JsonProperty("resourceType")
    @Valid
    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }
    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    /**
    * The ID of the attached resource (rule ID or action ID).
    **/
    public PolicyResourceResponse resourceId(String resourceId) {

        this.resourceId = resourceId;
        return this;
    }
    
    @ApiModelProperty(example = "rule-123", value = "The ID of the attached resource (rule ID or action ID).")
    @JsonProperty("resourceId")
    @Valid
    public String getResourceId() {
        return resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
    **/
    public PolicyResourceResponse rule(RuleResponse rule) {

        this.rule = rule;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("rule")
    @Valid
    public RuleResponse getRule() {
        return rule;
    }
    public void setRule(RuleResponse rule) {
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
        PolicyResourceResponse policyResourceResponse = (PolicyResourceResponse) o;
        return Objects.equals(this.id, policyResourceResponse.id) &&
            Objects.equals(this.target, policyResourceResponse.target) &&
            Objects.equals(this.resourceType, policyResourceResponse.resourceType) &&
            Objects.equals(this.resourceId, policyResourceResponse.resourceId) &&
            Objects.equals(this.rule, policyResourceResponse.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, target, resourceType, resourceId, rule);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyResourceResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    target: ").append(toIndentedString(target)).append("\n");
        sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
        sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
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


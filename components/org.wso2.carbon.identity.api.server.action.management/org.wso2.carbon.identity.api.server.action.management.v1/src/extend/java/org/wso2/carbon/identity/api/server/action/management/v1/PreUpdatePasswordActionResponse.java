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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

/**
 * Pre Update Password Action Response.
 */
public class PreUpdatePasswordActionResponse extends ActionResponse {

    private PasswordSharing passwordSharing;
    private List<String> attributes;

    public PreUpdatePasswordActionResponse(ActionResponse actionResponse) {

        setId(actionResponse.getId());
        setType(actionResponse.getType());
        setName(actionResponse.getName());
        setDescription(actionResponse.getDescription());
        setStatus(actionResponse.getStatus());
        setCreatedAt(actionResponse.getCreatedAt());
        setUpdatedAt(actionResponse.getUpdatedAt());
        setEndpoint(actionResponse.getEndpoint());
        setRule(actionResponse.getRule());
    }

    public PreUpdatePasswordActionResponse passwordSharing(PasswordSharing passwordSharing) {

        this.passwordSharing = passwordSharing;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("passwordSharing")
    @Valid
    public PasswordSharing getPasswordSharing() {

        return passwordSharing;
    }

    public void setPasswordSharing(PasswordSharing passwordSharing) {

        this.passwordSharing = passwordSharing;
    }

    public PreUpdatePasswordActionResponse attributes(List<String> attributes) {

        this.attributes = attributes;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("attributes")
    @Valid
    public List<String> getAttributes() {

        return attributes;
    }

    public void setAttributes(List<String> attributes) {

        this.attributes = attributes;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreUpdatePasswordActionResponse actionResponse = (PreUpdatePasswordActionResponse) o;
        return Objects.equals(this.getId(), actionResponse.getId()) &&
                Objects.equals(this.getType(), actionResponse.getType()) &&
                Objects.equals(this.getName(), actionResponse.getName()) &&
                Objects.equals(this.getDescription(), actionResponse.getDescription()) &&
                Objects.equals(this.getStatus(), actionResponse.getStatus()) &&
                Objects.equals(this.getCreatedAt(), actionResponse.getCreatedAt()) &&
                Objects.equals(this.getUpdatedAt(), actionResponse.getUpdatedAt()) &&
                Objects.equals(this.getEndpoint(), actionResponse.getEndpoint()) &&
                Objects.equals(this.passwordSharing, actionResponse.passwordSharing) &&
                Objects.equals(this.attributes, actionResponse.attributes) &&
                Objects.equals(this.getRule(), actionResponse.getRule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getName(), getDescription(), getStatus(), getCreatedAt(),
                getUpdatedAt(), getEndpoint(), passwordSharing, attributes, getRule());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ActionResponse {\n");
        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    type: ").append(toIndentedString(getType())).append("\n");
        sb.append("    name: ").append(toIndentedString(getName())).append("\n");
        sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
        sb.append("    status: ").append(toIndentedString(getStatus())).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(getCreatedAt())).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(getUpdatedAt())).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(getEndpoint())).append("\n");
        sb.append("    passwordSharing: ").append(toIndentedString(passwordSharing)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
        sb.append("    rule: ").append(toIndentedString(getRule())).append("\n");
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
        return o.toString().replace("\n", "\n    ");
    }
}

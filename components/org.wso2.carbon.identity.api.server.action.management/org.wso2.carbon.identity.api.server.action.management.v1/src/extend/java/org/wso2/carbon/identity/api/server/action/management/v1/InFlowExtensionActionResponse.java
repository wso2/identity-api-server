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

import java.util.Objects;

import javax.validation.Valid;

/**
 * In-Flow Extension Action Response.
 * Extends the base ActionResponse with an access configuration.
 */
public class InFlowExtensionActionResponse extends ActionResponse {

    private AccessConfigModel accessConfig;
    private EncryptionModel encryption;
    private String iconUrl;

    public InFlowExtensionActionResponse(ActionResponse actionResponse) {

        setId(actionResponse.getId());
        setType(actionResponse.getType());
        setName(actionResponse.getName());
        setDescription(actionResponse.getDescription());
        setStatus(actionResponse.getStatus());
        setVersion(actionResponse.getVersion());
        setCreatedAt(actionResponse.getCreatedAt());
        setUpdatedAt(actionResponse.getUpdatedAt());
        setEndpoint(actionResponse.getEndpoint());
        setRule(actionResponse.getRule());
    }

    public InFlowExtensionActionResponse accessConfig(AccessConfigModel accessConfig) {

        this.accessConfig = accessConfig;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("accessConfig")
    @Valid
    public AccessConfigModel getAccessConfig() {

        return accessConfig;
    }

    public void setAccessConfig(AccessConfigModel accessConfig) {

        this.accessConfig = accessConfig;
    }

    public InFlowExtensionActionResponse encryption(EncryptionModel encryption) {

        this.encryption = encryption;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("encryption")
    @Valid
    public EncryptionModel getEncryption() {

        return encryption;
    }

    public void setEncryption(EncryptionModel encryption) {

        this.encryption = encryption;
    }

    public InFlowExtensionActionResponse iconUrl(String iconUrl) {

        this.iconUrl = iconUrl;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("iconUrl")
    public String getIconUrl() {

        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {

        this.iconUrl = iconUrl;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InFlowExtensionActionResponse that = (InFlowExtensionActionResponse) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getType(), that.getType()) &&
                Objects.equals(this.getName(), that.getName()) &&
                Objects.equals(this.getDescription(), that.getDescription()) &&
                Objects.equals(this.getStatus(), that.getStatus()) &&
                Objects.equals(this.getEndpoint(), that.getEndpoint()) &&
                Objects.equals(this.accessConfig, that.accessConfig) &&
                Objects.equals(this.encryption, that.encryption) &&
                Objects.equals(this.iconUrl, that.iconUrl) &&
                Objects.equals(this.getRule(), that.getRule());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getType(), getName(), getDescription(), getStatus(), getEndpoint(),
                accessConfig, encryption, iconUrl, getRule());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionActionResponse {\n");
        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    type: ").append(toIndentedString(getType())).append("\n");
        sb.append("    name: ").append(toIndentedString(getName())).append("\n");
        sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
        sb.append("    status: ").append(toIndentedString(getStatus())).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(getEndpoint())).append("\n");
        sb.append("    accessConfig: ").append(toIndentedString(accessConfig)).append("\n");
        sb.append("    encryption: ").append(toIndentedString(encryption)).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
        sb.append("    rule: ").append(toIndentedString(getRule())).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

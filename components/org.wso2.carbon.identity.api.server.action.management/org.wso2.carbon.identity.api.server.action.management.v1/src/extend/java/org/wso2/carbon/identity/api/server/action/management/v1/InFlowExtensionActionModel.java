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
 * In-Flow Extension Action Model.
 * Extends the base ActionModel with an access configuration.
 */
public class InFlowExtensionActionModel extends ActionModel {

    private AccessConfigModel accessConfig;
    private EncryptionModel encryption;

    public InFlowExtensionActionModel() {
        // Default constructor required for Jackson
    }

    public InFlowExtensionActionModel(ActionModel actionModel) {

        setName(actionModel.getName());
        setDescription(actionModel.getDescription());
        setEndpoint(actionModel.getEndpoint());
        setRule(actionModel.getRule());
    }

    public InFlowExtensionActionModel accessConfig(AccessConfigModel accessConfig) {

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

    public InFlowExtensionActionModel encryption(EncryptionModel encryption) {

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

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InFlowExtensionActionModel actionModel = (InFlowExtensionActionModel) o;
        return Objects.equals(this.getName(), actionModel.getName()) &&
                Objects.equals(this.getDescription(), actionModel.getDescription()) &&
                Objects.equals(this.getEndpoint(), actionModel.getEndpoint()) &&
                Objects.equals(this.accessConfig, actionModel.accessConfig) &&
                Objects.equals(this.encryption, actionModel.encryption) &&
                Objects.equals(this.getRule(), actionModel.getRule());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getDescription(), getEndpoint(), accessConfig, encryption, getRule());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionActionModel {\n");
        sb.append("    name: ").append(toIndentedString(getName())).append("\n");
        sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(getEndpoint())).append("\n");
        sb.append("    accessConfig: ").append(toIndentedString(accessConfig)).append("\n");
        sb.append("    encryption: ").append(toIndentedString(encryption)).append("\n");
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

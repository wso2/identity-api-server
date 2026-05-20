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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;

/**
 * Represents a single active in-flow extension connection available for use in flow builders.
 */
@ApiModel(description = "In-flow extension connection info for flow metadata")
public class InFlowExtensionConnectionInfo {

    private String actionId;
    private String name;
    private String iconUrl;

    /**
     **/
    public InFlowExtensionConnectionInfo actionId(String actionId) {

        this.actionId = actionId;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("actionId")
    @Valid
    public String getActionId() {

        return actionId;
    }

    public void setActionId(String actionId) {

        this.actionId = actionId;
    }

    /**
     **/
    public InFlowExtensionConnectionInfo name(String name) {

        this.name = name;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    /**
     **/
    public InFlowExtensionConnectionInfo iconUrl(String iconUrl) {

        this.iconUrl = iconUrl;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("iconUrl")
    @Valid
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
        InFlowExtensionConnectionInfo that = (InFlowExtensionConnectionInfo) o;
        return Objects.equals(this.actionId, that.actionId) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.iconUrl, that.iconUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(actionId, name, iconUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionConnectionInfo {\n");
        sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

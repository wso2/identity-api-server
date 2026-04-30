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

/**
 * In-Flow Extension Action Basic Response.
 * Extends the base ActionBasicResponse with an optional icon URL.
 */
public class InFlowExtensionBasicResponse extends ActionBasicResponse {

    private String iconUrl;

    public InFlowExtensionBasicResponse(ActionBasicResponse basicResponse) {

        setId(basicResponse.getId());
        setType(basicResponse.getType());
        setName(basicResponse.getName());
        setDescription(basicResponse.getDescription());
        setStatus(basicResponse.getStatus());
        setVersion(basicResponse.getVersion());
        setCreatedAt(basicResponse.getCreatedAt());
        setUpdatedAt(basicResponse.getUpdatedAt());
        setLinks(basicResponse.getLinks());
    }

    public InFlowExtensionBasicResponse iconUrl(String iconUrl) {

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
        InFlowExtensionBasicResponse that = (InFlowExtensionBasicResponse) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getType(), that.getType()) &&
                Objects.equals(this.getName(), that.getName()) &&
                Objects.equals(this.getDescription(), that.getDescription()) &&
                Objects.equals(this.getStatus(), that.getStatus()) &&
                Objects.equals(this.iconUrl, that.iconUrl) &&
                Objects.equals(this.getLinks(), that.getLinks());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getType(), getName(), getDescription(), getStatus(), iconUrl, getLinks());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionBasicResponse {\n");
        sb.append("    id: ").append(toIndentedString(getId())).append("\n");
        sb.append("    type: ").append(toIndentedString(getType())).append("\n");
        sb.append("    name: ").append(toIndentedString(getName())).append("\n");
        sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
        sb.append("    status: ").append(toIndentedString(getStatus())).append("\n");
        sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
        sb.append("    links: ").append(toIndentedString(getLinks())).append("\n");
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

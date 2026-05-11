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
 * Access Configuration model for In-Flow Extension actions.
 * Defines what context data is exposed and what operations are allowed.
 *
 * <p>The {@code expose} field accepts either plain strings (path prefixes) or objects with
 * {@code path} and {@code encrypted} properties. When strings are provided, encryption
 * defaults to {@code false}.</p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * {
 *   "expose": [
 *     "/user/userId",
 *     { "path": "/user/claims/", "encrypted": true },
 *     { "path": "/user/credentials/password", "encrypted": true },
 *     "/properties/"
 *   ],
 *   "modify": [
 *     "/properties/riskScore",
 *     { "path": "/user/claims/email", "encrypted": true }
 *   ]
 * }
 * }</pre>
 */
public class AccessConfigModel {

    private List<Object> expose;
    private List<Object> modify;

    public AccessConfigModel() {
        // Default constructor required for Jackson
    }

    public AccessConfigModel expose(List<Object> expose) {

        this.expose = expose;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("expose")
    @Valid
    public List<Object> getExpose() {

        return expose;
    }

    public void setExpose(List<Object> expose) {

        this.expose = expose;
    }

    public AccessConfigModel modify(List<Object> modify) {

        this.modify = modify;
        return this;
    }

    @ApiModelProperty()
    @JsonProperty("modify")
    @Valid
    public List<Object> getModify() {

        return modify;
    }

    public void setModify(List<Object> modify) {

        this.modify = modify;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessConfigModel that = (AccessConfigModel) o;
        return Objects.equals(this.expose, that.expose) &&
                Objects.equals(this.modify, that.modify);
    }

    @Override
    public int hashCode() {

        return Objects.hash(expose, modify);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AccessConfigModel {\n");
        sb.append("    expose: ").append(toIndentedString(expose)).append("\n");
        sb.append("    modify: ").append(toIndentedString(modify)).append("\n");
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

/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents a single IDP that references a presentation definition.
 */
public class ConnectedIdpItem {

    private String idpId;
    private String name;
    private String self;

    @ApiModelProperty(value = "UUID of the identity provider.")
    @JsonProperty("idpId")
    public String getIdpId() {

        return idpId;
    }

    public void setIdpId(String idpId) {

        this.idpId = idpId;
    }

    @ApiModelProperty(value = "Display name of the IDP.")
    @JsonProperty("name")
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @ApiModelProperty(value = "URI of the IDP resource.")
    @JsonProperty("self")
    public String getSelf() {

        return self;
    }

    public void setSelf(String self) {

        this.self = self;
    }
}

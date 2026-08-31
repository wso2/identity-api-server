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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Requested Credential Model.
 */
@ApiModel(description = "Requested Credential Model")
public class RequestedCredentialModel {

    @NotNull
    private String id;
    @NotNull
    private String type;
    private String format = "dc+sd-jwt";
    private List<ClaimConstraintModel> claims;

    @ApiModelProperty(required = true,
            value = "User-defined identifier for this credential (alphanumeric, underscores, hyphens).")
    @JsonProperty("id")
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    @ApiModelProperty(required = true, value = "Type URI of the requested credential.")
    @JsonProperty("type")
    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    @ApiModelProperty(
            value = "Credential format. One of: dc+sd-jwt, mso_mdoc, jwt_vc_json. Defaults to dc+sd-jwt.")
    @JsonProperty("format")
    public String getFormat() {

        return format;
    }

    public void setFormat(String format) {

        this.format = format;
    }

    @ApiModelProperty(value = "Claim constraints for this credential.")
    @JsonProperty("claims")
    public List<ClaimConstraintModel> getClaims() {

        return claims;
    }

    public void setClaims(List<ClaimConstraintModel> claims) {

        this.claims = claims;
    }
}

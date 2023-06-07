/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

/**
 * Claim error DTO for all the server claim API related error responses.
 */
public class ClaimErrorDTO extends ErrorDTO {

    private static final long serialVersionUID = 5169725164223318439L;
    private String claimURI = null;

    public ClaimErrorDTO() {

        super();
    }

    public ClaimErrorDTO(ErrorDTO errorDTO) {

        this.setCode(errorDTO.getCode());
        this.setMessage(errorDTO.getMessage());
        this.setDescription(errorDTO.getDescription());
        this.setRef(errorDTO.getRef());
    }

    @JsonProperty("claimURI")
    public String getClaimURI() {

        return claimURI;
    }

    public void setClaimURI(String claimURI) {

        this.claimURI = claimURI;
    }

    @Override
    @JsonIgnore
    public String getRef() {
        return null;
    }
}

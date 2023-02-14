/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
* Associated External claim for local claim.
**/
@ApiModel(description = "External claim response.")
public class AssociatedExternalClaimDTO {

@Valid
private String id = null;

@Valid
private String claimURI = null;

@Valid
private String claimDialectURI = null;


/**
* External claim ID.
**/
@ApiModelProperty(value = "External claim ID.")
@JsonProperty("id")
public String getId() {
    return id;
}
public void setId(String id) {
    this.id = id;
}

/**
* Claim URI of the external claim.
**/
@ApiModelProperty(value = "Claim URI of the external claim.")
@JsonProperty("claimURI")
public String getClaimURI() {
    return claimURI;
}
public void setClaimURI(String claimURI) {
    this.claimURI = claimURI;
}

/**
* Dialect URI of the external claim.
**/
@ApiModelProperty(value = "Dialect URI of the external claim.")
@JsonProperty("claimDialectURI")
public String getClaimDialectURI() {
    return claimDialectURI;
}
public void setClaimDialectURI(String claimDialectURI) {
    this.claimDialectURI = claimDialectURI;
}

@Override
public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalClaimResDTO {\n");
    sb.append("    id: ").append(id).append("\n");
    sb.append("    claimURI: ").append(claimURI).append("\n");
    sb.append("    claimDialectURI: ").append(claimDialectURI).append("\n");
    sb.append("}\n");
    return sb.toString();
}
}

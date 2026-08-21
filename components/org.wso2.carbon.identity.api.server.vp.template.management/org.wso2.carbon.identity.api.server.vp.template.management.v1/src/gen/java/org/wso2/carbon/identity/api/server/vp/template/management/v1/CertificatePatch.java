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

/**
 * Patch operation for trusted CA certificates.
 */
@ApiModel(description = "Patch operation for trusted CA certificates")
public class CertificatePatch {

    public enum OperationEnum {
        ADD, REMOVE, REPLACE
    }

    private OperationEnum operation;
    private Integer certificateIndex;
    private String certificate;

    @ApiModelProperty(required = true, value = "The patch operation to perform.")
    @JsonProperty("operation")
    public OperationEnum getOperation() {

        return operation;
    }

    public void setOperation(OperationEnum operation) {

        this.operation = operation;
    }

    @ApiModelProperty(value = "Zero-based index of the certificate to remove or replace.")
    @JsonProperty("certificateIndex")
    public Integer getCertificateIndex() {

        return certificateIndex;
    }

    public void setCertificateIndex(Integer certificateIndex) {

        this.certificateIndex = certificateIndex;
    }

    @ApiModelProperty(value = "Base64-encoded PEM certificate (required for ADD and REPLACE).")
    @JsonProperty("certificate")
    public String getCertificate() {

        return certificate;
    }

    public void setCertificate(String certificate) {

        this.certificate = certificate;
    }
}

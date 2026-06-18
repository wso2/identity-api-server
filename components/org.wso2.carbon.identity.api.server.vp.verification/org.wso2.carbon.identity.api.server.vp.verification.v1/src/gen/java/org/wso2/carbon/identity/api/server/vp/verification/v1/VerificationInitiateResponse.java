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

package org.wso2.carbon.identity.api.server.vp.verification.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response returned when a verification session is created.
 */
@ApiModel(description = "Response returned when a verification session is created.")
public class VerificationInitiateResponse {

    private String txnId;
    private String walletUrl;
    private String requestUri;
    private Long expiresAt;

    @ApiModelProperty(value = "Transaction ID used to poll for the verification status.")
    @JsonProperty("txnId")
    public String getTxnId() { return txnId; }
    public void setTxnId(String txnId) { this.txnId = txnId; }

    @ApiModelProperty(value = "Deep-link URL to launch the wallet (show as QR code).")
    @JsonProperty("walletUrl")
    public String getWalletUrl() { return walletUrl; }
    public void setWalletUrl(String walletUrl) { this.walletUrl = walletUrl; }

    @ApiModelProperty(value = "URI from which the wallet will fetch the signed request JWT.")
    @JsonProperty("requestUri")
    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }

    @ApiModelProperty(value = "Unix epoch milliseconds when the session expires.")
    @JsonProperty("expiresAt")
    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
}

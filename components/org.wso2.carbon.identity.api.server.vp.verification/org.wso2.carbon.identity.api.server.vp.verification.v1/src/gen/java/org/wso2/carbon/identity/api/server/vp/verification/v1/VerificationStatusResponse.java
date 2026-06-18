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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * Response for a VP verification session status poll.
 */
@ApiModel(description = "Response for a VP verification session status poll.")
public class VerificationStatusResponse {

    private String txnId;
    private String status;
    private Presentation presentation;
    private List<String> errors;

    @ApiModelProperty(value = "Transaction ID of the verification session.")
    @JsonProperty("txnId")
    public String getTxnId() { return txnId; }
    public void setTxnId(String txnId) { this.txnId = txnId; }

    @ApiModelProperty(value = "Current status: ACTIVE, VERIFIED, FAILED.")
    @JsonProperty("status")
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @ApiModelProperty(value = "Full presentation details (present when status=VERIFIED).")
    @JsonProperty("presentation")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Presentation getPresentation() { return presentation; }
    public void setPresentation(Presentation presentation) { this.presentation = presentation; }

    @ApiModelProperty(value = "Error descriptions when status=FAILED.")
    @JsonProperty("errors")
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }

    // ── Nested model classes ──────────────────────────────────────────────────

    /**
     * Top-level presentation envelope — format, timing, credential details, holder, and key binding.
     */
    public static class Presentation {

        private String format;
        private String submittedAt;
        private Credential credential;
        private Holder holder;
        private KeyBinding keyBinding;

        @JsonProperty("format")
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }

        @ApiModelProperty(value = "ISO-8601 timestamp when IS received the wallet's VP response.")
        @JsonProperty("submittedAt")
        public String getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }

        @JsonProperty("credential")
        public Credential getCredential() { return credential; }
        public void setCredential(Credential credential) { this.credential = credential; }

        @JsonProperty("holder")
        public Holder getHolder() { return holder; }
        public void setHolder(Holder holder) { this.holder = holder; }

        @ApiModelProperty(value = "KB-JWT details — present only when the wallet included a Key Binding JWT.")
        @JsonProperty("keyBinding")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public KeyBinding getKeyBinding() { return keyBinding; }
        public void setKeyBinding(KeyBinding keyBinding) { this.keyBinding = keyBinding; }
    }

    /**
     * Issuer-signed credential metadata.
     */
    public static class Credential {

        private String type;
        private String issuer;
        private String issuedAt;
        private String expiresAt;
        private String signingAlgorithm;
        private HolderBinding holderBinding;

        @ApiModelProperty(value = "Credential type (vct claim).")
        @JsonProperty("type")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        @ApiModelProperty(value = "Credential issuer (iss claim).")
        @JsonProperty("issuer")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }

        @ApiModelProperty(value = "ISO-8601 timestamp when the credential was issued (iat claim).")
        @JsonProperty("issuedAt")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getIssuedAt() { return issuedAt; }
        public void setIssuedAt(String issuedAt) { this.issuedAt = issuedAt; }

        @ApiModelProperty(value = "ISO-8601 expiry of the credential (exp claim); null if no expiry set.")
        @JsonProperty("expiresAt")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getExpiresAt() { return expiresAt; }
        public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }

        @ApiModelProperty(value = "Signing algorithm used by the issuer (JWT header alg).")
        @JsonProperty("signingAlgorithm")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getSigningAlgorithm() { return signingAlgorithm; }
        public void setSigningAlgorithm(String signingAlgorithm) { this.signingAlgorithm = signingAlgorithm; }

        @ApiModelProperty(value = "Holder binding key info (cnf claim); null if no holder binding configured.")
        @JsonProperty("holderBinding")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public HolderBinding getHolderBinding() { return holderBinding; }
        public void setHolderBinding(HolderBinding holderBinding) { this.holderBinding = holderBinding; }
    }

    /**
     * Holder binding key info from the {@code cnf.jwk} claim.
     */
    public static class HolderBinding {

        private String method;
        private String keyType;
        private String curve;

        @ApiModelProperty(value = "Binding method, e.g. 'cnf.jwk'.")
        @JsonProperty("method")
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }

        @ApiModelProperty(value = "Public key type: EC, RSA, or OKP.")
        @JsonProperty("keyType")
        public String getKeyType() { return keyType; }
        public void setKeyType(String keyType) { this.keyType = keyType; }

        @ApiModelProperty(value = "Elliptic curve name (e.g. P-256, Ed25519); null for RSA.")
        @JsonProperty("curve")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getCurve() { return curve; }
        public void setCurve(String curve) { this.curve = curve; }
    }

    /**
     * Credential holder — subject identifier and disclosed attribute claims.
     */
    public static class Holder {

        private String id;
        private Map<String, Object> claims;

        @ApiModelProperty(value = "Subject identifier (sub claim).")
        @JsonProperty("id")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        @ApiModelProperty(value = "Disclosed attribute claims (everything except JWT/SD-JWT technical fields).")
        @JsonProperty("claims")
        public Map<String, Object> getClaims() { return claims; }
        public void setClaims(Map<String, Object> claims) { this.claims = claims; }
    }

    /**
     * Key Binding JWT details — proves the wallet held the private key at presentation time.
     */
    public static class KeyBinding {

        private boolean verified;
        private String presentedAt;
        private String audience;
        private String nonce;

        @ApiModelProperty(value = "True when the KB-JWT signature, iat, sd_hash, and nonce all passed verification.")
        @JsonProperty("verified")
        public boolean isVerified() { return verified; }
        public void setVerified(boolean verified) { this.verified = verified; }

        @ApiModelProperty(value = "ISO-8601 timestamp from the KB-JWT iat — when the wallet created this presentation.")
        @JsonProperty("presentedAt")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getPresentedAt() { return presentedAt; }
        public void setPresentedAt(String presentedAt) { this.presentedAt = presentedAt; }

        @ApiModelProperty(value = "Audience claim from KB-JWT — the verifier this presentation was intended for.")
        @JsonProperty("audience")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getAudience() { return audience; }
        public void setAudience(String audience) { this.audience = audience; }

        @ApiModelProperty(value = "Nonce from KB-JWT — matches the nonce from the original VP request.")
        @JsonProperty("nonce")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getNonce() { return nonce; }
        public void setNonce(String nonce) { this.nonce = nonce; }
    }
}

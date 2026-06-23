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
    private String type;
    private String purpose;
    private String issuer;
    private List<ClaimConstraintModel> claims;
    private Boolean enforceTrustedIssuers;
    private List<String> trustedIssuers;

    @ApiModelProperty(required = true, value = "Type of the requested credential.")
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(value = "Purpose of requesting the credential.")
    @JsonProperty("purpose")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @ApiModelProperty(value = "The trusted issuer for this credential.")
    @JsonProperty("issuer")
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @ApiModelProperty(value = "List of claim constraints for this credential.")
    @JsonProperty("claims")
    public List<ClaimConstraintModel> getClaims() {
        return claims;
    }

    public void setClaims(List<ClaimConstraintModel> claims) {
        this.claims = claims;
    }

    @ApiModelProperty(value = "Whether to enforce trusted issuer validation for this credential.")
    @JsonProperty("enforceTrustedIssuers")
    public Boolean getEnforceTrustedIssuers() {
        return enforceTrustedIssuers;
    }

    public void setEnforceTrustedIssuers(Boolean enforceTrustedIssuers) {
        this.enforceTrustedIssuers = enforceTrustedIssuers;
    }

    @ApiModelProperty(value = "List of trusted issuer URIs/DIDs for this credential.")
    @JsonProperty("trustedIssuers")
    public List<String> getTrustedIssuers() {
        return trustedIssuers;
    }

    public void setTrustedIssuers(List<String> trustedIssuers) {
        this.trustedIssuers = trustedIssuers;
    }
}

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
    private Boolean enforceTrustedIssuer = Boolean.FALSE;
    private List<String> trustedCaPems;
    private String keyResolutionMethod = "x5c";
    private String jwksUri;
    private String issuerPem;
    private List<ClaimConstraintModel> claims;

    @ApiModelProperty(required = true, value = "User-defined identifier for this credential (alphanumeric, underscores, hyphens).")
    @JsonProperty("id")
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    @ApiModelProperty(required = true, value = "Type of the requested credential.")
    @JsonProperty("type")
    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    @ApiModelProperty(value = "Credential format as defined by OID4VP §6.1. One of: dc+sd-jwt, mso_mdoc, jwt_vc_json. Defaults to dc+sd-jwt.")
    @JsonProperty("format")
    public String getFormat() {

        return format;
    }

    public void setFormat(String format) {

        this.format = format;
    }

    @ApiModelProperty(value = "When true, validates the x5c chain against one of the configured trustedCaPems root CAs. When false (default), signature is verified without CA chain validation.")
    @JsonProperty("enforceTrustedIssuer")
    public Boolean getEnforceTrustedIssuer() {

        return enforceTrustedIssuer;
    }

    public void setEnforceTrustedIssuer(Boolean enforceTrustedIssuer) {

        this.enforceTrustedIssuer = enforceTrustedIssuer;
    }

    @ApiModelProperty(value = "Base64-encoded PEM root CA certificates trusted for x5c chain validation.")
    @JsonProperty("trustedCaPems")
    public List<String> getTrustedCaPems() {

        return trustedCaPems;
    }

    public void setTrustedCaPems(List<String> trustedCaPems) {

        this.trustedCaPems = trustedCaPems;
    }

    @ApiModelProperty(value = "Key resolution method for verifying the issuer's signature. One of: x5c, jwks_uri, pem. Defaults to x5c.")
    @JsonProperty("keyResolutionMethod")
    public String getKeyResolutionMethod() {

        return keyResolutionMethod;
    }

    public void setKeyResolutionMethod(String keyResolutionMethod) {

        this.keyResolutionMethod = keyResolutionMethod;
    }

    @ApiModelProperty(value = "JWKS endpoint URL for fetching the issuer's public key (used when keyResolutionMethod is 'jwks_uri').")
    @JsonProperty("jwksUri")
    public String getJwksUri() {

        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {

        this.jwksUri = jwksUri;
    }

    @ApiModelProperty(value = "PEM-encoded issuer certificate for public key extraction (used when keyResolutionMethod is 'pem').")
    @JsonProperty("issuerPem")
    public String getIssuerPem() {

        return issuerPem;
    }

    public void setIssuerPem(String issuerPem) {

        this.issuerPem = issuerPem;
    }

    @ApiModelProperty(value = "List of claim constraints for this credential.")
    @JsonProperty("claims")
    public List<ClaimConstraintModel> getClaims() {

        return claims;
    }

    public void setClaims(List<ClaimConstraintModel> claims) {

        this.claims = claims;
    }

}

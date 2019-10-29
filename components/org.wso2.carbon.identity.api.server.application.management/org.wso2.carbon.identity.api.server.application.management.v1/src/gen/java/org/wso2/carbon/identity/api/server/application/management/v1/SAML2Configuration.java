/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SAML2Configuration  {
  
    private String issuer;
    private List<String> assertionConsumerUrls = null;

    private String defaultAssertionConsumerUrl;
    private Boolean enableRequestSigning = true;
    private Boolean enableAssertionEncryption = false;
    private Boolean enableResponseSigning = true;
    private String serviceProviderQualifier;
    private String nameIdFormat = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
    private String requestValidationCertificateAlias;
    private String responseSigningAlgorithm;
    private String responseDigestAlgorithm;
    private String assertionEncryptionAlgroithm;
    private String keyEncryptionAlgorithm;
    private Boolean enableSingleLogout = true;
    private String singleLogoutResponseUrl;
    private String singleLogoutRequestUrl;

@XmlType(name="SingleLogoutMethodEnum")
@XmlEnum(String.class)
public enum SingleLogoutMethodEnum {

    @XmlEnumValue("backchannel") BACKCHANNEL(String.valueOf("backchannel")), @XmlEnumValue("frontchannel_http_redirect") FRONTCHANNEL_HTTP_REDIRECT(String.valueOf("frontchannel_http_redirect")), @XmlEnumValue("frontchannel_http_post") FRONTCHANNEL_HTTP_POST(String.valueOf("frontchannel_http_post"));


    private String value;

    SingleLogoutMethodEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static SingleLogoutMethodEnum fromValue(String value) {
        for (SingleLogoutMethodEnum b : SingleLogoutMethodEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private SingleLogoutMethodEnum singleLogoutMethod;
    private Boolean enableIdpInitiatedSingleSignOn = false;
    private Boolean enableIdpInitiatedSingleLogOut = false;

    /**
    **/
    public SAML2Configuration issuer(String issuer) {

        this.issuer = issuer;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("issuer")
    @Valid
    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
    **/
    public SAML2Configuration assertionConsumerUrls(List<String> assertionConsumerUrls) {

        this.assertionConsumerUrls = assertionConsumerUrls;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("assertionConsumerUrls")
    @Valid
    public List<String> getAssertionConsumerUrls() {
        return assertionConsumerUrls;
    }
    public void setAssertionConsumerUrls(List<String> assertionConsumerUrls) {
        this.assertionConsumerUrls = assertionConsumerUrls;
    }

    public SAML2Configuration addAssertionConsumerUrlsItem(String assertionConsumerUrlsItem) {
        if (this.assertionConsumerUrls == null) {
            this.assertionConsumerUrls = new ArrayList<>();
        }
        this.assertionConsumerUrls.add(assertionConsumerUrlsItem);
        return this;
    }

        /**
    **/
    public SAML2Configuration defaultAssertionConsumerUrl(String defaultAssertionConsumerUrl) {

        this.defaultAssertionConsumerUrl = defaultAssertionConsumerUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("defaultAssertionConsumerUrl")
    @Valid
    public String getDefaultAssertionConsumerUrl() {
        return defaultAssertionConsumerUrl;
    }
    public void setDefaultAssertionConsumerUrl(String defaultAssertionConsumerUrl) {
        this.defaultAssertionConsumerUrl = defaultAssertionConsumerUrl;
    }

    /**
    **/
    public SAML2Configuration enableRequestSigning(Boolean enableRequestSigning) {

        this.enableRequestSigning = enableRequestSigning;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableRequestSigning")
    @Valid
    public Boolean getEnableRequestSigning() {
        return enableRequestSigning;
    }
    public void setEnableRequestSigning(Boolean enableRequestSigning) {
        this.enableRequestSigning = enableRequestSigning;
    }

    /**
    **/
    public SAML2Configuration enableAssertionEncryption(Boolean enableAssertionEncryption) {

        this.enableAssertionEncryption = enableAssertionEncryption;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableAssertionEncryption")
    @Valid
    public Boolean getEnableAssertionEncryption() {
        return enableAssertionEncryption;
    }
    public void setEnableAssertionEncryption(Boolean enableAssertionEncryption) {
        this.enableAssertionEncryption = enableAssertionEncryption;
    }

    /**
    **/
    public SAML2Configuration enableResponseSigning(Boolean enableResponseSigning) {

        this.enableResponseSigning = enableResponseSigning;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableResponseSigning")
    @Valid
    public Boolean getEnableResponseSigning() {
        return enableResponseSigning;
    }
    public void setEnableResponseSigning(Boolean enableResponseSigning) {
        this.enableResponseSigning = enableResponseSigning;
    }

    /**
    **/
    public SAML2Configuration serviceProviderQualifier(String serviceProviderQualifier) {

        this.serviceProviderQualifier = serviceProviderQualifier;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("serviceProviderQualifier")
    @Valid
    public String getServiceProviderQualifier() {
        return serviceProviderQualifier;
    }
    public void setServiceProviderQualifier(String serviceProviderQualifier) {
        this.serviceProviderQualifier = serviceProviderQualifier;
    }

    /**
    **/
    public SAML2Configuration nameIdFormat(String nameIdFormat) {

        this.nameIdFormat = nameIdFormat;
        return this;
    }
    
    @ApiModelProperty(example = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress", value = "")
    @JsonProperty("nameIdFormat")
    @Valid
    public String getNameIdFormat() {
        return nameIdFormat;
    }
    public void setNameIdFormat(String nameIdFormat) {
        this.nameIdFormat = nameIdFormat;
    }

    /**
    **/
    public SAML2Configuration requestValidationCertificateAlias(String requestValidationCertificateAlias) {

        this.requestValidationCertificateAlias = requestValidationCertificateAlias;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("requestValidationCertificateAlias")
    @Valid
    public String getRequestValidationCertificateAlias() {
        return requestValidationCertificateAlias;
    }
    public void setRequestValidationCertificateAlias(String requestValidationCertificateAlias) {
        this.requestValidationCertificateAlias = requestValidationCertificateAlias;
    }

    /**
    **/
    public SAML2Configuration responseSigningAlgorithm(String responseSigningAlgorithm) {

        this.responseSigningAlgorithm = responseSigningAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("responseSigningAlgorithm")
    @Valid
    public String getResponseSigningAlgorithm() {
        return responseSigningAlgorithm;
    }
    public void setResponseSigningAlgorithm(String responseSigningAlgorithm) {
        this.responseSigningAlgorithm = responseSigningAlgorithm;
    }

    /**
    **/
    public SAML2Configuration responseDigestAlgorithm(String responseDigestAlgorithm) {

        this.responseDigestAlgorithm = responseDigestAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("responseDigestAlgorithm")
    @Valid
    public String getResponseDigestAlgorithm() {
        return responseDigestAlgorithm;
    }
    public void setResponseDigestAlgorithm(String responseDigestAlgorithm) {
        this.responseDigestAlgorithm = responseDigestAlgorithm;
    }

    /**
    **/
    public SAML2Configuration assertionEncryptionAlgroithm(String assertionEncryptionAlgroithm) {

        this.assertionEncryptionAlgroithm = assertionEncryptionAlgroithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("assertionEncryptionAlgroithm")
    @Valid
    public String getAssertionEncryptionAlgroithm() {
        return assertionEncryptionAlgroithm;
    }
    public void setAssertionEncryptionAlgroithm(String assertionEncryptionAlgroithm) {
        this.assertionEncryptionAlgroithm = assertionEncryptionAlgroithm;
    }

    /**
    **/
    public SAML2Configuration keyEncryptionAlgorithm(String keyEncryptionAlgorithm) {

        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("keyEncryptionAlgorithm")
    @Valid
    public String getKeyEncryptionAlgorithm() {
        return keyEncryptionAlgorithm;
    }
    public void setKeyEncryptionAlgorithm(String keyEncryptionAlgorithm) {
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
    }

    /**
    **/
    public SAML2Configuration enableSingleLogout(Boolean enableSingleLogout) {

        this.enableSingleLogout = enableSingleLogout;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableSingleLogout")
    @Valid
    public Boolean getEnableSingleLogout() {
        return enableSingleLogout;
    }
    public void setEnableSingleLogout(Boolean enableSingleLogout) {
        this.enableSingleLogout = enableSingleLogout;
    }

    /**
    * Single logout response accepting endpoint
    **/
    public SAML2Configuration singleLogoutResponseUrl(String singleLogoutResponseUrl) {

        this.singleLogoutResponseUrl = singleLogoutResponseUrl;
        return this;
    }
    
    @ApiModelProperty(value = "Single logout response accepting endpoint")
    @JsonProperty("singleLogoutResponseUrl")
    @Valid
    public String getSingleLogoutResponseUrl() {
        return singleLogoutResponseUrl;
    }
    public void setSingleLogoutResponseUrl(String singleLogoutResponseUrl) {
        this.singleLogoutResponseUrl = singleLogoutResponseUrl;
    }

    /**
    * Single logout request accepting endpoint
    **/
    public SAML2Configuration singleLogoutRequestUrl(String singleLogoutRequestUrl) {

        this.singleLogoutRequestUrl = singleLogoutRequestUrl;
        return this;
    }
    
    @ApiModelProperty(value = "Single logout request accepting endpoint")
    @JsonProperty("singleLogoutRequestUrl")
    @Valid
    public String getSingleLogoutRequestUrl() {
        return singleLogoutRequestUrl;
    }
    public void setSingleLogoutRequestUrl(String singleLogoutRequestUrl) {
        this.singleLogoutRequestUrl = singleLogoutRequestUrl;
    }

    /**
    **/
    public SAML2Configuration singleLogoutMethod(SingleLogoutMethodEnum singleLogoutMethod) {

        this.singleLogoutMethod = singleLogoutMethod;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("singleLogoutMethod")
    @Valid
    public SingleLogoutMethodEnum getSingleLogoutMethod() {
        return singleLogoutMethod;
    }
    public void setSingleLogoutMethod(SingleLogoutMethodEnum singleLogoutMethod) {
        this.singleLogoutMethod = singleLogoutMethod;
    }

    /**
    **/
    public SAML2Configuration enableIdpInitiatedSingleSignOn(Boolean enableIdpInitiatedSingleSignOn) {

        this.enableIdpInitiatedSingleSignOn = enableIdpInitiatedSingleSignOn;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableIdpInitiatedSingleSignOn")
    @Valid
    public Boolean getEnableIdpInitiatedSingleSignOn() {
        return enableIdpInitiatedSingleSignOn;
    }
    public void setEnableIdpInitiatedSingleSignOn(Boolean enableIdpInitiatedSingleSignOn) {
        this.enableIdpInitiatedSingleSignOn = enableIdpInitiatedSingleSignOn;
    }

    /**
    **/
    public SAML2Configuration enableIdpInitiatedSingleLogOut(Boolean enableIdpInitiatedSingleLogOut) {

        this.enableIdpInitiatedSingleLogOut = enableIdpInitiatedSingleLogOut;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableIdpInitiatedSingleLogOut")
    @Valid
    public Boolean getEnableIdpInitiatedSingleLogOut() {
        return enableIdpInitiatedSingleLogOut;
    }
    public void setEnableIdpInitiatedSingleLogOut(Boolean enableIdpInitiatedSingleLogOut) {
        this.enableIdpInitiatedSingleLogOut = enableIdpInitiatedSingleLogOut;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SAML2Configuration saML2Configuration = (SAML2Configuration) o;
        return Objects.equals(this.issuer, saML2Configuration.issuer) &&
            Objects.equals(this.assertionConsumerUrls, saML2Configuration.assertionConsumerUrls) &&
            Objects.equals(this.defaultAssertionConsumerUrl, saML2Configuration.defaultAssertionConsumerUrl) &&
            Objects.equals(this.enableRequestSigning, saML2Configuration.enableRequestSigning) &&
            Objects.equals(this.enableAssertionEncryption, saML2Configuration.enableAssertionEncryption) &&
            Objects.equals(this.enableResponseSigning, saML2Configuration.enableResponseSigning) &&
            Objects.equals(this.serviceProviderQualifier, saML2Configuration.serviceProviderQualifier) &&
            Objects.equals(this.nameIdFormat, saML2Configuration.nameIdFormat) &&
            Objects.equals(this.requestValidationCertificateAlias, saML2Configuration.requestValidationCertificateAlias) &&
            Objects.equals(this.responseSigningAlgorithm, saML2Configuration.responseSigningAlgorithm) &&
            Objects.equals(this.responseDigestAlgorithm, saML2Configuration.responseDigestAlgorithm) &&
            Objects.equals(this.assertionEncryptionAlgroithm, saML2Configuration.assertionEncryptionAlgroithm) &&
            Objects.equals(this.keyEncryptionAlgorithm, saML2Configuration.keyEncryptionAlgorithm) &&
            Objects.equals(this.enableSingleLogout, saML2Configuration.enableSingleLogout) &&
            Objects.equals(this.singleLogoutResponseUrl, saML2Configuration.singleLogoutResponseUrl) &&
            Objects.equals(this.singleLogoutRequestUrl, saML2Configuration.singleLogoutRequestUrl) &&
            Objects.equals(this.singleLogoutMethod, saML2Configuration.singleLogoutMethod) &&
            Objects.equals(this.enableIdpInitiatedSingleSignOn, saML2Configuration.enableIdpInitiatedSingleSignOn) &&
            Objects.equals(this.enableIdpInitiatedSingleLogOut, saML2Configuration.enableIdpInitiatedSingleLogOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, assertionConsumerUrls, defaultAssertionConsumerUrl, enableRequestSigning, enableAssertionEncryption, enableResponseSigning, serviceProviderQualifier, nameIdFormat, requestValidationCertificateAlias, responseSigningAlgorithm, responseDigestAlgorithm, assertionEncryptionAlgroithm, keyEncryptionAlgorithm, enableSingleLogout, singleLogoutResponseUrl, singleLogoutRequestUrl, singleLogoutMethod, enableIdpInitiatedSingleSignOn, enableIdpInitiatedSingleLogOut);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SAML2Configuration {\n");
        
        sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
        sb.append("    assertionConsumerUrls: ").append(toIndentedString(assertionConsumerUrls)).append("\n");
        sb.append("    defaultAssertionConsumerUrl: ").append(toIndentedString(defaultAssertionConsumerUrl)).append("\n");
        sb.append("    enableRequestSigning: ").append(toIndentedString(enableRequestSigning)).append("\n");
        sb.append("    enableAssertionEncryption: ").append(toIndentedString(enableAssertionEncryption)).append("\n");
        sb.append("    enableResponseSigning: ").append(toIndentedString(enableResponseSigning)).append("\n");
        sb.append("    serviceProviderQualifier: ").append(toIndentedString(serviceProviderQualifier)).append("\n");
        sb.append("    nameIdFormat: ").append(toIndentedString(nameIdFormat)).append("\n");
        sb.append("    requestValidationCertificateAlias: ").append(toIndentedString(requestValidationCertificateAlias)).append("\n");
        sb.append("    responseSigningAlgorithm: ").append(toIndentedString(responseSigningAlgorithm)).append("\n");
        sb.append("    responseDigestAlgorithm: ").append(toIndentedString(responseDigestAlgorithm)).append("\n");
        sb.append("    assertionEncryptionAlgroithm: ").append(toIndentedString(assertionEncryptionAlgroithm)).append("\n");
        sb.append("    keyEncryptionAlgorithm: ").append(toIndentedString(keyEncryptionAlgorithm)).append("\n");
        sb.append("    enableSingleLogout: ").append(toIndentedString(enableSingleLogout)).append("\n");
        sb.append("    singleLogoutResponseUrl: ").append(toIndentedString(singleLogoutResponseUrl)).append("\n");
        sb.append("    singleLogoutRequestUrl: ").append(toIndentedString(singleLogoutRequestUrl)).append("\n");
        sb.append("    singleLogoutMethod: ").append(toIndentedString(singleLogoutMethod)).append("\n");
        sb.append("    enableIdpInitiatedSingleSignOn: ").append(toIndentedString(enableIdpInitiatedSingleSignOn)).append("\n");
        sb.append("    enableIdpInitiatedSingleLogOut: ").append(toIndentedString(enableIdpInitiatedSingleLogOut)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}


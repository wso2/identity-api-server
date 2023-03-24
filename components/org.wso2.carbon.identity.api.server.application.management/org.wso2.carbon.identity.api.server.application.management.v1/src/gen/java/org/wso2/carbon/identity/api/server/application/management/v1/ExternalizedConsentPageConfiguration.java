/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

public class ExternalizedConsentPageConfiguration {

    private boolean enabled;
    private String consentPageUrl;

    /**
     * Decide whether externalized consent page is enabled.
     **/
    public ExternalizedConsentPageConfiguration enabled(boolean enabled) {

        this.enabled = enabled;
        return this;
    }

    @ApiModelProperty(value = "Decide whether externalized consent page is enabled.")
    @JsonProperty("enabled")
    @Valid
    public boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Consent URL.
     **/
    public ExternalizedConsentPageConfiguration consentPageUrl(String consentPageUrl) {

        this.consentPageUrl = consentPageUrl;
        return this;
    }

    @ApiModelProperty(value = "Consent Page URL.")
    @JsonProperty("consentPageUrl")
    @Valid
    public String getConsentPageUrl() {
        return consentPageUrl;
    }
    public void setConsentPageUrl(String consentPageUrl) {
        this.consentPageUrl = consentPageUrl;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalizedConsentPageConfiguration externalizedConsentPageConfiguration =
                (ExternalizedConsentPageConfiguration) o;
        return Objects.equals(this.enabled, externalizedConsentPageConfiguration.enabled) &&
                Objects.equals(this.consentPageUrl, externalizedConsentPageConfiguration.consentPageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, consentPageUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExternalizedConsentPageConfiguration {\n");

        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    consentPageUrl: ").append(toIndentedString(consentPageUrl)).append("\n");
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

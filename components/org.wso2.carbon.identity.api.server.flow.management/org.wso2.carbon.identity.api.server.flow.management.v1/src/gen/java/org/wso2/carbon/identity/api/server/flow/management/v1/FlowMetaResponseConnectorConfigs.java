/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

import javax.validation.Valid;

public class FlowMetaResponseConnectorConfigs  {
  
    private Boolean passwordRecoveryEnabled;
    private Boolean multiAttributeLoginEnabled;
    private Boolean selfRegistrationEnabled;

    /**
    **/
    public FlowMetaResponseConnectorConfigs passwordRecoveryEnabled(Boolean passwordRecoveryEnabled) {

        this.passwordRecoveryEnabled = passwordRecoveryEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("passwordRecoveryEnabled")
    @Valid
    public Boolean getPasswordRecoveryEnabled() {
        return passwordRecoveryEnabled;
    }
    public void setPasswordRecoveryEnabled(Boolean passwordRecoveryEnabled) {
        this.passwordRecoveryEnabled = passwordRecoveryEnabled;
    }

    /**
    **/
    public FlowMetaResponseConnectorConfigs multiAttributeLoginEnabled(Boolean multiAttributeLoginEnabled) {

        this.multiAttributeLoginEnabled = multiAttributeLoginEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("multiAttributeLoginEnabled")
    @Valid
    public Boolean getMultiAttributeLoginEnabled() {
        return multiAttributeLoginEnabled;
    }
    public void setMultiAttributeLoginEnabled(Boolean multiAttributeLoginEnabled) {
        this.multiAttributeLoginEnabled = multiAttributeLoginEnabled;
    }

    /**
     **/
    public FlowMetaResponseConnectorConfigs selfRegistrationEnabled(Boolean selfRegistrationEnabled) {

        this.selfRegistrationEnabled = selfRegistrationEnabled;
        return this;
    }

    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("selfRegistrationEnabled")
    @Valid
    public Boolean getSelfRegistrationEnabled() {
        return selfRegistrationEnabled;
    }
    public void setSelfRegistrationEnabled(Boolean selfRegistrationEnabled) {
        this.selfRegistrationEnabled = selfRegistrationEnabled;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowMetaResponseConnectorConfigs flowMetaResponseConnectorConfigs = (FlowMetaResponseConnectorConfigs) o;
        return Objects.equals(this.passwordRecoveryEnabled, flowMetaResponseConnectorConfigs.passwordRecoveryEnabled) &&
            Objects.equals(this.multiAttributeLoginEnabled, flowMetaResponseConnectorConfigs.multiAttributeLoginEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passwordRecoveryEnabled, multiAttributeLoginEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowMetaResponseConnectorConfigs {\n");
        
        sb.append("    passwordRecoveryEnabled: ").append(toIndentedString(passwordRecoveryEnabled)).append("\n");
        sb.append("    multiAttributeLoginEnabled: ").append(toIndentedString(multiAttributeLoginEnabled)).append("\n");
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


/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

public class ConfiguredAuthenticatorStepModal  {

    private Integer stepId = null;
    private List<String> localAuthenticators = null;

    private List<String> federatedAuthenticators = null;


    /**
     **/
    public ConfiguredAuthenticatorStepModal stepId(Integer stepId) {

        this.stepId = stepId;
        return this;
    }

    @ApiModelProperty(example = "0", value = "")
    @JsonProperty("stepId")
    @Valid
    public Integer getStepId() {
        return stepId;
    }
    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    /**
     **/
    public ConfiguredAuthenticatorStepModal localAuthenticators(List<String> localAuthenticators) {

        this.localAuthenticators = localAuthenticators;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("localAuthenticators")
    @Valid
    public List<String> getLocalAuthenticators() {
        return localAuthenticators;
    }
    public void setLocalAuthenticators(List<String> localAuthenticators) {
        this.localAuthenticators = localAuthenticators;
    }

    public ConfiguredAuthenticatorStepModal addLocalAuthenticatorsItem(String localAuthenticatorsItem) {
        if (this.localAuthenticators == null) {
            this.localAuthenticators = new ArrayList<>();
        }
        this.localAuthenticators.add(localAuthenticatorsItem);
        return this;
    }

    /**
     **/
    public ConfiguredAuthenticatorStepModal federatedAuthenticators(List<String> federatedAuthenticators) {

        this.federatedAuthenticators = federatedAuthenticators;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("federatedAuthenticators")
    @Valid
    public List<String> getFederatedAuthenticators() {
        return federatedAuthenticators;
    }
    public void setFederatedAuthenticators(List<String> federatedAuthenticators) {
        this.federatedAuthenticators = federatedAuthenticators;
    }

    public ConfiguredAuthenticatorStepModal addFederatedAuthenticatorsItem(String federatedAuthenticatorsItem) {
        if (this.federatedAuthenticators == null) {
            this.federatedAuthenticators = new ArrayList<>();
        }
        this.federatedAuthenticators.add(federatedAuthenticatorsItem);
        return this;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfiguredAuthenticatorStepModal configuredAuthenticatorStepModal = (ConfiguredAuthenticatorStepModal) o;
        return Objects.equals(this.stepId, configuredAuthenticatorStepModal.stepId) &&
                Objects.equals(this.localAuthenticators, configuredAuthenticatorStepModal.localAuthenticators) &&
                Objects.equals(this.federatedAuthenticators, configuredAuthenticatorStepModal.federatedAuthenticators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepId, localAuthenticators, federatedAuthenticators);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConfiguredAuthenticatorStepModal {\n");

        sb.append("    stepId: ").append(toIndentedString(stepId)).append("\n");
        sb.append("    localAuthenticators: ").append(toIndentedString(localAuthenticators)).append("\n");
        sb.append("    federatedAuthenticators: ").append(toIndentedString(federatedAuthenticators)).append("\n");
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

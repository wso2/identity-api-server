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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MoesifPublisherPatchReq  {

    private String apiKeyValue;
    private Map<String, Boolean> eventPublisherEnablement = null;


    /**
    * Moesif collector API key value. If omitted or null, the existing API key is retained.
    **/
    public MoesifPublisherPatchReq apiKeyValue(String apiKeyValue) {

        this.apiKeyValue = apiKeyValue;
        return this;
    }

    @ApiModelProperty(example = "mzf_live_1234567890abcdef", value = "Moesif collector API key value. If omitted or null, the existing API key is retained.")
    @JsonProperty("apiKeyValue")
    @Valid
    public String getApiKeyValue() {
        return apiKeyValue;
    }
    public void setApiKeyValue(String apiKeyValue) {
        this.apiKeyValue = apiKeyValue;
    }

    /**
    * Map of event publisher key to enabled flag. Keys absent from the map default to false
    * (replace-all semantics). Known keys: "moesif-authentication-publisher",
    * "moesif-registration-publisher", "moesif-flow-publisher", "moesif-oauth2-token-publisher".
    **/
    public MoesifPublisherPatchReq eventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {

        this.eventPublisherEnablement = eventPublisherEnablement;
        return this;
    }

    @ApiModelProperty(example = "{\"moesif-authentication-publisher\":true,\"moesif-registration-publisher\":false,\"moesif-flow-publisher\":true,\"moesif-oauth2-token-publisher\":true}", required = true, value = "Map of event publisher key to enabled flag.")
    @JsonProperty("eventPublisherEnablement")
    @Valid
    @NotNull(message = "Property eventPublisherEnablement cannot be null.")
    public Map<String, Boolean> getEventPublisherEnablement() {
        return eventPublisherEnablement;
    }
    public void setEventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {
        this.eventPublisherEnablement = eventPublisherEnablement;
    }


    public MoesifPublisherPatchReq putEventPublisherEnablementItem(String key, Boolean eventPublisherEnablementItem) {
        if (this.eventPublisherEnablement == null) {
            this.eventPublisherEnablement = new HashMap<>();
        }
        this.eventPublisherEnablement.put(key, eventPublisherEnablementItem);
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
        MoesifPublisherPatchReq moesifPublisherPatchReq = (MoesifPublisherPatchReq) o;
        return Objects.equals(this.apiKeyValue, moesifPublisherPatchReq.apiKeyValue) &&
            Objects.equals(this.eventPublisherEnablement, moesifPublisherPatchReq.eventPublisherEnablement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKeyValue, eventPublisherEnablement);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MoesifPublisherPatchReq {\n");

        sb.append("    apiKeyValue: ").append(toIndentedString(apiKeyValue)).append("\n");
        sb.append("    eventPublisherEnablement: ").append(toIndentedString(eventPublisherEnablement)).append("\n");
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

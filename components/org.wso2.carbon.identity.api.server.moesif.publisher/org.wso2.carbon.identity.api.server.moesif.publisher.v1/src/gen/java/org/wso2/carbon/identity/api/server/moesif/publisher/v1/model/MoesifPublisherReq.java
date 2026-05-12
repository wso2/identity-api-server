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

public class MoesifPublisherReq  {
  
    private String apiKeyValue;
    private Map<String, Boolean> eventPublisherEnablement = null;


    /**
    * Moesif collector API key value.
    **/
    public MoesifPublisherReq apiKeyValue(String apiKeyValue) {

        this.apiKeyValue = apiKeyValue;
        return this;
    }
    
    @ApiModelProperty(example = "mzf_live_1234567890abcdef", required = true, value = "Moesif collector API key value.")
    @JsonProperty("apiKeyValue")
    @Valid
    @NotNull(message = "Property apiKeyValue cannot be null.")

    public String getApiKeyValue() {
        return apiKeyValue;
    }
    public void setApiKeyValue(String apiKeyValue) {
        this.apiKeyValue = apiKeyValue;
    }

    /**
    * Map of event publisher key to enabled flag (e.g. {\&quot;moesif-authentication-publisher\&quot;: true, \&quot;moesif-registration-publisher\&quot;: false, \&quot;flow\&quot;: true, \&quot;moesif-oauth2-token-publisher\&quot;: true}). Keys absent from the map default to false (replace-all semantics). Known keys: \&quot;moesif-authentication-publisher\&quot;, \&quot;moesif-registration-publisher\&quot;, \&quot;moesif-flow-publisher\&quot;, \&quot;moesif-oauth2-token-publisher\&quot;. 
    **/
    public MoesifPublisherReq eventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {

        this.eventPublisherEnablement = eventPublisherEnablement;
        return this;
    }
    
    @ApiModelProperty(example = "{\"authentication\":true,\"registration\":false,\"flow\":true,\"oauthToken\":true}", value = "Map of event publisher key to enabled flag (e.g. {\"moesif-authentication-publisher\": true, \"moesif-registration-publisher\": false, \"flow\": true, \"moesif-oauth2-token-publisher\": true}). Keys absent from the map default to false (replace-all semantics). Known keys: \"moesif-authentication-publisher\", \"moesif-registration-publisher\", \"moesif-flow-publisher\", \"moesif-oauth2-token-publisher\". ")
    @JsonProperty("eventPublisherEnablement")
    @Valid
    public Map<String, Boolean> getEventPublisherEnablement() {
        return eventPublisherEnablement;
    }
    public void setEventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {
        this.eventPublisherEnablement = eventPublisherEnablement;
    }


    public MoesifPublisherReq putEventPublisherEnablementItem(String key, Boolean eventPublisherEnablementItem) {
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
        MoesifPublisherReq moesifPublisherReq = (MoesifPublisherReq) o;
        return Objects.equals(this.apiKeyValue, moesifPublisherReq.apiKeyValue) &&
            Objects.equals(this.eventPublisherEnablement, moesifPublisherReq.eventPublisherEnablement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKeyValue, eventPublisherEnablement);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MoesifPublisherReq {\n");
        
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


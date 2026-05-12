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
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MoesifPublisherReq  {
  
    private String apiKeyValue;
    private Map<String, Boolean> publisherTypes = new HashMap<>();

    /**
    * Moesif collector API key value.
    **/
    public MoesifPublisherReq apiKeyValue(String apiKeyValue) {

        this.apiKeyValue = apiKeyValue;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Moesif collector API key value.")
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
    * Map of publisher type key to enabled flag. Keys absent default to false.
    **/
    public MoesifPublisherReq publisherTypes(Map<String, Boolean> publisherTypes) {

        this.publisherTypes = publisherTypes;
        return this;
    }

    @ApiModelProperty(value = "Map of publisher type key to enabled flag. Keys absent default to false.")
    @JsonProperty("publisherTypes")
    @Valid
    public Map<String, Boolean> getPublisherTypes() {
        return publisherTypes;
    }
    public void setPublisherTypes(Map<String, Boolean> publisherTypes) {
        this.publisherTypes = publisherTypes;
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
                Objects.equals(this.publisherTypes, moesifPublisherReq.publisherTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKeyValue, publisherTypes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MoesifPublisherReq {\n");
        
        sb.append("    apiKeyValue: ").append(toIndentedString("[REDACTED]")).append("\n");
        sb.append("    publisherTypes: ").append(toIndentedString(publisherTypes)).append("\n");
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


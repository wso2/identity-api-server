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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationUpdateEntry;
import javax.validation.constraints.*;

/**
 * Admin update of an existing consent. All fields are optional; omit a field to leave that aspect of the consent unchanged. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Admin update of an existing consent. All fields are optional; omit a field to leave that aspect of the consent unchanged. ")
public class ConsentUpdateRequest  {
  
    private Long expiryTime;
    private Map<String, String> properties = null;

    private List<AuthorizationUpdateEntry> authorizations = null;


    /**
    * When present, sets or extends the consent&#39;s expiry time to the given milliseconds-since-epoch timestamp. Omit to leave the existing expiry unchanged. Pass any negative value (e.g. &#x60;-1&#x60;) to remove the expiry entirely (the consent no longer expires). 
    **/
    public ConsentUpdateRequest expiryTime(Long expiryTime) {

        this.expiryTime = expiryTime;
        return this;
    }
    
    @ApiModelProperty(example = "1766383796000", value = "When present, sets or extends the consent's expiry time to the given milliseconds-since-epoch timestamp. Omit to leave the existing expiry unchanged. Pass any negative value (e.g. `-1`) to remove the expiry entirely (the consent no longer expires). ")
    @JsonProperty("expiryTime")
    @Valid
    public Long getExpiryTime() {
        return expiryTime;
    }
    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
    * When present, replaces the consent&#39;s full property map. Omit to leave existing properties unchanged. 
    **/
    public ConsentUpdateRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"dataCategory\":\"personal\",\"region\":\"EU\"}", value = "When present, replaces the consent's full property map. Omit to leave existing properties unchanged. ")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ConsentUpdateRequest putPropertiesItem(String key, String propertiesItem) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, propertiesItem);
        return this;
    }

        /**
    * When present, upserts each listed user&#39;s authorization — adds the user as an authorizer if absent, or overrides the existing authorization state if present. Users not listed are left untouched. 
    **/
    public ConsentUpdateRequest authorizations(List<AuthorizationUpdateEntry> authorizations) {

        this.authorizations = authorizations;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"userId\":\"alice@wso2.com\",\"state\":\"REVOKED\"},{\"userId\":\"carol@wso2.com\",\"type\":\"USER\",\"state\":\"APPROVED\"}]", value = "When present, upserts each listed user's authorization — adds the user as an authorizer if absent, or overrides the existing authorization state if present. Users not listed are left untouched. ")
    @JsonProperty("authorizations")
    @Valid
    public List<AuthorizationUpdateEntry> getAuthorizations() {
        return authorizations;
    }
    public void setAuthorizations(List<AuthorizationUpdateEntry> authorizations) {
        this.authorizations = authorizations;
    }

    public ConsentUpdateRequest addAuthorizationsItem(AuthorizationUpdateEntry authorizationsItem) {
        if (this.authorizations == null) {
            this.authorizations = new ArrayList<>();
        }
        this.authorizations.add(authorizationsItem);
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
        ConsentUpdateRequest consentUpdateRequest = (ConsentUpdateRequest) o;
        return Objects.equals(this.expiryTime, consentUpdateRequest.expiryTime) &&
            Objects.equals(this.properties, consentUpdateRequest.properties) &&
            Objects.equals(this.authorizations, consentUpdateRequest.authorizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiryTime, properties, authorizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentUpdateRequest {\n");
        
        sb.append("    expiryTime: ").append(toIndentedString(expiryTime)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("    authorizations: ").append(toIndentedString(authorizations)).append("\n");
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


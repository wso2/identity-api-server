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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class LoginFlowGenerateRequest  {
  
    private Map<String, List<Map<String, Object>>> availableAuthenticators = null;

    private List<Map<String, Object>> userClaims = null;

    private String userQuery;

    /**
    **/
    public LoginFlowGenerateRequest availableAuthenticators(Map<String, List<Map<String, Object>>> availableAuthenticators) {

        this.availableAuthenticators = availableAuthenticators;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("available_authenticators")
    @Valid
    public Map<String, List<Map<String, Object>>> getAvailableAuthenticators() {
        return availableAuthenticators;
    }
    public void setAvailableAuthenticators(Map<String, List<Map<String, Object>>> availableAuthenticators) {
        this.availableAuthenticators = availableAuthenticators;
    }


    public LoginFlowGenerateRequest putAvailableAuthenticatorsItem(String key, List<Map<String, Object>> availableAuthenticatorsItem) {
        if (this.availableAuthenticators == null) {
            this.availableAuthenticators = new HashMap<>();
        }
        this.availableAuthenticators.put(key, availableAuthenticatorsItem);
        return this;
    }

        /**
    **/
    public LoginFlowGenerateRequest userClaims(List<Map<String, Object>> userClaims) {

        this.userClaims = userClaims;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("user_claims")
    @Valid
    public List<Map<String, Object>> getUserClaims() {
        return userClaims;
    }
    public void setUserClaims(List<Map<String, Object>> userClaims) {
        this.userClaims = userClaims;
    }

    public LoginFlowGenerateRequest addUserClaimsItem(Map<String, Object> userClaimsItem) {
        if (this.userClaims == null) {
            this.userClaims = new ArrayList<>();
        }
        this.userClaims.add(userClaimsItem);
        return this;
    }

        /**
    **/
    public LoginFlowGenerateRequest userQuery(String userQuery) {

        this.userQuery = userQuery;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("user_query")
    @Valid
    public String getUserQuery() {
        return userQuery;
    }
    public void setUserQuery(String userQuery) {
        this.userQuery = userQuery;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginFlowGenerateRequest loginFlowGenerateRequest = (LoginFlowGenerateRequest) o;
        return Objects.equals(this.availableAuthenticators, loginFlowGenerateRequest.availableAuthenticators) &&
            Objects.equals(this.userClaims, loginFlowGenerateRequest.userClaims) &&
            Objects.equals(this.userQuery, loginFlowGenerateRequest.userQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableAuthenticators, userClaims, userQuery);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LoginFlowGenerateRequest {\n");
        
        sb.append("    availableAuthenticators: ").append(toIndentedString(availableAuthenticators)).append("\n");
        sb.append("    userClaims: ").append(toIndentedString(userClaims)).append("\n");
        sb.append("    userQuery: ").append(toIndentedString(userQuery)).append("\n");
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


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
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CIBAAuthenticationRequestConfiguration  {
  
    private Long cibaAuthReqExpiryTime;
    private List<String> cibaNotificationChannels = null;


    /**
    * CIBA authentication request expiry time in seconds.
    **/
    public CIBAAuthenticationRequestConfiguration cibaAuthReqExpiryTime(Long cibaAuthReqExpiryTime) {

        this.cibaAuthReqExpiryTime = cibaAuthReqExpiryTime;
        return this;
    }
    
    @ApiModelProperty(example = "600", value = "CIBA authentication request expiry time in seconds.")
    @JsonProperty("cibaAuthReqExpiryTime")
    @Valid
    public Long getCibaAuthReqExpiryTime() {
        return cibaAuthReqExpiryTime;
    }
    public void setCibaAuthReqExpiryTime(Long cibaAuthReqExpiryTime) {
        this.cibaAuthReqExpiryTime = cibaAuthReqExpiryTime;
    }

    /**
    * List of allowed notification channels.
    **/
    public CIBAAuthenticationRequestConfiguration cibaNotificationChannels(List<String> cibaNotificationChannels) {

        this.cibaNotificationChannels = cibaNotificationChannels;
        return this;
    }
    
    @ApiModelProperty(example = "[\"email\",\"sms\"]", value = "List of allowed notification channels.")
    @JsonProperty("cibaNotificationChannels")
    @Valid
    public List<String> getCibaNotificationChannels() {
        return cibaNotificationChannels;
    }
    public void setCibaNotificationChannels(List<String> cibaNotificationChannels) {
        this.cibaNotificationChannels = cibaNotificationChannels;
    }

    public CIBAAuthenticationRequestConfiguration addCibaNotificationChannelsItem(String cibaNotificationChannelsItem) {
        if (this.cibaNotificationChannels == null) {
            this.cibaNotificationChannels = new ArrayList<>();
        }
        this.cibaNotificationChannels.add(cibaNotificationChannelsItem);
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
        CIBAAuthenticationRequestConfiguration ciBAAuthenticationRequestConfiguration = (CIBAAuthenticationRequestConfiguration) o;
        return Objects.equals(this.cibaAuthReqExpiryTime, ciBAAuthenticationRequestConfiguration.cibaAuthReqExpiryTime) &&
            Objects.equals(this.cibaNotificationChannels, ciBAAuthenticationRequestConfiguration.cibaNotificationChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cibaAuthReqExpiryTime, cibaNotificationChannels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CIBAAuthenticationRequestConfiguration {\n");
        
        sb.append("    cibaAuthReqExpiryTime: ").append(toIndentedString(cibaAuthReqExpiryTime)).append("\n");
        sb.append("    cibaNotificationChannels: ").append(toIndentedString(cibaNotificationChannels)).append("\n");
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


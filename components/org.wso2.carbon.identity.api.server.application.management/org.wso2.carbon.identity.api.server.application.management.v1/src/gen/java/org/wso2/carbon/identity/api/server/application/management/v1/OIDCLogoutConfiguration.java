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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OIDCLogoutConfiguration  {
  
    private String backChannelLogoutUrl;
    private String frontChannelLogoutUrl;

    /**
    **/
    public OIDCLogoutConfiguration backChannelLogoutUrl(String backChannelLogoutUrl) {

        this.backChannelLogoutUrl = backChannelLogoutUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://app.example.com/backchannel/callback", value = "")
    @JsonProperty("backChannelLogoutUrl")
    @Valid
    public String getBackChannelLogoutUrl() {
        return backChannelLogoutUrl;
    }
    public void setBackChannelLogoutUrl(String backChannelLogoutUrl) {
        this.backChannelLogoutUrl = backChannelLogoutUrl;
    }

    /**
    **/
    public OIDCLogoutConfiguration frontChannelLogoutUrl(String frontChannelLogoutUrl) {

        this.frontChannelLogoutUrl = frontChannelLogoutUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://app.example.com/frontchannel/callback", value = "")
    @JsonProperty("frontChannelLogoutUrl")
    @Valid
    public String getFrontChannelLogoutUrl() {
        return frontChannelLogoutUrl;
    }
    public void setFrontChannelLogoutUrl(String frontChannelLogoutUrl) {
        this.frontChannelLogoutUrl = frontChannelLogoutUrl;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OIDCLogoutConfiguration oiDCLogoutConfiguration = (OIDCLogoutConfiguration) o;
        return Objects.equals(this.backChannelLogoutUrl, oiDCLogoutConfiguration.backChannelLogoutUrl) &&
            Objects.equals(this.frontChannelLogoutUrl, oiDCLogoutConfiguration.frontChannelLogoutUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backChannelLogoutUrl, frontChannelLogoutUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OIDCLogoutConfiguration {\n");
        
        sb.append("    backChannelLogoutUrl: ").append(toIndentedString(backChannelLogoutUrl)).append("\n");
        sb.append("    frontChannelLogoutUrl: ").append(toIndentedString(frontChannelLogoutUrl)).append("\n");
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


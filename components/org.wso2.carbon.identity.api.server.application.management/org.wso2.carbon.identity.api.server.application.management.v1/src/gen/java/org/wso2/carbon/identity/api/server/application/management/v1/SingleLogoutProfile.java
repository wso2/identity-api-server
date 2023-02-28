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
import org.wso2.carbon.identity.api.server.application.management.v1.IdpInitiatedSingleLogout;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SingleLogoutProfile  {
  
    private Boolean enabled = true;
    private String logoutRequestUrl;
    private String logoutResponseUrl;

@XmlType(name="LogoutMethodEnum")
@XmlEnum(String.class)
public enum LogoutMethodEnum {

    @XmlEnumValue("BACKCHANNEL") BACKCHANNEL(String.valueOf("BACKCHANNEL")), @XmlEnumValue("FRONTCHANNEL_HTTP_REDIRECT") FRONTCHANNEL_HTTP_REDIRECT(String.valueOf("FRONTCHANNEL_HTTP_REDIRECT")), @XmlEnumValue("FRONTCHANNEL_HTTP_POST") FRONTCHANNEL_HTTP_POST(String.valueOf("FRONTCHANNEL_HTTP_POST"));


    private String value;

    LogoutMethodEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static LogoutMethodEnum fromValue(String value) {
        for (LogoutMethodEnum b : LogoutMethodEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private LogoutMethodEnum logoutMethod;
    private IdpInitiatedSingleLogout idpInitiatedSingleLogout;

    /**
    **/
    public SingleLogoutProfile enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    * Single logout request accepting endpoint
    **/
    public SingleLogoutProfile logoutRequestUrl(String logoutRequestUrl) {

        this.logoutRequestUrl = logoutRequestUrl;
        return this;
    }
    
    @ApiModelProperty(value = "Single logout request accepting endpoint")
    @JsonProperty("logoutRequestUrl")
    @Valid
    public String getLogoutRequestUrl() {
        return logoutRequestUrl;
    }
    public void setLogoutRequestUrl(String logoutRequestUrl) {
        this.logoutRequestUrl = logoutRequestUrl;
    }

    /**
    * Single logout response accepting endpoint
    **/
    public SingleLogoutProfile logoutResponseUrl(String logoutResponseUrl) {

        this.logoutResponseUrl = logoutResponseUrl;
        return this;
    }
    
    @ApiModelProperty(value = "Single logout response accepting endpoint")
    @JsonProperty("logoutResponseUrl")
    @Valid
    public String getLogoutResponseUrl() {
        return logoutResponseUrl;
    }
    public void setLogoutResponseUrl(String logoutResponseUrl) {
        this.logoutResponseUrl = logoutResponseUrl;
    }

    /**
    **/
    public SingleLogoutProfile logoutMethod(LogoutMethodEnum logoutMethod) {

        this.logoutMethod = logoutMethod;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("logoutMethod")
    @Valid
    public LogoutMethodEnum getLogoutMethod() {
        return logoutMethod;
    }
    public void setLogoutMethod(LogoutMethodEnum logoutMethod) {
        this.logoutMethod = logoutMethod;
    }

    /**
    **/
    public SingleLogoutProfile idpInitiatedSingleLogout(IdpInitiatedSingleLogout idpInitiatedSingleLogout) {

        this.idpInitiatedSingleLogout = idpInitiatedSingleLogout;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("idpInitiatedSingleLogout")
    @Valid
    public IdpInitiatedSingleLogout getIdpInitiatedSingleLogout() {
        return idpInitiatedSingleLogout;
    }
    public void setIdpInitiatedSingleLogout(IdpInitiatedSingleLogout idpInitiatedSingleLogout) {
        this.idpInitiatedSingleLogout = idpInitiatedSingleLogout;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingleLogoutProfile singleLogoutProfile = (SingleLogoutProfile) o;
        return Objects.equals(this.enabled, singleLogoutProfile.enabled) &&
            Objects.equals(this.logoutRequestUrl, singleLogoutProfile.logoutRequestUrl) &&
            Objects.equals(this.logoutResponseUrl, singleLogoutProfile.logoutResponseUrl) &&
            Objects.equals(this.logoutMethod, singleLogoutProfile.logoutMethod) &&
            Objects.equals(this.idpInitiatedSingleLogout, singleLogoutProfile.idpInitiatedSingleLogout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, logoutRequestUrl, logoutResponseUrl, logoutMethod, idpInitiatedSingleLogout);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SingleLogoutProfile {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    logoutRequestUrl: ").append(toIndentedString(logoutRequestUrl)).append("\n");
        sb.append("    logoutResponseUrl: ").append(toIndentedString(logoutResponseUrl)).append("\n");
        sb.append("    logoutMethod: ").append(toIndentedString(logoutMethod)).append("\n");
        sb.append("    idpInitiatedSingleLogout: ").append(toIndentedString(idpInitiatedSingleLogout)).append("\n");
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


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

/**
 * Decides the trusted app configurations for the application.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Decides the trusted app configurations for the application.")
public class TrustedAppConfiguration  {
  
    private Boolean isFIDOTrustedApp;
    private String androidPackageName;
    private String androidThumbprints;
    private String appleAppId;

    /**
    * Decides whether the application is a FIDO trusted app.
    **/
    public TrustedAppConfiguration isFIDOTrustedApp(Boolean isFIDOTrustedApp) {

        this.isFIDOTrustedApp = isFIDOTrustedApp;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Decides whether the application is a FIDO trusted app.")
    @JsonProperty("isFIDOTrustedApp")
    @Valid
    public Boolean getIsFIDOTrustedApp() {
        return isFIDOTrustedApp;
    }
    public void setIsFIDOTrustedApp(Boolean isFIDOTrustedApp) {
        this.isFIDOTrustedApp = isFIDOTrustedApp;
    }

    /**
    * Decides the android package name for the application.
    **/
    public TrustedAppConfiguration androidPackageName(String androidPackageName) {

        this.androidPackageName = androidPackageName;
        return this;
    }
    
    @ApiModelProperty(example = "com.wso2.mobile.sample", value = "Decides the android package name for the application.")
    @JsonProperty("androidPackageName")
    @Valid
    public String getAndroidPackageName() {
        return androidPackageName;
    }
    public void setAndroidPackageName(String androidPackageName) {
        this.androidPackageName = androidPackageName;
    }

    /**
    * Decides the android thumbprints for the application.
    **/
    public TrustedAppConfiguration androidThumbprints(String androidThumbprints) {

        this.androidThumbprints = androidThumbprints;
        return this;
    }
    
    @ApiModelProperty(example = "18:94:0A:DE:63:77:B6:84:43:1E:85:8F:03:CF:8A:14:87:9C:DE:DF:EA:7A:25:53:CD:53:5A:AF:C3:54:A5:56", value = "Decides the android thumbprints for the application.")
    @JsonProperty("androidThumbprints")
    @Valid
    public String getAndroidThumbprints() {
        return androidThumbprints;
    }
    public void setAndroidThumbprints(String androidThumbprints) {
        this.androidThumbprints = androidThumbprints;
    }

    /**
    * Decides the apple app id for the application.
    **/
    public TrustedAppConfiguration appleAppId(String appleAppId) {

        this.appleAppId = appleAppId;
        return this;
    }
    
    @ApiModelProperty(example = "APPLETEAMID.com.wso2.mobile.sample", value = "Decides the apple app id for the application.")
    @JsonProperty("appleAppId")
    @Valid
    public String getAppleAppId() {
        return appleAppId;
    }
    public void setAppleAppId(String appleAppId) {
        this.appleAppId = appleAppId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrustedAppConfiguration trustedAppConfiguration = (TrustedAppConfiguration) o;
        return Objects.equals(this.isFIDOTrustedApp, trustedAppConfiguration.isFIDOTrustedApp) &&
            Objects.equals(this.androidPackageName, trustedAppConfiguration.androidPackageName) &&
            Objects.equals(this.androidThumbprints, trustedAppConfiguration.androidThumbprints) &&
            Objects.equals(this.appleAppId, trustedAppConfiguration.appleAppId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFIDOTrustedApp, androidPackageName, androidThumbprints, appleAppId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TrustedAppConfiguration {\n");
        
        sb.append("    isFIDOTrustedApp: ").append(toIndentedString(isFIDOTrustedApp)).append("\n");
        sb.append("    androidPackageName: ").append(toIndentedString(androidPackageName)).append("\n");
        sb.append("    androidThumbprints: ").append(toIndentedString(androidThumbprints)).append("\n");
        sb.append("    appleAppId: ").append(toIndentedString(appleAppId)).append("\n");
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

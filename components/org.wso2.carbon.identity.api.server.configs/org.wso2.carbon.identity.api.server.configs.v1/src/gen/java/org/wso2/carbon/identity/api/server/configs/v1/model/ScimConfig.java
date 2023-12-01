/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ScimConfig  {
  
    private String provisioningUserstore = "PRIMARY";
    private Boolean enableProxyMode = false;

    /**
    * userstore domain name to inbound provision users and groups. This property becomes only applicable if the enableProxyMode config is set to false
    **/
    public ScimConfig provisioningUserstore(String provisioningUserstore) {

        this.provisioningUserstore = provisioningUserstore;
        return this;
    }
    
    @ApiModelProperty(example = "PRIMARY", value = "userstore domain name to inbound provision users and groups. This property becomes only applicable if the enableProxyMode config is set to false")
    @JsonProperty("provisioningUserstore")
    @Valid
    public String getProvisioningUserstore() {
        return provisioningUserstore;
    }
    public void setProvisioningUserstore(String provisioningUserstore) {
        this.provisioningUserstore = provisioningUserstore;
    }

    /**
    * If this property is set to true, Users/Groups are not provisioned to the user store. They are only outbound provisioned
    **/
    public ScimConfig enableProxyMode(Boolean enableProxyMode) {

        this.enableProxyMode = enableProxyMode;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "If this property is set to true, Users/Groups are not provisioned to the user store. They are only outbound provisioned")
    @JsonProperty("enableProxyMode")
    @Valid
    public Boolean getEnableProxyMode() {
        return enableProxyMode;
    }
    public void setEnableProxyMode(Boolean enableProxyMode) {
        this.enableProxyMode = enableProxyMode;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScimConfig scimConfig = (ScimConfig) o;
        return Objects.equals(this.provisioningUserstore, scimConfig.provisioningUserstore) &&
            Objects.equals(this.enableProxyMode, scimConfig.enableProxyMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provisioningUserstore, enableProxyMode);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ScimConfig {\n");
        
        sb.append("    provisioningUserstore: ").append(toIndentedString(provisioningUserstore)).append("\n");
        sb.append("    enableProxyMode: ").append(toIndentedString(enableProxyMode)).append("\n");
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


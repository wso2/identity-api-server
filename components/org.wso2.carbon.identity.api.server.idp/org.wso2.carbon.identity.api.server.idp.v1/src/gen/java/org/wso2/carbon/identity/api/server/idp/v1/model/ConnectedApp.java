/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConnectedApp  {
  
    private String appId;
    private String connectedApp;

    /**
    **/
    public ConnectedApp appId(String appId) {

        this.appId = appId;
        return this;
    }
    
    @ApiModelProperty(example = "app-id", value = "")
    @JsonProperty("appId")
    @Valid
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
    **/
    public ConnectedApp connectedApp(String connectedApp) {

        this.connectedApp = connectedApp;
        return this;
    }
    
    @ApiModelProperty(example = "connected-app-url", value = "")
    @JsonProperty("connectedApp")
    @Valid
    public String getConnectedApp() {
        return connectedApp;
    }
    public void setConnectedApp(String connectedApp) {
        this.connectedApp = connectedApp;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectedApp connectedApp = (ConnectedApp) o;
        return Objects.equals(this.appId, connectedApp.appId) &&
            Objects.equals(this.connectedApp, connectedApp.connectedApp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, connectedApp);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectedApp {\n");
        
        sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
        sb.append("    connectedApp: ").append(toIndentedString(connectedApp)).append("\n");
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


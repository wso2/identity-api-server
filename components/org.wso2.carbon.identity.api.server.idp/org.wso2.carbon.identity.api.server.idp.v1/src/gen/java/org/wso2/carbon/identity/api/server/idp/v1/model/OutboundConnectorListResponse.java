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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OutboundConnectorListResponse  {
  
    private String defaultConnectorId;
    private List<OutboundConnectorListItem> connectors = null;


    /**
    **/
    public OutboundConnectorListResponse defaultConnectorId(String defaultConnectorId) {

        this.defaultConnectorId = defaultConnectorId;
        return this;
    }
    
    @ApiModelProperty(example = "U0NJTQ", value = "")
    @JsonProperty("defaultConnectorId")
    @Valid
    public String getDefaultConnectorId() {
        return defaultConnectorId;
    }
    public void setDefaultConnectorId(String defaultConnectorId) {
        this.defaultConnectorId = defaultConnectorId;
    }

    /**
    **/
    public OutboundConnectorListResponse connectors(List<OutboundConnectorListItem> connectors) {

        this.connectors = connectors;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("connectors")
    @Valid
    public List<OutboundConnectorListItem> getConnectors() {
        return connectors;
    }
    public void setConnectors(List<OutboundConnectorListItem> connectors) {
        this.connectors = connectors;
    }

    public OutboundConnectorListResponse addConnectorsItem(OutboundConnectorListItem connectorsItem) {
        if (this.connectors == null) {
            this.connectors = new ArrayList<>();
        }
        this.connectors.add(connectorsItem);
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
        OutboundConnectorListResponse outboundConnectorListResponse = (OutboundConnectorListResponse) o;
        return Objects.equals(this.defaultConnectorId, outboundConnectorListResponse.defaultConnectorId) &&
            Objects.equals(this.connectors, outboundConnectorListResponse.connectors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultConnectorId, connectors);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OutboundConnectorListResponse {\n");
        
        sb.append("    defaultConnectorId: ").append(toIndentedString(defaultConnectorId)).append("\n");
        sb.append("    connectors: ").append(toIndentedString(connectors)).append("\n");
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


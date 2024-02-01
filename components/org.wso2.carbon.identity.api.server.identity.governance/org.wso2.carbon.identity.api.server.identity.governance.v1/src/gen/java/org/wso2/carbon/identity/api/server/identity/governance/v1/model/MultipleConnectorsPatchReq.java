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

package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorReq;
import javax.validation.constraints.*;

/**
 * Multiple governance connectors properties patch request.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Multiple governance connectors properties patch request.")
public class MultipleConnectorsPatchReq  {
  

@XmlType(name="OperationEnum")
@XmlEnum(String.class)
public enum OperationEnum {

    @XmlEnumValue("UPDATE") UPDATE(String.valueOf("UPDATE"));


    private String value;

    OperationEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OperationEnum fromValue(String value) {
        for (OperationEnum b : OperationEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private OperationEnum operation;
    private List<ConnectorReq> connectors = new ArrayList<>();


    /**
    * Governance connector properties patch operation.
    **/
    public MultipleConnectorsPatchReq operation(OperationEnum operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(example = "UPDATE", required = true, value = "Governance connector properties patch operation.")
    @JsonProperty("operation")
    @Valid
    @NotNull(message = "Property operation cannot be null.")

    public OperationEnum getOperation() {
        return operation;
    }
    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    /**
    * Governance connectors to patch
    **/
    public MultipleConnectorsPatchReq connectors(List<ConnectorReq> connectors) {

        this.connectors = connectors;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Governance connectors to patch")
    @JsonProperty("connectors")
    @Valid
    @NotNull(message = "Property connectors cannot be null.")

    public List<ConnectorReq> getConnectors() {
        return connectors;
    }
    public void setConnectors(List<ConnectorReq> connectors) {
        this.connectors = connectors;
    }

    public MultipleConnectorsPatchReq addConnectorsItem(ConnectorReq connectorsItem) {
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
        MultipleConnectorsPatchReq multipleConnectorsPatchReq = (MultipleConnectorsPatchReq) o;
        return Objects.equals(this.operation, multipleConnectorsPatchReq.operation) &&
            Objects.equals(this.connectors, multipleConnectorsPatchReq.connectors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, connectors);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MultipleConnectorsPatchReq {\n");
        
        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
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


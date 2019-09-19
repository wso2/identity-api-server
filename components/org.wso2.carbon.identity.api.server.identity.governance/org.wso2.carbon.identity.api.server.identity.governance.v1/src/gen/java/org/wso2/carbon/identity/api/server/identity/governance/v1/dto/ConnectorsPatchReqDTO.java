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

package org.wso2.carbon.identity.api.server.identity.governance.v1.dto;

import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.PropertyReqDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



/**
 * Governance connector property patch request.
 **/


@ApiModel(description = "Governance connector property patch request.")
public class ConnectorsPatchReqDTO  {
  
  
  public enum OperationEnum {
     update, 
  };
  
  private OperationEnum operation = null;
  
  
  private List<PropertyReqDTO> properties = new ArrayList<PropertyReqDTO>();

  
  /**
   * Governance connector properties patch operation.
   **/
  @ApiModelProperty(value = "Governance connector properties patch operation.")
  @JsonProperty("operation")
  public OperationEnum getOperation() {
    return operation;
  }
  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }

  
  /**
   * Governance connector properties to patch.
   **/
  @ApiModelProperty(value = "Governance connector properties to patch.")
  @JsonProperty("properties")
  public List<PropertyReqDTO> getProperties() {
    return properties;
  }
  public void setProperties(List<PropertyReqDTO> properties) {
    this.properties = properties;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectorsPatchReqDTO {\n");
    
    sb.append("  operation: ").append(operation).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

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
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.CategoryConnectorsResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.LinkDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



/**
 * Governance connector category response.
 **/


@ApiModel(description = "Governance connector category response.")
public class CategoriesResDTO  {
  
  
  
  private String id = null;
  
  
  private String name = null;
  
  
  private List<LinkDTO> links = new ArrayList<LinkDTO>();
  
  
  private List<CategoryConnectorsResDTO> connectors = new ArrayList<CategoryConnectorsResDTO>();

  
  /**
   * Connector category id.
   **/
  @ApiModelProperty(value = "Connector category id.")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Connector category name.
   **/
  @ApiModelProperty(value = "Connector category name.")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * Connectors of the category with minimal attributes.
   **/
  @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
  @JsonProperty("links")
  public List<LinkDTO> getLinks() {
    return links;
  }
  public void setLinks(List<LinkDTO> links) {
    this.links = links;
  }

  
  /**
   * Connectors of the category with minimal attributes.
   **/
  @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
  @JsonProperty("connectors")
  public List<CategoryConnectorsResDTO> getConnectors() {
    return connectors;
  }
  public void setConnectors(List<CategoryConnectorsResDTO> connectors) {
    this.connectors = connectors;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoriesResDTO {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  links: ").append(links).append("\n");
    sb.append("  connectors: ").append(connectors).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

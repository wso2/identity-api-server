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
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.PropertyResDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



/**
 * Governance connector response.
 **/


@ApiModel(description = "Governance connector response.")
public class ConnectorsResDTO  {
  
  
  
  private String id = null;
  
  
  private String name = null;
  
  
  private String category = null;
  
  
  private String friendlyName = null;
  
  
  private Integer order = null;
  
  
  private String subCategory = null;
  
  
  private List<PropertyResDTO> properties = new ArrayList<PropertyResDTO>();

  
  /**
   * Connector id.
   **/
  @ApiModelProperty(value = "Connector id.")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Connector name.
   **/
  @ApiModelProperty(value = "Connector name.")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * Connector category.
   **/
  @ApiModelProperty(value = "Connector category.")
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

  
  /**
   * Connector friendly name.
   **/
  @ApiModelProperty(value = "Connector friendly name.")
  @JsonProperty("friendlyName")
  public String getFriendlyName() {
    return friendlyName;
  }
  public void setFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  
  /**
   * Connector order.
   **/
  @ApiModelProperty(value = "Connector order.")
  @JsonProperty("order")
  public Integer getOrder() {
    return order;
  }
  public void setOrder(Integer order) {
    this.order = order;
  }

  
  /**
   * Connector subcategory.
   **/
  @ApiModelProperty(value = "Connector subcategory.")
  @JsonProperty("subCategory")
  public String getSubCategory() {
    return subCategory;
  }
  public void setSubCategory(String subCategory) {
    this.subCategory = subCategory;
  }

  
  /**
   * Define any additional properties if required.
   **/
  @ApiModelProperty(value = "Define any additional properties if required.")
  @JsonProperty("properties")
  public List<PropertyResDTO> getProperties() {
    return properties;
  }
  public void setProperties(List<PropertyResDTO> properties) {
    this.properties = properties;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectorsResDTO {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  category: ").append(category).append("\n");
    sb.append("  friendlyName: ").append(friendlyName).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  subCategory: ").append(subCategory).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

package org.wso2.carbon.identity.rest.api.server.challenge.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class ErrorDTO  {
  
  
  @NotNull
  private String code = null;
  
  @NotNull
  private String message = null;
  
  
  private String description = null;

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("code")
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorDTO {\n");
    
    sb.append("  code: ").append(code).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

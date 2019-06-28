package org.wso2.carbon.identity.rest.api.server.challenge.v1.dto;

import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class ChallengeQuestionPatchDTO  {
  
  
  @NotNull
  private ChallengeQuestionDTO challengeQuestion = null;
  
  @NotNull
  private String operation = null;

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("challengeQuestion")
  public ChallengeQuestionDTO getChallengeQuestion() {
    return challengeQuestion;
  }
  public void setChallengeQuestion(ChallengeQuestionDTO challengeQuestion) {
    this.challengeQuestion = challengeQuestion;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeQuestionPatchDTO {\n");
    
    sb.append("  challengeQuestion: ").append(challengeQuestion).append("\n");
    sb.append("  operation: ").append(operation).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

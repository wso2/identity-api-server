package org.wso2.carbon.identity.rest.api.server.challenge.v1.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;





@ApiModel(description = "")
public class ChallengeSetDTO  {
  
  
  
  private String questionSetId = null;
  
  @NotNull
  private List<ChallengeQuestionDTO> questions = new ArrayList<ChallengeQuestionDTO>();

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("questionSetId")
  public String getQuestionSetId() {
    return questionSetId;
  }
  public void setQuestionSetId(String questionSetId) {
    this.questionSetId = questionSetId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("questions")
  public List<ChallengeQuestionDTO> getQuestions() {
    return questions;
  }
  public void setQuestions(List<ChallengeQuestionDTO> questions) {
    this.questions = questions;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeSetDTO {\n");
    
    sb.append("  questionSetId: ").append(questionSetId).append("\n");
    sb.append("  questions: ").append(questions).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

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

package org.wso2.carbon.identity.rest.api.server.challenge.v1.dto;

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class ChallengeSetDTO  {
  
  
  
  private String questionSetId = null;
  
  @NotNull 
  private List<ChallengeQuestionDTO> questions = new ArrayList<ChallengeQuestionDTO>();

  
  /**
   * A unique ID for the challenge set.
   **/
  @ApiModelProperty(value = "A unique ID for the challenge set.")
  @JsonProperty("questionSetId")
  public String getQuestionSetId() {
    return questionSetId;
  }
  public void setQuestionSetId(String questionSetId) {
    this.questionSetId = questionSetId;
  }

  
  /**
   * Challenge questions for the set.
   **/
  @ApiModelProperty(required = true, value = "Challenge questions for the set.")
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

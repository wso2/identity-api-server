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


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class ChallengeQuestionDTO  {
  
  
  
  private String locale = null;
  
  @NotNull 
  private String question = null;
  
  
  private String questionId = null;

  
  /**
   * The locale of the question.
   **/
  @ApiModelProperty(value = "The locale of the question.")
  @JsonProperty("locale")
  public String getLocale() {
    return locale;
  }
  public void setLocale(String locale) {
    this.locale = locale;
  }

  
  /**
   * Challenge question display value.
   **/
  @ApiModelProperty(required = true, value = "Challenge question display value.")
  @JsonProperty("question")
  public String getQuestion() {
    return question;
  }
  public void setQuestion(String question) {
    this.question = question;
  }

  
  /**
   * A unique ID for the challenge quesion within the set.
   **/
  @ApiModelProperty(value = "A unique ID for the challenge quesion within the set.")
  @JsonProperty("questionId")
  public String getQuestionId() {
    return questionId;
  }
  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeQuestionDTO {\n");
    
    sb.append("  locale: ").append(locale).append("\n");
    sb.append("  question: ").append(question).append("\n");
    sb.append("  questionId: ").append(questionId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

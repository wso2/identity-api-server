/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
 * Error model for debug API responses.
 */
@ApiModel(description = "Error model for debug API responses")
public class Error {
    
    private String code;
    private String message;
    private String description;
    private String traceId;

    /**
     * Error code.
     * 
     * @param code Error code
     * @return Error instance
     */
    public Error code(String code) {
        this.code = code;
        return this;
    }
    
    @ApiModelProperty(example = "DEBUG-00000", value = "Error code")
    @JsonProperty("code")
    @Valid
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Error message.
     * 
     * @param message Error message
     * @return Error instance
     */
    public Error message(String message) {
        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "Debug operation failed", value = "Error message")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Error description.
     * 
     * @param description Error description
     * @return Error instance
     */
    public Error description(String description) {
        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "The debug operation failed due to invalid credentials", value = "Error description")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Trace ID.
     * 
     * @param traceId Trace ID
     * @return Error instance
     */
    public Error traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    @ApiModelProperty(example = "e0fbcfeb-3617-43c4-8dd0-7b7d38e13047", value = "Trace ID")
    @JsonProperty("traceId")
    @Valid
    public String getTraceId() {
        return traceId;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}

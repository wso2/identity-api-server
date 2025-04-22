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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class BrandingGenerationResultModel  {
  

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("IN_PROGRESS") IN_PROGRESS(String.valueOf("IN_PROGRESS")), @XmlEnumValue("COMPLETED") COMPLETED(String.valueOf("COMPLETED")), @XmlEnumValue("FAILED") FAILED(String.valueOf("FAILED"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;
    private Object data;

    /**
    * The current result of the AI branding operation.
    **/
    public BrandingGenerationResultModel status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "The current result of the AI branding operation.")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * The payload of the response, which varies based on the operation status. - For IN_PROGRESS status, an empty JSON object is returned. - For COMPLETED status, the &#x60;BrandingPreferenceModel&#x60; is returned. - For FAILED status, an error message is returned. 
    **/
    public BrandingGenerationResultModel data(Object data) {

        this.data = data;
        return this;
    }
    
    @ApiModelProperty(value = "The payload of the response, which varies based on the operation status. - For IN_PROGRESS status, an empty JSON object is returned. - For COMPLETED status, the `BrandingPreferenceModel` is returned. - For FAILED status, an error message is returned. ")
    @JsonProperty("data")
    @Valid
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandingGenerationResultModel brandingGenerationResultModel = (BrandingGenerationResultModel) o;
        return Objects.equals(this.status, brandingGenerationResultModel.status) &&
            Objects.equals(this.data, brandingGenerationResultModel.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BrandingGenerationResultModel {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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


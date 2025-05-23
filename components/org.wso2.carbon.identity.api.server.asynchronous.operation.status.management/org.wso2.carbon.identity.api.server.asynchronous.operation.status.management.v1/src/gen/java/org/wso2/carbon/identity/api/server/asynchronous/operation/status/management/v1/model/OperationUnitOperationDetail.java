/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.net.URI;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationUnitOperationDetailSummary;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OperationUnitOperationDetail  {
  
    private URI ref;
    private OperationUnitOperationDetailSummary summary;

    /**
    * Reference that will return the corresponsing unitoperations.
    **/
    public OperationUnitOperationDetail ref(URI ref) {

        this.ref = ref;
        return this;
    }
    
    @ApiModelProperty(example = "/api/server/v1/async-operations/8a92bb92-c754-4dfe-8563-15ba930de75e/unit-operations?limit=10", value = "Reference that will return the corresponsing unitoperations.")
    @JsonProperty("ref")
    @Valid
    public URI getRef() {
        return ref;
    }
    public void setRef(URI ref) {
        this.ref = ref;
    }

    /**
    **/
    public OperationUnitOperationDetail summary(OperationUnitOperationDetailSummary summary) {

        this.summary = summary;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("summary")
    @Valid
    public OperationUnitOperationDetailSummary getSummary() {
        return summary;
    }
    public void setSummary(OperationUnitOperationDetailSummary summary) {
        this.summary = summary;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OperationUnitOperationDetail operationUnitOperationDetail = (OperationUnitOperationDetail) o;
        return Objects.equals(this.ref, operationUnitOperationDetail.ref) &&
            Objects.equals(this.summary, operationUnitOperationDetail.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref, summary);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OperationUnitOperationDetail {\n");
        
        sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
        sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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


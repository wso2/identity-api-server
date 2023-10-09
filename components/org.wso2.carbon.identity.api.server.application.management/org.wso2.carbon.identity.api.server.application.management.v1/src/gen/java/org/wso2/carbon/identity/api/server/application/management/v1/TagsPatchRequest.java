/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TagsPatchRequest  {


    @XmlType(name="OperationEnum")
    @XmlEnum(String.class)
    public enum OperationEnum {

        @XmlEnumValue("ADD") ADD(String.valueOf("ADD")), @XmlEnumValue("REMOVE") REMOVE(String.valueOf("REMOVE"));


        private String value;

        OperationEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static OperationEnum fromValue(String value) {
            for (OperationEnum b : OperationEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private OperationEnum operation;
    private List<ListValue> tags = null;


    /**
     **/
    public TagsPatchRequest operation(OperationEnum operation) {

        this.operation = operation;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("operation")
    @Valid
    public OperationEnum getOperation() {
        return operation;
    }
    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    /**
     **/
    public TagsPatchRequest tags(List<ListValue> tags) {

        this.tags = tags;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("tags")
    @Valid
    public List<ListValue> getTags() {
        return tags;
    }
    public void setTags(List<ListValue> tags) {
        this.tags = tags;
    }

    public TagsPatchRequest addTagsItem(ListValue tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagsPatchRequest tagsPatchRequest = (TagsPatchRequest) o;
        return Objects.equals(this.operation, tagsPatchRequest.operation) &&
                Objects.equals(this.tags, tagsPatchRequest.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, tags);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TagsPatchRequest {\n");

        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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


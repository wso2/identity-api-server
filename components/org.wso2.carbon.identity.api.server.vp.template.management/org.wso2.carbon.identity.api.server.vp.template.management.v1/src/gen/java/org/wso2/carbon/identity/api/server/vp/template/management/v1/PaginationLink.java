/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Cursor-based pagination link.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Cursor-based pagination link.")
public class PaginationLink  {
  

@XmlType(name="RelEnum")
@XmlEnum(String.class)
public enum RelEnum {

    @XmlEnumValue("next") NEXT(String.valueOf("next")), @XmlEnumValue("previous") PREVIOUS(String.valueOf("previous"));


    private String value;

    RelEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RelEnum fromValue(String value) {
        for (RelEnum b : RelEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private RelEnum rel;
    private String href;

    /**
    * Link relation — either \&quot;next\&quot; (forward) or \&quot;previous\&quot; (backward).
    **/
    public PaginationLink rel(RelEnum rel) {

        this.rel = rel;
        return this;
    }
    
    @ApiModelProperty(example = "next", value = "Link relation — either \"next\" (forward) or \"previous\" (backward).")
    @JsonProperty("rel")
    @Valid
    public RelEnum getRel() {
        return rel;
    }
    public void setRel(RelEnum rel) {
        this.rel = rel;
    }

    /**
    * Relative URL for fetching the linked page.
    **/
    public PaginationLink href(String href) {

        this.href = href;
        return this;
    }
    
    @ApiModelProperty(example = "/presentation-definitions?limit=10&after=NDoy", value = "Relative URL for fetching the linked page.")
    @JsonProperty("href")
    @Valid
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaginationLink paginationLink = (PaginationLink) o;
        return Objects.equals(this.rel, paginationLink.rel) &&
            Objects.equals(this.href, paginationLink.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rel, href);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PaginationLink {\n");
        
        sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
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


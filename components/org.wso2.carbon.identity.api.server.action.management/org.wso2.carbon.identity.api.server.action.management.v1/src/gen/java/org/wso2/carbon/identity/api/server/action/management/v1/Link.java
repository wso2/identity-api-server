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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Link  {
  
    private String href;

@XmlType(name="MethodEnum")
@XmlEnum(String.class)
public enum MethodEnum {

    @XmlEnumValue("GET") GET(String.valueOf("GET"));


    private String value;

    MethodEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static MethodEnum fromValue(String value) {
        for (MethodEnum b : MethodEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private MethodEnum method;
    private String rel;

    /**
    * Url of the endpoint.
    **/
    public Link href(String href) {

        this.href = href;
        return this;
    }
    
    @ApiModelProperty(value = "Url of the endpoint.")
    @JsonProperty("href")
    @Valid
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }

    /**
    * Http method.
    **/
    public Link method(MethodEnum method) {

        this.method = method;
        return this;
    }
    
    @ApiModelProperty(value = "Http method.")
    @JsonProperty("method")
    @Valid
    public MethodEnum getMethod() {
        return method;
    }
    public void setMethod(MethodEnum method) {
        this.method = method;
    }

    /**
    * Relation to the resource.
    **/
    public Link rel(String rel) {

        this.rel = rel;
        return this;
    }
    
    @ApiModelProperty(value = "Relation to the resource.")
    @JsonProperty("rel")
    @Valid
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href) &&
            Objects.equals(this.method, link.method) &&
            Objects.equals(this.rel, link.rel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, method, rel);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Link {\n");
        
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    method: ").append(toIndentedString(method)).append("\n");
        sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
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


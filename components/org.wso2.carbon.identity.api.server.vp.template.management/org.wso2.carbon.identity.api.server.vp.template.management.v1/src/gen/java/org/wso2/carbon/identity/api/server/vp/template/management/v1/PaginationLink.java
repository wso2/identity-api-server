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
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Pagination link with rel and href.
 */
public class PaginationLink {

    private String rel;
    private String href;

    @ApiModelProperty(example = "next")
    @JsonProperty("rel")
    public String getRel() {

        return rel;
    }

    public void setRel(String rel) {

        this.rel = rel;
    }

    public PaginationLink rel(String rel) {

        this.rel = rel;
        return this;
    }

    @ApiModelProperty(example = "/api/server/v1/openid4vp/presentation-definitions?after=NDoy")
    @JsonProperty("href")
    public String getHref() {

        return href;
    }

    public void setHref(String href) {

        this.href = href;
    }

    public PaginationLink href(String href) {

        this.href = href;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaginationLink that = (PaginationLink) o;
        return Objects.equals(rel, that.rel) && Objects.equals(href, that.href);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rel, href);
    }

    @Override
    public String toString() {

        return "PaginationLink{rel='" + rel + "', href='" + href + "'}";
    }
}

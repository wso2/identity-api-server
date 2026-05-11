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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core;

import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Link;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.ResponseAgentSharedOrgsDO;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Utility for building pagination links in shared-organizations API responses.
 */
class PaginationLinkBuilder {

    private PaginationLinkBuilder() {

    }

    static List<Link> buildPaginationLinks(ResponseAgentSharedOrgsDO result, String agentId, String filter,
                                           Integer limit, Boolean recursive, String attributes) {

        UriInfo uriInfo = getCurrentRequestUriInfo();
        List<Link> links = new ArrayList<>();

        int nextCursor = result.getNextPageCursor();
        if (nextCursor > 0) {
            links.add(new Link()
                    .rel("next")
                    .href(buildSharedOrgsPageLink(uriInfo, agentId, filter, limit, recursive, attributes,
                            encodeCursor(nextCursor), null)));
        }
        int prevCursor = result.getPreviousPageCursor();
        if (prevCursor > 0) {
            links.add(new Link()
                    .rel("previous")
                    .href(buildSharedOrgsPageLink(uriInfo, agentId, filter, limit, recursive, attributes,
                            null, encodeCursor(prevCursor))));
        }
        return links;
    }

    private static String buildSharedOrgsPageLink(UriInfo uriInfo, String agentId, String filter, Integer limit,
                                                  Boolean recursive, String attributes,
                                                  String after, String before) {

        if (uriInfo == null) {
            return buildRelativeSharedOrgsPageLink(agentId, filter, limit, recursive, attributes, after, before);
        }

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("agents").path(agentId).path("share");
        if (limit != null) {
            builder.queryParam("limit", limit);
        }
        if (filter != null && !filter.isEmpty()) {
            builder.queryParam("filter", filter);
        }
        if (recursive != null) {
            builder.queryParam("recursive", recursive);
        }
        if (attributes != null && !attributes.isEmpty()) {
            builder.queryParam("attributes", attributes);
        }
        if (after != null && !after.isEmpty()) {
            builder.queryParam("after", after);
        } else if (before != null && !before.isEmpty()) {
            builder.queryParam("before", before);
        }

        return builder.build().toString();
    }

    private static String buildRelativeSharedOrgsPageLink(String agentId, String filter, Integer limit,
                                                          Boolean recursive, String attributes,
                                                          String after, String before) {

        String base = "/o/api/server/v1/agents/" + urlEncode(agentId) + "/share";
        List<String> qp = new ArrayList<>();
        if (limit != null) {
            qp.add("limit=" + limit);
        }
        if (filter != null && !filter.isEmpty()) {
            qp.add("filter=" + urlEncode(filter));
        }
        if (recursive != null) {
            qp.add("recursive=" + recursive);
        }
        if (attributes != null && !attributes.isEmpty()) {
            qp.add("attributes=" + urlEncode(attributes));
        }
        if (after != null && !after.isEmpty()) {
            qp.add("after=" + urlEncode(after));
        } else if (before != null && !before.isEmpty()) {
            qp.add("before=" + urlEncode(before));
        }

        return qp.isEmpty() ? base : base + "?" + String.join("&", qp);
    }

    private static UriInfo getCurrentRequestUriInfo() {

        Message message = JAXRSUtils.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return new UriInfoImpl(message);
    }

    private static String encodeCursor(int cursor) {

        return Base64.getEncoder()
                .encodeToString(String.valueOf(cursor).getBytes(StandardCharsets.UTF_8));
    }

    private static String urlEncode(String value) {

        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

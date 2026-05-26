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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionContextTreeNode;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionContextTreeResponse;
import org.wso2.carbon.identity.flow.extension.metadata.FlowExtensionContextTreeMetadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps the engine-side {@link FlowExtensionContextTreeMetadata} DTO to the API-side
 * {@link FlowExtensionContextTreeResponse} model. A boundary-crossing copy keeps the
 * engine module independent of the API server's swagger-generated models.
 */
public final class FlowExtensionContextTreeMapper {

    private FlowExtensionContextTreeMapper() {

    }

    public static FlowExtensionContextTreeResponse toResponse(FlowExtensionContextTreeMetadata metadata) {

        FlowExtensionContextTreeResponse response = new FlowExtensionContextTreeResponse()
                .flowType(metadata.getFlowType())
                .redirectionEnabled(metadata.isRedirectionEnabled())
                .allowReadOnlyClaimsModification(metadata.isAllowReadOnlyClaimsModification());

        response.setContextTree(toApiNodes(metadata.getContextTree()));
        return response;
    }

    private static List<FlowExtensionContextTreeNode> toApiNodes(
            List<org.wso2.carbon.identity.flow.extension.metadata
                    .FlowExtensionContextTreeNode> nodes) {

        if (nodes == null) {
            return null;
        }
        return nodes.stream()
                .map(FlowExtensionContextTreeMapper::toApiNode)
                .collect(Collectors.toList());
    }

    private static FlowExtensionContextTreeNode toApiNode(
            org.wso2.carbon.identity.flow.extension.metadata
                    .FlowExtensionContextTreeNode node) {

        FlowExtensionContextTreeNode out = new FlowExtensionContextTreeNode()
                .key(node.getKey())
                .title(node.getTitle())
                .path(node.getPath())
                .dataType(node.getDataType())
                .nodeType(node.getNodeType() != null
                        ? FlowExtensionContextTreeNode.NodeTypeEnum.fromValue(node.getNodeType()) : null)
                .readOnly(node.isReadOnly())
                .replaceable(node.isReplaceable())
                .dynamicEntryAllowed(node.isDynamicEntryAllowed())
                .dynamicEntryType(node.getDynamicEntryType());

        if (node.getAllowedOperations() != null) {
            out.setAllowedOperations(node.getAllowedOperations().stream()
                    .map(FlowExtensionContextTreeNode.AllowedOperationsEnum::fromValue)
                    .collect(java.util.stream.Collectors.toList()));
        }
        if (node.getChildren() != null) {
            out.setChildren(toApiNodes(node.getChildren()));
        }
        return out;
    }
}

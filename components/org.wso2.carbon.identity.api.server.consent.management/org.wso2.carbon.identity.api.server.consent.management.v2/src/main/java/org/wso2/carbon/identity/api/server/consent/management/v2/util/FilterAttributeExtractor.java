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

package org.wso2.carbon.identity.api.server.consent.management.v2.util;

import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementException;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.model.OperationNode;

import java.util.HashSet;
import java.util.Set;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE;
import static org.wso2.carbon.consent.mgt.core.util.ConsentUtils.handleClientException;

/**
 * Validates filter attributes in SCIM-style filter expressions.
 */
public class FilterAttributeExtractor {

    /**
     * Validates that all filter attributes in the tree are supported.
     *
     * @param rootNode            Filter tree root node.
     * @param supportedAttributes Set of lowercase attribute names that are allowed.
     * @throws ConsentManagementException if any attribute is not supported.
     */
    public void validateFilterAttributes(Node rootNode, Set<String> supportedAttributes)
            throws ConsentManagementException {

        if (rootNode == null) {
            return;
        }
        Set<String> attributeNames = extractAttributeNames(rootNode);
        for (String attributeName : attributeNames) {
            if (!supportedAttributes.contains(attributeName)) {
                throw handleClientException(ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, attributeName);
            }
        }
    }

    /**
     * Recursively extracts all attribute names from a filter tree.
     *
     * @param node Filter tree node.
     * @return Set of attribute names found (lowercase).
     */
    public Set<String> extractAttributeNames(Node node) {

        Set<String> attributes = new HashSet<>();
        extractAttributeNamesRecursive(node, attributes);
        return attributes;
    }

    private void extractAttributeNamesRecursive(Node node, Set<String> attributes) {

        if (node == null) {
            return;
        }
        if (node instanceof ExpressionNode) {
            String attributeValue = ((ExpressionNode) node).getAttributeValue();
            if (attributeValue != null) {
                attributes.add(attributeValue.toLowerCase());
            }
        } else if (node instanceof OperationNode) {
            extractAttributeNamesRecursive(node.getLeftNode(), attributes);
            extractAttributeNamesRecursive(node.getRightNode(), attributes);
        }
    }
}

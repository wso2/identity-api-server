/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.extension.management.v1.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.extension.management.common.utils.ExtensionMgtUtils;
import org.wso2.carbon.identity.api.server.extension.management.v1.model.ExtensionListItem;
import org.wso2.carbon.identity.extension.mgt.model.ExtensionInfo;

import java.util.function.Function;

/**
 * Converts a {@link ExtensionInfo} to a {@link ExtensionListItem}.
 */
public class ExtensionListItemBuilder implements Function<ExtensionInfo, ExtensionListItem> {

    private static final Log log = LogFactory.getLog(ExtensionListItemBuilder.class);

    @Override
    public ExtensionListItem apply(ExtensionInfo extensionInfo) {

        if (log.isDebugEnabled()) {
            log.debug("Building ExtensionListItem for extension: " + (extensionInfo != null ? extensionInfo.getId() 
                    : "null"));
        }
        ExtensionListItem extensionListItem = new ExtensionListItem();
        extensionListItem.setId(extensionInfo.getId());
        extensionListItem.setVersion(extensionInfo.getVersion());
        extensionListItem.setName(extensionInfo.getName());
        extensionListItem.setDescription(extensionInfo.getDescription());
        extensionListItem.setImage(extensionInfo.getImage());
        extensionListItem.setDisplayOrder(extensionInfo.getDisplayOrder());
        extensionListItem.setTags(extensionInfo.getTags());
        extensionListItem.setCategory(extensionInfo.getCategory());
        extensionListItem.setType(extensionInfo.getType());
        extensionListItem.setSelf(ExtensionMgtUtils.getExtensionInfoLocation(extensionInfo.getType(),
                extensionInfo.getId()));
        if (extensionInfo.getCustomAttributes() != null) {
            extensionListItem.setCustomAttributes(extensionInfo.getCustomAttributes());
        }
        return extensionListItem;
    }
}

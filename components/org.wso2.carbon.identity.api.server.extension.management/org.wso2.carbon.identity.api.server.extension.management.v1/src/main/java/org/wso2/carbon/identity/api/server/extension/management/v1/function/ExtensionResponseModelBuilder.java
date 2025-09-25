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
import org.wso2.carbon.identity.api.server.extension.management.v1.model.ExtensionResponseModel;
import org.wso2.carbon.identity.extension.mgt.model.ExtensionInfo;

import java.util.function.Function;

/**
 * Converts a {@link ExtensionInfo} to a {@link ExtensionResponseModel}.
 */
public class ExtensionResponseModelBuilder implements Function<ExtensionInfo, ExtensionResponseModel> {

    private static final Log log = LogFactory.getLog(ExtensionResponseModelBuilder.class);

    @Override
    public ExtensionResponseModel apply(ExtensionInfo extensionInfo) {

        if (log.isDebugEnabled()) {
            log.debug("Building ExtensionResponseModel for extension: " + 
                (extensionInfo != null ? extensionInfo.getId() : "null"));
        }

        if (extensionInfo == null) {
            log.warn("ExtensionInfo is null. Cannot build ExtensionResponseModel.");
            return null;
        }

        ExtensionResponseModel responseModel = new ExtensionResponseModel();
        responseModel.setId(extensionInfo.getId());
        responseModel.setVersion(extensionInfo.getVersion());
        responseModel.setName(extensionInfo.getName());
        responseModel.setDescription(extensionInfo.getDescription());
        responseModel.setImage(extensionInfo.getImage());
        responseModel.setDisplayOrder(extensionInfo.getDisplayOrder());
        responseModel.setTags(extensionInfo.getTags());
        responseModel.setCategory(extensionInfo.getCategory());
        responseModel.setType(extensionInfo.getType());

        if (log.isDebugEnabled()) {
            log.debug("Successfully built ExtensionResponseModel for extension: " + extensionInfo.getId());
        }

        return responseModel;
    }
}

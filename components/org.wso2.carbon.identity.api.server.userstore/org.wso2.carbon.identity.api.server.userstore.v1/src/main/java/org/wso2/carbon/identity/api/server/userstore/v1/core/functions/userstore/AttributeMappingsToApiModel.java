/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.userstore.v1.core.functions.userstore;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreAttributeResponse;
import org.wso2.carbon.identity.user.store.configuration.model.UserStoreAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Convert userstore attribute mappings to API model.
 */
public class AttributeMappingsToApiModel
        implements Function<List<UserStoreAttribute>, List<UserStoreAttributeResponse>> {

    private static final Log LOG = LogFactory.getLog(AttributeMappingsToApiModel.class);

    @Override
    public List<UserStoreAttributeResponse> apply(List<UserStoreAttribute> userStoreAttributeDOs) {

        if (CollectionUtils.isNotEmpty(userStoreAttributeDOs)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Converting " + userStoreAttributeDOs.size() + " user store attributes to API model");
            }

            List<UserStoreAttributeResponse> userStoreAttributes = new ArrayList<>();
            userStoreAttributeDOs.stream().forEach(
                    userStoreAttribute -> {
                        UserStoreAttributeResponse userStoreAttributeResponse = new UserStoreAttributeResponse();
                        userStoreAttributeResponse.mappedAttribute(userStoreAttribute.getMappedAttribute());
                        userStoreAttributeResponse.claimId(userStoreAttribute.getClaimId());
                        userStoreAttributeResponse.claimURI(userStoreAttribute.getClaimUri());
                        userStoreAttributeResponse.displayName(userStoreAttribute.getDisplayName());
                        userStoreAttributes.add(userStoreAttributeResponse);
                    });
            if (LOG.isDebugEnabled()) {
                LOG.debug("Converted " + userStoreAttributes.size() + " user store attributes successfully");
            }
            return userStoreAttributes;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("No user store attributes to convert");
        }
        return Collections.emptyList();
    }
}

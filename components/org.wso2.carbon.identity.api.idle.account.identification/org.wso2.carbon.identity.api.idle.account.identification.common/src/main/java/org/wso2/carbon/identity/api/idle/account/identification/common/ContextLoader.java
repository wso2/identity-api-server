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

package org.wso2.carbon.identity.api.idle.account.identification.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.core.util.IdentityCoreConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;

/**
 * Load information from context.
 */
public class ContextLoader {

    private static final Log LOG = LogFactory.getLog(ContextLoader.class);

    /**
     * Retrieves loaded tenant domain from carbon context.
     *
     * @return tenant domain of the request is being served.
     */
    public static String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(IdentityCoreConstants.TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get()
                    .get(IdentityCoreConstants.TENANT_NAME_FROM_CONTEXT);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieved tenant domain from context: " + tenantDomain);
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No tenant domain found in context, using super tenant domain: " + 
                        tenantDomain);
            }
        }
        return tenantDomain;
    }
}

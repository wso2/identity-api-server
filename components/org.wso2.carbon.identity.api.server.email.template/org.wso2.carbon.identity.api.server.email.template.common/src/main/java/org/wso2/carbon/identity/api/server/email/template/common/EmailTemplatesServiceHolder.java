/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.email.template.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.email.mgt.EmailTemplateManager;

/**
 * Service holder class for email templates.
 */
public class EmailTemplatesServiceHolder {

    private EmailTemplatesServiceHolder() {}

    private static class EmailTemplateManagerHolder {

        private static final Log log = LogFactory.getLog(EmailTemplateManagerHolder.class);
        static final EmailTemplateManager SERVICE = initializeService();

        private static EmailTemplateManager initializeService() {

            EmailTemplateManager service = (EmailTemplateManager) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(EmailTemplateManager.class, null);
            if (service == null) {
                log.warn("EmailTemplateManager is not available in OSGi context.");
            } else {
                log.debug("EmailTemplateManager successfully retrieved from OSGi context.");
            }
            return service;
        }
    }

    /**
     * Get EmailTemplateManager osgi service.
     * @return EmailTemplateManager
     */
    public static EmailTemplateManager getEmailTemplateManager() {

        return EmailTemplateManagerHolder.SERVICE;
    }
}

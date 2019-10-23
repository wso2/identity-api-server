/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 * http://www.apache.org/licenses/LICENSE-2.0                              
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.email.template.common;

import org.wso2.carbon.email.mgt.EmailTemplateManager;

/**
 * Service holder class for email templates.
 */
public class EmailTemplatesServiceHolder {

    private static EmailTemplateManager emailTemplateManager;

    /**
     * Get EmailTemplateManager osgi service.
     * @return EmailTemplateManager
     */
    public static EmailTemplateManager getEmailTemplateManager() {
        return emailTemplateManager;
    }

    public static void setEmailTemplateManager(EmailTemplateManager emailTemplateManager) {
        EmailTemplatesServiceHolder.emailTemplateManager = emailTemplateManager;
    }
}

/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.template.mgt.TemplateMgtConstants;
import org.wso2.carbon.identity.template.mgt.model.Template;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerError;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Converts the Application Template API model into a Template object.
 */
public class ApplicationTemplateApiModelToTemplate implements Function<ApplicationTemplateModel, Template> {

    @Override
    public Template apply(ApplicationTemplateModel applicationTemplate) {

        // Set the additional properties specific to the Application Template as properties map in the template object.
        Map<String, String> propertiesMap = new HashMap<>();
        if (StringUtils.isNotEmpty(applicationTemplate.getAuthenticationProtocol())) {
            propertiesMap.put(ApplicationManagementConstants.TemplateProperties.INBOUND_PROTOCOL,
                    applicationTemplate.getAuthenticationProtocol());
        }
        if (applicationTemplate.getTypes() != null) {
            propertiesMap.put(ApplicationManagementConstants.TemplateProperties.TYPES, String.join(",",
                    applicationTemplate.getTypes()));
        }
        if (applicationTemplate.getCategory() != null) {
            propertiesMap.put(ApplicationManagementConstants.TemplateProperties.CATEGORY, applicationTemplate
                    .getCategory().value());
        }
        if (applicationTemplate.getDisplayOrder() != null) {
            propertiesMap.put(ApplicationManagementConstants.TemplateProperties.DISPLAY_ORDER, Integer
                    .toString(applicationTemplate.getDisplayOrder()));
        }
        if (applicationTemplate.getTemplateGroup() != null) {
            propertiesMap.put(ApplicationManagementConstants.TemplateProperties.TEMPLATE_GROUP,
                    applicationTemplate.getTemplateGroup());
        }

        Template template = new Template();
        template.setTemplateType(TemplateMgtConstants.TemplateType.APPLICATION_TEMPLATE);
        template.setTemplateName(applicationTemplate.getName());
        template.setDescription(applicationTemplate.getDescription());
        template.setImageUrl(applicationTemplate.getImage());
        template.setTenantId(IdentityTenantUtil.getTenantId(getTenantDomainFromContext()));
        template.setPropertiesMap(propertiesMap);
        template.setTemplateScript(createApplicationTemplateScript(applicationTemplate.getApplication()));
        return template;
    }

    private String createApplicationTemplateScript(ApplicationModel application) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(application);
        } catch (JsonProcessingException e) {
            throw buildServerError(
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getCode(),
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getMessage(),
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getDescription(),
                    e);
        }
    }
}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.template.mgt.model.Template;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerError;

/**
 * Converts the Template to ApplicationTemplate model.
 */
public class TemplateToApplicationTemplate implements Function<Template, ApplicationTemplateModel> {

    private static final Log log = LogFactory.getLog(TemplateToApplicationTemplate.class);

    @Override
    public ApplicationTemplateModel apply(Template template) {

        if (log.isDebugEnabled()) {
            log.debug("Converting template to application template: " + 
                (template != null ? template.getTemplateName() : "null"));
        }
        if (template == null) {
            return null;
        }
        ApplicationTemplateModel applicationTemplate = new ApplicationTemplateModel();
        applicationTemplate.setName(template.getTemplateName());
        applicationTemplate.setId(template.getTemplateId());
        applicationTemplate.setDescription(template.getDescription());
        applicationTemplate.setImage(template.getImageUrl());

        if (StringUtils.isNotBlank(template.getTemplateScript())) {
            applicationTemplate.setApplication(createApplicationTemplate(template.getTemplateScript()));
        }

        if (template.getPropertiesMap() != null) {
            template.getPropertiesMap().forEach((key, value) -> {
                if (ApplicationManagementConstants.TemplateProperties.TYPES.equals(key) && StringUtils.isNotBlank
                        (value)) {
                    applicationTemplate.setTypes(Arrays.asList(value.split(",")));
                }
                if (ApplicationManagementConstants.TemplateProperties.DISPLAY_ORDER.equals(key) && StringUtils
                        .isNotBlank(value)) {
                    applicationTemplate.setDisplayOrder(Integer.parseInt(value));
                }
                if (ApplicationManagementConstants.TemplateProperties.CATEGORY.equals(key)) {
                    if (ApplicationTemplateModel.CategoryEnum.VENDOR.value().equals(value)) {
                        applicationTemplate.setCategory(ApplicationTemplateModel.CategoryEnum.VENDOR);
                    } else {
                        applicationTemplate.setCategory(ApplicationTemplateModel.CategoryEnum.DEFAULT);
                    }
                }
                if (ApplicationManagementConstants.TemplateProperties.INBOUND_PROTOCOL.equals(key)) {
                    applicationTemplate.setAuthenticationProtocol(value);
                }
                if (ApplicationManagementConstants.TemplateProperties.TEMPLATE_GROUP.equals(key)) {
                    applicationTemplate.setTemplateGroup(value);
                }
            });
        }
        return applicationTemplate;
    }

    private ApplicationModel createApplicationTemplate(String applicationTemplate) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(applicationTemplate, ApplicationModel.class);
        } catch (IOException e) {
            log.error("Error occurred while parsing application template script", e);
            throw buildServerError(
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getCode(),
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getMessage(),
                    ApplicationManagementConstants.ErrorMessage.ERROR_RESOLVING_APPLICATION_TEMPLATE.getDescription(),
                    e);
        }
    }
}

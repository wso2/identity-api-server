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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesListItem;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.template.mgt.model.Template;

import java.util.Arrays;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants
        .APPLICATION_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants
        .APPLICATION_TEMPLATE_MANAGEMENT_PATH_COMPONENT;

/**
 * Converts the Template to ApplicationTemplateListItem model.
 */
public class TemplateToApplicationTemplateListItem implements Function<Template, ApplicationTemplatesListItem> {

    @Override
    public ApplicationTemplatesListItem apply(Template template) {

        ApplicationTemplatesListItem listItem = new ApplicationTemplatesListItem();
        listItem.setName(template.getTemplateName());
        listItem.setId(template.getTemplateId());
        listItem.setSelf(getApplicationTemplateLocation(template.getTemplateId()));
        listItem.setDescription(template.getDescription());
        listItem.setImage(template.getImageUrl());

        if (template.getPropertiesMap() != null) {
            template.getPropertiesMap().forEach((key, value) -> {
                if (ApplicationManagementConstants.TemplateProperties.TYPES.equals(key) && StringUtils.isNotBlank
                        (value)) {
                    listItem.setTypes(Arrays.asList(value.split(",")));
                }
                if (ApplicationManagementConstants.TemplateProperties.DISPLAY_ORDER.equals(key) && StringUtils
                        .isNotBlank(value)) {
                    listItem.setDisplayOrder(Integer.parseInt(value));
                }
                if (ApplicationManagementConstants.TemplateProperties.CATEGORY.equals(key)) {
                    if (ApplicationTemplatesListItem.CategoryEnum.VENDOR.value().equals(value)) {
                        listItem.setCategory(ApplicationTemplatesListItem.CategoryEnum.VENDOR);
                    } else {
                        listItem.setCategory(ApplicationTemplatesListItem.CategoryEnum.DEFAULT);
                    }
                }
                if (ApplicationManagementConstants.TemplateProperties.INBOUND_PROTOCOL.equals(key)) {
                    listItem.setAuthenticationProtocol(value);
                }
                if (ApplicationManagementConstants.TemplateProperties.TEMPLATE_GROUP.equals(key)) {
                    listItem.setTemplateGroup(value);
                }
            });
        }
        return listItem;
    }

    private String getApplicationTemplateLocation(String templateId) {

        return ContextLoader.buildURIForBody(
                Constants.V1_API_PATH_COMPONENT + APPLICATION_MANAGEMENT_PATH_COMPONENT +
                        APPLICATION_TEMPLATE_MANAGEMENT_PATH_COMPONENT + "/" + templateId).toString();
    }
}

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

package org.wso2.carbon.identity.api.server.extension.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.identity.api.server.extension.management.common.ExtensionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.extension.management.common.utils.ExtensionMgtConstants;
import org.wso2.carbon.identity.api.server.extension.management.common.utils.ExtensionMgtUtils;
import org.wso2.carbon.identity.api.server.extension.management.v1.ExtensionsApiService;
import org.wso2.carbon.identity.api.server.extension.management.v1.function.ExtensionListItemBuilder;
import org.wso2.carbon.identity.api.server.extension.management.v1.function.ExtensionResponseModelBuilder;
import org.wso2.carbon.identity.extension.mgt.exception.ExtensionManagementException;
import org.wso2.carbon.identity.extension.mgt.model.ExtensionInfo;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.extension.management.common.utils.ExtensionMgtUtils.validateExtensionType;


/**
 * Implementation of the extension management service.
 */
public class ExtensionsApiServiceImpl implements ExtensionsApiService {

    private static final Log log = LogFactory.getLog(ExtensionsApiServiceImpl.class);

    /**
     * Get all the extensions.
     *
     * @return List of extensions.
     */
    @Override
    public Response listExtensions() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all extensions");
        }

        List<ExtensionInfo> extensionInfoList = ExtensionManagementServiceHolder.getExtensionManager()
                .getExtensions();

        if (log.isDebugEnabled()) {
            log.debug("Found " + extensionInfoList.size() + " extensions");
        }

        return Response.ok().entity(extensionInfoList.stream().map(new
                ExtensionListItemBuilder()).collect(Collectors.toList())).build();
    }

    /**
     * Get all the extensions of a given type.
     *
     * @param extensionType Type of the extension.
     * @return List of extensions.
     */
    @Override
    public Response listExtensionsByType(String extensionType) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving extensions by type: " + extensionType);
        }

        // TODO: Add pagination support.
        validateExtensionType(extensionType);
        try {
            List<ExtensionInfo> extensionInfoList = ExtensionManagementServiceHolder.getExtensionManager()
                    .getExtensionsByType(extensionType);

            if (log.isDebugEnabled()) {
                log.debug("Found " + extensionInfoList.size() + " extensions for type: " + extensionType);
            }

            return Response.ok().entity(extensionInfoList.stream().map(new
                    ExtensionListItemBuilder()).collect(Collectors.toList())).build();
        } catch (ExtensionManagementException e) {
            log.warn("Failed to retrieve extensions by type: " + extensionType + ". Error: " + e.getMessage());
            throw ExtensionMgtUtils.handleServerException(Response.Status.INTERNAL_SERVER_ERROR,
                    ExtensionMgtConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_EXTENSIONS_BY_TYPE, extensionType);
        }
    }

    /**
     * Get the extension by the extension id.
     *
     * @param extensionType Type of the extension.
     * @param extensionId   Id of the extension.
     * @return Extension.
     */
    @Override
    public Response getExtensionInfoById(String extensionType, String extensionId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving extension info for type: " + extensionType + ", id: " + extensionId);
        }

        validateExtensionType(extensionType);
        try {
            ExtensionInfo extensionInfo = ExtensionManagementServiceHolder.getExtensionManager()
                    .getExtensionByTypeAndId(extensionType, extensionId);
            if (extensionInfo == null) {
                log.warn("Extension not found for type: " + extensionType + ", id: " + extensionId);
                throw ExtensionMgtUtils.handleClientException(Response.Status.NOT_FOUND,
                        ExtensionMgtConstants.ErrorMessage.ERROR_CODE_EXTENSION_NOT_FOUND, extensionId, extensionType);
            }

            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved extension info for type: " + extensionType + ", id: " + 
                    extensionId);
            }

            return Response.ok().entity(new ExtensionResponseModelBuilder().apply(extensionInfo)).build();
        } catch (ExtensionManagementException e) {
            log.warn("Failed to retrieve extension for type: " + extensionType + ", id: " + extensionId + 
                ". Error: " + e.getMessage());
            throw ExtensionMgtUtils.handleServerException(Response.Status.INTERNAL_SERVER_ERROR,
                    ExtensionMgtConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_EXTENSION, extensionId, extensionType);
        }
    }

    /**
     * Get the template of the extension by the extension id.
     *
     * @param extensionType Type of the extension.
     * @param extensionId   Id of the extension.
     * @return Template of the extension.
     */
    @Override
    public Response getTemplateById(String extensionType, String extensionId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving template for extension type: " + extensionType + ", id: " + extensionId);
        }

        validateExtensionType(extensionType);
        try {
            JSONObject template = ExtensionManagementServiceHolder.getExtensionManager()
                    .getExtensionTemplate(extensionType, extensionId);
            if (template == null) {
                log.warn("Template not found for extension type: " + extensionType + ", id: " + extensionId);
                throw ExtensionMgtUtils.handleClientException(Response.Status.NOT_FOUND,
                        ExtensionMgtConstants.ErrorMessage.ERROR_CODE_TEMPLATE_NOT_FOUND, extensionId, extensionType);
            }

            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved template for extension type: " + extensionType + ", id: " + 
                    extensionId);
            }

            return Response.ok().entity(template.toString()).build();
        } catch (ExtensionManagementException e) {
            log.warn("Failed to retrieve template for extension type: " + extensionType + ", id: " + extensionId + 
                ". Error: " + e.getMessage());
            throw ExtensionMgtUtils.handleServerException(Response.Status.INTERNAL_SERVER_ERROR,
                    ExtensionMgtConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_TEMPLATE, extensionId, extensionType);
        }
    }

    /**
     * Get the metadata of the extension by the extension id.
     *
     * @param extensionType Type of the extension.
     * @param extensionId   Id of the extension.
     * @return Metadata of the extension.
     */
    @Override
    public Response getMetadataById(String extensionType, String extensionId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving metadata for extension type: " + extensionType + ", id: " + extensionId);
        }

        validateExtensionType(extensionType);
        try {
            JSONObject metadata = ExtensionManagementServiceHolder.getExtensionManager()
                    .getExtensionMetadata(extensionType, extensionId);
            if (metadata == null) {
                log.warn("Metadata not found for extension type: " + extensionType + ", id: " + extensionId);
                throw ExtensionMgtUtils.handleClientException(Response.Status.NOT_FOUND,
                        ExtensionMgtConstants.ErrorMessage.ERROR_CODE_METADATA_NOT_FOUND, extensionId, extensionType);
            }

            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved metadata for extension type: " + extensionType + ", id: " + 
                    extensionId);
            }

            return Response.ok().entity(metadata.toString()).build();
        } catch (ExtensionManagementException e) {
            log.warn("Failed to retrieve metadata for extension type: " + extensionType + ", id: " + extensionId + 
                ". Error: " + e.getMessage());
            throw ExtensionMgtUtils.handleServerException(Response.Status.INTERNAL_SERVER_ERROR,
                    ExtensionMgtConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_METADATA, extensionId, extensionType);
        }
    }
}

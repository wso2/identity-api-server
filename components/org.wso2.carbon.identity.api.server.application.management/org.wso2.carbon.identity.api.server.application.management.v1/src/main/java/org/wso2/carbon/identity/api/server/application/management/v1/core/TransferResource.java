/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

/**
 * The TransferResource class represents a resource to be transferred,
 * including its name, type, and byte array data.
 */
public class TransferResource {

    private String resourceName;
    private MediaType resourceType;
    private ByteArrayResource resource;

    public TransferResource(String resourceName, ByteArrayResource resource, MediaType resourceType) {

        this.resourceName = resourceName;
        this.resource = resource;
        this.resourceType = resourceType;
    }

    public String getResourceName() {

        return resourceName;
    }

    public void setResourceName(String resourceName) {

        this.resourceName = resourceName;
    }

    public ByteArrayResource getResource() {

        return resource;
    }

    public void setResource(ByteArrayResource resource) {

        this.resource = resource;
    }

    public MediaType getResourceType() {

        return resourceType;
    }

    public void setResourceType(MediaType resourceType) {

        this.resourceType = resourceType;
    }
}

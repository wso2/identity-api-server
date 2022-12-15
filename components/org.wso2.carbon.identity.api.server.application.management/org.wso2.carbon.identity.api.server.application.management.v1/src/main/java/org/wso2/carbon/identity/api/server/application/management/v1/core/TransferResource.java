package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

/**
 * TransferResource
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

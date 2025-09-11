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

package org.wso2.carbon.identity.api.server.keystore.management.v1.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.api.server.keystore.management.v1.KeystoresApiService;
import org.wso2.carbon.identity.api.server.keystore.management.v1.core.KeyStoreService;
import org.wso2.carbon.identity.api.server.keystore.management.v1.factories.KeyStoreServiceFactory;
import org.wso2.carbon.identity.api.server.keystore.management.v1.model.CertificateRequest;

import java.net.URI;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * API service implementation of Keystore management service operations.
 */
public class KeystoresApiServiceImpl implements KeystoresApiService {

    private static final Log LOG = LogFactory.getLog(KeystoresApiServiceImpl.class);
    private final KeyStoreService keyStoreService;

    public KeystoresApiServiceImpl() {

        try {
            this.keyStoreService = KeyStoreServiceFactory.getKeyStoreService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("KeystoresApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating key store management service.", e);
            throw new RuntimeException("Error occurred while initiating key store management service.", e);
        }
    }

    @Override
    public Response deleteCertificate(String alias) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Delete certificate operation not allowed for super tenant. Alias: " + alias);
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting certificate with alias: " + alias);
        }
        keyStoreService.deleteCertificate(alias);
        LOG.info("Certificate deleted successfully. Alias: " + alias);
        return Response.noContent().build();
    }

    @Override
    public Response getCertificate(String alias, Boolean encodeCert) {

        if (encodeCert == null) {
            encodeCert = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving certificate with alias: " + alias + ", encodeCert: " + encodeCert);
        }
        return Response.ok().entity(keyStoreService.getCertificate(alias, encodeCert)).build();
    }

    @Override
    public Response getCertificateAliases(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing certificate aliases with filter: " + filter);
        }
        return Response.ok().entity(keyStoreService.listCertificateAliases(filter)).build();
    }

    @Override
    public Response getClientCertificate(String alias, Boolean encodeCert) {

        if (!StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Get client certificate operation only allowed for super tenant. Alias: " + alias);
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }

        if (encodeCert == null) {
            encodeCert = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving client certificate with alias: " + alias + ", encodeCert: " + encodeCert);
        }
        return Response.ok().entity(keyStoreService.getClientCertificate(alias, encodeCert)).build();
    }

    @Override
    public Response getClientCertificateAliases(String filter) {

        if (!StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Get client certificate aliases operation only allowed for super tenant. Filter: " + filter);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing client certificate aliases with filter: " + filter);
        }
        return Response.ok().entity(keyStoreService.listClientCertificateAliases(filter)).build();
    }

    @Override
    public Response getPublicCertificate(Boolean encodeCert) {

        if (encodeCert == null) {
            encodeCert = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving public certificate, encodeCert: " + encodeCert);
        }
        return Response.ok().entity(keyStoreService.getPublicCertificate(encodeCert)).build();
    }

    @Override
    public Response uploadCertificate(CertificateRequest certificateRequest) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Upload certificate operation not allowed for super tenant. Alias: " + 
                    (certificateRequest != null ? certificateRequest.getAlias() : "null"));
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        if (certificateRequest == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String alias = certificateRequest.getAlias();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Uploading certificate with alias: " + alias);
        }
        URI certResource = keyStoreService.uploadCertificate(certificateRequest.getAlias(),
                certificateRequest.getCertificate());
        LOG.info("Certificate uploaded successfully. Alias: " + alias);
        NewCookie resourceCookie = new NewCookie("Location", certResource.toString());
        return Response.created(certResource).cookie(resourceCookie).build();
    }
}

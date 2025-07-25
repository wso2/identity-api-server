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

        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing KeystoresApiServiceImpl.");
        }
        try {
            this.keyStoreService = KeyStoreServiceFactory.getKeyStoreService();
            LOG.info("KeystoresApiServiceImpl initialized successfully.");
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating key store management service.");
            throw new RuntimeException("Error occurred while initiating key store management service.", e);
        }
    }

    @Override
    public Response deleteCertificate(String alias) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Delete certificate request received for alias: " + alias);
        }
        String tenantDomain = getTenantDomainFromContext();
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Certificate deletion not allowed for super tenant domain: " + tenantDomain);
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        keyStoreService.deleteCertificate(alias);
        LOG.info("Certificate deleted successfully for alias: " + alias + " in tenant: " + tenantDomain);
        return Response.noContent().build();
    }

    @Override
    public Response getCertificate(String alias, Boolean encodeCert) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get certificate request received for alias: " + alias + ", encodeCert: " + encodeCert);
        }
        if (encodeCert == null) {
            encodeCert = false;
        }
        return Response.ok().entity(keyStoreService.getCertificate(alias, encodeCert)).build();
    }

    @Override
    public Response getCertificateAliases(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get certificate aliases request received with filter: " + filter);
        }
        return Response.ok().entity(keyStoreService.listCertificateAliases(filter)).build();
    }

    @Override
    public Response getClientCertificate(String alias, Boolean encodeCert) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get client certificate request received for alias: " + alias + ", encodeCert: " + encodeCert);
        }
        String tenantDomain = getTenantDomainFromContext();
        if (!StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Client certificate access not allowed for non-super tenant: " + tenantDomain);
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }

        if (encodeCert == null) {
            encodeCert = false;
        }
        return Response.ok().entity(keyStoreService.getClientCertificate(alias, encodeCert)).build();
    }

    @Override
    public Response getClientCertificateAliases(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get client certificate aliases request received with filter: " + filter);
        }
        String tenantDomain = getTenantDomainFromContext();
        if (!StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Client certificate aliases access not allowed for non-super tenant: " + tenantDomain);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(keyStoreService.listClientCertificateAliases(filter)).build();
    }

    @Override
    public Response getPublicCertificate(Boolean encodeCert) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get public certificate request received with encodeCert: " + encodeCert);
        }
        if (encodeCert == null) {
            encodeCert = false;
        }
        return Response.ok().entity(keyStoreService.getPublicCertificate(encodeCert)).build();
    }

    @Override
    public Response uploadCertificate(CertificateRequest certificateRequest) {

        String alias = certificateRequest != null ? certificateRequest.getAlias() : null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Upload certificate request received for alias: " + alias);
        }
        String tenantDomain = getTenantDomainFromContext();
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            LOG.warn("Certificate upload not allowed for super tenant domain: " + tenantDomain);
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        URI certResource = keyStoreService.uploadCertificate(certificateRequest.getAlias(),
                certificateRequest.getCertificate());
        LOG.info("Certificate uploaded successfully for alias: " + alias + " in tenant: " + tenantDomain);
        NewCookie resourceCookie = new NewCookie("Location", certResource.toString());
        return Response.created(certResource).cookie(resourceCookie).build();
    }
}

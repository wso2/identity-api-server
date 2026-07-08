/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.verification.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.vp.verification.common.VPVerificationConstants;
import org.wso2.carbon.identity.api.server.vp.verification.common.VPVerificationConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.vp.verification.common.VPVerificationServiceHolder;
import org.wso2.carbon.identity.api.server.vp.verification.v1.Error;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationInitiateRequest;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationInitiateResponse;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.Credential;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.HolderBinding;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.Holder;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.KeyBinding;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.Presentation;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.exception.VPAuthenticatorException;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowSession;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowStatus;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.service.StandaloneVerificationService;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowInitiationResult;
import org.wso2.carbon.identity.openid4vc.presentation.verification.dto.PresentationMetadata;
import org.wso2.carbon.identity.openid4vc.presentation.verification.dto.VerificationResult;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Core business logic for the VP Verification REST API.
 * Delegates to StandaloneVerificationService (obtained via OSGi) and maps to REST response models.
 */
public class ServerVPVerificationService {

    private static final Log LOG = LogFactory.getLog(ServerVPVerificationService.class);

    /**
     * Initiate a new standalone VP verification session.
     *
     * @param request Initiation request containing presentationDefinitionId.
     * @return JAX-RS Response with VerificationInitiateResponse (201) or Error.
     */
    public Response initiateVerification(VerificationInitiateRequest request) {

        if (request == null || StringUtils.isBlank(request.getPresentationDefinitionId())) {
            return buildBadRequestResponse(ErrorMessage.ERROR_CODE_INVALID_REQUEST,
                    "presentationDefinitionId is required.");
        }

        StandaloneVerificationService service = getService();
        if (service == null) {
            return buildNotImplementedResponse();
        }

        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        try {
            VPFlowInitiationResult initiation =
                    service.initiate(request.getPresentationDefinitionId(), tenantDomain);

            VerificationInitiateResponse resp = new VerificationInitiateResponse();
            resp.setTxnId(initiation.getTxnId());
            resp.setWalletUrl(initiation.getWalletUrl());
            resp.setRequestUri(initiation.getRequestUri());
            resp.setExpiresAt(initiation.getExpiresAt());

            URI location = URI.create(
                    VPVerificationConstants.VP_VERIFICATION_STATUS_PATH + "/" + initiation.getTxnId());
            return Response.created(location).entity(resp).build();

        } catch (VPAuthenticatorException e) {
            LOG.error("Failed to initiate VP verification session.", e);
            return buildInternalErrorResponse(ErrorMessage.ERROR_CODE_INTERNAL_ERROR, e.getMessage());
        }
    }

    /**
     * Poll the status of a verification session.
     *
     * @param txnId Transaction ID from initiation.
     * @return JAX-RS Response with VerificationStatusResponse (200) or Error.
     */
    public Response getVerificationStatus(String txnId) {

        if (StringUtils.isBlank(txnId)) {
            return buildBadRequestResponse(ErrorMessage.ERROR_CODE_INVALID_REQUEST,
                    "txnId is required.");
        }

        StandaloneVerificationService service = getService();
        if (service == null) {
            return buildNotImplementedResponse();
        }

        VPFlowSession session = service.getSession(txnId);
        if (session == null) {
            return buildNotFoundResponse(txnId);
        }

        VerificationStatusResponse resp = new VerificationStatusResponse();
        resp.setTxnId(txnId);
        resp.setStatus(session.getStatus() != null ? session.getStatus().name() : VPFlowStatus.FAILED.name());

        if (session.getVerificationResult() != null && session.getStatus() == VPFlowStatus.VERIFIED) {
            resp.setPresentation(buildPresentation(session.getVerificationResult()));
            resp.setErrors(session.getVerificationResult().getErrors());
        } else if (session.getStatus() == VPFlowStatus.FAILED) {
            if (session.getVerificationResult() != null) {
                resp.setErrors(session.getVerificationResult().getErrors());
            }
        }

        return Response.ok(resp).build();
    }

    private Presentation buildPresentation(VerificationResult result) {

        List<PresentationMetadata> metadataList = result.getCredentialMetadataList();
        PresentationMetadata firstMeta = (metadataList != null && !metadataList.isEmpty())
                ? metadataList.get(0) : null;

        Presentation p = new Presentation();
        p.setFormat(firstMeta != null ? firstMeta.getVpFormat() : null);
        p.setSubmittedAt(firstMeta != null
                ? Instant.ofEpochMilli(firstMeta.getPresentationTime()).toString() : null);

        // ── Credentials — one entry per verified credential ───────────────────
        List<Credential> credentials = new ArrayList<>();
        if (metadataList != null) {
            for (PresentationMetadata meta : metadataList) {
                Credential cred = new Credential();
                cred.setType(meta.getCredentialType());
                cred.setIssuer(meta.getIssuerDid());
                cred.setSigningAlgorithm(meta.getAlgorithm());
                if (meta.getIssuedAt() != null) {
                    cred.setIssuedAt(Instant.ofEpochMilli(meta.getIssuedAt()).toString());
                }
                if (meta.getExpiresAt() != null) {
                    cred.setExpiresAt(Instant.ofEpochMilli(meta.getExpiresAt()).toString());
                }
                if (StringUtils.isNotBlank(meta.getHolderBindingMethod())) {
                    HolderBinding hb = new HolderBinding();
                    hb.setMethod(meta.getHolderBindingMethod());
                    hb.setKeyType(meta.getHolderKeyType());
                    hb.setCurve(meta.getHolderKeyCurve());
                    cred.setHolderBinding(hb);
                }
                cred.setClaims(meta.getCredentialClaims());
                credentials.add(cred);
            }
        }
        p.setCredentials(credentials);

        // ── Holder — only emitted when a sub claim was present in the credential ─
        if (firstMeta != null && StringUtils.isNotBlank(firstMeta.getHolderDid())) {
            Holder holder = new Holder();
            holder.setId(firstMeta.getHolderDid());
            p.setHolder(holder);
        }

        // ── Key Binding — use first credential that carried a verified KB-JWT ─
        // All KB-JWTs in a VP share the same nonce and audience, so any one is representative.
        if (metadataList != null) {
            for (PresentationMetadata meta : metadataList) {
                if (meta.isKbJwtVerified()) {
                    KeyBinding kb = new KeyBinding();
                    kb.setVerified(true);
                    if (meta.getKbJwtPresentedAt() != null) {
                        kb.setPresentedAt(Instant.ofEpochMilli(meta.getKbJwtPresentedAt()).toString());
                    }
                    kb.setAudience(meta.getKbJwtAudience());
                    kb.setNonce(meta.getNonce());
                    p.setKeyBinding(kb);
                    break;
                }
            }
        }

        return p;
    }

    private StandaloneVerificationService getService() {
        return VPVerificationServiceHolder.getStandaloneVerificationService();
    }

    private Response buildBadRequestResponse(ErrorMessage errorMsg, String description) {
        Error error = new Error();
        error.setCode(errorMsg.getCode());
        error.setMessage(errorMsg.getMessage());
        error.setDescription(description);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    private Response buildNotFoundResponse(String txnId) {
        Error error = new Error();
        error.setCode(ErrorMessage.ERROR_CODE_SESSION_NOT_FOUND.getCode());
        error.setMessage(ErrorMessage.ERROR_CODE_SESSION_NOT_FOUND.getMessage());
        error.setDescription("No verification session found for txnId: " + txnId);
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    private Response buildInternalErrorResponse(ErrorMessage errorMsg, String description) {
        Error error = new Error();
        error.setCode(errorMsg.getCode());
        error.setMessage(errorMsg.getMessage());
        error.setDescription(description);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

    private Response buildNotImplementedResponse() {
        Error error = new Error();
        error.setCode("OID4VP-60001");
        error.setMessage("OpenID4VP feature is not enabled.");
        error.setDescription(
                "The OpenID4VP feature is disabled. Enable it via [openid4vp] enabled=true in deployment.toml.");
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(error).build();
    }
}

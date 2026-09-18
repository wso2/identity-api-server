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
import org.slf4j.MDC;
import org.wso2.carbon.context.PrivilegedCarbonContext;
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
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.KeyBinding;
import org.wso2.carbon.identity.api.server.vp.verification.v1.VerificationStatusResponse.Presentation;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.exception.VPAuthenticatorErrorCode;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.exception.VPAuthenticatorException;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowInitiationResult;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowSession;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.model.VPFlowStatus;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.service.VPFlowService;
import org.wso2.carbon.identity.openid4vc.presentation.verification.dto.PresentationMetadata;
import org.wso2.carbon.identity.openid4vc.presentation.verification.dto.VerificationResult;
import org.wso2.carbon.identity.openid4vc.template.management.exception.PresentationManagementException;
import org.wso2.carbon.identity.openid4vc.template.management.model.PresentationDefinition;
import org.wso2.carbon.identity.openid4vc.template.management.service.PresentationDefinitionService;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Response;

/**
 * Core business logic for the VP Verification REST API.
 * Delegates to VPFlowService (obtained via OSGi) and maps to REST response models.
 */
public class ServerVPVerificationService {

    private static final Log LOG = LogFactory.getLog(ServerVPVerificationService.class);
    private static final Log AUDIT_LOG = LogFactory.getLog("AUDIT_LOG");

    /**
     * Initiate a new standalone VP verification session.
     *
     * @param request the initiation request containing the human-readable identifier of the presentation definition.
     * @return a response with the wallet deeplink URL and session identifier on success, or an error response.
     */
    public Response initiateVerification(VerificationInitiateRequest request) {

        if (request == null || StringUtils.isBlank(request.getPresentationDefinitionIdentifier())) {
            return buildBadRequestResponse(ErrorMessage.ERROR_CODE_INVALID_REQUEST,
                    "presentationDefinitionIdentifier is required.");
        }

        VPFlowService service = getService();
        if (service == null) {
            return buildNotImplementedResponse();
        }

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        String identifier = request.getPresentationDefinitionIdentifier();

        // Resolve the human-readable identifier to the internal UUID required by VPFlowService.
        String definitionId;
        try {
            PresentationDefinitionService definitionService = getPresentationDefinitionService();
            if (definitionService == null) {
                return buildInternalErrorResponse(ErrorMessage.ERROR_CODE_INTERNAL_ERROR,
                        "PresentationDefinitionService is not available.");
            }
            PresentationDefinition definition =
                    definitionService.getPresentationDefinitionByIdentifier(identifier, tenantId);
            if (definition == null) {
                return buildNotFoundResponse(ErrorMessage.ERROR_CODE_DEFINITION_NOT_FOUND,
                        "No presentation definition found for identifier: " + identifier);
            }
            definitionId = definition.getDefinitionId();
        } catch (PresentationManagementException e) {
            LOG.error("Failed to resolve presentation definition identifier: " + identifier, e);
            return buildInternalErrorResponse(ErrorMessage.ERROR_CODE_INTERNAL_ERROR,
                    "An error occurred while resolving the presentation definition.");
        }

        try {
            VPFlowInitiationResult initiation = service.initiate(definitionId, tenantDomain);

            VerificationInitiateResponse initiationResponse = new VerificationInitiateResponse();
            initiationResponse.setRequestId(initiation.getRequestId());
            initiationResponse.setWalletUrl(initiation.getWalletUrl());
            initiationResponse.setRequestUri(initiation.getRequestUri());
            initiationResponse.setExpiresAt(initiation.getExpiresAt());
            initiationResponse.setRespondedAt(Instant.now().toString());
            initiationResponse.setCorrelationId(resolveCorrelationId());

            AUDIT_LOG.info("Initiator : " + tenantDomain
                    + " | Action : VP-Verification-Initiated"
                    + " | Target : " + initiation.getRequestId()
                    + " | Data : { \"presentationDefinitionIdentifier\": \"" + identifier
                    + "\", \"presentationDefinitionId\": \"" + definitionId
                    + "\", \"tenantDomain\": \"" + tenantDomain
                    + "\", \"correlationId\": \"" + initiationResponse.getCorrelationId() + "\" }"
                    + " | Result : Success");

            URI location = ContextLoader.buildURIForHeader(
                    VPVerificationConstants.CREDENTIAL_VERIFICATIONS_PATH + "/" + initiation.getRequestId());
            return Response.created(location).entity(initiationResponse).build();

        } catch (VPAuthenticatorException e) {
            LOG.error("Failed to initiate VP verification session.", e);
            VPAuthenticatorErrorCode errorCode = e.getErrorCode();
            if (errorCode == VPAuthenticatorErrorCode.FEATURE_DISABLED) {
                return buildNotImplementedResponse();
            }
            if (errorCode != null && errorCode.getCode().startsWith(VPVerificationConstants.CLIENT_ERROR_CODE_PREFIX)) {
                return buildBadRequestResponse(ErrorMessage.ERROR_CODE_INVALID_REQUEST,
                        errorCode.getDescription());
            }
            return buildInternalErrorResponse(ErrorMessage.ERROR_CODE_INTERNAL_ERROR,
                    "An error occurred while initiating the verification session.");
        }
    }

    /**
     * Poll the status of a verification session.
     *
     * @param requestId the session identifier returned by the initiation call.
     * @return a response with the current session status and, when verified, the full presentation details.
     */
    public Response getVerificationStatus(String requestId) {

        if (StringUtils.isBlank(requestId)) {
            return buildBadRequestResponse(ErrorMessage.ERROR_CODE_INVALID_REQUEST,
                    "requestId is required.");
        }

        VPFlowService service = getService();
        if (service == null) {
            return buildNotImplementedResponse();
        }

        VPFlowSession session;
        try {
            session = service.getSession(requestId);
        } catch (VPAuthenticatorException e) {
            LOG.error("Failed to retrieve VP verification session: " + requestId, e);
            return buildInternalErrorResponse(ErrorMessage.ERROR_CODE_INTERNAL_ERROR,
                    "An error occurred while retrieving the verification session.");
        }
        if (session == null) {
            return buildNotFoundResponse(requestId);
        }

        VPFlowStatus status = session.getStatus() != null ? session.getStatus() : VPFlowStatus.FAILED;

        VerificationStatusResponse statusResponse = new VerificationStatusResponse();
        statusResponse.setRequestId(requestId);
        statusResponse.setStatus(status.name());
        statusResponse.setRespondedAt(Instant.now().toString());
        statusResponse.setCorrelationId(resolveCorrelationId());

        if (status == VPFlowStatus.VERIFIED) {
            if (session.getVerificationResult() != null) {
                statusResponse.setPresentation(buildPresentation(session.getVerificationResult()));
            }
            // Remove session immediately after returning the verified presentation — the caller
            // is expected to persist the claims themselves. Keeps PII off the cache.
            service.removeSession(requestId);
            Presentation p = statusResponse.getPresentation();
            String submittedAt = (p != null) ? p.getSubmittedAt() : null;
            String credentialType = (p != null && p.getCredentials() != null && !p.getCredentials().isEmpty())
                    ? p.getCredentials().get(0).getType() : null;
            String issuer = (p != null && p.getCredentials() != null && !p.getCredentials().isEmpty())
                    ? p.getCredentials().get(0).getIssuer() : null;
            AUDIT_LOG.info("Initiator : " + ContextLoader.getTenantDomainFromContext()
                    + " | Action : VP-Verified"
                    + " | Target : " + requestId
                    + " | Data : { \"requestId\": \"" + requestId
                    + "\", \"credentialType\": \"" + credentialType
                    + "\", \"issuer\": \"" + issuer
                    + "\", \"submittedAt\": \"" + submittedAt
                    + "\", \"correlationId\": \"" + statusResponse.getCorrelationId() + "\" }"
                    + " | Result : Success");
        } else if (status == VPFlowStatus.FAILED) {
            // WalletSubmissionServlet sets failureReason on the session, not verificationResult,
            // for all failure paths (wallet error response and verification failure alike).
            String reason = session.getFailureReason();
            if (StringUtils.isNotBlank(reason)) {
                List<String> errors = new ArrayList<>();
                errors.add(reason);
                statusResponse.setErrors(errors);
            }
            service.removeSession(requestId);
            // reason is wallet-controlled and may contain user identifiers — kept out of the audit log.
            AUDIT_LOG.info("Initiator : " + ContextLoader.getTenantDomainFromContext()
                    + " | Action : VP-Verification-Failed"
                    + " | Target : " + requestId
                    + " | Data : { \"requestId\": \"" + requestId
                    + "\", \"correlationId\": \"" + statusResponse.getCorrelationId() + "\" }"
                    + " | Result : Failure");
        }

        return Response.ok(statusResponse).build();
    }

    private Presentation buildPresentation(VerificationResult result) {

        List<PresentationMetadata> metadataList = result.getCredentialMetadataList();
        PresentationMetadata firstMeta = (metadataList != null && !metadataList.isEmpty())
                ? metadataList.get(0) : null;

        Presentation presentation = new Presentation();
        presentation.setFormat(firstMeta != null ? firstMeta.getVpFormat() : null);
        presentation.setSubmittedAt(firstMeta != null
                ? Instant.ofEpochMilli(firstMeta.getPresentationTime()).toString() : null);

        // ── Credentials — one entry per verified credential ───────────────────
        List<Credential> credentials = new ArrayList<>();
        if (metadataList != null) {
            for (PresentationMetadata meta : metadataList) {
                Credential credential = new Credential();
                credential.setType(meta.getCredentialType());
                credential.setIssuer(meta.getIssuer());
                credential.setSigningAlgorithm(meta.getAlgorithm());
                if (meta.getIssuedAt() != null) {
                    credential.setIssuedAt(Instant.ofEpochMilli(meta.getIssuedAt()).toString());
                }
                if (meta.getExpiresAt() != null) {
                    credential.setExpiresAt(Instant.ofEpochMilli(meta.getExpiresAt()).toString());
                }
                if (StringUtils.isNotBlank(meta.getHolderBindingMethod())) {
                    HolderBinding holderBinding = new HolderBinding();
                    holderBinding.setMethod(meta.getHolderBindingMethod());
                    holderBinding.setKeyType(meta.getHolderKeyType());
                    holderBinding.setCurve(meta.getHolderKeyCurve());
                    credential.setHolderBinding(holderBinding);
                }
                credential.setClaims(meta.getCredentialClaims());
                credentials.add(credential);
            }
        }
        presentation.setCredentials(credentials);

        // ── Key Binding — use first credential that carried a verified KB-JWT ─
        // All KB-JWTs in a VP share the same nonce and audience, so any one is representative.
        if (metadataList != null) {
            for (PresentationMetadata meta : metadataList) {
                if (meta.isKbJwtVerified()) {
                    KeyBinding keyBinding = new KeyBinding();
                    keyBinding.setVerified(true);
                    if (meta.getKbJwtPresentedAt() != null) {
                        keyBinding.setPresentedAt(Instant.ofEpochMilli(meta.getKbJwtPresentedAt()).toString());
                    }
                    keyBinding.setAudience(meta.getKbJwtAudience());
                    keyBinding.setNonce(meta.getNonce());
                    presentation.setKeyBinding(keyBinding);
                    break;
                }
            }
        }

        return presentation;
    }

    /**
     * Returns the correlation ID set by WSO2 IS's CorrelationLogInterceptor in MDC,
     * or generates a fresh UUID when the header was absent from the request.
     */
    private String resolveCorrelationId() {

        String correlationId = MDC.get("Correlation-ID");
        return StringUtils.isNotBlank(correlationId) ? correlationId : UUID.randomUUID().toString();
    }

    private VPFlowService getService() {

        return VPVerificationServiceHolder.getVPFlowService();
    }

    private PresentationDefinitionService getPresentationDefinitionService() {

        return VPVerificationServiceHolder.getPresentationDefinitionService();
    }

    private Response buildBadRequestResponse(ErrorMessage errorMsg, String description) {

        Error error = new Error();
        error.setCode(errorMsg.getCode());
        error.setMessage(errorMsg.getMessage());
        error.setDescription(description);
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    private Response buildNotFoundResponse(String requestId) {

        Error error = new Error();
        error.setCode(ErrorMessage.ERROR_CODE_SESSION_NOT_FOUND.getCode());
        error.setMessage(ErrorMessage.ERROR_CODE_SESSION_NOT_FOUND.getMessage());
        error.setDescription("No verification session found for requestId: " + requestId);
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    private Response buildNotFoundResponse(ErrorMessage errorMsg, String description) {

        Error error = new Error();
        error.setCode(errorMsg.getCode());
        error.setMessage(errorMsg.getMessage());
        error.setDescription(description);
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
        error.setCode(ErrorMessage.ERROR_CODE_FEATURE_DISABLED.getCode());
        error.setMessage(ErrorMessage.ERROR_CODE_FEATURE_DISABLED.getMessage());
        error.setDescription(
                "The OpenID4VP feature is disabled. Enable it via [openid4vp] enabled=true in deployment.toml.");
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(error).build();
    }
}

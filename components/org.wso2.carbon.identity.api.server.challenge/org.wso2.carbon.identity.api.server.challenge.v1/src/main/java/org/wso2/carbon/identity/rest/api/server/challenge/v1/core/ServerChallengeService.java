/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.challenge.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryServerException;
import org.wso2.carbon.identity.recovery.model.ChallengeQuestion;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.core.functions.ChallengeQuestionToExternal;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.common.Constants.ErrorMessages.ERROR_CHALLENGE_SET_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.common.Constants.ErrorMessages.ERROR_CODE_ERROR_OPERATION_NOT_SUPPORTED;
import static org.wso2.carbon.identity.api.server.common.Constants.ErrorPrefix.CHALLENGE_QUESTION_PREFIX;
import static org.wso2.carbon.identity.api.server.common.Constants.OPERATION_ADD;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.LOCALE_EN_US;

public class ServerChallengeService {
    private static final Log log = LogFactory.getLog(ServerChallengeService.class);
//    private static ChallengeQuestionManager questionManager = ChallengeQuestionManager.getInstance();
    private static ChallengeQuestionManager questionManager = (ChallengeQuestionManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
        .getOSGiService(ChallengeQuestionManager.class, null);
    public static final String WSO2_CLAIM_DIALECT = "http://wso2.org/claims/";

    public String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(Constants.TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(Constants.TENANT_NAME_FROM_CONTEXT);
        }
        return tenantDomain;
    }

    public List<ChallengeSetDTO> getChallenges(String locale, Integer offset, Integer limit) {

        try {
            if (StringUtils.isEmpty(locale)) {

                return buildChallengesDTO(questionManager.getAllChallengeQuestions(getTenantDomainFromContext()),
                        offset, limit);
            } else {
                return buildChallengesDTO(questionManager.getAllChallengeQuestions(getTenantDomainFromContext(), locale),
                        offset, limit);
            }
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_RETRIVING_CHALLENGES);
            return null;
        }
    }

    public ChallengeSetDTO getChallengeSet(String challengeSetId, String locale, Integer offset, Integer
            limit) {

        try {
            if (!isChallengeSetExists(challengeSetId, getTenantDomainFromContext())) {
                handleError(Response.Status.NOT_FOUND, ERROR_CHALLENGE_SET_NOT_EXISTS);
            }
            if (StringUtils.isEmpty(locale)) {
                return buildChallengeDTO(questionManager.getAllChallengeQuestions(getTenantDomainFromContext()),
                        challengeSetId,
                        offset, limit);
            } else {
                return buildChallengeDTO(questionManager.getAllChallengeQuestions(getTenantDomainFromContext(), locale),
                        challengeSetId, offset, limit);
            }
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_RETRIVING_CHALLENGE);
            return null;
        }

    }

    public boolean deleteQuestion(String challengeSetId, String questionId, String locale) {

        if (StringUtils.isEmpty(locale)) {
            locale = StringUtils.EMPTY;
        }
        try {
            if (!isChallengeSetExists(challengeSetId, getTenantDomainFromContext())) {
                handleError(Response.Status.NOT_FOUND, ERROR_CHALLENGE_SET_NOT_EXISTS);
            }

            ChallengeQuestion[] toDelete = {new ChallengeQuestion(challengeSetId, questionId, StringUtils.EMPTY, locale)};
            questionManager.deleteChallengeQuestions(toDelete, getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_DELETING_CHALLENGE);
        }
        return true;
    }

    public boolean deleteQuestionSet(String challengeSetId, String locale) {

        if (StringUtils.isEmpty(locale)) {
            locale = StringUtils.EMPTY;
        }
        try {
            if (!isChallengeSetExists(challengeSetId, getTenantDomainFromContext())) {
                handleError(Response.Status.NOT_FOUND, ERROR_CHALLENGE_SET_NOT_EXISTS);
            }
            questionManager.deleteChallengeQuestionSet(challengeSetId, locale, getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_DELETING_CHALLENGES);
        }
        return true;
    }

    public boolean addChallengeSets(List<ChallengeSetDTO> challengeSets) {

        ChallengeQuestion[] toAdd = buildChallengeQuestionSets(challengeSets);

        try {
            questionManager.addChallengeQuestions(toAdd, getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_ADDING_CHALLENGES);

        }
        return true;
    }

    public boolean updateChallengeSets(String challengeSetId, List<ChallengeQuestionDTO> challenges) {

        if (!isChallengeSetExists(challengeSetId, getTenantDomainFromContext())) {
            handleError(Response.Status.NOT_FOUND, ERROR_CHALLENGE_SET_NOT_EXISTS);
        }
        deleteQuestionSet(challengeSetId, null);

        List<ChallengeQuestion> questions = buildChallengeQuestions(challenges, challengeSetId);
        ChallengeQuestion[] toPut = questions.toArray(new ChallengeQuestion[questions.size()]);
        try {
            questionManager.addChallengeQuestions(toPut, getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, Constants.ErrorMessages.ERROR_CODE_ERROR_UPDATING_CHALLENGE_SET);
        }
        return true;
    }

    public boolean patchChallengeSet(String challengeSetId, ChallengeQuestionPatchDTO
            challengeQuestionPatchDTO) {

        if (!isChallengeSetExists(challengeSetId, getTenantDomainFromContext())) {
            handleError(Response.Status.NOT_FOUND, ERROR_CHALLENGE_SET_NOT_EXISTS);
        }
        if (OPERATION_ADD.equalsIgnoreCase(challengeQuestionPatchDTO.getOperation())) {
            List<ChallengeQuestionDTO> challenges = new ArrayList<>();
            ChallengeQuestionDTO challengeQuestion = challengeQuestionPatchDTO.getChallengeQuestion();
            challenges.add(challengeQuestion);
            List<ChallengeQuestion> questions = buildChallengeQuestions(challenges, challengeSetId);

            ChallengeQuestion[] toPatch = questions.toArray(new ChallengeQuestion[questions.size()]);

            try {
                questionManager.addChallengeQuestions(toPatch, getTenantDomainFromContext());
            } catch (IdentityRecoveryException e) {
                handleIdentityRecoveryException(e, Constants.ErrorMessages
                        .ERROR_CODE_ERROR_ADDING_CHALLENGE_QUESTION_TO_A_SET);
            }
            return true;
        } else {
            handleError(Response.Status.NOT_IMPLEMENTED, ERROR_CODE_ERROR_OPERATION_NOT_SUPPORTED);
        }
        return false;
    }

    private ChallengeQuestion[] buildChallengeQuestionSets(List<ChallengeSetDTO> challengeSets) {
        List<ChallengeQuestion> questions = new ArrayList<>();
        for (ChallengeSetDTO challengeSet : challengeSets) {
            String setId = challengeSet.getQuestionSetId();
            questions = buildChallengeQuestions(challengeSet.getQuestions(), setId);
        }
        return questions.toArray(new ChallengeQuestion[questions.size()]);
    }

    private List<ChallengeQuestion> buildChallengeQuestions(List<ChallengeQuestionDTO> challengeSet, String setId) {
        List<ChallengeQuestion> questions = new ArrayList<>();
        for (ChallengeQuestionDTO q : challengeSet) {
            if (StringUtils.isBlank(q.getLocale())) {
                q.setLocale(LOCALE_EN_US);
            }
            questions.add(createChallenceQuestion(setId, q));
        }
        return questions;
    }

    private ChallengeQuestion createChallenceQuestion(String setId, ChallengeQuestionDTO q) {
        return new ChallengeQuestion(WSO2_CLAIM_DIALECT + setId, q.getQuestionId(), q.getQuestion(), q
                .getLocale());
    }

    private List<ChallengeSetDTO> buildChallengesDTO(List<ChallengeQuestion> challengeQuestions, Integer offset,
                                                     Integer limit) {

        Map<String, List<ChallengeQuestion>> challengeSets = groupChallenges(challengeQuestions);
        return challengeSets.entrySet().stream().map((e) ->
                getChallengeSetDTO(e.getKey(), e.getValue())
        ).collect(Collectors.toList());
    }

    private ChallengeSetDTO getChallengeSetDTO(String questionSetId, List<ChallengeQuestion> questions) {
        ChallengeSetDTO challenge = new ChallengeSetDTO();
        challenge.setQuestionSetId(questionSetId);
        List<ChallengeQuestionDTO> questionDTOs = questions.stream().map(new ChallengeQuestionToExternal()).collect(
                Collectors.toList());
        challenge.setQuestions(questionDTOs);
        return challenge;
    }

    private ChallengeSetDTO buildChallengeDTO(List<ChallengeQuestion> challengeQuestions, String
            challengeSetId, Integer offset, Integer limit) {

        List<ChallengeQuestion> challengeSets = filterChallengesBySetId(challengeQuestions, challengeSetId);
        return getChallengeSetDTO(challengeSetId, challengeSets);
    }

    private Map<String, List<ChallengeQuestion>> groupChallenges(List<ChallengeQuestion> challengeQuestions) {
        return challengeQuestions.stream()
                .collect(groupingBy(question -> question.getQuestionSetId().split(WSO2_CLAIM_DIALECT)[1]));
    }

    private List<ChallengeQuestion> filterChallengesBySetId(List<ChallengeQuestion> challengeQuestions, String setId) {
        return challengeQuestions.stream()
                .filter(question -> question.getQuestionSetId().split(WSO2_CLAIM_DIALECT)[1].equals(setId))
                .collect(Collectors.toList());
    }

    private boolean isChallengeSetExists(String setID, String tenantDomain) {

        try {
            List<String> existingChallenges = questionManager.getAllChallengeQuestionSetsURIs(tenantDomain);
            if (existingChallenges.contains(WSO2_CLAIM_DIALECT.concat(setID))) {
                return true;
            }
        } catch (IdentityRecoveryServerException e) {
            log.error("Unable to retrieve existing challenge sets.", e);
        }
        return false;
    }

    private void handleIdentityRecoveryException(IdentityRecoveryException e, Constants.ErrorMessages errorEnum) {
        ErrorResponse errorResponse = new ErrorResponse.Builder().withError(errorEnum).build(log, e, errorEnum
                .getDescription());

        Response.Status status;

        if (e instanceof IdentityRecoveryClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : CHALLENGE_QUESTION_PREFIX.getPrefix()
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        throw new APIError(status, errorResponse);
    }

    private void handleError(Response.Status status, Constants.ErrorMessages error) {
        throw new APIError(status, new ErrorResponse.Builder().withError(error).build());
    }
}

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
import org.wso2.carbon.identity.api.server.challenge.common.ChallengeConstant;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryServerException;
import org.wso2.carbon.identity.recovery.model.ChallengeQuestion;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.core.functions.ChallengeQuestionToExternal;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.challenge.common.ChallengeQuestionDataHolder.getChallengeQuestionManager;
import static java.util.stream.Collectors.groupingBy;

/**
 * Call internal osgi services to perform server challenge related operations
 */
public class ServerChallengeService {

    private static final Log log = LogFactory.getLog(ServerChallengeService.class);
    private static final String WSO2_CLAIM_DIALECT = "http://wso2.org/claims/";

    /**
     * Get all challenges of the loaded tenant.
     *
     * @param locale challenge question locale
     * @param offset offset to start listing the challenge questions
     * @param limit  number of challenge questions to list
     * @return list of challenge questions of the given locale
     */
    public List<ChallengeSetDTO> getChallenges(String locale, Integer offset, Integer limit) {

        try {
            if (StringUtils.isEmpty(locale)) {

                return buildChallengesDTO(getChallengeQuestionManager()
                        .getAllChallengeQuestions(ContextLoader.getTenantDomainFromContext()), offset, limit);
            } else {
                return buildChallengesDTO(getChallengeQuestionManager()
                        .getAllChallengeQuestions(ContextLoader.getTenantDomainFromContext(), locale), offset, limit);
            }
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_RETRIVING_CHALLENGES);
        }
    }

    /**
     * Get an specific challenge set of the loaded tenant.
     *
     * @param challengeSetId challenge question set id
     * @param locale         challenge question locale
     * @param offset
     * @param limit
     * @return
     */
    public ChallengeSetDTO getChallengeSet(String challengeSetId, String locale, Integer offset, Integer limit) {

        try {
            if (!isChallengeSetExists(challengeSetId, ContextLoader.getTenantDomainFromContext())) {
                throw handleError(Response.Status.NOT_FOUND,
                        ChallengeConstant.ErrorMessage.ERROR_CHALLENGE_SET_NOT_EXISTS);
            }
            if (StringUtils.isEmpty(locale)) {
                return buildChallengeDTO(getChallengeQuestionManager()
                                .getAllChallengeQuestions(ContextLoader.getTenantDomainFromContext()), challengeSetId
                        , offset,
                        limit);
            } else {
                return buildChallengeDTO(getChallengeQuestionManager()
                                .getAllChallengeQuestions(ContextLoader.getTenantDomainFromContext(), locale),
                        challengeSetId,
                        offset, limit);
            }
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_RETRIVING_CHALLENGE);
        }

    }

    /**
     * Delete specific challenge question of a given set.
     *
     * @param challengeSetId challenge question set id
     * @param questionId     challenge question to delete
     * @param locale
     * @return
     */
    public boolean deleteQuestion(String challengeSetId, String questionId, String locale) {

        if (StringUtils.isEmpty(locale)) {
            locale = StringUtils.EMPTY;
        }
        try {
            if (isChallengeSetExists(challengeSetId, ContextLoader.getTenantDomainFromContext())) {
                ChallengeQuestion[] toDelete = {
                        new ChallengeQuestion(challengeSetId, questionId, StringUtils.EMPTY, locale)
                };
                getChallengeQuestionManager()
                        .deleteChallengeQuestions(toDelete, ContextLoader.getTenantDomainFromContext());
            }

        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGE);
        }
        return true;
    }

    /**
     * Delete a challenge set
     *
     * @param challengeSetId challenge question set id to delete
     * @param locale
     * @return
     */
    public boolean deleteQuestionSet(String challengeSetId, String locale) {

        if (StringUtils.isEmpty(locale)) {
            locale = StringUtils.EMPTY;
        }
        try {
            if (isChallengeSetExists(challengeSetId, ContextLoader.getTenantDomainFromContext())) {
                getChallengeQuestionManager()
                        .deleteChallengeQuestionSet(challengeSetId, locale, ContextLoader.getTenantDomainFromContext());
            }
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGES);
        }
        return true;
    }

    /**
     * Add a new challenge set
     *
     * @param challengeSets challenge question sets to add
     * @return
     */
    public boolean addChallengeSets(List<ChallengeSetDTO> challengeSets) {

        ChallengeQuestion[] toAdd = buildChallengeQuestionSets(challengeSets);

        try {
            getChallengeQuestionManager().addChallengeQuestions(toAdd, ContextLoader.getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e, ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_ADDING_CHALLENGES);

        }
        return true;
    }

    /**
     * Update an existing challenge set with new questions
     *
     * @param challengeSetId
     * @param challenges
     * @return
     */
    public boolean updateChallengeSets(String challengeSetId, List<ChallengeQuestionDTO> challenges) {

        if (!isChallengeSetExists(challengeSetId, ContextLoader.getTenantDomainFromContext())) {
            throw handleError(Response.Status.NOT_FOUND, ChallengeConstant.ErrorMessage.ERROR_CHALLENGE_SET_NOT_EXISTS);
        }
        deleteQuestionSet(challengeSetId, null);

        List<ChallengeQuestion> questions = buildChallengeQuestions(challenges, challengeSetId);
        ChallengeQuestion[] toPut = questions.toArray(new ChallengeQuestion[0]);
        try {
            getChallengeQuestionManager().addChallengeQuestions(toPut, ContextLoader.getTenantDomainFromContext());
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CHALLENGE_SET);
        }
        return true;
    }

    /**
     * Update a specific challenge questions of an existing set
     *
     * @param challengeSetId
     * @param challengeQuestionPatchDTO
     * @return
     */
    public boolean patchChallengeSet(String challengeSetId, ChallengeQuestionPatchDTO challengeQuestionPatchDTO) {

        if (!isChallengeSetExists(challengeSetId, ContextLoader.getTenantDomainFromContext())) {
            throw handleError(Response.Status.NOT_FOUND, ChallengeConstant.ErrorMessage.ERROR_CHALLENGE_SET_NOT_EXISTS);
        }
        if (Constants.OPERATION_ADD.equalsIgnoreCase(challengeQuestionPatchDTO.getOperation())) {
            List<ChallengeQuestionDTO> challenges = new ArrayList<>();
            ChallengeQuestionDTO challengeQuestion = challengeQuestionPatchDTO.getChallengeQuestion();
            challenges.add(challengeQuestion);
            List<ChallengeQuestion> questions = buildChallengeQuestions(challenges, challengeSetId);

            ChallengeQuestion[] toPatch = questions.toArray(new ChallengeQuestion[0]);

            try {
                getChallengeQuestionManager()
                        .addChallengeQuestions(toPatch, ContextLoader.getTenantDomainFromContext());
            } catch (IdentityRecoveryException e) {
                throw handleIdentityRecoveryException(e,
                        ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_ADDING_CHALLENGE_QUESTION_TO_A_SET);
            }
        } else {
            throw handleError(Response.Status.NOT_IMPLEMENTED,
                    ChallengeConstant.ErrorMessage.ERROR_CODE_ERROR_OPERATION_NOT_SUPPORTED);
        }
        return true;
    }

    private ChallengeQuestion[] buildChallengeQuestionSets(List<ChallengeSetDTO> challengeSets) {

        List<ChallengeQuestion> questions = new ArrayList<>();
        for (ChallengeSetDTO challengeSet : challengeSets) {
            String setId = challengeSet.getQuestionSetId();
            questions = buildChallengeQuestions(challengeSet.getQuestions(), setId);
        }
        return questions.toArray(new ChallengeQuestion[0]);
    }

    private List<ChallengeQuestion> buildChallengeQuestions(List<ChallengeQuestionDTO> challengeSet, String setId) {

        List<ChallengeQuestion> questions = new ArrayList<>();
        for (ChallengeQuestionDTO q : challengeSet) {
            if (StringUtils.isBlank(q.getLocale())) {
                q.setLocale(IdentityRecoveryConstants.LOCALE_EN_US);
            }
            questions.add(createChallengeQuestion(setId, q));
        }
        return questions;
    }

    private ChallengeQuestion createChallengeQuestion(String setId, ChallengeQuestionDTO q) {

        return new ChallengeQuestion(WSO2_CLAIM_DIALECT + setId, q.getQuestionId(), q.getQuestion(), q.getLocale());
    }

    private List<ChallengeSetDTO> buildChallengesDTO(List<ChallengeQuestion> challengeQuestions, Integer offset,
                                                     Integer limit) {

        Map<String, List<ChallengeQuestion>> challengeSets = groupChallenges(challengeQuestions);
        return challengeSets.entrySet().stream().map((e) -> getChallengeSetDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private ChallengeSetDTO getChallengeSetDTO(String questionSetId, List<ChallengeQuestion> questions) {

        ChallengeSetDTO challenge = new ChallengeSetDTO();
        challenge.setQuestionSetId(questionSetId);
        List<ChallengeQuestionDTO> questionDTOs = questions.stream().map(new ChallengeQuestionToExternal())
                .collect(Collectors.toList());
        challenge.setQuestions(questionDTOs);
        return challenge;
    }

    private ChallengeSetDTO buildChallengeDTO(List<ChallengeQuestion> challengeQuestions, String challengeSetId,
                                              Integer offset, Integer limit) {

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
            List<String> existingChallenges = getChallengeQuestionManager()
                    .getAllChallengeQuestionSetsURIs(tenantDomain);
            if (existingChallenges.contains(WSO2_CLAIM_DIALECT.concat(setID))) {
                return true;
            }
        } catch (IdentityRecoveryServerException e) {
            log.error("Unable to retrieve existing challenge sets.", e);
        }
        return false;
    }

    /**
     * Handle IdentityRecoveryException, extract error code, error description and status code to be sent in the
     * response
     *
     * @param e
     * @param errorEnum
     * @return
     */
    private APIError handleIdentityRecoveryException(IdentityRecoveryException e,
                                                     ChallengeConstant.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof IdentityRecoveryClientException) {
            errorResponse = getErrorBuilder(errorEnum).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(Constants.ERROR_CODE_DELIMITER) ? errorCode :
                        ChallengeConstant.CHALLENGE_QUESTION_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleError(Response.Status status, ChallengeConstant.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    private ErrorResponse.Builder getErrorBuilder(ChallengeConstant.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }
}

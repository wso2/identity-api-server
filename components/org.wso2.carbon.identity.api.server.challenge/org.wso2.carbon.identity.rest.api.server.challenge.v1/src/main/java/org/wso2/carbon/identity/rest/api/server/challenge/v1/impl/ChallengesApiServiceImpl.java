package org.wso2.carbon.identity.rest.api.server.challenge.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.ChallengesApiService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.core.ServerChallengeService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;

import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.challenge.common.ChallengeConstant.CHALLENGES_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.challenge.common.ChallengeConstant.CHALLENGE_QUESTION_SET_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;

/**
 * API service implementation of server challenge operations.
 */
public class ChallengesApiServiceImpl extends ChallengesApiService {

    @Autowired
    private ServerChallengeService challengeService;

    @Override
    public Response addChallengeQuestionToASet(String challengeSetId, ChallengeQuestionPatchDTO challengeQuestion) {

        challengeService.patchChallengeSet(challengeSetId, challengeQuestion);
        String challengeQuestionPath = String
                .format(V1_API_PATH_COMPONENT + CHALLENGE_QUESTION_SET_PATH_COMPONENT, challengeSetId);
        return Response.created(buildURIForHeader(challengeQuestionPath)).build();
    }

    @Override
    public Response addChallenges(List<ChallengeSetDTO> challengeSet) {

        challengeService.addChallengeSets(challengeSet);
        return Response.created(buildURIForHeader(V1_API_PATH_COMPONENT + CHALLENGES_PATH_COMPONENT)).build();
    }

    @Override
    public Response deleteChallengeQuestion(String questionId, String challengeSetId, String locale) {

        challengeService.deleteQuestion(challengeSetId, questionId, locale);
        return Response.noContent().build();
    }

    @Override
    public Response deleteChallengeQuestionSet(String challengeSetId, String locale) {

        challengeService.deleteQuestionSet(challengeSetId, locale);
        return Response.noContent().build();
    }

    @Override
    public Response getChallengeQuestionSet(String challengeSetId, String locale, Integer offset, Integer limit) {

        return Response.ok().entity(challengeService.getChallengeSet(challengeSetId, locale, limit, offset)).build();
    }

    @Override
    public Response searchChallenges(String locale, Integer offset, Integer limit) {

        return Response.ok().entity(challengeService.getChallenges(locale, limit, offset)).build();
    }

    @Override
    public Response updateChallengeQuestionSet(String challengeSetId, List<ChallengeQuestionDTO> challengeSet) {

        challengeService.updateChallengeSets(challengeSetId, challengeSet);
        return Response.ok().build();
    }
}

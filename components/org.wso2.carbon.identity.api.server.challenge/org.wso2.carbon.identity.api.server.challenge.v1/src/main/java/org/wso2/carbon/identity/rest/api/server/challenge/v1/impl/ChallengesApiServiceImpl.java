package org.wso2.carbon.identity.rest.api.server.challenge.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.ChallengesApiService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.core.ServerChallengeService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;

import javax.ws.rs.core.Response;
import java.util.List;

public class ChallengesApiServiceImpl extends ChallengesApiService {

    @Autowired
    private ServerChallengeService challengeService;

    @Override
    public Response addChallengeQuestionToASet(String challengeSetId,ChallengeQuestionPatchDTO challengeQuestion){

        challengeService.patchChallengeSet(challengeSetId, challengeQuestion);
        return Response.ok().build();
    }

    @Override
    public Response addChallenges(List<ChallengeSetDTO> challengeSet){

        challengeService.addChallengeSets(challengeSet);
        return Response.ok().build();
    }

    @Override
    public Response deleteChallengeQuestion(String questionId,String challengeSetId,String locale){

        challengeService.deleteQuestion(challengeSetId, questionId, locale);
        return Response.ok().build();
    }
    @Override
    public Response deleteChallengeQuestionSet(String challengeSetId,String locale){

        challengeService.deleteQuestionSet(challengeSetId, locale);
        return Response.ok().build();
    }
    @Override
    public Response getChallengeQuestionSet(String challengeSetId,String locale,Integer offset,Integer limit){

        return Response.ok().entity(challengeService.getChallengeSet(challengeSetId, locale, limit, offset)).build();
    }
    @Override
    public Response searchChallenges(String locale,Integer offset,Integer limit){

        return Response.ok().entity(challengeService.getChallenges(locale, limit, offset)).build();
    }
    @Override
    public Response updateChallengeQuestionSet(String challengeSetId,List<ChallengeQuestionDTO> challengeSet){

        challengeService.updateChallengeSets(challengeSetId, challengeSet);
        return Response.ok().build();
    }
}

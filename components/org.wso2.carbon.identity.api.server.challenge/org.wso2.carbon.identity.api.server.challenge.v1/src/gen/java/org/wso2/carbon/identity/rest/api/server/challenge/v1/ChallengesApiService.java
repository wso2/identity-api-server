package org.wso2.carbon.identity.rest.api.server.challenge.v1;

import org.wso2.carbon.identity.rest.api.server.challenge.v1.*;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.*;

import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class ChallengesApiService {
    public abstract Response addChallengeQuestionToASet(String challengeSetId,ChallengeQuestionPatchDTO challengeQuestion);
    public abstract Response addChallenges(List<ChallengeSetDTO> challengeSet);
    public abstract Response deleteChallengeQuestion(String questionId,String challengeSetId,String locale);
    public abstract Response deleteChallengeQuestionSet(String challengeSetId,String locale);
    public abstract Response getChallengeQuestionSet(String challengeSetId,String locale,Integer offset,Integer limit);
    public abstract Response searchChallenges(String locale,Integer offset,Integer limit);
    public abstract Response updateChallengeQuestionSet(String challengeSetId,List<ChallengeQuestionDTO> challengeSet);
}


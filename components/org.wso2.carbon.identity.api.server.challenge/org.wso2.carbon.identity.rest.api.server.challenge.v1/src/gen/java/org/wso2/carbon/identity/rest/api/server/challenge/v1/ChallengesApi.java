package org.wso2.carbon.identity.rest.api.server.challenge.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.*;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.ChallengesApiService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.factories.ChallengesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionPatchDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeSetDTO;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.dto.ChallengeQuestionDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/challenges")


@io.swagger.annotations.Api(value = "/challenges", description = "the challenges API")
public class ChallengesApi  {

   @Autowired
   private ChallengesApiService delegate;

    @PATCH
    @Path("/{challenge-set-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update challenge question", notes = "Add new challenge question for an existing set\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeQuestionToASet(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "challenge-question to update"  ) ChallengeQuestionPatchDTO challengeQuestion)
    {
    return delegate.addChallengeQuestionToASet(challengeSetId,challengeQuestion);
    }
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "adds a new challenge question", notes = "Adds a new challenge question to the system\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallenges(@ApiParam(value = "challenge-question to add"  ) List<ChallengeSetDTO> challengeSet)
    {
    return delegate.addChallenges(challengeSet);
    }
    @DELETE
    @Path("/{challenge-set-id}/questions/{question-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes a challenge question", notes = "Removes an existing challenge question set from the system. By specifying the locale query parameter, locale specific entry for the question can be deleted.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeQuestion(@ApiParam(value = "Challenge Question Id",required=true ) @PathParam("question-id")  String questionId,
    @ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "pass an optional search string for looking up challenge-question based on locale") @QueryParam("locale")  String locale)
    {
    return delegate.deleteChallengeQuestion(questionId,challengeSetId,locale);
    }
    @DELETE
    @Path("/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes a challenge question set", notes = "Removes an existing challenge question set from the system. By specifying the locale query parameter, questions of specific locale can be deleted within the Set.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeQuestionSet(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "pass an optional search string for looking up challenge-question based on locale") @QueryParam("locale")  String locale)
    {
    return delegate.deleteChallengeQuestionSet(challengeSetId,locale);
    }
    @GET
    @Path("/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "searches challenge-question", notes = "By passing in the appropriate options, you can search for\navailable challenge-question in the system\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getChallengeQuestionSet(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "pass an optional search string for looking up challenge-question based on locale") @QueryParam("locale")  String locale,
    @ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit)
    {
    return delegate.getChallengeQuestionSet(challengeSetId,locale,offset,limit);
    }
    @GET
    
    
    
    @io.swagger.annotations.ApiOperation(value = "searches challenge-question", notes = "By passing in the appropriate options, you can search for\navailable challenge-question in the system\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response searchChallenges(@ApiParam(value = "pass an optional search string for looking up challenge-question based on locale") @QueryParam("locale")  String locale,
    @ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit)
    {
    return delegate.searchChallenges(locale,offset,limit);
    }
    @PUT
    @Path("/{challenge-set-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update challenge question", notes = "Updates an existing challenge question set in the system\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeQuestionSet(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "challenge-question for the set"  ) List<ChallengeQuestionDTO> challengeSet)
    {
    return delegate.updateChallengeQuestionSet(challengeSetId,challengeSet);
    }
}


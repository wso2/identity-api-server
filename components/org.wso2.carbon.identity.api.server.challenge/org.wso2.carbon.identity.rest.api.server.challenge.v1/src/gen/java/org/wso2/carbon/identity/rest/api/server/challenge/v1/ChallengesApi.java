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
    
    @io.swagger.annotations.ApiOperation(value = "Add a challenge question to a set", notes = "Add new challenge question to an existing set.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/update\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeQuestionToASet(@ApiParam(value = "Challenge Question set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "A challenge question to add."  ) ChallengeQuestionPatchDTO challengeQuestion)
    {
    return delegate.addChallengeQuestionToASet(challengeSetId,challengeQuestion);
    }
    @POST
    
    @Consumes({ "application/json" })
    
    @io.swagger.annotations.ApiOperation(value = "Add a new challenge question set.", notes = "Adds a new challenge question set to the system. A challenge question set can have any number of questions.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/create\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallenges(@ApiParam(value = "Challenge question set to add"  ) List<ChallengeSetDTO> challengeSet)
    {
    return delegate.addChallenges(challengeSet);
    }
    @DELETE
    @Path("/{challenge-set-id}/questions/{question-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Remove a challenge question in a set.", notes = "Removes a specific question from an existing challenge question set. By specifying the locale query parameter, locale specific challenge question entry for the question can be deleted.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/delete\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeQuestion(@ApiParam(value = "Challenge Question ID",required=true ) @PathParam("question-id")  String questionId,
    @ApiParam(value = "Challenge Question set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "An optional search string to look-up challenge-questions based on locale.\n") @QueryParam("locale")  String locale)
    {
    return delegate.deleteChallengeQuestion(questionId,challengeSetId,locale);
    }
    @DELETE
    @Path("/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Removes a challenge question set.", notes = "Removes an existing challenge question set from the system. By specifying the locale query parameter, questions of specific locale can be deleted within the Set.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/delete\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeQuestionSet(@ApiParam(value = "Challenge Question set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "An optional search string to look-up challenge-questions based on locale.\n") @QueryParam("locale")  String locale)
    {
    return delegate.deleteChallengeQuestionSet(challengeSetId,locale);
    }
    @GET
    @Path("/{challenge-set-id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve a challenge set.", notes = "Retrieve the challenge questions in the system in a set identified by the challenge-set-id.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/view\n", response = ChallengeSetDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getChallengeQuestionSet(@ApiParam(value = "Challenge Question set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "An optional search string to look-up challenge-questions based on locale.\n") @QueryParam("locale")  String locale,
    @ApiParam(value = "Number of records to skip for pagination. _*This filtering is not yet supported._") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Maximum number of records to return. _*This filtering is not yet supported._") @QueryParam("limit")  Integer limit)
    {
    return delegate.getChallengeQuestionSet(challengeSetId,locale,offset,limit);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve all the challenge questions.", notes = "Retrieve all the challenge questions in the system.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/view\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response searchChallenges(@ApiParam(value = "An optional search string to look-up challenge-questions based on locale.\n") @QueryParam("locale")  String locale,
    @ApiParam(value = "Number of records to skip for pagination. _*This filtering is not yet supported._") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Maximum number of records to return. _*This filtering is not yet supported._") @QueryParam("limit")  Integer limit)
    {
    return delegate.searchChallenges(locale,offset,limit);
    }
    @PUT
    @Path("/{challenge-set-id}")
    @Consumes({ "application/json" })
    
    @io.swagger.annotations.ApiOperation(value = "Update challenge questions of a set.", notes = "Updates an existing challenge question set in the system. This will override the existing challenge questions in the set by new challenge questions.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity/challenge/update\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeQuestionSet(@ApiParam(value = "Challenge Question set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "Challenge-questions for the set"  ) List<ChallengeQuestionDTO> challengeSet)
    {
    return delegate.updateChallengeQuestionSet(challengeSetId,challengeSet);
    }
}


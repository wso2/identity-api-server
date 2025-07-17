/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.identity.governance.v1;

import org.wso2.carbon.identity.api.server.identity.governance.v1.factories.IdentityGovernanceApiServiceFactory;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoriesRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.MultipleConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceResp;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceSearchAttribute;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyRevertReq;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/identity-governance")
@Api(description = "The identity-governance API")

public class IdentityGovernanceApi  {

    private final IdentityGovernanceApiService delegate;

    public IdentityGovernanceApi() {

        this.delegate = IdentityGovernanceApiServiceFactory.getIdentityGovernanceApi();
    }

    @Valid
    @GET
    @Path("/")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connector categories.", notes = "Retrieve governance connector categories.  <b>Permission required:</b> <br>   * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = CategoriesRes.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Governance connector categories.", response = CategoriesRes.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getCategories(    @Valid@ApiParam(value = "Maximum number of records to return.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination.")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted.")  @QueryParam("sort") String sort) {

        return delegate.getCategories(limit,  offset,  filter,  sort );
    }

    @Valid
    @GET
    @Path("/{category-id}/connectors/{connector-id}")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connector.", notes = "Retrieve governance connector.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = ConnectorRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector.", response = ConnectorRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getConnector(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId, @ApiParam(value = "Id of the connector.",required=true) @PathParam("connector-id") String connectorId) {

        return delegate.getConnector(categoryId,  connectorId );
    }

    @Valid
    @GET
    @Path("/{category-id}")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connectors of a category.", notes = "Retrieve governance connectors of a category. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = CategoryRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector category.", response = CategoryRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getConnectorCategory(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId) {

        return delegate.getConnectorCategory(categoryId );
    }

    @Valid
    @GET
    @Path("/{category-id}/connectors")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connectors of a category.", notes = "Retrieve governance connectors of a category.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = ConnectorRes.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector category.", response = ConnectorRes.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getConnectorsOfCategory(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId) {

        return delegate.getConnectorsOfCategory(categoryId );
    }

    @Valid
    @POST
    @Path("/preferences")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve preferences of governance connector configurations.", notes = "This API returns information about configuration preference of governance connectors. This API will accept following keys.   <table>     <tr>       <td><b>Connector names</b></td>       <td><b>Properties</b></td>       <td><b>Description</b></td>     </tr>     <tr>       <td rowspan=\"9\">self-sign-up</td>       <td>SelfRegistration.Enable</td>       <td>Allow user's to self register to the system</td>     </tr>     <tr>       <td>SelfRegistration.LockOnCreation</td>       <td>Lock self registered user account until e-mail verification.</td>     </tr>     <tr>       <td>SelfRegistration.Notification.InternallyManage</td>       <td>Disable if the client application handles notification sending</td>     </tr>     <tr>       <td>SelfRegistration.ReCaptcha</td>       <td>Enable reCaptcha verification during self registration.</td>     </tr>     <tr>       <td>SelfRegistration.VerificationCode.ExpiryTime</td>       <td>Specify the expiry time in minutes for the verification link.</td>     </tr>     <tr>       <td>SelfRegistration.VerificationCode.SMSOTP.ExpiryTime</td>       <td>Specify the expiry time in minutes for the SMS OTP.</td>     </tr>     <tr>       <td>SelfRegistration.CallbackRegex</td>       <td>This prefix will be used to validate the callback URL.</td>     </tr>     <tr>       <td>SelfRegistration.NotifyAccountConfirmation</td>       <td>Enable sending notification for self sign up confirmation.</td>     </tr>     <tr>       <td>SelfRegistration.ResendConfirmationReCaptcha</td>       <td>Prompt reCaptcha verification for resend confirmation</td>     </tr>     <tr>       <td rowspan=\"7\">lite-user-sign-up</td>       <td>LiteRegistration.Enable</td>       <td>Allow user's to self register to the system without a password.</td>     </tr>     <tr>       <td>LiteRegistration.LockOnCreation</td>       <td>Lock self registered user account until e-mail verification.</td>     </tr>     <tr>       <td>LiteRegistration.Notification.InternallyManage</td>       <td>Disable if the client application handles notification sending</td>     </tr>     <tr>       <td>LiteRegistration.ReCaptcha</td>       <td>Enable reCaptcha verification during self registration.</td>     </tr>     <tr>       <td>LiteRegistration.VerificationCode.ExpiryTime</td>       <td>Specify the expiry time in minutes for the verification link.</td>     </tr>     <tr>       <td>LiteRegistration.VerificationCode.SMSOTP.ExpiryTime</td>       <td>Specify the expiry time in minutes for the SMS OTP.</td>     </tr>     <tr>       <td>LiteRegistration.CallbackRegex</td>       <td>This prefix will be used to validate the callback URL.</td>     </tr>     <tr>       <td rowspan=\"6\">user-email-verification</td>       <td>EmailVerification.Enable</td>       <td>A verification notification will be triggered during user creation.</td>     </tr>     <tr>       <td>EmailVerification.LockOnCreation</td>       <td>The user account will be locked during user creation.</td>     </tr>     <tr>       <td>EmailVerification.Notification.InternallyManage</td>       <td>Disable if the client application handles notification sending.</td>     </tr>     <tr>       <td>EmailVerification.ExpiryTime</td>       <td>Set the time span that the verification e-mail would be valid, in minutes. (For infinite validity period, set -1)</td>     </tr>     <tr>       <td>EmailVerification.AskPassword.ExpiryTime</td>       <td>Set the time span that the ask password e-mail would be valid, in minutes. (For infinite validity period, set -1)</td>     </tr>     <tr>       <td>EmailVerification.AskPassword.PasswordGenerator</td>       <td>Temporary password generation extension point in ask password feature.)</td>     </tr>     <tr>     <td rowspan=\"2\">passwordHistory</td>     <td>passwordHistory.enable</td>     <td>User will not be allowed to use previously used passwords.</td>     </tr>     <tr>       <td>passwordHistory.count</td>       <td>Restrict using this number of last used passwords during password update.</td>     </tr>     <tr>       <td rowspan=\"5\">passwordPolicy</td>       <td>passwordPolicy.enable</td>       <td>Validate user passwords against a policy</td>     </tr>     <tr>       <td>passwordPolicy.min.length</td>       <td>Minimum number of characters in the password.</td>     </tr>     <tr>       <td>passwordPolicy.max.length</td>       <td>Maximum number of characters in the password.</td>     </tr>     <tr>       <td>passwordPolicy.pattern</td>       <td>The regular expression pattern to validate the password.</td>     </tr>     <tr>       <td>passwordPolicy.errorMsg</td>       <td>This error message will be displayed when a pattern violation is detected.</td>     </tr>     <tr>       <td rowspan=\"5\">account.lock.handler</td>       <td>account.lock.handler.enable</td>       <td>Lock user accounts on failed login attempts</td>     </tr>     <tr>       <td>account.lock.handler.On.Failure.Max.Attempts</td>       <td>Number of failed login attempts allowed until account lock.</td>     </tr>     <tr>       <td>account.lock.handler.Time</td>       <td>Initial account lock time period in minutes. Account will be automatically unlocked after this time period.</td>     </tr>     <tr>       <td>account.lock.handler.login.fail.timeout.ratio</td>       <td>Account lock duration will be increased by this factor. Ex: Initial duration: 5m; Increment factor: 2; Next lock duration: 5 x 2 = 10m</td>     </tr>     <tr>       <td>account.lock.handler.notification.manageInternally</td>       <td>Disable if the client application handles notification sending</td>     </tr>     <tr>       <td rowspan=\"3\">sso.login.recaptcha</td>       <td>sso.login.recaptcha.enable.always</td>       <td>Always prompt reCaptcha verification during SSO login flow.</td>     </tr>     <tr>       <td>sso.login.recaptcha.enable</td>       <td>Prompt reCaptcha verification during SSO login flow only after the max failed attempts exceeded.</td>     </tr>     <tr>       <td>sso.login.recaptcha.on.max.failed.attempts</td>       <td>Number of failed attempts allowed without prompting reCaptcha verification.</td>     </tr>     <tr>       <td rowspan=\"4\">user-claim-update</td>       <td>UserClaimUpdate.Email.EnableVerification</td>       <td>Trigger a verification notification when user's email address is updated.</td>     </tr>     <tr>       <td>UserClaimUpdate.Email.VerificationCode.ExpiryTime</td>       <td>Validity time of the email confirmation link in minutes.</td>     </tr>     <tr>       <td>UserClaimUpdate.MobileNumber.EnableVerification</td>       <td>Trigger a verification SMS OTP when user's mobile number is updated.</td>     </tr>     <tr>       <td>UserClaimUpdate.MobileNumber.VerificationCode.ExpiryTime</td>       <td>Validity time of the mobile number confirmation OTP in minutes.</td>     </tr>     <tr>     <td rowspan=\"3\">suspension.notification</td>     <td>suspension.notification.enable</td>     <td>Lock user account after a given idle period.</td>     </tr>     <tr>       <td>suspension.notification.account.disable.delay</td>       <td>Time period in days before locking the user account.</td>     </tr>     <tr>       <td>suspension.notification.delays</td>       <td>Send warning alerts to users before locking the account, after each period. Comma separated multiple values accepted.</td>     </tr>     <td rowspan=\"2\">account.disable.handler</td>     <td>account.disable.handler.enable</td>     <td>Allow an administrative user to disable user accounts</td>     </tr>     <tr>       <td>account.disable.handler.notification.manageInternally</td>       <td>Disable, if the client application handles notification sending</td>     </tr>     <tr>       <td rowspan=\"20\">account-recovery</td>       <td>Recovery.Notification.Password.Enable</td>       <td>Notification based password recovery</td>     </tr>     <tr>       <td>Recovery.ReCaptcha.Password.Enable</td>       <td>Enable reCaptcha for password recovery</td>     </tr>     <tr>       <td>Recovery.Question.Password.Enable</td>       <td>Security question based password recovery</td>     </tr>     <tr>       <td>Recovery.Question.Password.MinAnswers</td>       <td>Number of questions required for password recovery</td>     </tr>     <tr>       <td>Recovery.Question.Answer.Regex</td>       <td>Security question answer regex</td>     </tr>     <tr>       <td>Recovery.Question.Answer.Uniquenes</td>       <tdEnforce security question answer uniqueness</td>     </tr>     <tr>       <td>Recovery.Question.Password.ReCaptcha.Enable</td>       <td>rompt reCaptcha for security question based password recovery</td>     </tr>     <tr>       <td>Recovery.Question.Password.ReCaptcha.MaxFailedAttempts</td>       <td>Max failed attempts for reCaptcha</td>     </tr>     <tr>       <td>Recovery.Notification.Username.Enable</td>       <td>Username recovery</td>     </tr><tr>       <td>Recovery.ReCaptcha.Username.Enable</td>       <td>Enable reCaptcha for username recovery</td>     </tr>     <tr>       <td>Recovery.Notification.InternallyManage</td>       <td>Disable if the client application handles notification sending</td>     </tr>     <tr>       <td>Recovery.NotifySuccess</td>       <td>Notify when recovery success</td>     </tr><tr>       <td>Recovery.Question.Password.NotifyStart</td>       <td>Notify when security questions based recovery starts</td>     </tr>     <tr>       <td>Recovery.ExpiryTime</td>       <td>Recovery link expiry time</td>     </tr>     <tr>       <td>Recovery.Notification.Password.ExpiryTime.smsOtp</td>       <td>Expiration time of the SMS OTP code for password recovery</td>     </tr><tr>       <td>Recovery.Notification.Password.smsOtp.Regex</td>       <td>Regex for SMS OTP in format [allowed characters]{length}. Supported character ranges are a-z, A-Z, 0-9.</td>     </tr>     <tr>       <td>Recovery.Question.Password.Forced.Enable</td>       <td>Force users to provide answers to security questions during sign in</td>     </tr>     <tr>       <td>Recovery.Question.MinQuestionsToAnswer</td>       <td>Force users to provide answers to security questions during sign in if user has answered lesser than this value</td>     </tr>     <tr>       <td>Recovery.CallbackRegex</td>       <td>Recovery callback URL regex</td>     </tr><tr>       <td>Recovery.AutoLogin.Enable</td>       <td>User will be logged in automatically after completing the Password Reset wizard</td>     </tr>     <tr>     <td rowspan=\"3\">admin-forced-password-reset</td>     <td>Recovery.AdminPasswordReset.RecoveryLink</td>     <td>User gets notified with a link to reset password</td>     </tr>     <tr>       <td>Recovery.AdminPasswordReset.OTP</td>       <td>User gets notified with a one time password to try with SSO login</td>     </tr>     <tr>       <td>Recovery.AdminPasswordReset.Offline</td>       <td>An OTP generated and stored in users claims.</td>     </tr>   </table>  <b>scope required:</b> * internal_login ", response = PreferenceResp.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configuration preferences", response = PreferenceResp.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class)
    })
    public Response getPreferenceByPost(@ApiParam(value = "This represents the connector and the properties which preferences needs to be returned." ,required=true) @Valid List<PreferenceSearchAttribute> preferenceSearchAttribute) {

        return delegate.getPreferenceByPost(preferenceSearchAttribute );
    }

    @Valid
    @PATCH
    @Path("/{category-id}/connectors/{connector-id}")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "Patch governance connector.", notes = "Patch governance connector.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response patchConnector(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId, @ApiParam(value = "Id of the connector.",required=true) @PathParam("connector-id") String connectorId, @ApiParam(value = "governance-connector to update" ) @Valid ConnectorsPatchReq connectorsPatchReq) {

        return delegate.patchConnector(categoryId,  connectorId,  connectorsPatchReq );
    }

    @Valid
    @PATCH
    @Path("/{category-id}/connectors")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "Patch governance connectors of a category.", notes = "Patch governance connectors of a category.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response patchConnectorsOfCategory(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId, @ApiParam(value = "Governance connectors and properties to update" ,required=true) @Valid MultipleConnectorsPatchReq multipleConnectorsPatchReq) {

        return delegate.patchConnectorsOfCategory(categoryId,  multipleConnectorsPatchReq );
    }

    @Valid
    @POST
    @Path("/{category-id}/connectors/revert")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "Remove properties of a governance connector", notes = "Remove given governance connector properties of a category.<br> <b>Scope (Permission) required:</b> <br>         * internal_idp_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response revertConnectorProperties(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId,     @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Id of the governance connector.",required=true)  @QueryParam("connectorId") String connectorId, @ApiParam(value = "Array of properties to delete." ) @Valid PropertyRevertReq propertyRevertReq) {

        return delegate.revertConnectorProperties(categoryId,  connectorId,  propertyRevertReq );
    }
}

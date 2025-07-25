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

package org.wso2.carbon.identity.api.server.common.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.user.core.UserStoreConfigConstants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Function;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Build user object from user id and tenant domain
 */
public class UserIdtoUser implements Function<String[], User> {

    private static final Log log = LogFactory.getLog(UserIdtoUser.class);

    @Override
    public User apply(String... args) {
        if (log.isDebugEnabled()) {
            log.debug("Extracting user from userId and tenant domain");
        }
        return extractUser(args[0], args[1]);
    }

    private User extractUser(String userId, String tenantDomain) {

        try {
            if (StringUtils.isBlank(userId)) {
                log.warn("UserID is empty or blank");
                throw new WebApplicationException("UserID is empty.");
            }

            String decodedUsername = new String(Base64.getDecoder().decode(userId), StandardCharsets.UTF_8);
            if (log.isDebugEnabled()) {
                log.debug("Decoded username from userId for tenant: " + tenantDomain);
            }

            String[] strComponent = decodedUsername.split("/");

            String username;
            String realm = UserStoreConfigConstants.PRIMARY;

            if (strComponent.length == 1) {
                username = strComponent[0];
            } else if (strComponent.length == 2) {
                realm = strComponent[0];
                username = strComponent[1];
            } else {
                log.warn("UserID format is invalid. Expected format: [realm/]username");
                throw new WebApplicationException("Provided UserID is " + "not in the correct format.");
            }

            User user = new User();
            user.setUserName(username);
            user.setUserStoreDomain(realm);
            user.setTenantDomain(tenantDomain);

            if (log.isDebugEnabled()) {
                log.debug("Successfully extracted user with realm: " + realm + " and tenant: " + tenantDomain);
            }
            return user;
        } catch (Exception e) {
            throw new APIError(Response.Status.BAD_REQUEST,
                    new ErrorResponse.Builder().withCode(Constants.ErrorMessages.ERROR_CODE_INVALID_USERNAME.getCode())
                            .withMessage(Constants.ErrorMessages.ERROR_CODE_INVALID_USERNAME.getMessage())
                            .withDescription(Constants.ErrorMessages.ERROR_CODE_INVALID_USERNAME.getDescription())
                            .build(log, e, "Invalid userId: " + userId));
        }
    }
}

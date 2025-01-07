/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.idle.account.identification.v1;

import org.wso2.carbon.identity.api.idle.account.identification.v1.*;
import org.wso2.carbon.identity.api.idle.account.identification.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.idle.account.identification.v1.model.Error;
import org.wso2.carbon.identity.api.idle.account.identification.v1.model.InactiveUser;
import org.wso2.carbon.identity.api.idle.account.identification.v1.model.Unauthorized;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccountIdentificationClientException;

import javax.ws.rs.core.Response;

public interface InactiveUsersApiService {

    public Response getInactiveUsers(String inactiveAfter, String excludeBefore);

    /**
     * Get inactive users list for a specified period.
     *
     * @param inactiveAfter The date after which the users are considered as inactive.
     * @param excludeBefore The date before which the users are considered as inactive. (optional)
     * @param filter Filter inactive users based isDisabled attribute. (optional)
     * @return InactiveUser
     * @throws IdleAccountIdentificationClientException If an error occurs while retrieving inactive users.
     */
     Response getInactiveUsers(String inactiveAfter, String excludeBefore, String filter)
            throws IdleAccountIdentificationClientException;
}

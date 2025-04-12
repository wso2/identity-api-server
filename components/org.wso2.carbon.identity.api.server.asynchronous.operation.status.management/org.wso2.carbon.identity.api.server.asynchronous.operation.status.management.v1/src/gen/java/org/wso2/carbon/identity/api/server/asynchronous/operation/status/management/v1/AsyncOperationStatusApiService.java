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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1;

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.*;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationRecordsResponse;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperationRecordsResponse;
import javax.ws.rs.core.Response;


public interface AsyncOperationStatusApiService {

      public Response asyncOperationStatusCorrelationIdOperationsGet(String correlationId, String after, String before, Integer limit, String filter);

      public Response asyncOperationStatusOperationsOperationIdUnitOperationsGet(String operationId, String after, String before, Integer limit, String filter);

      public Response asyncOperationStatusSubjectTypesOperationSubjectTypeGet(String operationSubjectType, String after, String before, Integer limit, String filter);

      public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdGet(String operationSubjectType, String operationSubjectId, String after, String before, Integer limit, String filter);

      public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdOperationTypeOperationTypeGet(String operationSubjectType, String operationSubjectId, String operationType, String after, String before, Integer limit, String filter);
}

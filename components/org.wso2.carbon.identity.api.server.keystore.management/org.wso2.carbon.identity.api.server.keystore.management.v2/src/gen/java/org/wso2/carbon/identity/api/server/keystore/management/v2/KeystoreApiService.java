/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.keystore.management.v2;

import org.wso2.carbon.identity.api.server.keystore.management.v2.*;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.AddPrivateKeyRequest;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.CertificateData;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.KeysData;
import javax.ws.rs.core.Response;


public interface KeystoreApiService {

      public Response deletePrivateKey(String alias);

      public Response getPrivateKey(String alias);

      public Response getPrivateKeyAliases(String filter);

      public Response uploadPrivateKey(AddPrivateKeyRequest addPrivateKeyRequest);
}

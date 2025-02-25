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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1;

import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.*;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateAddRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateUpdateRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificates;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validators;
import javax.ws.rs.core.Response;


public interface CertificateValidationApiService {

      public Response addCACertificate(CACertificateAddRequest caCertificateAddRequest);

      public Response deleteCACertificateById(String certificateId);

      public Response getCACertificateById(String certificateId);

      public Response getCACertificates();

      public Response getCertificateRevocationValidator(String validatorName);

      public Response getCertificateRevocationValidators();

      public Response updateCACertificateById(String certificateId, CACertificateUpdateRequest caCertificateUpdateRequest);

      public Response updateCertificateRevocationValidator(String validatorName, Validator validator);
}

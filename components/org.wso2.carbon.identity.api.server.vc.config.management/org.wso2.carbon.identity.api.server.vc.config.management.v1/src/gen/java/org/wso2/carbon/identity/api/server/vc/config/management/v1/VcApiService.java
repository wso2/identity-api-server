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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import org.wso2.carbon.identity.api.server.vc.config.management.v1.*;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.Error;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfiguration;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationUpdateModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOffer;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferUpdateModel;
import javax.ws.rs.core.Response;


public interface VcApiService {

      public Response addVCCredentialConfiguration(VCCredentialConfigurationCreationModel vcCredentialConfigurationCreationModel);

      public Response createVCOffer(VCOfferCreationModel vcOfferCreationModel);

      public Response deleteVCCredentialConfiguration(String configId);

      public Response deleteVCOffer(String offerId);

      public Response getVCCredentialConfiguration(String configId);

      public Response getVCOffer(String offerId);

      public Response listVCCredentialConfigurations();

      public Response listVCOffers();

      public Response updateVCCredentialConfiguration(String configId, VCCredentialConfigurationUpdateModel vcCredentialConfigurationUpdateModel);

      public Response updateVCOffer(String offerId, VCOfferUpdateModel vcOfferUpdateModel);
}

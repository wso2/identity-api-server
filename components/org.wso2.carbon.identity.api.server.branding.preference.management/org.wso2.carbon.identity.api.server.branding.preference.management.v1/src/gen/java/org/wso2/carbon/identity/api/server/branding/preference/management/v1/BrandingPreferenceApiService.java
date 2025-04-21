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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1;

import org.wso2.carbon.identity.api.server.branding.preference.management.v1.*;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationRequestModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResponseModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationResultModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingGenerationStatusModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceWithResolveModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.CustomTextModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.Error;
import javax.ws.rs.core.Response;


public interface BrandingPreferenceApiService {

      public Response addBrandingPreference(BrandingPreferenceModel brandingPreferenceModel);

      public Response addCustomText(CustomTextModel customTextModel);

      public Response deleteBrandingPreference(String type, String name, String locale);

      public Response deleteCustomText(String type, String name, String locale, String screen);

      public Response generateBrandingPreference(BrandingGenerationRequestModel brandingGenerationRequestModel);

      public Response getBrandingGenerationResult(String operationId);

      public Response getBrandingGenerationStatus(String operationId);

      public Response getBrandingPreference(String type, String name, String locale);

      public Response getCustomText(String type, String name, String locale, String screen);

      public Response resolveBrandingPreference(String type, String name, String locale, Boolean restrictToPublished);

      public Response resolveCustomText(String type, String name, String locale, String screen);

      public Response updateBrandingPreference(BrandingPreferenceModel brandingPreferenceModel);

      public Response updateCustomText(CustomTextModel customTextModel);
}

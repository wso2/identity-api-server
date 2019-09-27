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

package org.wso2.carbon.identity.rest.api.server.email.template.v1;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.*;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.*;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateRequestDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateTypeDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateTypeDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateTypeResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.LocaleResponseDTO;
import java.util.List;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class EmailApiService {

    public abstract Response addEmailTemplate(String emailTemplateTypeId, CompleteEmailTemplateRequestDTO type);

    public abstract Response addEmailTemplateType(CompleteEmailTemplateTypeDTO type);

    public abstract Response deleteEmailTemplate(String emailTemplateTypeId, String localeCode);

    public abstract Response deleteEmailTemplateType(String emailTemplateTypeId);

    public abstract Response getAllEmailTemplateTypeLocale(String emailTemplateTypeId, String localeCode, Integer limit, Integer offset, String sort, String sortBy);

    public abstract Response getAllEmailTemplateTypes(Integer limit, Integer offset, String sort, String sortBy);

    public abstract Response getEmailTemplateType(String emailTemplateTypeId, Integer limit, Integer offset, String sort, String sortBy);

    public abstract Response getEmailTemplateTypeForLocale(String emailTemplateTypeId, Integer limit, Integer offset, String sort, String sortBy);

    public abstract Response updateEmailTemplate(String emailTemplateTypeId, String localeCode, CompleteEmailTemplateRequestDTO templates);

    public abstract Response updateEmailTemplateType(String emailTemplateTypeId, List<CompleteEmailTemplateRequestDTO> templates);

}

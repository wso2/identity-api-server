/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.rest.api.server.email.template.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.email.mgt.exceptions.I18nEmailMgtException;
import org.wso2.carbon.email.mgt.model.EmailTemplate;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.email.template.common.Constants;
import org.wso2.carbon.identity.api.server.email.template.common.EmailTemplatesServiceHolder;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.LocaleDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateTypeDTO;

import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.email.template.common.Constants.EMAIL_TEMPLATES_API_BASE_PATH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Call internal osgi services to perform email templates related operations.
 */
public class ServerEmailTemplatesService {

    private static final Log log = LogFactory.getLog(ServerEmailTemplatesService.class);
    private static final String PATH_SEPARATOR = "/";
    private static final String LOCALE_CODE_DELIMITER = "_";

    // I18nEmailUtil.getNormalizedName()

    /**
     * Return all email template types in the system with limited information of the templates inside.
     *
     * @param limit  Limit the number of email template types in the response. **Not supported at the moment**
     * @param offset Offset to be used with the limit parameter. **Not supported at the moment**
     * @param sort   Sort the response in ascending order or descending order. **Not supported at the moment**
     * @param sortBy Element to sort the responses. **Not supported at the moment**
     * @return A list of email template types.
     */
    public List<SimpleEmailTemplateTypeDTO> getAllEmailTemplateTypes(Integer limit, Integer offset, String sort,
                                                                     String sortBy) {

        try {
            List<EmailTemplate> emailTemplates = EmailTemplatesServiceHolder.getEmailTemplateManager().
                    getAllEmailTemplates(ContextLoader.getTenantDomainFromContext());
            return buildSimpleEmailTemplateTypeDTOList(emailTemplates);
        } catch (I18nEmailMgtException e) {
            throw handleI18nEmailMgtException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPES);
        }
    }

    /**
     * Create a list SimpleEmailTemplateTypeDTO objects by reading EmailTemplate list.
     *
     * @param emailTemplates List of EmailTemplate objects.
     * @return List of SimpleEmailTemplateTypeDTO objects.
     */
    private List<SimpleEmailTemplateTypeDTO> buildSimpleEmailTemplateTypeDTOList(List<EmailTemplate> emailTemplates) {

        Map<String, SimpleEmailTemplateTypeDTO> templateTypeMap = new HashMap<>();
        for (EmailTemplate emailTemplate : emailTemplates) {
            if (templateTypeMap.containsKey(emailTemplate.getTemplateType())) {

                SimpleEmailTemplateDTO simpleEmailTemplateDTO = getSimpleEmailTemplateDTO(emailTemplate);

                templateTypeMap.get(emailTemplate.getTemplateType()).getItems().add(simpleEmailTemplateDTO);
            } else {
                SimpleEmailTemplateTypeDTO simpleEmailTemplateTypeDTO = new SimpleEmailTemplateTypeDTO();
                simpleEmailTemplateTypeDTO.setDisplayName(emailTemplate.getTemplateDisplayName());
                simpleEmailTemplateTypeDTO.setName(emailTemplate.getTemplateType());
                String templateTypeId = base64URLEncode(emailTemplate.getTemplateType());
                simpleEmailTemplateTypeDTO.setId(templateTypeId);
                simpleEmailTemplateTypeDTO.setLocation(EMAIL_TEMPLATES_API_BASE_PATH + PATH_SEPARATOR + templateTypeId);

                SimpleEmailTemplateDTO simpleEmailTemplateDTO = getSimpleEmailTemplateDTO(emailTemplate);
                List<SimpleEmailTemplateDTO> emailTemplatesList = new ArrayList<>();

                emailTemplatesList.add(simpleEmailTemplateDTO);
                simpleEmailTemplateTypeDTO.setItems(emailTemplatesList);

                templateTypeMap.put(emailTemplate.getTemplateType(), simpleEmailTemplateTypeDTO);
            }
        }

        return new ArrayList<>(templateTypeMap.values());
    }

    /**
     * Create a SimpleEmailTemplateDTO object by reading EmailTemplate object.
     *
     * @param emailTemplate Original EmailTemplate object.
     * @return Created SimpleEmailTemplateDTO object.
     */
    private SimpleEmailTemplateDTO getSimpleEmailTemplateDTO(EmailTemplate emailTemplate) {

        SimpleEmailTemplateDTO simpleEmailTemplateDTO = new SimpleEmailTemplateDTO();
        simpleEmailTemplateDTO.setContentType(emailTemplate.getEmailContentType());

        LocaleDTO localeDTO = getLocaleDTO(emailTemplate.getLocale());
        simpleEmailTemplateDTO.setLocale(localeDTO);
        return simpleEmailTemplateDTO;
    }

    /**
     * Create a LocaleDTO using a given locale code. This local code must be supported by {@link Locale}
     * to obtain a proper display name.
     *
     * @param localeCode Locale code with language and country. Ex: en_US.
     * @return Created LocaleDTO with locale code and display name.
     */
    private LocaleDTO getLocaleDTO(String localeCode) {

        LocaleDTO localeDTO = new LocaleDTO();
        localeDTO.setCode(localeCode);

        String[] localeElements = localeCode.split(LOCALE_CODE_DELIMITER);
        Locale locale = new Locale(localeElements[0], localeElements[1]);
        localeDTO.setDisplayName(locale.getDisplayName());

        return localeDTO;
    }

    /**
     * Handle I18nEmailMgtException, i.e. extract error description from the exception and set to the
     * API Error Response, along with an status code to be sent in the response.
     *
     * @param exception Exception thrown
     * @param errorEnum Corresponding error enum
     * @return API Error object.
     */
    private APIError handleI18nEmailMgtException(I18nEmailMgtException exception, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, exception, errorEnum.getDescription());

        Response.Status status;

        if (exception != null) {
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).
                withMessage(errorMsg.getMessage()).withDescription(errorMsg.getDescription());
    }
}

/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.org).
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

package org.wso2.carbon.identity.api.server.input.validation.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.input.validation.common.InputValidationServiceHolder;
import org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.Mapping;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.PropertyModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RuleModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidatorModel;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtClientException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtServerException;
import org.wso2.carbon.identity.input.validation.mgt.model.*;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.input.validation.common.util.Utils.getCorrelation;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.*;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.INPUT_VALIDATION_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.INPUT_VALIDATION_MGT_ERROR_CODE_DELIMITER;

/**
 * Calls internal osgi services to perform input validation management related operations.
 */
public class ValidationRulesManagementApiService {

    private static final Log log = LogFactory.getLog(ValidationRulesManagementApiService.class);

    /**
     * Method to get input validation configuration.
     *
     * @param tenantDomain  Tenant Domain.
     * @return ValidationConfigModal.
     */
    public List<ValidationConfigModel> getValidationConfiguration(String tenantDomain) {

        try {
            List<ValidationConfiguration> configurations = InputValidationServiceHolder.getInputValidationMgtService()
                    .getInputValidationConfiguration(tenantDomain);
            return buildResponse(configurations);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to update input validation configuration.
     *
     * @param validationConfigModel Validation Configuration Model.
     * @param tenantDomain          Tenant domain name.
     */
    public List<ValidationConfigModel> updateInputValidationConfiguration(List<ValidationConfigModel> validationConfigModel,
                                                   String tenantDomain) {

        try {
            List<ValidationConfiguration> requestDTO = buildRequestDTOFromValidationRequest(validationConfigModel);
            List<ValidationConfiguration> configurations = InputValidationServiceHolder.getInputValidationMgtService()
                    .updateInputValidationConfiguration(requestDTO, tenantDomain);
            return buildResponse(configurations);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to validate input values.
     *
     * @param tenantDomain      Tenant domain.
     */
    public List<ValidatorModel> getValidators(String tenantDomain) {

        try {
            List<ValidatorConfiguration> validators = InputValidationServiceHolder.getInputValidationMgtService()
                    .getValidators(tenantDomain);
            return buildValidationResponse(validators);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    private List<ValidatorModel> buildValidationResponse(List<ValidatorConfiguration> validators) {

        List<ValidatorModel> response = new ArrayList<>();
        for (ValidatorConfiguration configuration : validators) {
            ValidatorModel validator = new ValidatorModel();
            validator.setName(configuration.getName());
            validator.setType(configuration.getType());

            List<Property> propertiesConfig = configuration.getProperties();
            List<PropertyModel> properties = new ArrayList<>();
            for (Property property : propertiesConfig) {
                PropertyModel model = new PropertyModel();
                model.setName(property.getName());
                model.setDescription(property.getDescription());
                model.setDisplayName(property.getDisplayName());
                model.setType(property.getType());
                model.setDisplayOrder(property.getDisplayOrder());

                properties.add(model);
            }
            validator.setProperties(properties);
            response.add(validator);
        }
        return response;
    }

    /**
     * Method to build RequestDTO from validation request.
     *
     * @param validationConfigModels    Validation configuration request.
     * @return  list of validation configurations.
     */
    private List<ValidationConfiguration> buildRequestDTOFromValidationRequest(
            List<ValidationConfigModel> validationConfigModels
    ) throws InputValidationMgtClientException {

        List<ValidationConfiguration> requestDTO = new ArrayList<>();
        for (ValidationConfigModel configModel: validationConfigModels) {
            ValidationConfiguration configurationDTO = new ValidationConfiguration();

            configurationDTO.setField(configModel.getField());

            // Ensure the validation configuration is configured with either rules or regex.
            if ((configModel.getRules() != null && configModel.getRegEx() != null) ||
                    (configModel.getRules() == null && configModel.getRegEx() == null)) {
                throw new InputValidationMgtClientException(ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getCode(),
                        ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getMessage(),
                        ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getDescription());
            }

            if (configModel.getRules() != null) {
                configurationDTO.setRules(buildRulesDTO(configModel.getRules()));
            }

            if (configModel.getRegEx() != null) {
                configurationDTO.setRegEx(buildRulesDTO(configModel.getRegEx()));
            }
            requestDTO.add(configurationDTO);
        }
        return requestDTO;
    }

    private List<RulesConfiguration> buildRulesDTO(List<RuleModel> rules) {

        List<RulesConfiguration> rulesDTO = new ArrayList<>();
        for (RuleModel rule: rules) {
            RulesConfiguration ruleDTO = new RulesConfiguration();
            ruleDTO.setValidator(rule.getValidator());

            Map<String, String> rulesMap =
                    rule.getProperties().stream()
                            .collect(Collectors.toMap(Mapping::getKey, Mapping::getValue));
            ruleDTO.setProperties(rulesMap);
            rulesDTO.add(ruleDTO);
        }
        return rulesDTO;
    }

    /**
     * Method to build response.
     *
     * @param configurations@return ValidationConfigModal.
     */
    private List<ValidationConfigModel> buildResponse(List<ValidationConfiguration> configurations) {

        List<ValidationConfigModel> response = new ArrayList<>();

        for (ValidationConfiguration configuration: configurations) {
            ValidationConfigModel configModel = new ValidationConfigModel();
            configModel.setField(configuration.getField());

            if (configuration.getRules() != null) {
                configModel.setRules(buildRulesModel(configuration.getRules()));
            }
            if (configModel.getRegEx() != null) {
                configModel.setRules(buildRulesModel(configuration.getRegEx()));
            }
            response.add(configModel);
        }

        return response;
    }

    private List<RuleModel> buildRulesModel(List<RulesConfiguration> rulesConfigurations) {

        List<RuleModel> rules = new ArrayList<>();
        for (RulesConfiguration ruleConfig: rulesConfigurations) {

            List<Mapping> properties =
                    ruleConfig.getProperties().entrySet().stream()
                            .filter(property -> property.getValue() != null && !"null".equals(property.getValue()))
                            .map(this::getMapping)
                            .collect(Collectors.toList());
            RuleModel rule = new RuleModel();
            rule.setValidator(ruleConfig.getValidator());
            rule.setProperties(properties);
            rules.add(rule);
        }
        return rules;
    }
    private Mapping getMapping(Map.Entry entry) {

        Mapping mapping = new Mapping();
        mapping.setKey((String) entry.getKey());
        mapping.setValue((String) entry.getValue());
        return mapping;
    }

    /**
     * Handle input validation management exceptions and return an API error.
     *
     * @param exception Input validation management exception
     * @param errorEnum Input validation management error enum.
     * @param data      Relevant data.
     * @return Processed API Error.
     */
    private APIError handleInputValidationMgtException(InputValidationMgtException exception,
                                                          ValidationManagementConstants.ErrorMessage errorEnum,
                                                          String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (exception instanceof InputValidationMgtClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception.getMessage());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(INPUT_VALIDATION_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        INPUT_VALIDATION_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            errorResponse.setRef(getCorrelation());
            if (StringUtils.isNotEmpty(exception.getDescription())) {
                errorResponse.setMessage(exception.getMessage());
                errorResponse.setDescription(exception.getDescription());
            }
            if (ERROR_CODE_INPUT_VALIDATION_NOT_EXISTS.getCode().equals(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof InputValidationMgtServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode = errorCode.contains(INPUT_VALIDATION_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        INPUT_VALIDATION_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            errorResponse.setRef(getCorrelation());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.getDescription());
            errorResponse.setRef(getCorrelation());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(ValidationManagementConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private String includeData(ValidationManagementConstants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }
}

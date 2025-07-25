/*
 * Copyright (c) 2022-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.v1.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.MappingModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RuleModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidatorModel;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtClientException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtServerException;
import org.wso2.carbon.identity.input.validation.mgt.model.FieldValidationConfigurationHandler;
import org.wso2.carbon.identity.input.validation.mgt.model.RulesConfiguration;
import org.wso2.carbon.identity.input.validation.mgt.model.ValidationConfiguration;
import org.wso2.carbon.identity.input.validation.mgt.model.ValidationContext;
import org.wso2.carbon.identity.input.validation.mgt.model.Validator;
import org.wso2.carbon.identity.input.validation.mgt.model.ValidatorConfiguration;
import org.wso2.carbon.identity.input.validation.mgt.model.validators.AbstractRegExValidator;
import org.wso2.carbon.identity.input.validation.mgt.model.validators.AbstractRulesValidator;
import org.wso2.carbon.identity.input.validation.mgt.services.InputValidationManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.input.validation.common.util.Utils.getCorrelation;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_VALIDATORS;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_REVERTING_VALIDATION_CONFIG;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_FIELD_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.ErrorMessage.ERROR_CODE_INPUT_VALIDATION_NOT_EXISTS;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.INPUT_VALIDATION_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants.INPUT_VALIDATION_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.ErrorMessages.ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.ErrorMessages.ERROR_VALIDATION_PARAM_NOT_SUPPORTED;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.ErrorMessages.ERROR_VALIDATOR_NOT_SUPPORTED;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.ErrorMessages.ERROR_VALIDATOR_NOT_SUPPORTED_FOR_FIELD;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.SUPPORTED_PARAMS;

/**
 * Calls internal osgi services to perform input validation management related operations.
 */
public class ValidationRulesManagementApiService {

    private final InputValidationManagementService inputValidationManagementService;
    private static final Log LOGGER = LogFactory.getLog(ValidationRulesManagementApiService.class);

    public ValidationRulesManagementApiService(InputValidationManagementService inputValidationManagementService) {

        this.inputValidationManagementService = inputValidationManagementService;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ValidationRulesManagementApiService instance created.");
        }
    }

    /**
     * Method to get input validation configuration.
     *
     * @param tenantDomain  Tenant Domain.
     * @return ValidationConfigModal.
     */
    public List<ValidationConfigModel> getValidationConfiguration(String tenantDomain) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting validation configuration for tenant: " + tenantDomain);
        }
        try {
            List<ValidationConfiguration> configurations = inputValidationManagementService
                    .getInputValidationConfiguration(tenantDomain);
            LOGGER.info("Successfully retrieved validation configurations for tenant: " + tenantDomain);
            return buildResponse(configurations);
        } catch (InputValidationMgtException e) {
            LOGGER.warn("Failed to get validation configuration for tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to get input validation configuration.
     *
     * @param tenantDomain  Tenant Domain.
     * @return ValidationConfigModel.
     */
    public ValidationConfigModel getValidationConfigurationForField(String tenantDomain, String field) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting validation configuration for field: " + field + " in tenant: " + tenantDomain);
        }
        try {
            isFieldSupported(field);
            ValidationConfiguration configuration = inputValidationManagementService
                    .getInputValidationConfigurationForField(tenantDomain, field);
            LOGGER.info("Successfully retrieved validation configuration for field: " + field + 
                    " in tenant: " + tenantDomain);
            return buildResponse(configuration);
        } catch (InputValidationMgtException e) {
            LOGGER.warn("Failed to get validation configuration for field: " + field + 
                    " in tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to update input validation configuration.
     *
     * @param validationConfigModel Validation Configuration Model.
     * @param tenantDomain          Tenant domain name.
     */
    public List<ValidationConfigModel> updateInputValidationConfiguration(
            List<ValidationConfigModel> validationConfigModel, String tenantDomain) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating validation configuration for tenant: " + tenantDomain);
        }
        try {
            List<ValidationConfiguration> requestDTO = buildRequestDTOFromValidationRequest(validationConfigModel);
            validateProperties(requestDTO, tenantDomain);
            List<ValidationConfiguration> configurations = inputValidationManagementService
                    .updateInputValidationConfiguration(requestDTO, tenantDomain);
            LOGGER.info("Successfully updated validation configuration for tenant: " + tenantDomain);
            return buildResponse(configurations);
        } catch (InputValidationMgtException e) {
            LOGGER.warn("Failed to update validation configuration for tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to update input validation configuration.
     *
     * @param validationConfigModel Validation Configuration Model.
     * @param tenantDomain          Tenant domain name.
     * @return ValidationConfigModel for the field.
     */
    public ValidationConfigModel updateInputValidationConfigurationForField(
            ValidationConfigModel validationConfigModel, String tenantDomain) {

        String field = validationConfigModel != null ? validationConfigModel.getField() : null;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating validation configuration for field: " + field + " in tenant: " + tenantDomain);
        }
        try {
            isFieldSupported(validationConfigModel.getField());
            List<ValidationConfigModel> configModels = new ArrayList<>();
            configModels.add(validationConfigModel);
            List<ValidationConfiguration> requestDTO = buildRequestDTOFromValidationRequest(configModels);
            validateProperties(requestDTO, tenantDomain);
            ValidationConfiguration configurations = inputValidationManagementService
                    .updateValidationConfiguration(requestDTO.get(0), tenantDomain);
            LOGGER.info("Successfully updated validation configuration for field: " + field + 
                    " in tenant: " + tenantDomain);
            return buildResponse(configurations);
        } catch (InputValidationMgtException e) {
            LOGGER.warn("Failed to update validation configuration for field: " + field + 
                    " in tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to revert input validation configuration for given fields.
     *
     * @param fields        List of fields to revert validation configurations.
     * @param tenantDomain  Tenant domain name.
     */
    public void revertInputValidationConfigurationForFields(List<String> fields, String tenantDomain) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Reverting validation configuration for fields in tenant: " + tenantDomain);
        }
        try {
            for (String field : fields) {
                isFieldSupported(field);
            }

            inputValidationManagementService.revertInputValidationConfiguration(fields, tenantDomain);
            LOGGER.info("Successfully reverted validation configuration for fields in tenant: " + tenantDomain);
        } catch (InputValidationMgtException e) {
            LOGGER.warn("Failed to revert validation configuration for fields in tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_REVERTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to validate input values.
     *
     * @param tenantDomain  Tenant domain.
     */
    public List<ValidatorModel> getValidators(String tenantDomain) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Getting validators for tenant: " + tenantDomain);
        }
        List<ValidatorConfiguration> validators;
        try {
            validators = inputValidationManagementService.getValidatorConfigurations(tenantDomain);
            LOGGER.info("Successfully retrieved validators for tenant: " + tenantDomain);
            return buildValidatorResponse(validators);
        } catch (InputValidationMgtException e) {
            if (ERROR_CODE_INPUT_VALIDATION_NOT_EXISTS.getCode().contains(e.getErrorCode())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Can not find a validator configurations for tenant: " +
                            tenantDomain, e);
                }
            }
            LOGGER.warn("Failed to get validators for tenant: " + tenantDomain);
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATORS, tenantDomain);
        }
    }

    /**
     * Method to build validator response object.
     *
     * @param validators    List of validator configurations.
     * @return  Response object of validators.
     */
    private List<ValidatorModel> buildValidatorResponse(List<ValidatorConfiguration> validators) {

        List<ValidatorModel> response = new ArrayList<>();
        for (ValidatorConfiguration configuration : validators) {
            ObjectMapper mapper = new ObjectMapper();
            ValidatorModel validator = mapper.convertValue(configuration, ValidatorModel.class);
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
            // Ensure the validation configuration is configured with either rules or regex.
            if ((configModel.getRules() != null && configModel.getRegEx() != null) ||
                    (configModel.getRules() == null && configModel.getRegEx() == null)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Can not configure validation with both rules and regex empty " +
                            "or available.");
                }
                throw new InputValidationMgtClientException(ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getCode(),
                        ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getMessage(),
                        ERROR_CODE_CONFIGURE_EITHER_RULES_OR_REGEX.getDescription());
            }

            ValidationConfiguration configurationDTO = new ValidationConfiguration();
            configurationDTO.setField(configModel.getField());
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

    /**
     * Method to build rules object.
     *
     * @param rules List of rules.
     * @return  List of rules configuration.
     */
    private List<RulesConfiguration> buildRulesDTO(List<RuleModel> rules) {

        List<RulesConfiguration> rulesDTO = new ArrayList<>();
        for (RuleModel rule: rules) {
            RulesConfiguration ruleDTO = new RulesConfiguration();
            ruleDTO.setValidatorName(rule.getValidator());

            Map<String, String> rulesMap =
                    rule.getProperties().stream()
                            .collect(Collectors.toMap(MappingModel::getKey, MappingModel::getValue));
            ruleDTO.setProperties(rulesMap);
            rulesDTO.add(ruleDTO);
        }
        return rulesDTO;
    }

    /**
     * Method to build response.
     *
     * @param configurations    Configurations.
     * @return ValidationConfigModal.
     */
    private List<ValidationConfigModel> buildResponse(List<ValidationConfiguration> configurations) {

        List<ValidationConfigModel> response = new ArrayList<>();

        for (ValidationConfiguration configuration: configurations) {
            response.add(buildResponse(configuration));
        }
        return response;
    }

    /**
     * Method to build response for single field.
     *
     * @param configuration    Configuration of the field.
     * @return ValidationConfigModal of the field.
     */
    private ValidationConfigModel buildResponse(ValidationConfiguration configuration) {

        ValidationConfigModel configModel = new ValidationConfigModel();
        configModel.setField(configuration.getField());

        if (configuration.getRules() != null) {
            configModel.setRules(buildRulesModel(configuration.getRules()));
        }
        if (configuration.getRegEx() != null) {
            configModel.setRegEx(buildRulesModel(configuration.getRegEx()));
        }

        return configModel;
    }

    /**
     * Method to build rules model.
     *
     * @param rulesConfigurations   Rule configurations.
     * @return  List of rules.
     */
    private List<RuleModel> buildRulesModel(List<RulesConfiguration> rulesConfigurations) {

        List<RuleModel> rules = new ArrayList<>();
        for (RulesConfiguration ruleConfig: rulesConfigurations) {

            List<MappingModel> properties =
                    ruleConfig.getProperties().entrySet().stream()
                            .filter(property -> property.getValue() != null && !"null".equals(property.getValue()))
                            .map(this::getMapping)
                            .collect(Collectors.toList());
            RuleModel rule = new RuleModel();
            rule.setValidator(ruleConfig.getValidatorName());
            rule.setProperties(properties);
            rules.add(rule);
        }
        return rules;
    }

    /**
     * Method to get mapping.
     *
     * @param entry Entry with mapping details.
     * @return  Mapping of property and value.
     */
    private MappingModel getMapping(Map.Entry entry) {

        MappingModel mapping = new MappingModel();
        mapping.setKey((String) entry.getKey());
        mapping.setValue((String) entry.getValue());
        return mapping;
    }

    /**
     * Method to validate properties.
     *
     * @param configurations    Validation configuration.
     * @param tenantDomain      Tenant domain name.
     * @throws InputValidationMgtClientException If an error occurred when validating configurations.
     */
    private void validateProperties(List<ValidationConfiguration> configurations, String tenantDomain)
            throws InputValidationMgtClientException {

        for (ValidationConfiguration config: configurations) {
            if (!SUPPORTED_PARAMS.contains(config.getField())) {
                throw new InputValidationMgtClientException(ERROR_VALIDATION_PARAM_NOT_SUPPORTED.getCode(),
                        String.format(ERROR_VALIDATION_PARAM_NOT_SUPPORTED.getDescription(), config.getField(),
                                tenantDomain));
            }
            boolean isRules = false;
            List<RulesConfiguration> rules = new ArrayList<>();
            if (config.getRules() != null) {
                isRules = true;
                rules = config.getRules();
            } else if (config.getRegEx() != null) {
                rules = config.getRegEx();
            }
            validateProperties(config.getField(), isRules, rules, tenantDomain);
        }
    }

    /**
     * Method to validate rules configuration.
     *
     * @param isRules       Type of validation.
     * @param rules         List of rule configs.
     * @param tenantDomain  Tenant domain name.
     * @throws InputValidationMgtClientException If an error occurred when validating rules.
     */
    private void validateProperties(String field, boolean isRules, List<RulesConfiguration> rules, String tenantDomain)
            throws InputValidationMgtClientException {

        Map<String, Validator> allValidators = inputValidationManagementService.getValidators(tenantDomain);
        ValidationContext context;
        for (RulesConfiguration rule: rules) {
            Validator validator = allValidators.get(rule.getValidatorName());
            if ((isRules && validator instanceof AbstractRulesValidator && validator.canHandle(rule.getValidatorName()))
                    || (!isRules && validator instanceof AbstractRegExValidator &&
                    validator.canHandle(rule.getValidatorName()))) {

                // Check whether validator is allowed for the field.
                if (!validator.isAllowedField(field)) {
                    throw new InputValidationMgtClientException(ERROR_VALIDATOR_NOT_SUPPORTED_FOR_FIELD.getCode(),
                            String.format(ERROR_VALIDATOR_NOT_SUPPORTED_FOR_FIELD.getDescription(),
                                    rule.getValidatorName(), field));
                }
                context = new ValidationContext(field, tenantDomain, rule.getProperties(), null);
                validator.validateProps(context);
            } else {
                throw new InputValidationMgtClientException(ERROR_VALIDATOR_NOT_SUPPORTED.getCode(),
                        String.format(ERROR_VALIDATOR_NOT_SUPPORTED.getDescription(), rule.getValidatorName(),
                                isRules ? "rules" : "regex"));
            }
        }

        // Validate provided validation is allowed for the field.
        for (FieldValidationConfigurationHandler handler: inputValidationManagementService
                .getFieldValidationConfigurationHandlers().values()) {
            if (handler.canHandle(field)) {
                handler.validateValidationConfiguration(rules);
            }
        }
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
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception.getMessage());
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
            if (ERROR_CODE_INPUT_VALIDATION_NOT_EXISTS.getCode().contains(exception.getErrorCode()) ||
                    ERROR_CODE_FIELD_NOT_EXISTS.getCode().contains(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else if (exception instanceof InputValidationMgtServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception, errorEnum.getDescription());
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
            errorResponse = getErrorBuilder(errorEnum, data).build(LOGGER, exception, errorEnum.getDescription());
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

    /**
     * Check whether field is supported for validation configurations.
     *
     * @param field     Field name.
     * @return True if given field is supported.
     * @throws InputValidationMgtClientException if field is not supported or invalid.
     */
    private boolean isFieldSupported(String field) throws InputValidationMgtClientException {

        if (SUPPORTED_PARAMS.contains(field)) {
            return true;
        }
        throw new InputValidationMgtClientException(ERROR_CODE_FIELD_NOT_EXISTS.getCode(), ERROR_CODE_FIELD_NOT_EXISTS
                .getMessage(), String.format(ERROR_CODE_FIELD_NOT_EXISTS.getDescription(), field));
    }
}

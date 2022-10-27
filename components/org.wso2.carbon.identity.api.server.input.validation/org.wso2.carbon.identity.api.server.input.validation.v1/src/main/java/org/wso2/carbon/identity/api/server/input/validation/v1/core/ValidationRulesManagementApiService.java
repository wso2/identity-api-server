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
import org.wso2.carbon.identity.api.server.input.validation.v1.models.*;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtClientException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtException;
import org.wso2.carbon.identity.input.validation.mgt.exceptions.InputValidationMgtServerException;
import org.wso2.carbon.identity.input.validation.mgt.model.*;

import javax.ws.rs.core.Response;

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
    public ValidationConfigModal getValidationConfiguration(String tenantDomain) {

        InputValidationConfiguration validationDTO;
        try {
            validationDTO = InputValidationServiceHolder.getInputValidationManager()
                    .getInputValidationConfiguration(tenantDomain);
            return buildResponse(validationDTO);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to update input validation configuration.
     *
     * @param configuration Input validation configuration.
     * @param tenantDomain  Tenant domain.
     * @return Validation configuration.
     */
    public ValidationConfigModal updateInputValidationConfiguration(ValidationConfigModal configuration,
                                                                    String tenantDomain) {
        InputValidationConfiguration validationDTO = buildRequestDTOFromValidationRequest(configuration);
        try {
            validationDTO = InputValidationServiceHolder.getInputValidationManager()
                    .updateInputValidationConfiguration(validationDTO, tenantDomain);
            return buildResponse(validationDTO);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG, tenantDomain);
        }
    }

    /**
     * Method to validate input values.
     *
     * @param validateRequest   Object contains the params to be validated.
     * @param tenantDomain      Tenant domain.
     */
    public void validateValues(ValidateRequest validateRequest, String tenantDomain) {

        ValidationParam param = new ValidationParam(validateRequest.getName(), validateRequest.getValue());
        try {
            InputValidationServiceHolder.getInputValidationManager()
                    .validateValues(tenantDomain, param);
        } catch (InputValidationMgtException e) {
            throw handleInputValidationMgtException(e, ERROR_CODE_ERROR_VALIDATING_PARAM, tenantDomain);
        }
    }

    /**
     * Method to build RequestDTO from validation request.
     *
     * @param configuration ValidationConfigModal.
     * @return  inputValidationConfiguration.
     */
    private InputValidationConfiguration buildRequestDTOFromValidationRequest(ValidationConfigModal configuration) {

        InputValidationConfiguration inputValidationConfiguration = new InputValidationConfiguration();
        PasswordValidator passwordValidatorDTO = new PasswordValidator();

        PasswordValidationModal passwordConfigReq = configuration.getPassword();
        if (passwordConfigReq.getRules() != null) {
            passwordValidatorDTO.setRulesValidator(buildPasswordRulesDTO(passwordConfigReq.getRules()));
        } else if (passwordConfigReq.getRegEx() != null) {
            passwordValidatorDTO.setRegExValidator(buildRegExDTO(passwordConfigReq.getRegEx()));
        }

        inputValidationConfiguration.setPasswordValidator(passwordValidatorDTO);
        return inputValidationConfiguration;
    }

    /**
     * Method to build password rules DTO.
     *
     * @param passwordRulesReq  password rules request.
     * @return RulesValidator.
     */
    private RulesValidator buildPasswordRulesDTO(ValidationRulesModal passwordRulesReq) {

        RulesValidator passwordRulesValidatorDTO = new RulesValidator();
        boolean caseSensitive = false;

        if (passwordRulesReq.getLengthValidator() != null) {
            passwordRulesValidatorDTO.setLengthValidator(buildLengthValidatorDTO(passwordRulesReq.getLengthValidator()));
        }
        if(passwordRulesReq.getLowercaseValidator() != null) {
            passwordRulesValidatorDTO.setLowerCaseValidator(buildLengthValidatorDTO(passwordRulesReq.getLowercaseValidator()));
        }
        if(passwordRulesReq.getUpperCaseValidator() != null) {
            passwordRulesValidatorDTO.setUpperCaseValidator(buildLengthValidatorDTO(passwordRulesReq.getUpperCaseValidator()));
        }
        if(passwordRulesReq.getNumeralsValidator() != null) {
            passwordRulesValidatorDTO.setNumeralsValidator(buildLengthValidatorDTO(passwordRulesReq.getNumeralsValidator()));
        }
        if(passwordRulesReq.getSpecialCharactersValidator() != null) {
            passwordRulesValidatorDTO.setSpecialCharacterValidator(buildLengthValidatorDTO(passwordRulesReq.getSpecialCharactersValidator()));
        }
        if (passwordRulesReq.getRepeatedCharactersValidator() != null &&
                passwordRulesReq.getRepeatedCharactersValidator().getEnabled() != null &&
                passwordRulesReq.getRepeatedCharactersValidator().getEnabled() &&
                passwordRulesReq.getRepeatedCharactersValidator().getMaxConsecutiveLength() != null) {

            RepeatedCharactersValidatorModal repeatedChrValidator = passwordRulesReq.getRepeatedCharactersValidator();
            RepeatedCharacterValidator repeatedCharacterValidatorDTO = new RepeatedCharacterValidator();
            repeatedCharacterValidatorDTO.setEnable(true);
            repeatedCharacterValidatorDTO.setMaxConsecutiveLength(repeatedChrValidator.getMaxConsecutiveLength());

            if (repeatedChrValidator.getCaseSensitive() != null) {
                caseSensitive = repeatedChrValidator.getCaseSensitive();
            }
            repeatedCharacterValidatorDTO.setCaseSensitive(caseSensitive);
            passwordRulesValidatorDTO.setRepeatedCharacterValidator(repeatedCharacterValidatorDTO);
        }

        if (passwordRulesReq.getUniqueCharactersValidator() != null &&
                passwordRulesReq.getUniqueCharactersValidator().getEnabled() != null &&
                passwordRulesReq.getUniqueCharactersValidator().getEnabled() &&
                passwordRulesReq.getUniqueCharactersValidator().getMinUniqueCharacters()!= null) {

            UniqueCharactersValidatorModal uniqueChrValidatorReq = passwordRulesReq.getUniqueCharactersValidator();
            UniqueCharacterValidator uniqueChrValidatorDTO = new UniqueCharacterValidator();
            uniqueChrValidatorDTO.setEnable(true);
            uniqueChrValidatorDTO.setMinUniqueCharacter(uniqueChrValidatorReq.getMinUniqueCharacters());

            if (uniqueChrValidatorReq.getCaseSensitive() != null) {
                caseSensitive = uniqueChrValidatorReq.getCaseSensitive();
            }
            uniqueChrValidatorDTO.setCaseSensitive(caseSensitive);
            passwordRulesValidatorDTO.setUniqueCharacterValidator(uniqueChrValidatorDTO);
        }

        return passwordRulesValidatorDTO;
    }

    /**
     * Method to build regEx DTO.
     *
     * @param requestRegEx  regex request object.
     * @return RegExValidator.
     */
    private RegExValidator buildRegExDTO(ValidationRegExModal requestRegEx) {

        RegExValidator regExValidatorDTO = new RegExValidator();
        if (StringUtils.isNotEmpty(requestRegEx.getJavaRegExValidator())) {
            regExValidatorDTO.setJavaRegExPattern(requestRegEx.getJavaRegExValidator());
        }
        if (StringUtils.isNotEmpty(requestRegEx.getJsRegExValidator())) {
            regExValidatorDTO.setJsRegExPattern(requestRegEx.getJsRegExValidator());
        }

        return regExValidatorDTO;
    }

    /**
     * Method to build length validator object.
     *
     * @param validatorRequest  Validator request object.
     * @return DefaultValidator.
     */
    private DefaultValidator buildLengthValidatorDTO(BasicValidatorModal validatorRequest) {

        DefaultValidator validator = new DefaultValidator();
        if (validatorRequest.getMax() != null) {
            validator.setMax(validatorRequest.getMax());
        }
        if (validatorRequest.getMin() != null) {
            validator.setMin(validatorRequest.getMin());
        }
        return validator;
    }

    /**
     * Method to build response.
     *
     * @param validationDTO InputValidationConfiguration.
     * @return ValidationConfigModal.
     */
    private ValidationConfigModal buildResponse(InputValidationConfiguration validationDTO) {

        ValidationConfigModal response = new ValidationConfigModal();
        PasswordValidationModal pswValModal = new PasswordValidationModal();
        if (validationDTO.getPasswordValidator().getRulesValidator() != null) {
            RulesValidator validatorDTO = validationDTO.getPasswordValidator().getRulesValidator();
            pswValModal.setRules(buildPasswordRulesResponse(validatorDTO));
        } else if (validationDTO.getPasswordValidator().getRegExValidator() != null) {
            RegExValidator regexDTO = validationDTO.getPasswordValidator().getRegExValidator();
            ValidationRegExModal passwordValidationModalRegEx = new ValidationRegExModal();
            if (StringUtils.isNotEmpty(regexDTO.getJavaRegExPattern())) {
                passwordValidationModalRegEx.setJavaRegExValidator(regexDTO.getJavaRegExPattern());
            }
            if (StringUtils.isNotEmpty(regexDTO.getJsRegExPattern())) {
                passwordValidationModalRegEx.setJsRegExValidator(regexDTO.getJsRegExPattern());
            }
            pswValModal.setRegEx(passwordValidationModalRegEx);
        }
        response.setPassword(pswValModal);

        return response;
    }

    /**
     * Method to build password rules response.
     *
     * @param rulesValidator    Rules validator.
     * @return  ValidationRulesModal.
     */
    private ValidationRulesModal buildPasswordRulesResponse(RulesValidator rulesValidator) {

        ValidationRulesModal validationRulesModal = new ValidationRulesModal();

        // Set length Validator
        if (rulesValidator.getLengthValidator() != null) {
            validationRulesModal.setLengthValidator(buildBasicValidatorResponse(rulesValidator.getLengthValidator()));
        }
        // Set uppercase validator.
        if (rulesValidator.getUpperCaseValidator() != null) {
            validationRulesModal.setUpperCaseValidator(buildBasicValidatorResponse(rulesValidator
                    .getUpperCaseValidator()));
        }
        // Set lowercase validator.
        if (rulesValidator.getLowerCaseValidator() != null) {
            validationRulesModal.setLowercaseValidator(buildBasicValidatorResponse(rulesValidator
                    .getLowerCaseValidator()));
        }
        // Set numerals validator.
        if (rulesValidator.getNumeralsValidator() != null) {
            validationRulesModal.setNumeralsValidator(buildBasicValidatorResponse(rulesValidator
                    .getNumeralsValidator()));
        }
        // Set special character validator.
        if (rulesValidator.getSpecialCharacterValidator() != null) {
            validationRulesModal.setSpecialCharactersValidator(buildBasicValidatorResponse(rulesValidator
                    .getSpecialCharacterValidator()));
        }
        // Set unique character validator.
        if (rulesValidator.getUniqueCharacterValidator() != null) {
            CharacterSequenceValidator uniqueChrValidator = rulesValidator.getUniqueCharacterValidator();
            if (uniqueChrValidator.isEnable()) {
                UniqueCharactersValidatorModal uniqueChrValidatorModal = new UniqueCharactersValidatorModal();
                uniqueChrValidatorModal.setEnabled(true);
                uniqueChrValidatorModal.setCaseSensitive(uniqueChrValidator.isCaseSensitive());
                uniqueChrValidatorModal.setMinUniqueCharacters(
                        ((UniqueCharacterValidator)uniqueChrValidator).getMinUniqueCharacter());
                validationRulesModal.setUniqueCharactersValidator(uniqueChrValidatorModal);
            }
        }
        // Set repeated character validator.
        if (rulesValidator.getRepeatedCharacterValidator() != null) {
            CharacterSequenceValidator repeatedChrValidator = rulesValidator.getRepeatedCharacterValidator();
            if (repeatedChrValidator.isEnable()) {
                RepeatedCharactersValidatorModal repeatedChrValidatorModal = new RepeatedCharactersValidatorModal();
                repeatedChrValidatorModal.setEnabled(true);
                repeatedChrValidatorModal.setCaseSensitive(repeatedChrValidator.isCaseSensitive());
                repeatedChrValidatorModal.setMaxConsecutiveLength(
                        ((RepeatedCharacterValidator)repeatedChrValidator).getMaxConsecutiveLength());
                validationRulesModal.setRepeatedCharactersValidator(repeatedChrValidatorModal);
            }
        }
        return validationRulesModal;
    }

    /**
     * Method to build basic validation object.
     *
     * @param defaultValidator  DefaultValidator.
     * @return BasicValidatorModal.
     */
    private BasicValidatorModal buildBasicValidatorResponse(DefaultValidator defaultValidator) {

        BasicValidatorModal response = new BasicValidatorModal();
        response.setMax(defaultValidator.getMax());
        response.setMin(defaultValidator.getMin());

        return response;
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

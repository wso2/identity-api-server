/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.v1.impl;

import org.wso2.carbon.identity.api.server.input.validation.v1.ValidationRulesApiService;
import org.wso2.carbon.identity.api.server.input.validation.v1.core.ValidationRulesManagementApiService;
import org.wso2.carbon.identity.api.server.input.validation.v1.factories.ValidationRulesManagementApiServiceFactory;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RevertFields;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModelForField;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Implementation of ValidationRulesApiService.
 */
public class ValidationRulesApiServiceImpl implements ValidationRulesApiService {

    private final ValidationRulesManagementApiService validationRulesManagementApiService;

    public ValidationRulesApiServiceImpl() {
    
        try {
            this.validationRulesManagementApiService = ValidationRulesManagementApiServiceFactory
                    .getValidationRulesManagementApiService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ValidationRulesManagementApiService.", e);
        }
    }

    @Override
    public Response getValidationRules() {

        String tenantDomain = getTenantDomainFromContext();
        return Response.ok().entity(validationRulesManagementApiService
                .getValidationConfiguration(tenantDomain)).build();
    }

    /**
     * Method to get configured validation rules for a field.
     *
     * @param field   Field that validation configurations need to be retrieved.
     * @return  List of configured validation rules for the field.
     */
    @Override
    public Response getValidationRulesForField(String field) {

        String tenantDomain = getTenantDomainFromContext();
        return Response.ok().entity(validationRulesManagementApiService
                .getValidationConfigurationForField(tenantDomain, field)).build();
    }

    @Override
    public Response getValidators() {

        String tenantDomain = getTenantDomainFromContext();
        return Response.ok().entity(validationRulesManagementApiService
                .getValidators(tenantDomain)).build();
    }

    @Override
    public Response updateValidationRules(List<ValidationConfigModel> validationConfigModels) {

        String tenantDomain = getTenantDomainFromContext();
        return Response.ok().entity(validationRulesManagementApiService
                .updateInputValidationConfiguration(validationConfigModels, tenantDomain)).build();
    }

    /**
     * Method to revert validation rules for fields.
     *
     * @param revertFields   List of fields that validation rules need to be reverted.
     * @return  Response indicating the success or failure of the operation.
     */
    @Override
    public Response revertValidationRulesForFields(RevertFields revertFields) {

        String tenantDomain = getTenantDomainFromContext();
        validationRulesManagementApiService.revertInputValidationConfigurationForFields(revertFields.getFields(),
                tenantDomain);
        return Response.ok().build();
    }

    /**
     * Method to update validation rules for a field.
     *
     * @param field                             Field that validations need to be updated.
     * @param validationConfigModelForField     List of validation rules to be updated for the field.
     * @return  Updated validation rules for the field.
     */
    @Override
    public Response updateValidationRulesForField(String field, ValidationConfigModelForField
            validationConfigModelForField) {

        String tenantDomain = getTenantDomainFromContext();
        ValidationConfigModel validationConfigModel = new ValidationConfigModel();
        validationConfigModel.setField(field);
        validationConfigModel.setRules(validationConfigModelForField.getRules());
        validationConfigModel.setRegEx(validationConfigModelForField.getRegEx());
        return Response.ok().entity(validationRulesManagementApiService
                .updateInputValidationConfigurationForField(validationConfigModel, tenantDomain)).build();
    }
}

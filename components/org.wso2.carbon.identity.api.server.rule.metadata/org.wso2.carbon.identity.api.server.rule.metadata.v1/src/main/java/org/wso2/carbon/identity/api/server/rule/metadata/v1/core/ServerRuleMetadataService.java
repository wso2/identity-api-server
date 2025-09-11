/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Field;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Link;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.util.RuleMetadataAPIErrorBuilder;
import org.wso2.carbon.identity.rule.metadata.api.exception.RuleMetadataException;
import org.wso2.carbon.identity.rule.metadata.api.model.FieldDefinition;
import org.wso2.carbon.identity.rule.metadata.api.model.FlowType;
import org.wso2.carbon.identity.rule.metadata.api.model.InputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Operator;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsInputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsReferenceValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Value;
import org.wso2.carbon.identity.rule.metadata.api.service.RuleMetadataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Rule Metadata Service.
 */
public class ServerRuleMetadataService {

    private static final Log LOG = LogFactory.getLog(ServerRuleMetadataService.class);
    private final RuleMetadataService ruleMetadataService;

    public ServerRuleMetadataService(RuleMetadataService ruleMetadataService) {

        this.ruleMetadataService = ruleMetadataService;
    }

    /**
     * Get the expression metadata for the given flow.
     * @param flow Flow type
     * @return List of field definitions
     */
    public List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition> getExpressionMeta(
            String flow) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving expression metadata for flow: " + flow + " in tenant: " + tenantDomain);
        }

        try {
            FlowType flowType = FlowType.valueOfFlowAlias(flow);

            List<FieldDefinition> fieldDefinitions = ruleMetadataService.getExpressionMeta(flowType, tenantDomain);

            List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition>
                    fieldDefinitionResponseList = new ArrayList<>();
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                fieldDefinitionResponseList.add(buildFieldDefinitionResponse(fieldDefinition));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + fieldDefinitionResponseList.size() + 
                        " field definitions for flow: " + flow);
            }

            return fieldDefinitionResponseList;
        } catch (RuleMetadataException e) {
            throw RuleMetadataAPIErrorBuilder.buildAPIError(e);
        }
    }

    private org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition buildFieldDefinitionResponse(
            FieldDefinition fieldDefinition) {

        org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition fieldDefinitionResponse =
                new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition();

        Field field = new Field().name(fieldDefinition.getField().getName())
                .displayName(fieldDefinition.getField().getDisplayName());
        fieldDefinitionResponse.setField(field);

        setOperators(fieldDefinition, fieldDefinitionResponse);
        setValue(fieldDefinition, fieldDefinitionResponse);

        return fieldDefinitionResponse;
    }

    private void setOperators(FieldDefinition fieldDefinition,
                              org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition
                                      fieldDefinitionResponse) {

        List<Operator> operators = fieldDefinition.getOperators();
        for (Operator operator : operators) {
            org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator operatorItem =
                    new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator().name(operator.getName())
                            .displayName(operator.getDisplayName());
            fieldDefinitionResponse.addOperatorsItem(operatorItem);
        }
    }

    private void setValue(FieldDefinition fieldDefinition,
                          org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition
                                  fieldDefinitionResponse) {

        Value value = fieldDefinition.getValue();
        if (value instanceof InputValue) {
            setInputValue((InputValue) value, fieldDefinitionResponse);
        } else if (value instanceof OptionsInputValue) {
            setOptionsInputValue((OptionsInputValue) value, fieldDefinitionResponse);
        } else if (value instanceof OptionsReferenceValue) {
            setOptionsReferenceValue((OptionsReferenceValue) value, fieldDefinitionResponse);
        }
    }

    private void setInputValue(
            InputValue value,
            org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition fieldDefinitionResponse) {

        org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value valueItem =
                new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value().inputType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.InputTypeEnum.valueOf(
                                        value.getInputType().name()))
                        .valueType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.ValueTypeEnum.valueOf(
                                        value.getValueType().name()));
        fieldDefinitionResponse.value(valueItem);
    }

    private void setOptionsInputValue(OptionsInputValue optionsInputValue,
                                      org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition
                                              fieldDefinitionResponse) {

        org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value valueItem =
                new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value().inputType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.InputTypeEnum.valueOf(
                                        optionsInputValue.getInputType().name()))
                        .valueType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.ValueTypeEnum.valueOf(
                                        optionsInputValue.getValueType().name()));
        for (OptionsValue optionsValue : optionsInputValue.getValues()) {
            org.wso2.carbon.identity.api.server.rule.metadata.v1.model.ValueObject valueObjectItem =
                    new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.ValueObject()
                            .displayName(optionsValue.getDisplayName()).name(optionsValue.getName());
            valueItem.addValuesItem(valueObjectItem);
        }
        fieldDefinitionResponse.value(valueItem);
    }

    private void setOptionsReferenceValue(OptionsReferenceValue optionsReferenceValue,
                                          org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition
                                                  fieldDefinitionResponse) {

        org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value valueItem =
                new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value().inputType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.InputTypeEnum.valueOf(
                                        optionsReferenceValue.getInputType().name()))
                        .valueType(
                                org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value.ValueTypeEnum.valueOf(
                                        optionsReferenceValue.getValueType().name()))
                        .valueDisplayAttribute(optionsReferenceValue.getValueDisplayAttribute())
                        .valueReferenceAttribute(optionsReferenceValue.getValueReferenceAttribute());
        for (org.wso2.carbon.identity.rule.metadata.api.model.Link link : optionsReferenceValue.getLinks()) {
            org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Link linkItem =
                    new org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Link().href(link.getHref())
                            .rel(Link.RelEnum.fromValue(link.getRel()))
                            .method(Link.MethodEnum.fromValue(link.getMethod()));
            valueItem.addLinksItem(linkItem);
        }
        fieldDefinitionResponse.value(valueItem);
    }
}

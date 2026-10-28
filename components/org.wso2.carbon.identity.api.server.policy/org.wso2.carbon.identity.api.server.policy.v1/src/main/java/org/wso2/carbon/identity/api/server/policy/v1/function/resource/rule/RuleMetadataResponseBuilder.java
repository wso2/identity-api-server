/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.policy.v1.function.resource.rule;

import org.wso2.carbon.identity.api.server.policy.v1.model.RuleField;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldMetadata;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValue;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValueLink;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValueOption;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleOperator;
import org.wso2.carbon.identity.rule.metadata.api.model.FieldDefinition;
import org.wso2.carbon.identity.rule.metadata.api.model.InputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Link;
import org.wso2.carbon.identity.rule.metadata.api.model.Operator;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsInputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsReferenceValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds the rule metadata response of the Policy API from rule field definitions.
 */
final class RuleMetadataResponseBuilder {

    private RuleMetadataResponseBuilder() {

    }

    /**
     * Build the rule metadata response.
     *
     * @param fieldDefinitions Rule field definitions.
     * @return Rule field metadata.
     */
    static List<RuleFieldMetadata> buildRuleMetadataResponse(List<FieldDefinition> fieldDefinitions) {

        return fieldDefinitions.stream()
                .map(RuleMetadataResponseBuilder::buildRuleFieldMetadata)
                .collect(Collectors.toList());
    }

    private static RuleFieldMetadata buildRuleFieldMetadata(FieldDefinition fieldDefinition) {

        RuleFieldMetadata fieldMetadata = new RuleFieldMetadata()
                .field(new RuleField()
                        .name(fieldDefinition.getField().getName())
                        .displayName(fieldDefinition.getField().getDisplayName()));
        for (Operator operator : fieldDefinition.getOperators()) {
            fieldMetadata.addOperatorsItem(new RuleOperator()
                    .name(operator.getName())
                    .displayName(operator.getDisplayName()));
        }
        fieldMetadata.setValue(buildRuleFieldValue(fieldDefinition.getValue()));
        return fieldMetadata;
    }

    private static RuleFieldValue.ValueTypeEnum toValueType(Value.ValueType valueType) {

        // The API names the date time value type 'date'; every other type maps by name.
        if (valueType == Value.ValueType.DATE_TIME) {
            return RuleFieldValue.ValueTypeEnum.DATE;
        }
        return RuleFieldValue.ValueTypeEnum.valueOf(valueType.name());
    }

    private static RuleFieldValue buildRuleFieldValue(Value value) {

        if (value == null) {
            return null;
        }
        RuleFieldValue fieldValue = new RuleFieldValue()
                .inputType(RuleFieldValue.InputTypeEnum.valueOf(value.getInputType().name()))
                .valueType(toValueType(value.getValueType()));
        if (value instanceof OptionsInputValue) {
            for (OptionsValue optionsValue : ((OptionsInputValue) value).getValues()) {
                fieldValue.addValuesItem(new RuleFieldValueOption()
                        .name(optionsValue.getName())
                        .displayName(optionsValue.getDisplayName()));
            }
        } else if (value instanceof OptionsReferenceValue) {
            OptionsReferenceValue referenceValue = (OptionsReferenceValue) value;
            fieldValue.valueReferenceAttribute(referenceValue.getValueReferenceAttribute())
                    .valueDisplayAttribute(referenceValue.getValueDisplayAttribute());
            for (Link link : referenceValue.getLinks()) {
                fieldValue.addLinksItem(new RuleFieldValueLink()
                        .href(link.getHref())
                        .method(RuleFieldValueLink.MethodEnum.fromValue(link.getMethod()))
                        .rel(RuleFieldValueLink.RelEnum.fromValue(link.getRel())));
            }
        } else if (!(value instanceof InputValue)) {
            throw new IllegalStateException("Unsupported rule field value: " + value.getClass().getName());
        }
        return fieldValue;
    }
}

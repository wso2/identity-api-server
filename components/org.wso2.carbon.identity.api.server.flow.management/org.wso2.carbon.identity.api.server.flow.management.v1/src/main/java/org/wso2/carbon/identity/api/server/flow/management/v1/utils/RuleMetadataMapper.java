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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.wso2.carbon.identity.api.server.flow.management.v1.RuleField;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleFieldDefinition;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleLink;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleOperator;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValue;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValueDefinition;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValueFieldDefinition;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValueOption;
import org.wso2.carbon.identity.rule.metadata.api.model.Field;
import org.wso2.carbon.identity.rule.metadata.api.model.FieldDefinition;
import org.wso2.carbon.identity.rule.metadata.api.model.Link;
import org.wso2.carbon.identity.rule.metadata.api.model.Operator;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsInputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsReferenceValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Value;

import java.util.ArrayList;

/**
 * Maps the rule metadata the framework publishes for a flow to the condition metadata of the flow API.
 */
public class RuleMetadataMapper {

    private RuleMetadataMapper() {

    }

    /**
     * Map a field a condition can be written against.
     *
     * @param definition Field definition from the rule metadata service.
     * @return The field definition as the flow API shows it.
     */
    public static RuleFieldDefinition toRuleFieldDefinition(FieldDefinition definition) {

        Field field = definition.getField();
        RuleFieldDefinition ruleField = new RuleFieldDefinition()
                .field(new RuleField()
                        .name(field.getName())
                        .displayName(field.getDisplayName())
                        .qualifier(toRuleValue(field.getQualifier())));
        for (Operator operator : definition.getOperators()) {
            ruleField.addOperatorsItem(new RuleOperator()
                    .name(operator.getName())
                    .displayName(operator.getDisplayName())
                    .valueInput(RuleOperator.ValueInputEnum.fromValue(operator.getValueInput().getValue())));
        }

        RuleValueDefinition value = new RuleValueDefinition().literal(toRuleValue(definition.getValue()));
        if (definition.getValueFieldOptions() != null && !definition.getValueFieldOptions().getNames().isEmpty()) {
            value.field(new RuleValueFieldDefinition()
                    .names(new ArrayList<>(definition.getValueFieldOptions().getNames())));
        }
        return ruleField.value(value);
    }

    /**
     * Map how a value, or a field's qualifier, is entered.
     *
     * @param value Value metadata, possibly null.
     * @return The value as the flow API shows it, or null when there is none.
     */
    private static RuleValue toRuleValue(Value value) {

        if (value == null) {
            return null;
        }
        RuleValue ruleValue = new RuleValue()
                .inputType(RuleValue.InputTypeEnum.valueOf(value.getInputType().name()))
                .valueType(toValueType(value.getValueType()));
        if (value instanceof OptionsInputValue) {
            for (OptionsValue option : ((OptionsInputValue) value).getValues()) {
                ruleValue.addValuesItem(new RuleValueOption().name(option.getName())
                        .displayName(option.getDisplayName()));
            }
        } else if (value instanceof OptionsReferenceValue) {
            OptionsReferenceValue reference = (OptionsReferenceValue) value;
            ruleValue.valueReferenceAttribute(reference.getValueReferenceAttribute())
                    .valueDisplayAttribute(reference.getValueDisplayAttribute());
            for (Link link : reference.getLinks()) {
                ruleValue.addLinksItem(new RuleLink().href(link.getHref())
                        .method(RuleLink.MethodEnum.fromValue(link.getMethod()))
                        .rel(RuleLink.RelEnum.fromValue(link.getRel())));
            }
        }
        return ruleValue;
    }

    /**
     * Map a value type of the rule metadata to the API. The rule metadata calls a date value DATE_TIME, and the API
     * calls it date.
     *
     * @param valueType Value type in the rule metadata.
     * @return The value type in the API.
     */
    private static RuleValue.ValueTypeEnum toValueType(Value.ValueType valueType) {

        return valueType == Value.ValueType.DATE_TIME
                ? RuleValue.ValueTypeEnum.DATE
                : RuleValue.ValueTypeEnum.valueOf(valueType.name());
    }
}

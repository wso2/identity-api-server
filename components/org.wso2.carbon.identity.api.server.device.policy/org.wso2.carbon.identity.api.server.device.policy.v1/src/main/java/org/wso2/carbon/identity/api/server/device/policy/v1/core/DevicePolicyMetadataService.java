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

package org.wso2.carbon.identity.api.server.device.policy.v1.core;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.device.policy.common.Constants;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyField;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyFieldDefinition;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyLink;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyOperator;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyValue;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyValueObject;
import org.wso2.carbon.identity.api.server.device.policy.v1.util.DevicePolicyAPIErrorBuilder;
import org.wso2.carbon.identity.device.policy.api.service.DeviceFieldMetadataService;
import org.wso2.carbon.identity.rule.metadata.api.exception.RuleMetadataException;
import org.wso2.carbon.identity.rule.metadata.api.model.FieldDefinition;
import org.wso2.carbon.identity.rule.metadata.api.model.FlowType;
import org.wso2.carbon.identity.rule.metadata.api.model.Link;
import org.wso2.carbon.identity.rule.metadata.api.model.Operator;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsInputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsReferenceValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsValue;
import org.wso2.carbon.identity.rule.metadata.api.model.Value;
import org.wso2.carbon.identity.rule.metadata.api.service.RuleMetadataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for the Device Policy API — serves device policy field metadata,
 * optionally filtered by platform.
 */
public class DevicePolicyMetadataService {

    private static final Set<String> SUPPORTED_PLATFORMS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("android", "ios", "macos", "windows")));

    private final RuleMetadataService ruleMetadataService;
    private final DeviceFieldMetadataService deviceFieldMetadataService;

    public DevicePolicyMetadataService(RuleMetadataService ruleMetadataService,
                                       DeviceFieldMetadataService deviceFieldMetadataService) {

        this.ruleMetadataService = ruleMetadataService;
        this.deviceFieldMetadataService = deviceFieldMetadataService;
    }

    /**
     * Returns device policy field metadata, optionally filtered to a specific platform.
     * Retrieves field definitions from the rule metadata service and applies the platform
     * filter based on the device field applicable-platforms mapping.
     *
     * @param platform Platform to filter by (android/ios/macos/windows); null returns all fields.
     * @return List of device policy field definitions.
     */
    public List<DevicePolicyFieldDefinition> getMetadata(String platform) {

        if (platform != null && !SUPPORTED_PLATFORMS.contains(platform)) {
            throw DevicePolicyAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_PLATFORM);
        }

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<FieldDefinition> allFields =
                    ruleMetadataService.getExpressionMeta(FlowType.DEVICE_POLICY, tenantDomain);

            Map<String, List<String>> applicablePlatforms =
                    deviceFieldMetadataService.getFieldApplicablePlatforms();

            return allFields.stream()
                    .filter(fd -> isApplicable(fd.getField().getName(), platform, applicablePlatforms))
                    .map(this::toApiModel)
                    .collect(Collectors.toList());
        } catch (RuleMetadataException e) {
            throw DevicePolicyAPIErrorBuilder.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_METADATA, e);
        }
    }

    private boolean isApplicable(String fieldName, String platform,
                                 Map<String, List<String>> applicablePlatformsMap) {

        if (platform == null) {
            return true;
        }
        List<String> platforms = applicablePlatformsMap.get(fieldName);
        return platforms == null || platforms.contains(platform);
    }

    private DevicePolicyFieldDefinition toApiModel(FieldDefinition fd) {

        DevicePolicyField field = new DevicePolicyField();
        field.setName(fd.getField().getName());
        field.setDisplayName(fd.getField().getDisplayName());

        List<DevicePolicyOperator> operators = new ArrayList<>();
        for (Operator op : fd.getOperators()) {
            DevicePolicyOperator apiOp = new DevicePolicyOperator();
            apiOp.setName(op.getName());
            apiOp.setDisplayName(op.getDisplayName());
            operators.add(apiOp);
        }

        DevicePolicyValue value = new DevicePolicyValue();
        Value coreValue = fd.getValue();
        value.setInputType(DevicePolicyValue.InputTypeEnum.fromValue(
                coreValue.getInputType().name().toLowerCase()));
        value.setValueType(DevicePolicyValue.ValueTypeEnum.fromValue(
                mapValueType(coreValue.getValueType())));

        if (coreValue instanceof OptionsInputValue) {
            List<DevicePolicyValueObject> values = new ArrayList<>();
            for (OptionsValue ov : ((OptionsInputValue) coreValue).getValues()) {
                DevicePolicyValueObject vo = new DevicePolicyValueObject();
                vo.setName(ov.getName());
                vo.setDisplayName(ov.getDisplayName());
                values.add(vo);
            }
            value.setValues(values);
        } else if (coreValue instanceof OptionsReferenceValue) {
            List<DevicePolicyLink> links = new ArrayList<>();
            for (Link l : ((OptionsReferenceValue) coreValue).getLinks()) {
                DevicePolicyLink link = new DevicePolicyLink();
                link.setHref(l.getHref());
                link.setMethod(DevicePolicyLink.MethodEnum.fromValue(l.getMethod()));
                link.setRel(DevicePolicyLink.RelEnum.fromValue(l.getRel()));
                links.add(link);
            }
            value.setLinks(links);
        }

        DevicePolicyFieldDefinition result = new DevicePolicyFieldDefinition();
        result.setField(field);
        result.setOperators(operators);
        result.setValue(value);
        return result;
    }

    private String mapValueType(Value.ValueType valueType) {

        if (valueType == Value.ValueType.DATE_TIME) {
            return "date";
        }
        return valueType.name().toLowerCase();
    }
}

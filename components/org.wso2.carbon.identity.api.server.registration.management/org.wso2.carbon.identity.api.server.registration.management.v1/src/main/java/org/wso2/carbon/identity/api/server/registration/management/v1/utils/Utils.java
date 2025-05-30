/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.registration.management.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.registration.management.v1.Action;
import org.wso2.carbon.identity.api.server.registration.management.v1.Component;
import org.wso2.carbon.identity.api.server.registration.management.v1.Data;
import org.wso2.carbon.identity.api.server.registration.management.v1.Executor;
import org.wso2.carbon.identity.api.server.registration.management.v1.Position;
import org.wso2.carbon.identity.api.server.registration.management.v1.Size;
import org.wso2.carbon.identity.api.server.registration.management.v1.Step;
import org.wso2.carbon.identity.api.server.registration.management.v1.constants.RegistrationFlowEndpointConstants;
import org.wso2.carbon.identity.user.registration.mgt.exception.RegistrationClientException;
import org.wso2.carbon.identity.user.registration.mgt.exception.RegistrationFrameworkException;
import org.wso2.carbon.identity.user.registration.mgt.model.ActionDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.DataDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ExecutorDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.StepDTO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.registration.management.v1.constants.RegistrationFlowEndpointConstants.Schema.IDP_NAME;

/**
 * Utility class for registration flow management.
 */
public class Utils {

    private static final Log LOG = LogFactory.getLog(Utils.class);

    /**
     * Handles exceptions and returns an APIError object.
     *
     * @param status      Response status.
     * @param errorCode   Error code.
     * @param message     Error message.
     * @param description Error description.
     * @return APIError object.
     */
    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    /**
     * Handles registration exceptions and returns an APIError object.
     *
     * @param e RegistrationFrameworkException object.
     * @return APIError object.
     */
    public static APIError handleRegistrationException(RegistrationFrameworkException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof RegistrationClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                RegistrationFlowEndpointConstants.REGISTRATION_FLOW_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return A generic error with the specified details.
     */
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    /**
     * Converts a StepDTO object to a Step object.
     *
     * @param stepDTO StepDTO object.
     * @return Step object.
     */
    public static Step convertToStep(StepDTO stepDTO) {

        return new Step()
                .id(stepDTO.getId())
                .type(stepDTO.getType())
                .size(new Size()
                        .height(BigDecimal.valueOf(stepDTO.getHeight()))
                        .width(BigDecimal.valueOf(stepDTO.getWidth()))
                )
                .position(new Position()
                        .x(BigDecimal.valueOf(stepDTO.getCoordinateX()))
                        .y(BigDecimal.valueOf(stepDTO.getCoordinateY()))
                ).data(convertToData(stepDTO.getData())
                );
    }

    private static Data convertToData(DataDTO dataDTO) {

        return new Data()
                .components((dataDTO == null || dataDTO.getComponents().isEmpty()) ? null :
                        dataDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()))
                .action(convertToAction(dataDTO.getAction()));
    }

    private static Component convertToComponent(ComponentDTO componentDTO) {

        if (componentDTO == null) {
            return null;
        }

        return new Component()
                .id(componentDTO.getId())
                .category(componentDTO.getCategory())
                .type(componentDTO.getType())
                .variant(componentDTO.getVariant())
                .config(convertToMap(componentDTO.getConfigs()))
                .action(convertToAction(componentDTO.getAction()))
                .components(componentDTO.getComponents() != null ? componentDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()) : null);
    }

    private static Action convertToAction(ActionDTO actionDTO) {

        if (actionDTO == null) {
            return null;
        }
        return new Action()
                .type(actionDTO.getType())
                .next(actionDTO.getNextId())
                .executor(convertToExecutor(actionDTO.getExecutor()));
    }

    private static Executor convertToExecutor(ExecutorDTO executorDTO) {

        if (executorDTO == null) {
            return null;
        }

        Executor executor = new Executor()
                .name(executorDTO.getName());

        if (executorDTO.getIdpName() != null) {
            Map<String, String> meta = new java.util.HashMap<>();
            meta.put(IDP_NAME, executorDTO.getIdpName());
            executor.meta(meta);
        }
        return executor;
    }

    /**
     * Converts a Step object to a StepDTO object.
     *
     * @param step Step object.
     * @return StepDTO object.
     */
    public static StepDTO convertToStepDTO(Step step) {

        return new StepDTO.Builder()
                .id(step.getId())
                .type(step.getType())
                .coordinateX(step.getPosition().getX().doubleValue())
                .coordinateY(step.getPosition().getY().doubleValue())
                .width(step.getSize().getWidth().doubleValue())
                .height(step.getSize().getHeight().doubleValue())
                .data(convertToDataDTO(step.getType(), step.getData()))
                .build();
    }

    private static DataDTO convertToDataDTO(String type, Data data) {

        DataDTO.Builder dataDTOBuilder = new DataDTO.Builder();
        if (data.getComponents() != null && !data.getComponents().isEmpty()) {
            dataDTOBuilder.components(data.getComponents().stream()
                    .map(Utils::convertToComponentDTO)
                    .collect(Collectors.toList()));
        }
        if (data.getAction() != null) {
            dataDTOBuilder.action(convertToActionDTO(data.getAction()));
        }
        return dataDTOBuilder.build();
    }

    private static ComponentDTO convertToComponentDTO(Component component) {

        if (component == null) {
            return null;
        }

        return new ComponentDTO.Builder()
                .id(component.getId())
                .category(component.getCategory())
                .type(component.getType())
                .variant(component.getVariant())
                .configs(convertToMap(component.getConfig()))
                .action(convertToActionDTO(component.getAction()))
                .components(component.getComponents() != null ? component.getComponents().stream()
                        .map(Utils::convertToComponentDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private static ActionDTO convertToActionDTO(Action action) {

        if (action == null) {
            return null;
        }

        return new ActionDTO.Builder()
                .type(action.getType())
                .nextId(action.getNext())
                .executor(convertToExecutorDTO(action.getExecutor()))
                .build();
    }

    private static ExecutorDTO convertToExecutorDTO(Executor executor) {

        if (executor == null) {
            return null;
        }

        ExecutorDTO executorDTO = new ExecutorDTO.Builder().name(executor.getName()).build();
        Map<String, Object> meta = convertToMap(executor.getMeta());
        if (meta != null && !meta.isEmpty() && meta.containsKey(IDP_NAME)) {
            executorDTO.setIdpName(String.valueOf(meta.get(IDP_NAME)));
        }
        return executorDTO;
    }

    private static Map<String, Object> convertToMap(Object map) {

        if (map == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, new TypeReference<Map<String, Object>>() {
        });
    }
}

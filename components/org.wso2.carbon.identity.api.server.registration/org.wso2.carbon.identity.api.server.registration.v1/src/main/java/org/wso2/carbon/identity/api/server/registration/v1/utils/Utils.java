package org.wso2.carbon.identity.api.server.registration.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wso2.carbon.identity.api.server.registration.v1.Action;
import org.wso2.carbon.identity.api.server.registration.v1.Block;
import org.wso2.carbon.identity.api.server.registration.v1.Element;
import org.wso2.carbon.identity.api.server.registration.v1.Executor;
import org.wso2.carbon.identity.api.server.registration.v1.Position;
import org.wso2.carbon.identity.api.server.registration.v1.Size;
import org.wso2.carbon.identity.api.server.registration.v1.Step;
import org.wso2.carbon.identity.api.server.registration.v1.constants.RegistrationFlowEndpointConstants;
import org.wso2.carbon.identity.user.registration.mgt.model.ActionDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.BlockDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ExecutorDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.StepDTO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

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
                )
                .blocks(stepDTO.getBlocks().stream().map(Utils::convertToBlock).collect(Collectors.toList()));
    }

    private static Block convertToBlock(BlockDTO blockDTO) {

        return new Block()
                .id(blockDTO.getId())
                .elements(blockDTO.getComponents().stream().map(Utils::convertToElement).collect(Collectors.toList()));
    }

    private static Element convertToElement(ComponentDTO elementDTO) {

        return new Element()
                .id(elementDTO.getId())
                .category(elementDTO.getCategory())
                .type(elementDTO.getType())
                .variant(elementDTO.getIdentifier())
                .config(convertToMap(elementDTO.getProperties()))
                .action(convertToAction(elementDTO.getAction()));
    }

    private static Action convertToAction(ActionDTO actionDTO) {

        return new Action()
                .type(Action.TypeEnum.valueOf(actionDTO.getType()))
                .next(actionDTO.getNextId())
                .executor(convertToExecutor(actionDTO.getExecutor()));
    }

    private static Executor convertToExecutor(ExecutorDTO executorDTO) {

        return new Executor()
                .name(executorDTO.getName())
                .meta(convertToMap(executorDTO.getIdpName()));
    }

    public static StepDTO convertToStepDTO(Step step) {

        return new StepDTO.Builder()
                .id(step.getId())
                .type(step.getType())
                .coordinateX(step.getPosition().getX().doubleValue())
                .coordinateY(step.getPosition().getY().doubleValue())
                .width(step.getSize().getWidth().doubleValue())
                .height(step.getSize().getHeight().doubleValue())
                .blocks(step.getBlocks().stream().map(Utils::convertToBlockDTO).collect(Collectors.toList()))
                .build();
    }

    private static BlockDTO convertToBlockDTO(Block block) {

        if (block == null) {
            return null;
        }
        return new BlockDTO.Builder()
                .id(block.getId())
                .components(block.getElements().stream().map(Utils::convertToComponentDTO).collect(Collectors.toList()))
                .build();
    }

    private static ComponentDTO convertToComponentDTO(Element element) {

        if (element == null) {
            return null;
        }

        return new ComponentDTO.Builder()
                .id(element.getId())
                .category(element.getCategory())
                .type(element.getType())
                .identifier(element.getVariant())
                .properties(convertToMap(element.getConfig()))
                .action(convertToActionDTO(element.getAction()))
                .build();
    }

    private static ActionDTO convertToActionDTO(Action action) {

        if (action == null) {
            return null;
        }

        return new ActionDTO.Builder()
                .setType(action.getType().toString())
                .setNextId(action.getNext())
                .setExecutor(convertToExecutorDTO(action.getExecutor()))
                .build();
    }

    private static ExecutorDTO convertToExecutorDTO(Executor executor) {

        if (executor == null) {
            return null;
        }

        return new ExecutorDTO.Builder()
                .name(executor.getName())
                .idpName(String.valueOf(convertToMap(executor.getMeta())
                        .get(RegistrationFlowEndpointConstants.Schema.IDP_NAME)))
                .build();
    }

    private static Map<String, Object> convertToMap(Object map) {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, new TypeReference<>() {
        });
    }
}

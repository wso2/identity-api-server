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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.registration.management.v1.Action;
import org.wso2.carbon.identity.api.server.registration.management.v1.Component;
import org.wso2.carbon.identity.api.server.registration.management.v1.Data;
import org.wso2.carbon.identity.api.server.registration.management.v1.Executor;
import org.wso2.carbon.identity.api.server.registration.management.v1.Position;
import org.wso2.carbon.identity.api.server.registration.management.v1.Size;
import org.wso2.carbon.identity.api.server.registration.management.v1.Step;
import org.wso2.carbon.identity.user.registration.mgt.model.ActionDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.DataDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.ExecutorDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.StepDTO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Test class for Utils.
 */
public class UtilsTest {

    @BeforeClass
    public void setupClass() {

    }

    @AfterClass
    public void tearDownClass() {

    }

    private StepDTO buildComponentStepDTO() {

        StepDTO step1 = new StepDTO.Builder().id("step1").type("step1").build();
        ComponentDTO component1 = new ComponentDTO();
        component1.setId("component1");
        component1.setType("component1");
        component1.setCategory("category1");
        component1.setIdentifier("identifier1");
        component1.addConfig("key1", "value1");
        ComponentDTO component2 = new ComponentDTO();
        component2.setId("component2");
        component2.setType("component2");
        component2.setCategory("category2");
        component2.setIdentifier("identifier2");
        component1.setComponents(Collections.singletonList(component2));
        List<ComponentDTO> components = new java.util.ArrayList<>();
        components.add(component1);
        DataDTO dataDTO = new DataDTO.Builder().components(components).build();
        step1.setData(dataDTO);
        return step1;
    }

    private StepDTO buildActionStepDTO() {

        StepDTO step1 = new StepDTO.Builder().id("step2").type("step2").build();
        ActionDTO action1 = new ActionDTO();
        action1.setType("action1");
        ExecutorDTO executor1 = new ExecutorDTO();
        executor1.setName("executor1");
        action1.setExecutor(executor1);
        action1.setNextId("nextId1");
        DataDTO dataDTO = new DataDTO.Builder()
                .action(action1).build();
        step1.setData(dataDTO);
        return step1;
    }

    @Test
    public void testConvertToStep() {

        Step componentStep = Utils.convertToStep(buildComponentStepDTO());
        Step actionStep = Utils.convertToStep(buildActionStepDTO());
        assertEquals(componentStep.getId(), "step1");
        assertEquals(componentStep.getType(), "step1");
        List<Component> components = componentStep.getData().getComponents();
        assertEquals(components.size(), 1);
        assertEquals(components.get(0).getId(), "component1");
        assertEquals(components.get(0).getComponents().size(), 1);
        List<Component> subComponents = components.get(0).getComponents();
        assertEquals(subComponents.get(0).getId(), "component2");

        assertEquals(actionStep.getId(), "step2");
        assertEquals(actionStep.getType(), "step2");
        assertEquals(actionStep.getData().getAction().getType(), "action1");
        assertEquals(actionStep.getData().getAction().getNext(), "nextId1");
    }

    private Step buildComponentStep() {

        List<Component> components = new java.util.ArrayList<>();
        Map<String, String> config = new HashMap<>();
        config.put("key1", "value1");
        components.add(new Component()
                .id("component1")
                .category("category1")
                .type("component1")
                .variant("identifier1")
                .components(Collections.singletonList(new Component()
                        .id("component2")
                        .category("category2")
                        .type("component2")
                        .variant("identifier2")))
                .config(config));
        return new Step()
                .id("step1")
                .type("step1")
                .size(new Size()
                        .height(BigDecimal.valueOf(10.0))
                        .width(BigDecimal.valueOf(10.0)))
                .position(new Position()
                        .x(BigDecimal.valueOf(10.0))
                        .y(BigDecimal.valueOf(10.0)))
                .data(new Data().components(components));
    }

    private Step buildActionStep() {

        return new Step()
                .id("step2")
                .type("step2")
                .size(new Size()
                        .height(BigDecimal.valueOf(10.0))
                        .width(BigDecimal.valueOf(10.0)))
                .position(new Position()
                        .x(BigDecimal.valueOf(10.0))
                        .y(BigDecimal.valueOf(10.0)))
                .data(new Data().action(
                        new Action()
                                .type("action1")
                                .next("nextId1")
                                .executor(new Executor()
                                        .name("executor1")
                                        .meta(new HashMap<>())
                                )
                ));
    }

    @Test
    public void testConvertToStepDTO() {

        StepDTO componentStepDTO = Utils.convertToStepDTO(buildComponentStep());
        StepDTO actionStepDTO = Utils.convertToStepDTO(buildActionStep());
        assertEquals(componentStepDTO.getId(), "step1");
        assertEquals(componentStepDTO.getType(), "step1");
        List<ComponentDTO> components = componentStepDTO.getData().getComponents();
        assertEquals(components.size(), 1);
        assertEquals(components.get(0).getId(), "component1");
        List<ComponentDTO> subComponents = components.get(0).getComponents();
        assertEquals(subComponents.size(), 1);
        assertEquals(subComponents.get(0).getId(), "component2");

        assertEquals(actionStepDTO.getId(), "step2");
        assertEquals(actionStepDTO.getType(), "step2");
        assertEquals(actionStepDTO.getData().getAction().getType(), "action1");
        assertEquals(actionStepDTO.getData().getAction().getNextId(), "nextId1");
    }
}

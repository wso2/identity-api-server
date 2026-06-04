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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.flow.execution.v1.utils;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.flow.execution.v1.Data;
import org.wso2.carbon.identity.flow.mgt.Constants;
import org.wso2.carbon.identity.flow.mgt.model.DataDTO;
import org.wso2.carbon.identity.flow.mgt.model.Message;

import java.util.Collections;

/**
 * Unit tests for {@link Utils}.
 */
public class UtilsTest {

    @Test
    public void testConvertToDataMapsMessagesAndKeepsAdditionalData() {

        Message message = new Message();
        message.setType(Message.MessageType.ERROR);
        message.setMessage("User does not exist");
        message.setI18nKey("error.invalid.identifier");

        DataDTO dataDTO = new DataDTO();
        dataDTO.addAdditionalData("error", "The provided identifier is invalid.");
        dataDTO.setMessages(Collections.singletonList(message));

        Data data = Utils.convertToData(dataDTO, Constants.StepTypes.VIEW);

        Assert.assertNotNull(data);

        // additionalData must remain mapped unchanged for backward compatibility.
        Assert.assertNotNull(data.getAdditionalData());
        Assert.assertEquals(data.getAdditionalData().get("error"), "The provided identifier is invalid.");

        // messages list must be surfaced with type, message and i18nKey populated.
        Assert.assertNotNull(data.getMessages());
        Assert.assertEquals(data.getMessages().size(), 1);
        org.wso2.carbon.identity.api.server.flow.execution.v1.Message mappedMessage = data.getMessages().get(0);
        Assert.assertEquals(mappedMessage.getType(),
                org.wso2.carbon.identity.api.server.flow.execution.v1.Message.TypeEnum.ERROR);
        Assert.assertEquals(mappedMessage.getMessage(), "User does not exist");
        Assert.assertEquals(mappedMessage.getI18nKey(), "error.invalid.identifier");
    }

    @Test
    public void testConvertToDataMapsInfoAndWarningMessageTypes() {

        Message infoMessage = new Message();
        infoMessage.setType(Message.MessageType.INFO);
        infoMessage.setMessage("Your password will expire soon");

        Message warningMessage = new Message();
        warningMessage.setType(Message.MessageType.WARNING);
        warningMessage.setMessage("Account locked after further attempts");

        DataDTO dataDTO = new DataDTO();
        dataDTO.setMessages(java.util.Arrays.asList(infoMessage, warningMessage));

        Data data = Utils.convertToData(dataDTO, Constants.StepTypes.VIEW);

        Assert.assertNotNull(data.getMessages());
        Assert.assertEquals(data.getMessages().size(), 2);
        Assert.assertEquals(data.getMessages().get(0).getType(),
                org.wso2.carbon.identity.api.server.flow.execution.v1.Message.TypeEnum.INFO);
        Assert.assertEquals(data.getMessages().get(1).getType(),
                org.wso2.carbon.identity.api.server.flow.execution.v1.Message.TypeEnum.WARNING);
    }

    @Test
    public void testConvertToDataWithNullMessagesLeavesMessagesUnset() {

        DataDTO dataDTO = new DataDTO();
        dataDTO.addAdditionalData("error", "The provided identifier is invalid.");

        Data data = Utils.convertToData(dataDTO, Constants.StepTypes.VIEW);

        Assert.assertNotNull(data);
        Assert.assertNull(data.getMessages());
        Assert.assertEquals(data.getAdditionalData().get("error"), "The provided identifier is invalid.");
    }
}

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

package org.wso2.carbon.identity.api.server.user.credential.management.v1.core;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialHandler;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.impl.UserCredentialManagementServiceImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for UserCredentialManagementServiceImpl.
 */
public class UserCredentialManagementServiceImplTest {

    @Mock
    private UserCredentialHandler passkeyHandler;

    @Mock
    private UserCredentialHandler pushAuthHandler;

    @Mock
    private UserCredentialManagementServiceImpl userCredentialManagementService;

    private static final String TEST_USER_ID = "test-user-123";

    @BeforeMethod
    public void setUp() throws Exception {

        MockitoAnnotations.openMocks(this);

        // Inject the mocked handlers into the mocked service instance via reflection.
        Map<CredentialTypes, UserCredentialHandler> handlerMap = new EnumMap<>(CredentialTypes.class);
        handlerMap.put(CredentialTypes.PASSKEY, passkeyHandler);
        handlerMap.put(CredentialTypes.PUSH_AUTH, pushAuthHandler);
        Field handlerMapField = UserCredentialManagementServiceImpl.class.getDeclaredField("handlerMap");
        handlerMapField.setAccessible(true);
        handlerMapField.set(userCredentialManagementService, handlerMap);
    }

    /**
     * Test getCredentialsForUser when both handlers return credentials successfully.
     */
    @Test
    public void testGetCredentialsForUser() throws CredentialMgtException {

        UserCredentialDTO passkeyCredential = createMockCredential("passkey-1", "passkey", "Test Passkey");
        UserCredentialDTO pushCredential = createMockCredential("push-1", "push-auth", "Test Push Device");

        when(passkeyHandler.getCredentialsForUser(TEST_USER_ID))
                .thenReturn(Collections.singletonList(passkeyCredential));
        when(pushAuthHandler.getCredentialsForUser(TEST_USER_ID)).thenReturn(Collections.singletonList(pushCredential));
        doCallRealMethod().when(userCredentialManagementService).getCredentialsForUser(TEST_USER_ID);

        List<UserCredentialDTO> result = userCredentialManagementService.getCredentialsForUser(TEST_USER_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 2);
        Assert.assertTrue(result.contains(passkeyCredential));
        Assert.assertTrue(result.contains(pushCredential));

        verify(passkeyHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
        verify(pushAuthHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
    }

    /**
     * Test getCredentialsForUser when both handlers return empty lists.
     */
    @Test
    public void testGetCredentialsForUserEmptyResult() throws CredentialMgtException {

        when(passkeyHandler.getCredentialsForUser(TEST_USER_ID)).thenReturn(new ArrayList<>());
        when(pushAuthHandler.getCredentialsForUser(TEST_USER_ID)).thenReturn(new ArrayList<>());
        doCallRealMethod().when(userCredentialManagementService).getCredentialsForUser(TEST_USER_ID);

        List<UserCredentialDTO> result = userCredentialManagementService.getCredentialsForUser(TEST_USER_ID);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
        verify(passkeyHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
        verify(pushAuthHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
    }

    /**
     * Test getCredentialsForUser when one handler returns credentials and the other returns empty.
     */
    @Test
    public void testGetCredentialsSingleHandlerReturnsCredentials() throws CredentialMgtException {

        UserCredentialDTO passkeyCredential = createMockCredential("passkey-1", "passkey", "Test Passkey");

        when(passkeyHandler.getCredentialsForUser(TEST_USER_ID))
                .thenReturn(Collections.singletonList(passkeyCredential));
        when(pushAuthHandler.getCredentialsForUser(TEST_USER_ID)).thenReturn(new ArrayList<>());
        doCallRealMethod().when(userCredentialManagementService).getCredentialsForUser(TEST_USER_ID);

        List<UserCredentialDTO> result = userCredentialManagementService.getCredentialsForUser(TEST_USER_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), passkeyCredential);
        verify(passkeyHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
        verify(pushAuthHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
    }

    /**
     * Test getCredentialsForUser when a handler throws an exception.
     */
    @Test(expectedExceptions = APIError.class)
    public void testGetCredentialsHandlerThrowsException() throws CredentialMgtException {

        CredentialMgtException expectedException = new CredentialMgtException("ERROR", "Passkey handler error",
                "An error occurred in the passkey handler");

        when(passkeyHandler.getCredentialsForUser(TEST_USER_ID)).thenThrow(expectedException);
        doCallRealMethod().when(userCredentialManagementService).getCredentialsForUser(TEST_USER_ID);

        userCredentialManagementService.getCredentialsForUser(TEST_USER_ID);
    }

    /**
     * Test getCredentialsForUser when one handler fails and the other succeeds.
     */
    @Test(expectedExceptions = APIError.class)
    public void testGetCredentialsOneHandlerFailsOtherSucceeds() throws CredentialMgtException {

        UserCredentialDTO pushCredential = createMockCredential("push-1", "push-auth", "Test Push Device");
        CredentialMgtException expectedException = new CredentialMgtException("ERROR", "Passkey handler error",
                "An error occurred in the passkey handler");

        when(passkeyHandler.getCredentialsForUser(TEST_USER_ID)).thenThrow(expectedException);
        when(pushAuthHandler.getCredentialsForUser(TEST_USER_ID)).thenReturn(Collections.singletonList(pushCredential));
        doCallRealMethod().when(userCredentialManagementService).getCredentialsForUser(TEST_USER_ID);

        try {
            userCredentialManagementService.getCredentialsForUser(TEST_USER_ID);
        } finally {
            // Verify the first handler was called, but the second was not due to the exception.
            verify(passkeyHandler, times(1)).getCredentialsForUser(TEST_USER_ID);
            verify(pushAuthHandler, never()).getCredentialsForUser(anyString());
        }
    }

    @DataProvider(name = "validCredentialTypes")
    public Object[][] validCredentialTypes() {

        return new Object[][]{
                {"passkey", CredentialTypes.PASSKEY},
                {"PASSKEY", CredentialTypes.PASSKEY},
                {"push-auth", CredentialTypes.PUSH_AUTH},
                {"PUSH-AUTH", CredentialTypes.PUSH_AUTH},
                {"push_auth", CredentialTypes.PUSH_AUTH},
                {"PUSH_AUTH", CredentialTypes.PUSH_AUTH}
        };
    }

    /**
     * Test deleteCredentialForUser with valid credential types.
     */
    @Test(dataProvider = "validCredentialTypes")
    public void testDeleteCredentialWithValidTypes(String typeInput, CredentialTypes expectedType)
            throws CredentialMgtException {

        String credentialId = "credential-456";
        UserCredentialDeletionRequestDTO userCredentialDeletionRequest =
                createMockDeletionRequest(TEST_USER_ID, typeInput, credentialId);

        doCallRealMethod().when(userCredentialManagementService)
                .deleteCredentialForUser(userCredentialDeletionRequest);

        userCredentialManagementService.deleteCredentialForUser(userCredentialDeletionRequest);

        UserCredentialHandler expectedHandler = expectedType == CredentialTypes.PASSKEY ?
                passkeyHandler : pushAuthHandler;
        verify(expectedHandler, times(1)).deleteCredentialForUser(userCredentialDeletionRequest);
    }

    /**
     * Test deleteCredentialForUser with invalid credential type.
     */
    @Test(expectedExceptions = APIError.class)
    public void testDeleteCredentialWithInvalidCredentialType() throws CredentialMgtException {

        String type = "invalid-type";
        String credentialId = "credential-456";
        UserCredentialDeletionRequestDTO userCredentialDeletionRequest =
                createMockDeletionRequest(TEST_USER_ID, type, credentialId);
        doCallRealMethod().when(userCredentialManagementService).deleteCredentialForUser(userCredentialDeletionRequest);

        userCredentialManagementService.deleteCredentialForUser(userCredentialDeletionRequest);
    }

    /**
     * Test deleteCredentialForUser when the handler throws an exception.
     */
    @Test(expectedExceptions = APIError.class)
    public void testDeleteCredentialHandlerThrowsException() throws CredentialMgtException {

        String type = "passkey";
        String credentialId = "error-credential";
        UserCredentialDeletionRequestDTO userCredentialDeletionRequest =
                createMockDeletionRequest(TEST_USER_ID, type, credentialId);
        CredentialMgtException expectedException = new CredentialMgtException("ERROR", "Passkey handler error",
                "An error occurred in the passkey handler");

        doThrow(expectedException).when(passkeyHandler).deleteCredentialForUser(userCredentialDeletionRequest);
        doCallRealMethod().when(userCredentialManagementService).deleteCredentialForUser(userCredentialDeletionRequest);

        userCredentialManagementService.deleteCredentialForUser(userCredentialDeletionRequest);
    }

    @DataProvider(name = "edgeCaseInputs")
    public Object[][] edgeCaseInputs() {

        return new Object[][]{
                {"  passkey  ", CredentialTypes.PASSKEY},
                {"PassKey", CredentialTypes.PASSKEY}
        };
    }

    /**
     * Helper method to create mock credential DTOs for testing.
     */
    private UserCredentialDTO createMockCredential(String id, String type, String displayName) {

        UserCredentialDTO credential = mock(UserCredentialDTO.class);
        when(credential.getCredentialId()).thenReturn(id);
        when(credential.getType()).thenReturn(type);
        when(credential.getDisplayName()).thenReturn(displayName);
        return credential;
    }

    /**
     * Helper method to create mock deletion request DTOs for testing.
     */
    private UserCredentialDeletionRequestDTO createMockDeletionRequest(String userId, String type,
                                                                       String credentialId) {

        UserCredentialDeletionRequestDTO request = mock(UserCredentialDeletionRequestDTO.class);
        when(request.getUserId()).thenReturn(userId);
        when(request.getType()).thenReturn(type);
        when(request.getCredentialId()).thenReturn(credentialId);
        return request;
    }
}

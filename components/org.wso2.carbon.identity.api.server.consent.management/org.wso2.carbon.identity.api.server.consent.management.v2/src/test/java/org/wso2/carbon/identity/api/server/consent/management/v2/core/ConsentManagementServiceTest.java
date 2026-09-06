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

package org.wso2.carbon.identity.api.server.consent.management.v2.core;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.consent.mgt.core.ConsentManager;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentUpdateRequest;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Unit tests for {@link ConsentManagementService}.
 */
public class ConsentManagementServiceTest {

    @Mock
    private ConsentManager consentManager;

    private ConsentManagementService consentManagementService;

    @BeforeMethod
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        consentManagementService = new ConsentManagementService(consentManager);
    }

    @Test
    public void testCreateConsentWithPastExpiryTime() {

        ConsentCreateRequest request = new ConsentCreateRequest();
        request.setSubjectId("testUser");
        request.setServiceId("admin-dashboard");
        // Timestamp 1 hour in the past.
        request.setExpiryTime(System.currentTimeMillis() - 3600000L);

        try {
            consentManagementService.createConsent(request);
            Assert.fail("Expected APIError was not thrown for expired expiryTime.");
        } catch (APIError e) {
            Assert.assertEquals(e.getStatus(), Response.Status.BAD_REQUEST);
            verifyNoInteractions(consentManager);
        }
    }

    @Test
    public void testUpdateConsentWithPastExpiryTime() {

        ConsentUpdateRequest request = new ConsentUpdateRequest();
        // Timestamp 1 hour in the past.
        request.setExpiryTime(System.currentTimeMillis() - 3600000L);

        try {
            consentManagementService.updateConsent("test-receipt-id", request);
            Assert.fail("Expected APIError was not thrown for expired expiryTime.");
        } catch (APIError e) {
            Assert.assertEquals(e.getStatus(), Response.Status.BAD_REQUEST);
            verifyNoInteractions(consentManager);
        }
    }
}


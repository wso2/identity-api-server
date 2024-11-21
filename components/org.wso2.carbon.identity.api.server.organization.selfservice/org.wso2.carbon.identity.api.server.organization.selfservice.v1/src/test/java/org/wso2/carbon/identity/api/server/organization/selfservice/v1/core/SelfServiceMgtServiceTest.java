/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.core;

import org.mockito.MockedStatic;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.base.CarbonBaseConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.server.organization.selfservice.common.SelfServiceMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyPatchReq;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyReq;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyRes;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.util.SelfServiceMgtConstants;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.bean.ConnectorConfig;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test for SelfServiceMgtService.
 */
public class SelfServiceMgtServiceTest {

    private static final String TENANT_DOMAIN = "abc.com";
    private static final int TENANT_ID = 1;

    private IdentityGovernanceService identityGovernanceService;

    private SelfServiceMgtService selfServiceMgtService;

    @BeforeMethod
    public void setUp() {

        identityGovernanceService = mock(IdentityGovernanceService.class);
        ApplicationManagementService applicationManagementService = mock(ApplicationManagementService.class);
        APIResourceManager apiResourceManager = mock(APIResourceManager.class);
        AuthorizedAPIManagementService authorizedAPIManagementService = mock(AuthorizedAPIManagementService.class);

        // Create SelfServiceMgtService with mock dependencies
        selfServiceMgtService = new SelfServiceMgtService(
                identityGovernanceService,
                applicationManagementService,
                apiResourceManager,
                authorizedAPIManagementService
        );

        mockCarbonContext();
    }

    @Test
    public void testGetOrganizationGovernanceConfigs() throws IdentityGovernanceException {

        ConnectorConfig mockConnectorConfig = new ConnectorConfig();
        mockConnectorConfig.setName(SelfServiceMgtConstants.SELF_SERVICE_GOVERNANCE_CONNECTOR);

        Property property1 = new Property();
        property1.setName(SelfServiceMgtConstants.SELF_SERVICE_ENABLE_PROPERTY_NAME);
        property1.setValue("true");
        property1.setDisplayName("Enable suborganization self service");
        property1.setDescription("Allow user's to self service suborganization to the system.");

        Property property2 = new Property();
        property2.setName("Organization.SelfService.AdminEmailVerification");
        property2.setValue("true");
        property2.setDisplayName("Enable admin email verification");
        property2.setDescription("User gets email verification before proceeding with suborganization creation.");

        // Add properties to ConnectorConfig
        mockConnectorConfig.setProperties(new Property[]{property1, property2});

        when(identityGovernanceService.getConnectorWithConfigs(TENANT_DOMAIN,
                SelfServiceMgtConstants.SELF_SERVICE_GOVERNANCE_CONNECTOR))
                .thenReturn(mockConnectorConfig);

        List<PropertyRes> result;
        try (MockedStatic<SelfServiceMgtServiceHolder> mocked = mockStatic(SelfServiceMgtServiceHolder.class)) {
            mocked.when(SelfServiceMgtServiceHolder::getIdentityGovernanceService)
                    .thenReturn(identityGovernanceService);
            result = selfServiceMgtService.getOrganizationGovernanceConfigs();
        }

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(property1.getName(), result.get(0).getName());
        assertEquals(property1.getValue(), result.get(0).getValue());
        assertEquals(property1.getDisplayName(), result.get(0).getDisplayName());
        assertEquals(property1.getDescription(), result.get(0).getDescription());

        assertEquals(property2.getName(), result.get(1).getName());
        assertEquals(property2.getValue(), result.get(1).getValue());
        assertEquals(property2.getDisplayName(), result.get(1).getDisplayName());
        assertEquals(property2.getDescription(), result.get(1).getDescription());
    }

    @Test
    public void testUpdateOrganizationGovernanceConfigs() throws IdentityGovernanceException {

        PropertyPatchReq mockGovernanceConnector = mock(PropertyPatchReq.class);
        List<PropertyReq> propertyReqs = new ArrayList<>();

        // Update onboardAdminToSubOrg as false.
        PropertyReq property1 = new PropertyReq();
        property1.setName("Organization.SelfService.OnboardAdminToSubOrg");
        property1.setValue("false");

        Property property2 = new Property();
        property1.setName(property1.getName());
        property1.setValue(property1.getValue());

        propertyReqs.add(property1);
        when(mockGovernanceConnector.getProperties()).thenReturn(propertyReqs);

        Map<String, String> configurationDetails = new HashMap<>();
        configurationDetails.put(property1.getName(), property1.getValue());

        when(identityGovernanceService.getConfiguration(any(String[].class), anyString()))
                .thenReturn(new Property[]{property2});

        try (MockedStatic<SelfServiceMgtServiceHolder> mocked = mockStatic(SelfServiceMgtServiceHolder.class)) {
            mocked.when(SelfServiceMgtServiceHolder::getIdentityGovernanceService)
                    .thenReturn(identityGovernanceService);
            selfServiceMgtService.updateOrganizationGovernanceConfigs(mockGovernanceConnector, false);
            verify(identityGovernanceService, times(1))
                    .updateConfiguration(TENANT_DOMAIN, configurationDetails);
        }
    }

    private void mockCarbonContext() {

        String carbonHome = Paths.get(System.getProperty("user.dir"), "target", "test-classes").toString();
        System.setProperty(CarbonBaseConstants.CARBON_HOME, carbonHome);
        System.setProperty(CarbonBaseConstants.CARBON_CONFIG_DIR_PATH, Paths.get(carbonHome,
                "repository/conf").toString());
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(TENANT_DOMAIN);
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(TENANT_ID);
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername("admin");
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setUserId("1234");
    }
}

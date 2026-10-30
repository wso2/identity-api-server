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

package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.mockito.MockedStatic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.ClaimConfig;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProviderProperty;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Unit tests for the subject claim block built by {@link ServiceProviderToApiModel}.
 */
public class ServiceProviderToApiModelTest {

    private static final String USE_USER_ID_FOR_DEFAULT_SUBJECT = "useUserIdForDefaultSubject";
    private static final String EXPLICIT_SUBJECT_CLAIM = "http://wso2.org/claims/emailaddress";

    private ServiceProviderToApiModel model;

    @BeforeClass
    public void setUp() {

        ApplicationManagementService applicationManagementService = mock(ApplicationManagementService.class);
        when(applicationManagementService.getSystemApplications()).thenReturn(Collections.emptySet());
        try (MockedStatic<ApplicationManagementServiceHolder> holder =
                     mockStatic(ApplicationManagementServiceHolder.class)) {
            holder.when(ApplicationManagementServiceHolder::getApplicationManagementService)
                    .thenReturn(applicationManagementService);
            model = new ServiceProviderToApiModel();
        }
    }

    @DataProvider(name = "defaultSubjectClaimData")
    public Object[][] defaultSubjectClaimData() {

        return new Object[][] {
                // exclude config, stored subject claim, useUserIdForDefaultSubject, expected claim in the response
                { null, null, null, FrameworkConstants.USERNAME_CLAIM },
                { null, null, "true", FrameworkConstants.USER_ID_CLAIM },
                { "false", null, null, FrameworkConstants.USERNAME_CLAIM },
                { "false", null, "true", FrameworkConstants.USER_ID_CLAIM },
                { "true", null, null, null },
                { "true", null, "true", null },
                { "true", null, "false", null },
                { null, EXPLICIT_SUBJECT_CLAIM, null, EXPLICIT_SUBJECT_CLAIM },
                { "true", EXPLICIT_SUBJECT_CLAIM, null, EXPLICIT_SUBJECT_CLAIM },
                { "true", EXPLICIT_SUBJECT_CLAIM, "true", EXPLICIT_SUBJECT_CLAIM }
        };
    }

    @Test(dataProvider = "defaultSubjectClaimData")
    public void testBuildSubjectClaimConfig(String excludeDefaultSubjectClaim, String storedSubjectClaim,
                                            String useUserIdForDefaultSubject, String expectedClaim)
            throws Exception {

        ServiceProvider application = buildApplication(storedSubjectClaim, useUserIdForDefaultSubject);
        try (MockedStatic<IdentityUtil> identityUtil = mockStatic(IdentityUtil.class)) {
            identityUtil.when(() -> IdentityUtil.getProperty(
                    ApplicationManagementConstants.EXCLUDE_DEFAULT_SUBJECT_CLAIM))
                    .thenReturn(excludeDefaultSubjectClaim);

            SubjectConfig subjectConfig = buildSubjectClaimConfig(application);

            if (expectedClaim == null) {
                assertNull(subjectConfig.getClaim(), "No subject claim is expected in the response.");
            } else {
                assertEquals(subjectConfig.getClaim().getUri(), expectedClaim);
            }
            assertEquals(subjectConfig.getIncludeTenantDomain(), Boolean.TRUE);
            assertEquals(subjectConfig.getIncludeUserDomain(), Boolean.FALSE);
        }
    }

    private ServiceProvider buildApplication(String storedSubjectClaim, String useUserIdForDefaultSubject) {

        ServiceProvider application = new ServiceProvider();
        application.setApplicationName("test-app");

        ClaimConfig claimConfig = new ClaimConfig();
        claimConfig.setLocalClaimDialect(true);
        application.setClaimConfig(claimConfig);

        LocalAndOutboundAuthenticationConfig authConfig = new LocalAndOutboundAuthenticationConfig();
        authConfig.setSubjectClaimUri(storedSubjectClaim);
        authConfig.setUseTenantDomainInLocalSubjectIdentifier(true);
        authConfig.setUseUserstoreDomainInLocalSubjectIdentifier(false);
        application.setLocalAndOutBoundAuthenticationConfig(authConfig);

        if (useUserIdForDefaultSubject != null) {
            ServiceProviderProperty property = new ServiceProviderProperty();
            property.setName(USE_USER_ID_FOR_DEFAULT_SUBJECT);
            property.setValue(useUserIdForDefaultSubject);
            application.setSpProperties(new ServiceProviderProperty[] { property });
        }
        return application;
    }

    private SubjectConfig buildSubjectClaimConfig(ServiceProvider application) throws Exception {

        Method method = ServiceProviderToApiModel.class.getDeclaredMethod("buildSubjectClaimConfig",
                ServiceProvider.class);
        method.setAccessible(true);
        return (SubjectConfig) method.invoke(model, application);
    }
}

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

package org.wso2.carbon.identity.api.server.api.resource.v1.core;

import org.mockito.MockedStatic;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.resource.collection.mgt.APIResourceCollectionManager;
import org.wso2.carbon.identity.api.resource.collection.mgt.constant.APIResourceCollectionManagementConstants;
import org.wso2.carbon.identity.api.resource.collection.mgt.model.APIResourceCollection;
import org.wso2.carbon.identity.api.resource.collection.mgt.util.APIResourceCollectionManagementUtil;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceMap;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.common.model.APIResource;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Test for ServerAPIResourceCollectionManagementService, focusing on how the
 * {@code ConsoleSettings.UseGranularConsolePermissions} configuration gates the create/update/delete
 * buckets in the API resource map.
 */
public class ServerAPIResourceCollectionManagementServiceTest {

    private static final String COLLECTION_ID = "Y29sbGVjdGlvbi1pZA";

    private ServerAPIResourceCollectionManagementService service;

    @BeforeMethod
    public void setUp() {

        APIResourceCollectionManager apiResourceCollectionManager = mock(APIResourceCollectionManager.class);
        service = new ServerAPIResourceCollectionManagementService(apiResourceCollectionManager);
    }

    @Test
    public void testGranularConsolePermissionsEnabledExposesCreateUpdateDelete() throws Exception {

        APIResourceMap apiResources = buildApiResourceMapForCollection(true);

        assertNotNull(apiResources.getRead(), "Read bucket should always be populated.");
        assertNotNull(apiResources.getWrite(), "Write bucket should always be populated.");
        assertEquals(apiResources.getRead().size(), 1);
        assertEquals(apiResources.getWrite().size(), 1);

        // Granular buckets must be exposed when the config is enabled.
        assertNotNull(apiResources.getCreate(), "Create bucket should be populated when granular " +
                "console permissions are enabled.");
        assertNotNull(apiResources.getUpdate(), "Update bucket should be populated when granular " +
                "console permissions are enabled.");
        assertNotNull(apiResources.getDelete(), "Delete bucket should be populated when granular " +
                "console permissions are enabled.");
        assertEquals(apiResources.getCreate().size(), 1);
        assertEquals(apiResources.getUpdate().size(), 1);
        assertEquals(apiResources.getDelete().size(), 1);
    }

    @Test
    public void testGranularConsolePermissionsDisabledOmitsCreateUpdateDelete() throws Exception {

        APIResourceMap apiResources = buildApiResourceMapForCollection(false);

        // Legacy view/edit (read/write) behaviour is always preserved.
        assertNotNull(apiResources.getRead(), "Read bucket should always be populated.");
        assertNotNull(apiResources.getWrite(), "Write bucket should always be populated.");
        assertEquals(apiResources.getRead().size(), 1);
        assertEquals(apiResources.getWrite().size(), 1);

        // Granular buckets must stay null (and thus be omitted from the JSON response) when the config is off.
        assertNull(apiResources.getCreate(), "Create bucket should be omitted when granular console " +
                "permissions are disabled.");
        assertNull(apiResources.getUpdate(), "Update bucket should be omitted when granular console " +
                "permissions are disabled.");
        assertNull(apiResources.getDelete(), "Delete bucket should be omitted when granular console " +
                "permissions are disabled.");
    }

    /**
     * Builds the API resource map for a collection that carries all five scope types, with the granular console
     * permissions config stubbed to the given state. Invokes the private {@code buildAPIResourceMap} directly so the
     * gating logic can be exercised without standing up the Carbon runtime context required by the public endpoints.
     *
     * @param granularEnabled Value returned by {@code isGranularConsolePermissionsEnabled()}.
     * @return The API resource map built for the collection.
     */
    private APIResourceMap buildApiResourceMapForCollection(boolean granularEnabled) throws Exception {

        APIResourceCollection collection = collectionWithAllScopeTypes();

        try (MockedStatic<APIResourceCollectionManagementUtil> utilMock =
                     mockStatic(APIResourceCollectionManagementUtil.class);
             MockedStatic<ContextLoader> contextLoaderMock = mockStatic(ContextLoader.class)) {

            utilMock.when(APIResourceCollectionManagementUtil::isGranularConsolePermissionsEnabled)
                    .thenReturn(granularEnabled);
            contextLoaderMock.when(() -> ContextLoader.buildURIForBody(anyString()))
                    .thenReturn(URI.create("/api/resources"));

            Method buildAPIResourceMap = ServerAPIResourceCollectionManagementService.class
                    .getDeclaredMethod("buildAPIResourceMap", APIResourceCollection.class);
            buildAPIResourceMap.setAccessible(true);
            APIResourceMap apiResources = (APIResourceMap) buildAPIResourceMap.invoke(service, collection);
            assertNotNull(apiResources, "API resource map should never be null.");
            return apiResources;
        }
    }

    private APIResourceCollection collectionWithAllScopeTypes() {

        Map<String, List<APIResource>> apiResourcesByScopeType = new HashMap<>();
        apiResourcesByScopeType.put(APIResourceCollectionManagementConstants.READ,
                Collections.singletonList(buildApiResource("read-api")));
        apiResourcesByScopeType.put(APIResourceCollectionManagementConstants.WRITE,
                Collections.singletonList(buildApiResource("write-api")));
        apiResourcesByScopeType.put(APIResourceCollectionManagementConstants.CREATE,
                Collections.singletonList(buildApiResource("create-api")));
        apiResourcesByScopeType.put(APIResourceCollectionManagementConstants.UPDATE,
                Collections.singletonList(buildApiResource("update-api")));
        apiResourcesByScopeType.put(APIResourceCollectionManagementConstants.DELETE,
                Collections.singletonList(buildApiResource("delete-api")));

        return new APIResourceCollection.APIResourceCollectionBuilder()
                .id(COLLECTION_ID)
                .name("Applications")
                .displayName("Applications")
                .type("tenant")
                .apiResources(apiResourcesByScopeType)
                .build();
    }

    private APIResource buildApiResource(String name) {

        return new APIResource.APIResourceBuilder()
                .id(name + "-id")
                .name(name)
                .description(name + " description")
                .type("BUSINESS")
                .build();
    }
}

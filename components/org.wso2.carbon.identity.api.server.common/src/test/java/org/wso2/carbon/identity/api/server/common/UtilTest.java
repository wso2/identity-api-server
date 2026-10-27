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

package org.wso2.carbon.identity.api.server.common;

import org.mockito.MockedStatic;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Tests for the pagination link builders in {@link Util}.
 */
public class UtilTest {

    private static final String PATH = "/policies";

    private MockedStatic<ContextLoader> contextLoader;

    @BeforeMethod
    public void setUp() {

        contextLoader = mockStatic(ContextLoader.class);
        contextLoader.when(() -> ContextLoader.buildURIForBody(anyString()))
                .thenAnswer(invocation -> URI.create(invocation.getArgument(0)));
    }

    @AfterMethod
    public void tearDown() {

        contextLoader.close();
    }

    @Test
    public void testExistingSignatureKeepsLinkFormat() {

        Map<String, String> links = Util.buildPaginationLinks(10, 10, 30, PATH, "name,id", "name co dev");

        assertEquals(links.get("next"), "/v1/policies?offset=20&limit=10&attributes=name,id&filter=name+co+dev");
        assertEquals(links.get("previous"), "/v1/policies?offset=0&limit=10&attributes=name,id&filter=name+co+dev");
    }

    @Test
    public void testExistingSignatureMatchesOverloadWithoutAdditionalParams() {

        assertEquals(Util.buildPaginationLinks(5, 3, 20, PATH, null, "abc"),
                Util.buildPaginationLinks(5, 3, 20, PATH, null, "abc", Collections.emptyMap()));
        assertEquals(Util.buildPaginationLinks(5, 3, 20, PATH, null, "abc"),
                Util.buildPaginationLinks(5, 3, 20, PATH, null, "abc", null));
    }

    @Test
    public void testAdditionalParamsAddedToEveryLink() {

        Map<String, String> additionalParams = new LinkedHashMap<>();
        additionalParams.put("type", "DEVICE");
        additionalParams.put("empty", "");
        additionalParams.put("tag", "a b&c");

        Map<String, String> links = Util.buildPaginationLinks(10, 10, 30, PATH, null, "dev", additionalParams);

        assertEquals(links.get("next"), "/v1/policies?offset=20&limit=10&filter=dev&type=DEVICE&tag=a+b%26c");
        assertEquals(links.get("previous"), "/v1/policies?offset=0&limit=10&filter=dev&type=DEVICE&tag=a+b%26c");
    }

    @Test
    public void testNoLinksForSinglePage() {

        Map<String, String> links = Util.buildPaginationLinks(10, 0, 5, PATH, null, null,
                Collections.singletonMap("type", "DEVICE"));

        assertFalse(links.containsKey("next"));
        assertFalse(links.containsKey("previous"));
    }
}

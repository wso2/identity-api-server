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

package org.wso2.carbon.identity.api.server.script.library.v2.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.script.library.common.Constants;

import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class ScriptLibraryIdUtilTest {

    @DataProvider
    public Object[][] scriptLibraryNames() {

        return new Object[][]{
                {"age-based.js", "YWdlLWJhc2VkLmpz"},
                {"legacy lib.js", "bGVnYWN5IGxpYi5qcw"},
                {"my_lib.v2.js", "bXlfbGliLnYyLmpz"},
                // Not allowed by the backend today, but must still produce a URL safe id.
                {"a>b?.js", "YT5iPy5qcw"}
        };
    }

    @Test(dataProvider = "scriptLibraryNames")
    public void testEncodeAndDecode(String name, String expectedId) {

        String id = ScriptLibraryIdUtil.encode(name);
        assertEquals(id, expectedId);
        assertEquals(ScriptLibraryIdUtil.decode(id), name);
    }

    @Test
    public void testDecodeAcceptsPadding() {

        assertEquals(ScriptLibraryIdUtil.decode("bGVnYWN5IGxpYi5qcw=="), "legacy lib.js");
    }

    @DataProvider
    public Object[][] invalidIds() {

        return new Object[][]{
                {null},
                {""},
                {"   "},
                // Standard base64 alphabet characters that are not part of base64url.
                {"YT5iPy5qcw+/"},
                {"not base64!"},
                // Non canonical encoding of "a" (trailing bits set).
                {"YR"},
                // Valid base64url, but not valid UTF-8.
                {"_w"},
                // Decodes to a blank name.
                {"ICA"}
        };
    }

    @Test(dataProvider = "invalidIds")
    public void testDecodeRejectsInvalidIds(String id) {

        try {
            ScriptLibraryIdUtil.decode(id);
            fail("Expected an APIError for id: " + id);
        } catch (APIError e) {
            assertEquals(e.getStatus(), Response.Status.BAD_REQUEST);
            assertEquals(e.getCode(), Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_ID_VALIDATION.getCode());
        }
    }
}

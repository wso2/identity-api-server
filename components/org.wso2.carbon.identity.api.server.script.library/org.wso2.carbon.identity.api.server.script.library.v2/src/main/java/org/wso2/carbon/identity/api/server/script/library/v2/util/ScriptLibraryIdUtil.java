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

import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.script.library.common.Constants;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.core.Response;

/**
 * Converts between script library names and the ids used in the v2 API paths.
 * The id is the base64url encoded (RFC 4648 section 5, without padding) script library name, so that names which
 * are not valid in a URL path segment (e.g. names with spaces) never appear in the request URL.
 */
public class ScriptLibraryIdUtil {

    private ScriptLibraryIdUtil() {

    }

    /**
     * Build the id of a script library from its name.
     *
     * @param scriptLibraryName Name of the script library.
     * @return Script library id.
     */
    public static String encode(String scriptLibraryName) {

        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(scriptLibraryName.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Resolve the script library name from its id.
     *
     * @param scriptLibraryId Script library id.
     * @return Script library name.
     * @throws APIError If the id is not the canonical base64url encoding of a UTF-8 name.
     */
    public static String decode(String scriptLibraryId) {

        if (scriptLibraryId == null || scriptLibraryId.trim().isEmpty()) {
            throw invalidIdError(scriptLibraryId);
        }
        try {
            byte[] nameBytes = Base64.getUrlDecoder().decode(scriptLibraryId);
            String scriptLibraryName = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(nameBytes)).toString();
            // Only the canonical form is accepted, so that a script library has exactly one id.
            if (scriptLibraryName.trim().isEmpty()
                    || !encode(scriptLibraryName).equals(scriptLibraryId.replaceAll("=+$", ""))) {
                throw invalidIdError(scriptLibraryId);
            }
            return scriptLibraryName;
        } catch (IllegalArgumentException | CharacterCodingException e) {
            throw invalidIdError(scriptLibraryId);
        }
    }

    private static APIError invalidIdError(String scriptLibraryId) {

        Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_SCRIPT_LIBRARY_ID_VALIDATION;
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(error.getCode());
        errorResponse.setMessage(error.getMessage());
        errorResponse.setDescription(String.format(error.getDescription(), scriptLibraryId));
        errorResponse.setRef(Util.getCorrelation());
        return new APIError(Response.Status.BAD_REQUEST, errorResponse);
    }
}

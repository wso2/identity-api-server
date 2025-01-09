/*
 * Copyright (c) (2019-2023), WSO2 LLC. (http://www.wso2.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.claim.management.common;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Claim Management constant class.
 */
public class Constant {

    public static final String CLAIM_MANAGEMENT_PREFIX = "CMT-";
    public static final String CMT_PATH_COMPONENT = "/claim-dialects";
    public static final String LOCAL_DIALECT_PATH = "local";
    public static final String LOCAL_DIALECT = "http://wso2.org/claims";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_ADDING_DIALECT("50001",
                "Unable to add claim dialect.",
                "Server encountered an error while adding the claim dialect %s."),
        ERROR_CODE_ERROR_ADDING_EXTERNAL_CLAIM("50002",
                "Unable to add external claim.",
                "Server encountered an error while adding the external claim %s."),
        ERROR_CODE_ERROR_ADDING_LOCAL_CLAIM("50003",
                "Unable to add local claim.",
                "Server encountered an error while adding the local claim %s."),
        ERROR_CODE_ERROR_DELETING_DIALECT("50004",
                "Unable to delete claim dialect.",
                "Server encountered an error while deleting the claim dialect for identifier %s."),
        ERROR_CODE_ERROR_DELETING_EXTERNAL_CLAIM("50005",
                "Unable to delete external claim.",
                "Server encountered an error while deleting the external claim for identifier %s in dialect " +
                        "identifier %s."),
        ERROR_CODE_ERROR_DELETING_LOCAL_CLAIM("50006",
                "Unable to delete local claim.",
                "Server encountered an error while deleting the local claim for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_DIALECT("50007",
                "Unable to retrieve claim dialect.",
                "Server encountered an error while retrieving the claim dialect for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_DIALECTS("50008",
                "Unable to retrieve claim dialects.",
                "Server encountered an error while retrieving the claim dialects."),
        ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIM("50009",
                "Unable to retrieve external claim.",
                "Server encountered an error while retrieving the external claim for identifier %s in dialect " +
                        "identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_EXTERNAL_CLAIMS("50010",
                "Unable to retrieve external claims.",
                "Server encountered an error while retrieving the external claims for dialect identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIM("50011",
                "Unable to retrieve local claim.",
                "Server encountered an error while retrieving the local claim for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_LOCAL_CLAIMS("50012",
                "Unable to retrieve local claims.",
                "Server encountered an error while retrieving the local claims."),
        ERROR_CODE_ERROR_UPDATING_DIALECT("50013",
                "Unable to update claim dialect.",
                "Server encountered an error while updating the claim dialect for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_EXTERNAL_CLAIM("50014",
                "Unable to update external claim.",
                "Server encountered an error while updating the external claim for identifier %s in dialect " +
                        "identifier %s."),
        ERROR_CODE_ERROR_UPDATING_LOCAL_CLAIM("50015",
                "Unable to update local claim.",
                "Server encountered an error while updating the local claim for identifier %s."),
        ERROR_CODE_DIALECT_NOT_FOUND("50016",
                "Resource not found.",
                "Unable to find a resource matching the provided claim dialect identifier %s."),
        ERROR_CODE_CLAIMS_NOT_FOUND_FOR_DIALECT("50017",
                "Resource not found.",
                "Unable to find any claims matching the provided claim dialect identifier %s."),
        ERROR_CODE_EXTERNAL_CLAIM_NOT_FOUND("50018",
                "Resource not found.",
                "Unable to find a resource matching the provided external claim identifier %s in dialect " +
                        "identifier %s."),
        ERROR_CODE_LOCAL_CLAIM_NOT_FOUND("50019",
                "Resource not found.",
                "Unable to find a resource matching the provided local claim identifier %s."),
        ERROR_CODE_EXTERNAL_CLAIM_CONFLICT("50020",
                "Unable to update external claim.",
                "Existing external claim uri %s in dialect identifier %s cannot be changed."),
        ERROR_CODE_LOCAL_CLAIM_CONFLICT("50021",
                "Unable to update local claim.",
                "Existing local claim uri %s cannot be changed."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("50022",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("50023",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("50024",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("50025",
                "Attribute filtering not supported.",
                "Attribute filtering capability is not supported in this version of the API."),
        ERROR_CODE_INVALID_USERSTORE("50026",
                "Invalid attribute mapping.",
                "Invalid userstore %s provided in attribute mapping."),
        ERROR_CODE_INVALID_DIALECT_ID("50027",
                "Invalid dialect identifier.",
                "Used dialect identifier %s does not exist."),
        ERROR_CODE_EMPTY_CLAIM_DIALECT("50028",
                "Empty claim dialect URI.",
                "Claim dialect URI cannot be empty."),
        ERROR_CODE_EMPTY_LOCAL_CLAIM_URI("50029",
                "Empty local claim URI.",
                "Local claim URI cannot be empty."),
        ERROR_CODE_EMPTY_MAPPED_ATTRIBUTES_IN_LOCAL_CLAIM("50030",
                "Empty mapped attributes.",
                "Mapped attributes cannot be empty."),
        ERROR_CODE_LOCAL_CLAIM_HAS_MAPPED_EXTERNAL_CLAIM("50031",
                "Unable to remove local claim.",
                "Unable to remove local claim while having associations with external claims."),
        ERROR_CODE_EMPTY_EXTERNAL_CLAIM_URI("50032",
                "Empty external claim URI.",
                "External claim URI cannot be empty."),
        ERROR_CODE_INVALID_EXTERNAL_CLAIM_DIALECT("50033",
                "Invalid external claim dialect.",
                "The provided claim dialect is the local claim dialect and cannot be used as an external dialect."),
        ERROR_CODE_EMPTY_EXTERNAL_DIALECT_URI("50034",
                "Empty external claim dialect URI.",
                "External dialect URI cannot be empty."),
        ERROR_CODE_MAPPED_TO_EMPTY_LOCAL_CLAIM_URI("50035",
                "Empty mapped local claim URI.",
                "Mapped local claim URI cannot be empty."),
        ERROR_CODE_MAPPED_TO_INVALID_LOCAL_CLAIM_URI("50036",
                "Invalid mapped local claim URI.",
                "Mapped local claim URI is invalid."),
        ERROR_CODE_INVALID_INPUT("50037",
                "Invalid input.",
                "One of the given inputs is invalid."),
        ERROR_CODE_EXISTING_EXTERNAL_CLAIM_URI("50038", "Unable to add external claim.",
                "External claim URI already exists. External claim URI must be unique."),
        ERROR_CODE_EXISTING_LOCAL_CLAIM_URI("50039", "Unable to add local claim.",
                "Local claim URI already exists. Local claim URI must be unique."),
        ERROR_CODE_LOCAL_CLAIM_REFERRED_BY_APPLICATION("50040",
                "Unable to remove local claim.",
                "Unable to delete claim as it is referred by an application"),
        ERROR_CODE_LOCAL_CLAIM_REFERRED_BY_AN_IDP("50041",
                "Unable to remove local claim.",
                "Unable to delete claim as it is referred by an IDP."),
        ERROR_CODE_MISSING_MEDIA_TYPE("50042",
                "Missing media type.",
                "Media type is not specified in the request."),
        ERROR_CODE_MISSING_FILE_CONTENT("50043",
                "Missing file content in the request",
                "File content is not specified in the request."),
        ERROR_CODE_ERROR_PARSING_CLAIM_DIALECT("50044",
                "Unable to parse claim dialect to %s.",
                "Error when parsing claim dialect to %s file content."),
        ERROR_CODE_ERROR_READING_FILE_CONTENT("50045",
                "Unable to read %s file content for claim dialect.",
                "Error when reading %s file configuration for claim dialect."),
        ERROR_CODE_ERROR_IMPORTING_CLAIM_DIALECT("50046",
                "Unable to import claim dialect.",
                "Server encountered an error while importing the claim dialect."),
        ERROR_CODE_ERROR_IMPORTING_LOCAL_CLAIM_DIALECT("50047",
                "Unable to import local claim dialect.",
                "Importing local claim dialect is not allowed."),
        ERROR_CODE_INVALID_INPUT_FILE("50048",
                "Invalid input file format.",
                "Provided input file is not in the correct format"),
        ERROR_CODE_IMPORTING_EXTERNAL_CLAIMS("50049",
                "Some external claims were not imported.",
                "%s out of %s external claims could not be imported."),
        ERROR_CODE_UPDATING_EXTERNAL_CLAIMS("50050",
                "Some external claims were not updated.",
                "%s out of %s external claims could not be updated."),
        ERROR_CODE_UPDATING_LOCAL_CLAIMS("50051",
                "Some local claims were not updated.",
                "%s out of %s local claims could not be updated."),
        ERROR_CODE_INVALID_IDENTIFIER("CMT-60001", "Invalid identifier",
                "Invalid Identifier: %s"),
        ERROR_CODE_CLAIM_URI_NOT_SPECIFIED("CMT-60002", "Empty claim URI", "Claim URI is " +
                "not specified in the request"),
        ERROR_CODE_CLAIM_DISPLAY_NAME_NOT_SPECIFIED("CMT-60003", "Empty display name",
                "Claim display name is not specified in the request"),
        ERROR_CODE_CLAIM_DESCRIPTION_NOT_SPECIFIED("CMT-60004", "Empty description",
                "Claim description is not specified in the request"),
        ERROR_CODE_USERSTORE_NOT_SPECIFIED_IN_MAPPINGS("CMT-60005", "Userstore not specified",
                "Mapped userstore cannot be empty"),
        ERROR_CODE_EMPTY_ATTRIBUTE_MAPPINGS("CMT-60006", "Attribute mapping not specified",
                "Attribute mapping cannot be empty"),
        ERROR_CODE_UNAUTHORIZED_ORG_FOR_CLAIM_MANAGEMENT("CMT-60007", "Claim modification is not " +
                "allowed for this organization.", "Unable to execute the requested organization claim " +
                "management task."),
        ERROR_CODE_ERROR_RESOLVING_ORGANIZATION("CMT-60008", "Error resolving organization",
                "Unable to resolve organization.");

        private final String code;
        private final String message;
        private final String description;

        private static final Map<String, ErrorMessage> messageIndex = new HashMap<>(ErrorMessage.values().length);
        static final String BUNDLE = "ServerClientErrorMappings";
        static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE);

        static {
            for (ErrorMessage em : ErrorMessage.values()) {
                messageIndex.put(em.code, em);
            }
        }

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            if (code.contains(CLAIM_MANAGEMENT_PREFIX)) {
                return code;
            }
            return CLAIM_MANAGEMENT_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        /**
         * Get the proper error message mapped to the server side error.
         *
         * @param serverCode Error code from the server.
         * @return Error message.
         */
        public static ErrorMessage getMappedErrorMessage(String serverCode) {

            try {
                String errorCode = resourceBundle.getString(serverCode);
                return messageIndex.get(errorCode);
            } catch (Throwable e) {
                // Ignore if error mapping has invalid input.
            }
            return ErrorMessage.ERROR_CODE_INVALID_INPUT;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }
    }

    // Claim property keys.
    public static final String PROP_DESCRIPTION = "Description";
    public static final String PROP_DISPLAY_NAME = "DisplayName";
    public static final String PROP_DISPLAY_ORDER = "DisplayOrder";
    public static final String PROP_READ_ONLY = "ReadOnly";
    public static final String PROP_REG_EX = "RegEx";
    public static final String PROP_REQUIRED = "Required";
    public static final String PROP_SUPPORTED_BY_DEFAULT = "SupportedByDefault";
    public static final String PROP_UNIQUENESS_SCOPE = "UniquenessScope";
}

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

package org.wso2.carbon.identity.api.server.workflow.common;

import java.time.format.DateTimeFormatter;

/**
 * Common constants for workflow API.
 */
public class Constants {

    public static final String WORKFLOW_PREFIX = "WF-";
    public static final String APPROVAL_STEPS = "ApprovalSteps";
    public static final String APPROVAL_STEP = "Step-";
    public static final String TEMPLATE = "Template";
    public static final String STEP_NAME_DELIMITER = "-";
    public static final String PARAMETER_VALUE_SEPARATOR = ",";
    public static final int DEFAULT_OFFSET = 0;
    public static final String WORKFLOW_PATH_COMPONENT = "/workflows";
    public static final String WORKFLOW_ASSOCIATION_PATH_COMPONENT = "workflow-associations";

    /**
     * Workflow instance related constants.
     */
    public static final DateTimeFormatter WORKFLOW_INSTANCE_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter WORKFLOW_INSTANCE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String WORKFLOW_INSTANCE_REQUEST_TYPE_KEY = "requestType";
    public static final String WORKFLOW_INSTANCE_CREATED_DATE_KEY = "createdAt";
    public static final String WORKFLOW_INSTANCE_CREATED_START_DATE_KEY = "createdAtStartDate";
    public static final String WORKFLOW_INSTANCE_CREATED_END_DATE_KEY = "createdAtEndDate";
    public static final String WORKFLOW_INSTANCE_UPDATED_DATE_KEY = "updatedAt";
    public static final String WORKFLOW_INSTANCE_UPDATED_START_DATE_KEY = "updatedAtStartDate";
    public static final String WORKFLOW_INSTANCE_UPDATED_END_DATE_KEY = "updatedAtEndDate";
    public static final String WORKFLOW_INSTANCE_STATUS_KEY = "status";
    public static final String WORKFLOW_INSTANCE_OPERATION_TYPE_KEY = "operationType";
    public static final String WORKFLOW_INSTANCE_MY_TASKS_REQUEST_TYPE = "MY_TASKS";
    public static final String WORKFLOW_INSTANCE_ALL_TASKS_REQUEST_TYPE = "ALL_TASKS";

    public static final String EQUALS_OPERATOR = "eq";
    public static final String GREATER_THAN_OR_EQUAL_OPERATOR = "ge";
    public static final String LESS_THAN_OR_EQUAL_OPERATOR = "le";

    private Constants() {

    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors starting from 510xx.
        ERROR_CODE_WORKFLOW_NOT_FOUND("51001", "Resource not found.",
                "Unable to find a resource matching the provided workflow identifier %s."),
        ERROR_CODE_CLIENT_ERROR_LISTING_WORKFLOWS("51002", "Unable to list existing workflows",
                "Encountered an error while listing the workflows."),
        ERROR_CODE_CLIENT_ERROR_ADDING_WORKFLOW("51003", "Unable to add workflow",
                "Encountered an error while adding the workflow."),
        ERROR_CODE_CLIENT_ERROR_UPDATING_WORKFLOW("51004", "Unable to update workflow",
                "Encountered an error while updating the workflow for identifier %s."),
        ERROR_CODE_ASSOCIATION_NOT_FOUND("51005", "Resource not found.",
                "Unable to find a resource matching the provided workflow association identifier %s."),
        ERROR_CODE_CLIENT_ERROR_LISTING_ASSOCIATIONS("51006", "Unable to list existing workflow " +
                "associations", "Encountered an error while listing the workflow associations."),
        ERROR_CODE_CLIENT_ERROR_ADDING_ASSOCIATION("51007", "Unable to add workflow association",
                "Encountered an error while adding the workflow association with the name %s."),
        ERROR_CODE_CLIENT_ERROR_UPDATING_ASSOCIATION("51008", "Unable to update workflow association",
                "Encountered an error while updating the workflow association."),
        ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT("51009", "Invalid pagination parameters.",
                "'limit' shouldn't be negative."),
        ERROR_CODE_CLIENT_ERROR_WORKFLOW_INSTANCE_NOT_FOUND("51010", "Workflow log not found.",
                "Unable to find a resource matching the provided workflow log identifier %s."),
        ERROR_CODE_CLIENT_ERROR_LISTING_WORKFLOW_INSTANCES("51011", "Unable to list existing workflow " +
                "logs.", "Encountered an error while listing the workflow logs."),
        ERROR_CODE_CLIENT_ERROR_DELETING_WORKFLOW_INSTANCE("51012", "Unable to delete workflow log.",
                "Encountered an error while deleting the workflow log identifier %s."),
        // Server Errors starting from 500xx.
        ERROR_CODE_ERROR_LISTING_WORKFLOWS("50020", "Unable to list existing workflows",
                "Server encountered an error while listing the workflows."),
        ERROR_CODE_ERROR_REMOVING_WORKFLOW("50021", "Unable to delete the workflow",
                                                   "Server encountered an error while deleting " +
                                                           "the workflow for the identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_WORKFLOW("50022", "Unable to retrieve workflow.",
                                                     "Server encountered an error while " +
                                                             "retrieving the workflow for identifier %s."),
        ERROR_CODE_ERROR_ADDING_WORKFLOW("50023", "Unable to add workflow",
                                                 "Server encountered an error while adding the workflow."),
        ERROR_CODE_ERROR_UPDATING_WORKFLOW("50024", "Unable to update workflow",
                                                   "Server encountered an error while updating the " +
                                                           "workflow for identifier %s."),
        ERROR_CODE_ERROR_ADDING_ASSOCIATION("50025", "Unable to add workflow association",
                                                    "Server encountered an error while adding " +
                                                            "the workflow association with the name %s."),
        ERROR_CODE_ERROR_LISTING_ASSOCIATIONS("50026", "Unable to list existing workflow associations",
                                                      "Server encountered an error while " +
                                                              "listing the workflow associations."),
        ERROR_CODE_ERROR_REMOVING_ASSOCIATION("50027", "Unable to delete the workflow association",
                                                      "Server encountered an error while deleting " +
                                                              "the workflow association."),
        ERROR_CODE_ERROR_RETRIEVING_ASSOCIATION("50028", "Unable to retrieve workflow association.",
                                                        "Server encountered an error while retrieving " +
                                                                "the workflow association for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_ASSOCIATION("50029", "Unable to update workflow association",
                                                      "Server encountered an error while " +
                                                              "updating the workflow association."),
        ERROR_CODE_ERROR_DELETING_WORKFLOW_INSTANCE("50030", "Unable to delete workflow log.",
                "Server encountered an error while deleting the workflow log for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_WORKFLOW_INSTANCE("50031", "Unable to retrieve workflow log.",
                "Server encountered an error while retrieving the workflow log for identifier %s."),
        ERROR_CODE_ERROR_LISTING_WORKFLOW_INSTANCES("50032", "Unable to list existing workflow logs.",
                "Server encountered an error while listing the workflow logs.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return WORKFLOW_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}

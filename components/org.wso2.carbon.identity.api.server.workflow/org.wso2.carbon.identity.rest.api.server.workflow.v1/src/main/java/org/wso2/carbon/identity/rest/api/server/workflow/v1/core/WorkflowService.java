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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.workflow.common.Constants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.InstanceStatus;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Operation;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.OptionDetails;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationListItem;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationPatchRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceListItem;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowListItem;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplateBase;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplateParameters;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplateParametersBase;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.Workflow;
import org.wso2.carbon.identity.workflow.mgt.dto.Association;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowEvent;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowClientException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;

/**
 * Workflow service class.
 */
public class WorkflowService {

    private static final Log log = LogFactory.getLog(WorkflowService.class);
    private final WorkflowManagementService workflowManagementService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public WorkflowService(WorkflowManagementService workflowManagementService) {

        this.workflowManagementService = workflowManagementService;
    }

    /**
     * Add new workflow.
     *
     * @param workflow Workflow details
     * @return WorkflowResponse
     */
    public WorkflowResponse addWorkflow(WorkflowRequest workflow) {

        Workflow currentWorkflow;
        try {
            if (workflowManagementService.isWorkflowExistByName(workflow.getName())) {
                throw new WorkflowClientException("A workflow with name: " + workflow.getName() + " already exists.");
            }
            String workflowId = UUID.randomUUID().toString();
            currentWorkflow = createWorkflow(workflow, workflowId);
            List<WorkflowTemplateParameters> templateProperties = workflow.getTemplate().getSteps();
            List<Parameter> parameterList = createParameterList(workflowId, templateProperties);
            workflowManagementService.addWorkflow(currentWorkflow, parameterList,
                    CarbonContext.getThreadLocalCarbonContext().getTenantId());
            return getWorkflow(workflowId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_ADDING_WORKFLOW, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_WORKFLOW, null, e);
        }
    }

    /**
     * Update an existing workflow.
     *
     * @param workflow   Workflow details
     * @param workflowId Workflow ID
     * @return WorkflowResponse
     */
    public WorkflowResponse updateWorkflow(WorkflowRequest workflow, String workflowId) {

        Workflow currentWorkflow;
        try {
            Workflow existingWorkflow = workflowManagementService.getWorkflow(workflowId);

            if (existingWorkflow == null) {
                throw new WorkflowClientException("A workflow with ID: " + workflowId + "doesn't exist.");
            }
            currentWorkflow = createWorkflow(workflow, workflowId);
            List<WorkflowTemplateParameters> templateProperties = workflow.getTemplate().getSteps();
            List<Parameter> parameterList = createParameterList(workflowId, templateProperties);
            workflowManagementService.addWorkflow(currentWorkflow, parameterList,
                    CarbonContext.getThreadLocalCarbonContext().getTenantId());
            return getWorkflow(workflowId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_UPDATING_WORKFLOW, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_WORKFLOW, workflowId, e);
        }
    }

    /**
     * Retrieve workflow from workflow ID.
     *
     * @param workflowId workflow id
     * @return WorkflowResponse
     */
    public WorkflowResponse getWorkflow(String workflowId) {

        try {
            Workflow currentWorkflow = workflowManagementService.getWorkflow(workflowId);
            List<Parameter> workflowParameters = workflowManagementService.getWorkflowParameters(workflowId);

            return getWorkflowDetails(currentWorkflow, workflowParameters);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_WORKFLOW_NOT_FOUND, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_WORKFLOW, workflowId, e);
        }
    }

    /**
     * List paginated workflows of a tenant.
     *
     * @param limit  Items per page
     * @param offset Offset
     * @param filter filter string
     * @return WorkflowListResponse
     */
    public WorkflowListResponse listPaginatedWorkflows(Integer limit, Integer offset, String filter) {

        List<WorkflowListItem> workflowBasicInfoList = new ArrayList<>();
        List<Workflow> currentWorkflows;
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            limit = validateLimit(limit);
            offset = validateOffset(offset);
            currentWorkflows = workflowManagementService.listPaginatedWorkflows(tenantId, limit, offset, filter);
            for (Workflow workflow : currentWorkflows) {
                WorkflowListItem workflowTmp = getWorkflow(workflow);
                workflowBasicInfoList.add(workflowTmp);
            }
            return createWorkflowResponse(tenantId,
                    workflowBasicInfoList.toArray(new WorkflowListItem[workflowBasicInfoList.size()]), offset, filter);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_LISTING_WORKFLOWS, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOWS, null, e);
        }
    }

    /**
     * Remove a workflow by a given workflow ID.
     *
     * @param workflowId ID of workflow to remove
     */
    public void removeWorkflow(String workflowId) {

        try {
            workflowManagementService.removeWorkflow(workflowId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_WORKFLOW_NOT_FOUND, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_WORKFLOW, workflowId, e);
        }
    }

    /**
     * Add new workflow association.
     *
     * @param workflowAssociation Workflow association details.
     * @return Return WorkflowAssociationResponse.
     */
    public WorkflowAssociationResponse addAssociation(WorkflowAssociationRequest workflowAssociation) {

        try {
            Workflow currentWorkflow = workflowManagementService.getWorkflow(workflowAssociation.getWorkflowId());
            WorkflowEvent event = workflowManagementService.getEvent(workflowAssociation.getOperation().toString());
            if (currentWorkflow == null) {
                throw new WorkflowClientException("A workflow with ID: " + workflowAssociation.getWorkflowId() +
                        " doesn't exist.");
            }
            if (event == null) {
                throw new WorkflowClientException("An event with ID: " + workflowAssociation.getOperation().toString() +
                        " doesn't exist.");
            }
            workflowManagementService.addAssociation(workflowAssociation.getAssociationName(),
                    workflowAssociation.getWorkflowId(), workflowAssociation.getOperation().toString(),
                    null);
            Association createdAssociations = workflowManagementService
                    .getAssociationsForWorkflow(workflowAssociation.getWorkflowId()).stream()
                    .filter(association -> association.getAssociationName()
                            .equals(workflowAssociation.getAssociationName()) &&
                            association.getEventId().equals(workflowAssociation.getOperation().value()))
                    .findFirst()
                    .orElseThrow(() -> new WorkflowClientException("Failed to find the created association for " +
                            "workflow ID: " + workflowAssociation.getWorkflowId() + ", association name: "
                            + workflowAssociation.getAssociationName() + ", event: "
                            + workflowAssociation.getOperation().value()));
            return getAssociationDetails(createdAssociations);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_ADDING_ASSOCIATION,
                    workflowAssociation.getAssociationName(), e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_ASSOCIATION,
                    workflowAssociation.getAssociationName(), e);
        }
    }

    /**
     * Partially update an association.
     *
     * @param associationId       Association ID
     * @param workflowAssociation Association Details
     * @return WorkflowAssociationResponse
     */
    public WorkflowAssociationResponse updateAssociation(String associationId,
            WorkflowAssociationPatchRequest workflowAssociation) {

        boolean isEnable;
        String eventId;
        try {
            if (workflowAssociation.getIsEnabled() == null) {
                isEnable = workflowManagementService.getAssociation(associationId).isEnabled();
            } else {
                isEnable = workflowAssociation.getIsEnabled();
            }

            if (workflowAssociation.getOperation() == null) {
                eventId = null;
            } else {
                eventId = workflowAssociation.getOperation().toString();
            }
            workflowManagementService.updateAssociation(associationId, workflowAssociation.getAssociationName(),
                    workflowAssociation.getWorkflowId(), eventId,
                    null, isEnable);
            return getAssociation(associationId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_UPDATING_ASSOCIATION,
                    associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_ASSOCIATION, associationId, e);
        }
    }

    /**
     * Get an association by ID.
     *
     * @param associationId Association ID
     * @return WorkflowAssociationResponse
     */
    public WorkflowAssociationResponse getAssociation(String associationId) {

        try {
            Association association = workflowManagementService.getAssociation(associationId);
            return getAssociationDetails(association);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ASSOCIATION_NOT_FOUND, associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_ASSOCIATION, associationId, e);
        }
    }

    /**
     * List paginated associations of a tenant.
     *
     * @param limit  Items per page
     * @param offset Offset
     * @param filter Filter
     * @return WorkflowAssociationListResponse
     */
    public WorkflowAssociationListResponse listPaginatedAssociations(Integer limit, Integer offset, String filter) {

        List<WorkflowAssociationListItem> associations = new ArrayList<>();
        List<Association> associationList;
        try {
            limit = validateLimit(limit);
            offset = validateOffset(offset);
            associationList = workflowManagementService.listPaginatedAssociations(
                    CarbonContext.getThreadLocalCarbonContext().getTenantId(), limit, offset, filter);
            for (Association association : associationList) {
                associations.add(getAssociation(association));
            }
            return createAssociationListResponse(CarbonContext.getThreadLocalCarbonContext().getTenantId(),
                    associations.toArray(new WorkflowAssociationListItem[associations.size()]), offset, filter);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_LISTING_ASSOCIATIONS, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_ASSOCIATIONS, null, e);
        }
    }

    /**
     * Remove association.
     *
     * @param associationId ID of association to remove
     */
    public void removeAssociation(String associationId) {

        try {
            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null) {
                throw new WorkflowClientException("A workflow association with ID: " + associationId +
                        "doesn't exist.");
            }
            workflowManagementService.removeAssociation(Integer.parseInt(associationId));
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ASSOCIATION_NOT_FOUND, associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_ASSOCIATION, associationId, e);
        }
    }

    /**
     * Validate the offset.
     *
     * @param offset Offset value
     * @return Validated offset.
     */
    private int validateOffset(Integer offset) {

        if (offset != null && offset >= 0) {
            return offset;
        } else {
            return Constants.DEFAULT_OFFSET;
        }
    }

    /**
     * Validate the limit.
     *
     * @param limit Limit value.
     * @return Validated limit.
     */
    private int validateLimit(Integer limit) {

        final int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit == null) {
            return IdentityUtil.getDefaultItemsPerPage();
        } else if (limit <= maximumItemPerPage) {
            return limit;
        } else {
            return maximumItemPerPage;
        }
    }

    private WorkflowListResponse createWorkflowResponse(int tenantId, WorkflowListItem[] workflowListItems,
            Integer offset,
            String filter) throws WorkflowException {

        WorkflowListResponse workflowListResponse = new WorkflowListResponse();
        workflowListResponse.setTotalResults(workflowManagementService.getWorkflowsCount(tenantId, filter));
        if (workflowListItems != null && workflowListItems.length > 0) {
            workflowListResponse.setWorkflows(Arrays.asList(workflowListItems));
            workflowListResponse.setCount(workflowListItems.length);
        } else {
            workflowListResponse.setWorkflows(Collections.emptyList());
            workflowListResponse.setCount(0);
        }
        workflowListResponse.setStartIndex(offset != null ? offset + 1 : 1);
        return workflowListResponse;
    }

    private WorkflowListItem getWorkflow(Workflow currentWorkflow) {

        WorkflowListItem workflow = null;
        if (currentWorkflow != null) {
            workflow = new WorkflowListItem();
            workflow.setId(currentWorkflow.getWorkflowId());
            workflow.setName(currentWorkflow.getWorkflowName());
            workflow.setDescription(currentWorkflow.getWorkflowDescription());
            workflow.setTemplate(currentWorkflow.getTemplateId());
            workflow.setEngine(currentWorkflow.getWorkflowImplId());
        }
        return workflow;
    }

    /**
     * Converts a list of `WorkflowTemplateParameters` into a list of `Parameter`
     * objects.
     * Example:
     * * Given the following inputs:
     * * - workflowId = "wf123"
     * * - templateProperties = steps = [
     * * {step: 1, options: [
     * * {entity: "roles", values: ["123", "124"]},
     * * {entity: "users", values: ["234", "235"]}
     * * ]},
     * * {step: 2, options: [
     * * {entity: "roles", values: ["345"]}
     * * ]}
     * *
     * * The output `parameterList` will contain the following list of `Parameter`
     * objects:
     * * - parameterList = [
     * {workflowId = "wf123", paramName = "ApprovalSteps", paramValue = "123,124",
     * qName = "Step-1-roles", holder =
     * "TEMPLATE"},
     * {workflowId = "wf123", paramName = "ApprovalSteps", paramValue = "234,235",
     * qName = "Step-1-users", holder
     * = "TEMPLATE"},
     * {workflowId = "wf123", paramName = "ApprovalSteps", paramValue = "345", qName
     * = "Step-2-roles", holder =
     * "TEMPLATE"}
     * ]
     */
    private List<Parameter> createParameterList(String workflowId,
            List<WorkflowTemplateParameters> templateProperties) {

        List<Parameter> parameterList = new ArrayList<>();
        for (WorkflowTemplateParameters properties : templateProperties) {
            for (OptionDetails options : properties.getOptions()) {
                Parameter parameter = setWorkflowImplParameters(workflowId, Constants.APPROVAL_STEPS,
                        String.join(Constants.PARAMETER_VALUE_SEPARATOR, options.getValues()),
                        Constants.APPROVAL_STEP + properties.getStep() + Constants.STEP_NAME_DELIMITER +
                                options.getEntity(),
                        Constants.TEMPLATE);
                parameterList.add(parameter);
            }
        }
        return parameterList;
    }

    private Parameter setWorkflowImplParameters(String workflowId, String paramName, String paramValue, String qName,
            String holder) {

        Parameter parameter = new Parameter();
        parameter.setWorkflowId(workflowId);
        parameter.setParamName(paramName);
        parameter.setParamValue(paramValue);
        parameter.setqName(qName);
        parameter.setHolder(holder);
        return parameter;
    }

    private Workflow createWorkflow(WorkflowRequest workflow, String workflowId) throws WorkflowException {

        Workflow currentWorkflow = new Workflow();
        currentWorkflow.setWorkflowId(workflowId);
        currentWorkflow.setWorkflowName(workflow.getName());
        currentWorkflow.setWorkflowDescription(workflow.getDescription());
        String templateId = workflow.getTemplate().getName();

        if (StringUtils.isEmpty(templateId)) {
            throw new WorkflowException("Template ID can't be empty");
        }
        currentWorkflow.setTemplateId(templateId);
        String workflowImplId = workflow.getEngine();

        if (StringUtils.isEmpty(workflowImplId)) {
            throw new WorkflowException("Workflowimpl ID can't be empty");
        }
        currentWorkflow.setWorkflowImplId(workflowImplId);
        return currentWorkflow;
    }

    private WorkflowResponse getWorkflowDetails(Workflow currentWorkflow, List<Parameter> workflowParameters)
            throws WorkflowException {

        WorkflowResponse detailedWorkflow = new WorkflowResponse();
        detailedWorkflow.setId(currentWorkflow.getWorkflowId());
        detailedWorkflow.setName(currentWorkflow.getWorkflowName());
        detailedWorkflow.setDescription(currentWorkflow.getWorkflowDescription());
        WorkflowTemplateBase workflowTemplate = new WorkflowTemplateBase();
        workflowTemplate.setName(currentWorkflow.getTemplateId());

        Map<Integer, WorkflowTemplateParametersBase> templateParamsMap = new HashMap<>();

        for (Parameter parameter : workflowParameters) {

            if (Constants.TEMPLATE.equals(parameter.getHolder())) {
                String[] params = parameter.getqName().split(Constants.STEP_NAME_DELIMITER);
                int stepNumber = Integer.parseInt(params[1]);

                // Check if there's already a WorkflowTemplateParameters object for this step
                WorkflowTemplateParametersBase templateParameters = templateParamsMap.getOrDefault(stepNumber,
                        new WorkflowTemplateParametersBase());
                templateParameters.setStep(stepNumber);

                // Create and add new OptionDetails
                OptionDetails details = new OptionDetails();
                details.setEntity(params[2]);
                details.setValues(Arrays.asList(parameter.getParamValue().split(Constants.PARAMETER_VALUE_SEPARATOR)));

                // Ensure the list exists and add new details
                if (templateParameters.getOptions() == null) {
                    templateParameters.setOptions(new ArrayList<>());
                }
                templateParameters.getOptions().add(details);

                // Update the map
                templateParamsMap.put(stepNumber, templateParameters);
            }
        }
        List<WorkflowTemplateParametersBase> templateParams = new ArrayList<>(templateParamsMap.values());
        workflowTemplate.setSteps(templateParams);
        detailedWorkflow.setTemplate(workflowTemplate);
        detailedWorkflow.setEngine(currentWorkflow.getWorkflowImplId());
        return detailedWorkflow;
    }

    private WorkflowAssociationListResponse createAssociationListResponse(int tenantId,
            WorkflowAssociationListItem[] workflowAssociationListItems,
            Integer offset,
            String filter) throws WorkflowException {

        WorkflowAssociationListResponse workflowAssociationListResponse = new WorkflowAssociationListResponse();
        workflowAssociationListResponse.setTotalResults(workflowManagementService.getAssociationsCount(tenantId,
                filter));
        if (workflowAssociationListItems != null && workflowAssociationListItems.length > 0) {
            workflowAssociationListResponse.setWorkflowAssociations(Arrays.asList(workflowAssociationListItems));
            workflowAssociationListResponse.setCount(workflowAssociationListItems.length);
        } else {
            workflowAssociationListResponse.setWorkflowAssociations(Collections.emptyList());
            workflowAssociationListResponse.setCount(0);
        }
        workflowAssociationListResponse.setStartIndex(offset != null ? offset + 1 : 1);
        return workflowAssociationListResponse;
    }

    private WorkflowAssociationListItem getAssociation(Association association) {

        WorkflowAssociationListItem associationListItem = null;

        if (association != null) {
            associationListItem = new WorkflowAssociationListItem();
            associationListItem.setId(association.getAssociationId());
            associationListItem.setAssociationName(association.getAssociationName());
            associationListItem.setOperation(Operation.valueOf(association.getEventId()));
            associationListItem.setWorkflowName(association.getWorkflowName());
            associationListItem.setIsEnabled(association.isEnabled());
        }
        return associationListItem;
    }

    private WorkflowAssociationResponse getAssociationDetails(Association association) {

        WorkflowAssociationResponse associationResponse = null;

        if (association != null) {
            associationResponse = new WorkflowAssociationResponse();
            associationResponse.setId(association.getAssociationId());
            associationResponse.setAssociationName(association.getAssociationName());
            associationResponse.setOperation(Operation.valueOf(association.getEventId()));
            associationResponse.setWorkflowName(association.getWorkflowName());
            associationResponse.setIsEnabled(association.isEnabled());
        }
        return associationResponse;
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    private static String includeData(String errorMsg, String data) {

        String message = errorMsg;
        if (data != null) {
            message = String.format(errorMsg, data);
        }
        return message;
    }

    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = String.format(error.getDescription(), "");
        }
        return message;
    }

    private APIError handleServerError(Constants.ErrorMessage errorEnum, String data, Exception e) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, includeData(e.getMessage(), data));
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    private APIError handleClientError(Constants.ErrorMessage errorEnum, String data, WorkflowClientException e) {

        ErrorResponse errorResponse;

        if (e.getMessage() != null) {
            String errorMessage = e.getMessage();
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            errorResponse.setDescription(errorMessage);
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, includeData(e.getMessage(), data));
        }
        return new APIError(Response.Status.BAD_REQUEST, errorResponse);
    }

    public void deleteWorkflowInstance(String instanceId) {

        try {
            if (StringUtils.isBlank(instanceId)) {
                throw new WorkflowClientException("Workflow instance ID cannot be null or empty.");
            }
            workflowManagementService.deleteWorkflowRequestCreatedByAnyUser(instanceId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_WORKFLOW_INSTANCE_NOT_FOUND, instanceId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_WORKFLOW_INSTANCE, instanceId, e);
        }
    }

    public WorkflowInstanceResponse getWorkflowInstanceById(String instanceId) {
        try {
            if (StringUtils.isBlank(instanceId)) {
                throw new WorkflowClientException("Workflow instance ID cannot be null or empty.");
            }
            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest = workflowManagementService
                    .getWorkflowRequestBean(instanceId);
            if (workflowRequest == null) {
                throw new WorkflowClientException("Workflow instance with ID: " + instanceId + " does not exist.");
            }
            return mapWorkflowRequestToWorkflowRequestResponse(workflowRequest);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_WORKFLOW_INSTANCE_NOT_FOUND, instanceId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_WORKFLOW_INSTANCE, instanceId,
                    e);
        }
    }

    private WorkflowInstanceResponse mapWorkflowRequestToWorkflowRequestResponse(
            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest)
            throws WorkflowException {

        if (workflowRequest == null) {
            return null;
        }
        WorkflowInstanceResponse response = new WorkflowInstanceResponse();
        response.setWorkflowInstanceId(workflowRequest.getRequestId());
        response.setEventType(Operation.fromValue(workflowRequest.getEventType()));
        response.setRequestInitiator(workflowRequest.getCreatedBy());
        try {
            if (workflowRequest.getCreatedAt() != null) {
                response.setCreatedAt(workflowRequest.getCreatedAt());
            }
            if (workflowRequest.getUpdatedAt() != null) {
                response.setUpdatedAt(workflowRequest.getUpdatedAt());
            }
        } catch (DateTimeParseException e) {
            throw new WorkflowClientException("Invalid date format from database. Expected: yyyy-MM-dd HH:mm:ss.SSS");
        }

        response.setStatus(InstanceStatus.fromValue(workflowRequest.getStatus()));
        response.setRequestParameters(workflowRequest.getRequestParams());
        return response;
    }

    private WorkflowInstanceListItem mapWorkflowRequestToListItem(
            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest) throws WorkflowException {
        if (workflowRequest == null) {
            return null;
        }

        WorkflowInstanceListItem item = new WorkflowInstanceListItem();
        
        if (workflowRequest.getRequestId() != null) {
            item.setWorkflowInstanceId(workflowRequest.getRequestId());
        } else {
            throw new WorkflowClientException("Workflow request ID cannot be null.");
        }

        if (workflowRequest.getEventType() != null) {
            item.setEventType(Operation.fromValue(workflowRequest.getEventType()));
        }
        if (workflowRequest.getCreatedBy() != null) {
            item.setRequestInitiator(workflowRequest.getCreatedBy());
        }

        try {
            if (workflowRequest.getCreatedAt() != null) {
                item.setCreatedAt(workflowRequest.getCreatedAt());
            }
            if (workflowRequest.getUpdatedAt() != null) {
                item.setUpdatedAt(workflowRequest.getUpdatedAt());
            }
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date in workflow request: " + e.getMessage());
            throw new WorkflowClientException(
                    "Invalid date format in workflow request. Expected format: yyyy-MM-dd HH:mm:ss.SSS");

        }

        if (workflowRequest.getStatus() != null) {
            item.setStatus(InstanceStatus.fromValue(workflowRequest.getStatus()));
        }

        return item;
    }

    public WorkflowInstanceListResponse getWorkflowInstances(Integer limit, Integer offset, String filter) {
        try {
            limit = validateLimit(limit);
            offset = validateOffset(offset);
            return getPaginatedWorkflowInstances(limit, offset, filter);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_LISTING_WORKFLOW_INSTANCES, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOW_INSTANCES, null, e);
        }
    }

    private WorkflowInstanceListResponse getPaginatedWorkflowInstances(Integer limit, Integer offset, String filter)
            throws WorkflowException {

        try {
            String user = CarbonContext.getThreadLocalCarbonContext().getUsername();
            int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();

            WorkflowInstanceListResponse workflowInstanceListResponse = new WorkflowInstanceListResponse();
            workflowInstanceListResponse.setInstances(new ArrayList<>());

            Map<String, String> filterMap = parseWorkflowFilter(filter);

            String requestType = "ALL_TASKS";
            String beginDate = null;
            String endDate = null;
            String dateCategory = null;
            String status = null;

            if (filterMap != null && !filterMap.isEmpty()) {
                // Use filter values if provided
                requestType = blankToNull(filterMap.get("requestType"));
                if (requestType == null) {
                    requestType = "ALL_TASKS";
                }
                beginDate = blankToNull(filterMap.get("beginDate"));
                endDate = blankToNull(filterMap.get("endDate"));
                dateCategory = blankToNull(filterMap.get("dateCategory"));
                status = blankToNull(filterMap.get("status"));
            }

            String normalizedRequestType = requestType.toUpperCase();
            if (!"ALL_TASKS".equals(normalizedRequestType) && !"MY_TASKS".equals(normalizedRequestType)) {
                throw new WorkflowClientException("Invalid request type: " + requestType +
                        ". Valid types are 'ALL_TASKS' and 'MY_TASKS'.");
            }

            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest[] response = workflowManagementService
                    .getRequestsFromFilter(
                            "MY_TASKS".equals(normalizedRequestType) ? user : null,
                            beginDate,
                            endDate,
                            dateCategory,
                            tenantId,
                            status, limit, offset);

            List<WorkflowInstanceListItem> allItems = new ArrayList<>();
            if (response != null) {
                for (org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest : response) {
                    if (workflowRequest != null) {
                        WorkflowInstanceListItem item = mapWorkflowRequestToListItem(workflowRequest);
                        if (item != null) {
                            allItems.add(item);
                        }
                    }
                }
            }

            int totalCount = allItems.size();
            int startIndex = offset != null ? offset : 0;

            workflowInstanceListResponse.setInstances(allItems);
            workflowInstanceListResponse.setCount(totalCount);
            workflowInstanceListResponse.setStartIndex(startIndex + 1);
            workflowInstanceListResponse.setTotalResults(totalCount);

            return workflowInstanceListResponse;
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOW_INSTANCES, null, e);
        } catch (Exception e) {
            throw new WorkflowException("Unexpected error while listing workflow instances", e);
        }
    }

    private String blankToNull(String value) {

        return StringUtils.isBlank(value) ? null : value;
    }

    public Map<String, String> parseWorkflowFilter(String filter) {
        
    Map<String, String> result = new HashMap<>();

    if (StringUtils.isBlank(filter)) {
        return result;
    }

    try {
        String decodedFilter = URLDecoder.decode(filter, StandardCharsets.UTF_8.name());
        
        decodedFilter = decodedFilter.replace("+", " ");
        
        decodedFilter = decodedFilter.replace("%27", "'")
                                   .replace("%22", "\"");
        
        String[] conditions = decodedFilter.split("(?i)\\s+and\\s+");
        
        Pattern pattern = Pattern.compile(
            "(\\w+)\\s+(eq|ge|le)\\s+['\"]([^'\"]*)['\"]",
            Pattern.CASE_INSENSITIVE
        );

        for (String condition : conditions) {
            String trimmedCondition = condition.trim();
            Matcher matcher = pattern.matcher(trimmedCondition);
            
            if (matcher.matches()) {
                String field = matcher.group(1).toLowerCase();
                String operator = matcher.group(2).toLowerCase();
                String value = matcher.group(3);

                switch (field) {
                    case "requesttype":
                    case "status":
                    case "datecategory":
                        if ("eq".equals(operator)) {
                            result.put(field, value);
                        } else {
                            throw new IllegalArgumentException(
                                "Unsupported operator for " + field + ": " + operator
                            );
                        }
                        break;
                    case "begindate":
                        if ("ge".equals(operator)) {
                            result.put("beginDate", value);
                        } else {
                            throw new IllegalArgumentException(
                                "Only 'ge' operator supported for beginDate"
                            );
                        }
                        break;
                    case "enddate":
                        if ("le".equals(operator)) {
                            result.put("endDate", value);
                        } else {
                            throw new IllegalArgumentException(
                                "Only 'le' operator supported for endDate"
                            );
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(
                            "Unknown field in filter: " + field
                        );
                }
            } else {
                throw new IllegalArgumentException(
                    "Invalid filter format: " + trimmedCondition + 
                    "\nExpected format: field operator 'value'"
                );
            }
        }
    } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException("Failed to decode filter string", e);
    }

    return result;
    }
}

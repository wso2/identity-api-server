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
import org.wso2.carbon.identity.api.server.workflow.common.NotificationConstants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ApproverNotifications;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.InitiatorNotifications;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.InstanceStatus;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Operation;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.OptionDetails;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Property;
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
import org.wso2.carbon.identity.workflow.engine.ApprovalTaskService;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineClientException;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineException;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.Workflow;
import org.wso2.carbon.identity.workflow.mgt.dto.Association;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowEvent;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowClientException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;
import org.wso2.carbon.identity.workflow.mgt.util.WFConstant;
import org.wso2.carbon.identity.workflow.mgt.util.WorkflowRequestStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Workflow service class.
 */
public class WorkflowService {

    private static final Log log = LogFactory.getLog(WorkflowService.class);
    private static final String CHANNELS = "channels";
    private static final String EVENTS = "events";
    private final WorkflowManagementService workflowManagementService;
    private final ApprovalTaskService approvalEventService;

    public WorkflowService(WorkflowManagementService workflowManagementService,
                           ApprovalTaskService approvalEventService) {

        this.workflowManagementService = workflowManagementService;
        this.approvalEventService = approvalEventService;
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
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (workflowManagementService.isWorkflowExistByName(workflow.getName(), tenantDomain)) {
                throw new WorkflowClientException("A workflow with name: " + workflow.getName() + " already exists.");
            }
            String workflowId = UUID.randomUUID().toString();
            currentWorkflow = createWorkflow(workflow, workflowId);
            List<WorkflowTemplateParameters> templateProperties = workflow.getTemplate().getSteps();
            List<Parameter> parameterList = createParameterList(workflowId, templateProperties);

            // Add notification parameters for both initiator and approvers
            addNotificationParameters(parameterList, workflowId, workflow);

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

            // Add notification parameters for both initiator and approvers
            addNotificationParameters(parameterList, workflowId, workflow);

            List<Parameter> oldPrameterList =
                    workflowManagementService.getWorkflowParameters(workflowId);
            workflowManagementService.addWorkflow(currentWorkflow, parameterList,
                    CarbonContext.getThreadLocalCarbonContext().getTenantId());
            // Update the corresponding approval tasks if there are any.
            approvalEventService.updatePendingApprovalTasksOnWorkflowUpdate(workflowId, parameterList, oldPrameterList);
            return getWorkflow(workflowId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_UPDATING_WORKFLOW, workflowId, e);
        } catch (WorkflowException | WorkflowEngineException e) {
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
            throw handleClientError(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_WORKFLOW_NOT_FOUND,
                    workflowId, e);
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
            int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
            if (!workflowManagementService.listPaginatedAssociations(tenantId, 1, 0,
                    "operation eq " + event.getEventId()).isEmpty()) {
                throw new WorkflowClientException("A workflow association already exists for the event: " +
                        event.getEventFriendlyName());
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
            throw handleClientError(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_ASSOCIATION_NOT_FOUND,
                    associationId, e);
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

            // Ensure at least one association exists for the related workflow before allowing deletion.
            List<Association> associationsForWorkflow =
                    workflowManagementService.getAssociationsForWorkflow(association.getWorkflowId());
            if (associationsForWorkflow == null || associationsForWorkflow.size() <= 1) {
                throw new WorkflowClientException("The workflow association with ID: " + associationId +
                        " cannot be deleted as it is the only association for the related workflow: " +
                        association.getWorkflowId());
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

    /**
     * Add notification parameters to the parameter list.
     *
     * @param parameterList      List of parameters to add to
     * @param workflowId         Workflow ID
     * @param workflow           Workflow request containing notification settings
     * @throws WorkflowClientException if validation fails
     */
    private void addNotificationParameters(List<Parameter> parameterList, String workflowId,
                                           WorkflowRequest workflow) throws WorkflowClientException {

        // Add notification parameters for initiator
        if (workflow.getNotificationsForInitiator() != null) {
            InitiatorNotifications initiatorNotifications = workflow.getNotificationsForInitiator();

            addNotificationChannelsAndEvents(
                    parameterList,
                    workflowId,
                    initiatorNotifications.getChannels(),
                    initiatorNotifications.getEvents(),
                    Constants.NOTIFICATION_FOR_INITIATOR,
                    WFConstant.ParameterHolder.WORKFLOW_IMPL
            );
        }

        // Add notification parameters for approvers
        if (workflow.getTemplate() != null && workflow.getTemplate().getNotificationsForApprovers() != null) {
            ApproverNotifications approverNotifications = workflow.getTemplate().getNotificationsForApprovers();

            addNotificationChannelsAndEvents(
                    parameterList,
                    workflowId,
                    approverNotifications.getChannels(),
                    approverNotifications.getEvents(),
                    Constants.NOTIFICATION_FOR_APPROVER,
                    Constants.TEMPLATE
            );
        }
    }

    /**
     * Helper method to add notification channels and events as parameters.
     *
     * @param parameterList      List of parameters to add to
     * @param workflowId         Workflow ID
     * @param channels           List of channel strings
     * @param events             List of event strings
     * @param qNamePrefix        Prefix for qName (e.g., NotificationForInitiator)
     * @param holder             Parameter holder (e.g., Workflow or Template)
     * @throws WorkflowClientException if validation fails
     */
    private void addNotificationChannelsAndEvents(List<Parameter> parameterList, String workflowId,
                                                   List<String> channels, List<String> events,
                                                   String qNamePrefix, String holder)
                                                   throws WorkflowClientException {

        boolean isInitiator = Constants.NOTIFICATION_FOR_INITIATOR.equals(qNamePrefix);

        // Validate and add channels parameter
        if (channels != null && !channels.isEmpty()) {
            // Validate channels
            for (String channel : channels) {
                if (!NotificationConstants.isValidChannel(channel)) {
                    throw new WorkflowClientException("Invalid notification channel: " + channel +
                            ". Supported channels: " + NotificationConstants.getSupportedChannels());
                }
            }

            Parameter channelsParam = setWorkflowImplParameters(workflowId, Constants.NOTIFICATION,
                    String.join(Constants.PARAMETER_VALUE_SEPARATOR, channels),
                    qNamePrefix + Constants.STEP_NAME_DELIMITER + CHANNELS,
                    holder);
            parameterList.add(channelsParam);
        }

        // Validate and add events parameter
        if (events != null && !events.isEmpty()) {
            // Validate events based on context (initiator vs approver)
            for (String event : events) {
                boolean isValid = isInitiator
                        ? NotificationConstants.isValidInitiatorEvent(event)
                        : NotificationConstants.isValidApproverEvent(event);

                if (!isValid) {
                    String supportedEvents = isInitiator
                            ? NotificationConstants.getSupportedInitiatorEvents().toString()
                            : NotificationConstants.getSupportedApproverEvents().toString();
                    throw new WorkflowClientException("Invalid notification event: " + event +
                            " for " + (isInitiator ? "initiator" : "approver") +
                            ". Supported events: " + supportedEvents);
                }
            }

            Parameter eventsParam = setWorkflowImplParameters(workflowId, Constants.NOTIFICATION,
                    String.join(Constants.PARAMETER_VALUE_SEPARATOR, events),
                    qNamePrefix + Constants.STEP_NAME_DELIMITER + EVENTS,
                    holder);
            parameterList.add(eventsParam);
        }
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
        List<String> initiatorChannels = new ArrayList<>();
        List<String> initiatorEvents = new ArrayList<>();
        List<String> approverChannels = new ArrayList<>();
        List<String> approverEvents = new ArrayList<>();
        boolean hasInitiatorNotifications = false;
        boolean hasApproverNotifications = false;

        for (Parameter parameter : workflowParameters) {

            if (Constants.TEMPLATE.equals(parameter.getHolder())) {
                String qName = parameter.getqName();

                // Handle notification parameters for approvers
                if (qName.startsWith(Constants.NOTIFICATION_FOR_APPROVER)) {
                    hasApproverNotifications = true;
                    if (qName.endsWith(CHANNELS)) {
                        approverChannels.addAll(Arrays.asList(parameter.getParamValue().
                                split(Constants.PARAMETER_VALUE_SEPARATOR)));
                    } else if (qName.endsWith(EVENTS)) {
                        approverEvents.addAll(Arrays.asList(parameter.getParamValue().
                                split(Constants.PARAMETER_VALUE_SEPARATOR)));
                    }
                } else {
                    // Handle approval step parameters
                    String[] params = qName.split(Constants.STEP_NAME_DELIMITER);
                    int stepNumber = Integer.parseInt(params[1]);

                    // Check if there's already a WorkflowTemplateParameters object for this step
                    WorkflowTemplateParametersBase templateParameters = templateParamsMap.getOrDefault(stepNumber,
                            new WorkflowTemplateParametersBase());
                    templateParameters.setStep(stepNumber);

                    // Create and add new OptionDetails
                    OptionDetails details = new OptionDetails();
                    details.setEntity(params[2]);
                    details.setValues(Arrays.asList(parameter.getParamValue().
                            split(Constants.PARAMETER_VALUE_SEPARATOR)));

                    // Ensure the list exists and add new details
                    if (templateParameters.getOptions() == null) {
                        templateParameters.setOptions(new ArrayList<>());
                    }
                    templateParameters.getOptions().add(details);

                    // Update the map
                    templateParamsMap.put(stepNumber, templateParameters);
                }
            } else if (WFConstant.ParameterHolder.WORKFLOW_IMPL.equals(parameter.getHolder())) {
                // Handle notification parameters for initiator
                String qName = parameter.getqName();
                if (qName.startsWith(Constants.NOTIFICATION_FOR_INITIATOR)) {
                    hasInitiatorNotifications = true;
                    if (qName.endsWith(CHANNELS)) {
                        initiatorChannels.addAll(Arrays.asList(parameter.getParamValue().
                                split(Constants.PARAMETER_VALUE_SEPARATOR)));
                    } else if (qName.endsWith(EVENTS)) {
                        initiatorEvents.addAll(Arrays.asList(parameter.getParamValue().
                                split(Constants.PARAMETER_VALUE_SEPARATOR)));
                    }
                }
            }
        }

        List<WorkflowTemplateParametersBase> templateParams = new ArrayList<>(templateParamsMap.values());
        workflowTemplate.setSteps(templateParams);

        // Set approver notifications if present
        if (hasApproverNotifications) {
            ApproverNotifications approverNotifications = new ApproverNotifications();
            if (!approverChannels.isEmpty()) {
                approverNotifications.setChannels(approverChannels);
            }
            if (!approverEvents.isEmpty()) {
                approverNotifications.setEvents(approverEvents);
            }
            workflowTemplate.setNotificationsForApprovers(approverNotifications);
        }

        detailedWorkflow.setTemplate(workflowTemplate);

        // Set initiator notifications if present
        if (hasInitiatorNotifications) {
            InitiatorNotifications initiatorNotifications = new InitiatorNotifications();
            if (!initiatorChannels.isEmpty()) {
                initiatorNotifications.setChannels(initiatorChannels);
            }
            if (!initiatorEvents.isEmpty()) {
                initiatorNotifications.setEvents(initiatorEvents);
            }
            detailedWorkflow.setNotificationsForInitiator(initiatorNotifications);
        }

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

    public void abortWorkflowInstance(String instanceId) {

        try {
            if (StringUtils.isBlank(instanceId)) {
                throw new WorkflowClientException("Workflow instance ID cannot be null or empty.");
            }
            if (!WorkflowRequestStatus.PENDING.name()
                    .equals(workflowManagementService.getWorkflowRequestBean(instanceId).getStatus())) {
                throw new WorkflowClientException("Only PENDING workflow instances can be aborted.");
            }
            workflowManagementService.abortWorkflowRequest(instanceId);
            approvalEventService.deletePendingApprovalTasks(instanceId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_ABORTING_WORKFLOW_INSTANCE,
                    instanceId, e);
        } catch (WorkflowEngineClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_ABORTING_WORKFLOW_INSTANCE,
                    instanceId, new WorkflowClientException(e.getMessage(), e));
        } catch (WorkflowException | WorkflowEngineException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_ABORTING_WORKFLOW_INSTANCE, instanceId, e);
        }
    }

    /**
     * Deletes a workflow instance by its ID.
     *
     * @param instanceId The ID of the workflow instance to delete.
     */
    public void deleteWorkflowInstance(String instanceId) {

        try {
            if (StringUtils.isBlank(instanceId)) {
                throw new WorkflowClientException("Workflow instance ID cannot be null or empty.");
            }
            workflowManagementService.permanentlyDeleteWorkflowRequestByAnyUser(instanceId);
            approvalEventService.deletePendingApprovalTasks(instanceId);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_DELETING_WORKFLOW_INSTANCE,
                    instanceId, e);
        } catch (WorkflowEngineClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_DELETING_WORKFLOW_INSTANCE,
                    instanceId, new WorkflowClientException(e.getMessage(), e));
        } catch (WorkflowException | WorkflowEngineException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_WORKFLOW_INSTANCE, instanceId, e);
        }
    }

    /**
     * Retrieves a workflow instance by its ID.
     *
     * @param instanceId The ID of the workflow instance to retrieve.
     * @return WorkflowInstanceResponse containing the workflow instance details.
     */
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
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_WORKFLOW_INSTANCE_NOT_FOUND,
                    instanceId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_WORKFLOW_INSTANCE, instanceId,
                    e);
        }
    }

    /**
     * Retrieves a list of workflow instances with pagination and filtering.
     *
     * @param limit  Maximum number of instances to return.
     * @param offset Offset for pagination.
     * @param filter Filter string to apply on the results.
     * @return WorkflowInstanceListResponse containing the list of workflow instances.
     */
    public WorkflowInstanceListResponse getWorkflowInstances(Integer limit, Integer offset, String filter) {

        limit = validateLimit(limit);
        offset = validateOffset(offset);
        try {
            return getPaginatedWorkflowInstances(limit, offset, filter);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_CLIENT_ERROR_LISTING_WORKFLOW_INSTANCES, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOW_INSTANCES, null, e);
        }
    }

    /**
     * Maps a WorkflowRequest object to a WorkflowInstanceResponse object.
     *
     * @param workflowRequest The WorkflowRequest object to map.
     * @return WorkflowInstanceResponse object containing the mapped data.
     * @throws WorkflowException If an error occurs during mapping.
     */
    private WorkflowInstanceResponse mapWorkflowRequestToWorkflowRequestResponse(
            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest)
            throws WorkflowException {

        if (workflowRequest == null) {
            return null;
        }
        WorkflowInstanceResponse response = new WorkflowInstanceResponse();
        response.setWorkflowInstanceId(workflowRequest.getRequestId());
        response.setEventType(Operation.fromValue(workflowRequest.getOperationType()));
        response.setRequestInitiator(workflowRequest.getCreatedBy());

        if (workflowRequest.getProperties() != null) {

            List<Property> properties = workflowRequest.getProperties().stream().map(property -> {
                Property prop = new Property();
                prop.setKey(property.getKey());
                prop.setValue(property.getValue());
                return prop;
            }).collect(Collectors.toList());
            response.setProperties(properties);
        }
        try {
            if (workflowRequest.getCreatedAt() != null) {
                response.setCreatedAt(workflowRequest.getCreatedAt());
            }
            if (workflowRequest.getUpdatedAt() != null) {
                response.setUpdatedAt(workflowRequest.getUpdatedAt());
            }
        } catch (DateTimeParseException e) {
            throw new WorkflowClientException("Invalid date format from database. Expected: yyyy-MM-dd:HH:mm:ss.SSS");
        }

        response.setStatus(InstanceStatus.fromValue(workflowRequest.getStatus()));
        response.setRequestParameters(workflowRequest.getRequestParams());
        return response;
    }

    /**
     * Maps a WorkflowRequest object to a WorkflowInstanceListItem object.
     *
     * @param workflowRequest The WorkflowRequest object to map.
     * @return WorkflowInstanceListItem object containing the mapped data.
     */
    private WorkflowInstanceListItem mapWorkflowRequestToListItem(
            org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest) {

        if (workflowRequest == null) {
            return null;
        }

        WorkflowInstanceListItem workflowInstanceListItem = new WorkflowInstanceListItem();
        workflowInstanceListItem.setWorkflowInstanceId(workflowRequest.getRequestId());
        workflowInstanceListItem.setEventType(Operation.fromValue(workflowRequest.getOperationType()));
        workflowInstanceListItem.setRequestInitiator(workflowRequest.getCreatedBy());
        workflowInstanceListItem.setCreatedAt(workflowRequest.getCreatedAt());
        workflowInstanceListItem.setUpdatedAt(workflowRequest.getUpdatedAt());
        workflowInstanceListItem.setStatus(InstanceStatus.fromValue(workflowRequest.getStatus()));
        return workflowInstanceListItem;
    }

    /**
     * Retrieves paginated workflow instances based on the provided parameters.
     *
     * @param limit  Maximum number of instances to return.
     * @param offset Offset for pagination.
     * @param filter Filter string to apply on the results.
     * @return WorkflowInstanceListResponse containing the list of workflow instances.
     * @throws WorkflowException If an error occurs while retrieving the instances.
     */
    private WorkflowInstanceListResponse getPaginatedWorkflowInstances(Integer limit, Integer offset, String filter)
            throws WorkflowException {

        String user = null;
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();

        WorkflowInstanceListResponse workflowInstanceListResponse = new WorkflowInstanceListResponse();
        workflowInstanceListResponse.setInstances(new ArrayList<>());

        Map<String, String> filterMap = parseWorkflowFilter(filter);

        String beginDate = "1950-01-01 00:00:00.000";
        String endDate = getCurrentDateTime();
        String dateCategory = null;
        String status = null;
        String operationType = null;

        if (!filterMap.isEmpty()) {
            if (StringUtils.isNotEmpty(filterMap.get(Constants.WORKFLOW_INSTANCE_REQUEST_TYPE_KEY))) {
                String requestType = filterMap.get(Constants.WORKFLOW_INSTANCE_REQUEST_TYPE_KEY);
                if (!Constants.WORKFLOW_INSTANCE_MY_TASKS_REQUEST_TYPE.equals(requestType) &&
                        !Constants.WORKFLOW_INSTANCE_ALL_TASKS_REQUEST_TYPE.equals(requestType)) {
                    throw new WorkflowClientException("Invalid request type: " + requestType +
                            ". Valid types are 'MY_TASKS' and 'ALL_TASKS'.");
                }
                if (Constants.WORKFLOW_INSTANCE_MY_TASKS_REQUEST_TYPE.equals(requestType)) {
                    user = CarbonContext.getThreadLocalCarbonContext().getUsername();
                }
            }

            if (StringUtils.isNotEmpty(filterMap.get(Constants.WORKFLOW_INSTANCE_CREATED_START_DATE_KEY))) {
                beginDate = filterMap.get(Constants.WORKFLOW_INSTANCE_CREATED_START_DATE_KEY);
                dateCategory = "CREATED";
            } else if (StringUtils.isNotEmpty(filterMap.get(Constants.WORKFLOW_INSTANCE_UPDATED_START_DATE_KEY))) {
                dateCategory = "UPDATED";
                beginDate = filterMap.get(Constants.WORKFLOW_INSTANCE_UPDATED_START_DATE_KEY);
            }

            if (StringUtils.isNotEmpty(filterMap.get(Constants.WORKFLOW_INSTANCE_CREATED_END_DATE_KEY))) {
                endDate = filterMap.get(Constants.WORKFLOW_INSTANCE_CREATED_END_DATE_KEY);
                dateCategory = "CREATED";
            } else if (StringUtils.isNotEmpty(filterMap.get(Constants.WORKFLOW_INSTANCE_UPDATED_END_DATE_KEY))) {
                dateCategory = "UPDATED";
                endDate = filterMap.get(Constants.WORKFLOW_INSTANCE_UPDATED_END_DATE_KEY);
            }

            status = filterMap.get(Constants.WORKFLOW_INSTANCE_STATUS_KEY);
            operationType = filterMap.get(Constants.WORKFLOW_INSTANCE_OPERATION_TYPE_KEY);
        }

        org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequestFilterResponse response = workflowManagementService
                .getRequestsFromFilter(user, operationType, beginDate, endDate, dateCategory, tenantId, status, limit,
                        offset);

        org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest[] requests = response.getRequests();

        List<WorkflowInstanceListItem> allItems = new ArrayList<>();
        if (requests != null) {
            for (org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequest workflowRequest : requests) {
                WorkflowInstanceListItem item = mapWorkflowRequestToListItem(workflowRequest);
                allItems.add(item);
            }
        }

        int totalCount = allItems.size();
        int startIndex = offset;
        int totalResult = response.getTotalCount();

        workflowInstanceListResponse.setInstances(allItems);
        workflowInstanceListResponse.setCount(totalCount);
        workflowInstanceListResponse.setStartIndex(startIndex + 1);
        workflowInstanceListResponse.setTotalResults(totalResult);

        return workflowInstanceListResponse;
    }

    /**
     * Parses the filter string into a map of field-value pairs.
     * The expected format is: "field operator value and field operator value ..."
     * Supported fields: requestType, status, dateCategory, beginDate, endDate
     * Supported operators: eq (equals), ge (greater than or equal), le (less than or equal)
     *
     * @param filter Filter string
     * @return Map of field-value pairs
     * @throws WorkflowClientException If the filter format is invalid or unsupported
     */
    public Map<String, String> parseWorkflowFilter(String filter) throws WorkflowClientException {

        Map<String, String> result = new HashMap<>();

        if (StringUtils.isBlank(filter)) {
            return result;
        }
        
        try {
            // Decode the filter string to handle URL encoding.
            String decodedFilter = URLDecoder.decode(filter, StandardCharsets.UTF_8.name());

            // Parse the filter string using a more robust approach
            List<FilterCondition> conditions = parseFilterConditions(decodedFilter);

            // Process each condition and extract field, operator, and value.
            for (FilterCondition condition : conditions) {
                String field = condition.getField();
                String operator = condition.getOperator();
                String value = condition.getValue();

                switch (field) {
                    case Constants.WORKFLOW_INSTANCE_REQUEST_TYPE_KEY:
                    case Constants.WORKFLOW_INSTANCE_STATUS_KEY:
                    case Constants.WORKFLOW_INSTANCE_OPERATION_TYPE_KEY:
                        if (Constants.EQUALS_OPERATOR.equals(operator)) {
                            result.put(field, value);
                        } else {
                            throw new WorkflowClientException("Only `eq` operator is supported for " + field +
                                    ": " + operator);
                        }
                        break;
                    case Constants.WORKFLOW_INSTANCE_CREATED_DATE_KEY:
                        putValidatedDateInMap(result, Constants.WORKFLOW_INSTANCE_CREATED_DATE_KEY, value,
                                operator);
                        break;
                    case Constants.WORKFLOW_INSTANCE_UPDATED_DATE_KEY:
                        putValidatedDateInMap(result, Constants.WORKFLOW_INSTANCE_UPDATED_DATE_KEY, value,
                                operator);
                        break;
                    default:
                        throw new WorkflowClientException("Unknown field in filter: " + field);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new WorkflowClientException("Failed to decode filter string", e);
        }

        boolean hasCreatedDate = result.containsKey(Constants.WORKFLOW_INSTANCE_CREATED_START_DATE_KEY) ||
                result.containsKey(Constants.WORKFLOW_INSTANCE_CREATED_END_DATE_KEY);
        boolean hasUpdatedDate = result.containsKey(Constants.WORKFLOW_INSTANCE_UPDATED_START_DATE_KEY) ||
                result.containsKey(Constants.WORKFLOW_INSTANCE_UPDATED_END_DATE_KEY);
        if (hasCreatedDate && hasUpdatedDate) {
            throw new WorkflowClientException("Both createdAt and updatedAt date filters cannot be used together.");
        }

        return result;
    }

    /**
     * Parses filter conditions from a filter string using a two-step approach.
     * First splits by 'and' logical operator, then parses each condition by comparison operators.
     * 
     * @param filter The filter string to parse
     * @return List of FilterCondition objects
     * @throws WorkflowClientException If the filter format is invalid
     */
    private List<FilterCondition> parseFilterConditions(String filter) throws WorkflowClientException {
        
        List<FilterCondition> conditions = new ArrayList<>();
        
        // Step 1: Split by 'and' logical operator to get individual conditions
        List<String> conditionStrings = splitByAndLogicalOperator(filter);
        
        // Step 2: Parse each condition by comparison operators
        for (String conditionString : conditionStrings) {
            FilterCondition condition = parseIndividualCondition(conditionString.trim());
            conditions.add(condition);
        }
        
        if (conditions.isEmpty()) {
            throw new WorkflowClientException("No valid filter conditions found");
        }
        
        return conditions;
    }
    
    /**
     * Splits the filter string by the specified logical operator while preserving spaces within values.
     * 
     * @param filter The filter string to split
     * @return List of condition strings
     */
    private List<String> splitByAndLogicalOperator(String filter) {

        String logicalOperator = "and";
        List<String> conditions = new ArrayList<>();
        String normalizedFilter = filter.trim();
        int start = 0;
        
        while (start < normalizedFilter.length()) {
            // Find the next occurrence of the logical operator
            int operatorPos = findKeywordPosition(normalizedFilter, logicalOperator, start);
            
            if (operatorPos == -1) {
                // No more logical operators found, take the rest as a condition
                String condition = normalizedFilter.substring(start).trim();
                if (!condition.isEmpty()) {
                    conditions.add(condition);
                }
                break;
            }
            
            // Extract condition before the logical operator
            String condition = normalizedFilter.substring(start, operatorPos).trim();
            if (!condition.isEmpty()) {
                conditions.add(condition);
            }
            
            // Move past the logical operator
            start = operatorPos + logicalOperator.length();
        }
        
        return conditions;
    }
    
    /**
     * Parses an individual condition string to extract field, operator, and value.
     * 
     * @param conditionString The individual condition string (e.g., "createdAt ge 2021-07-26:23 45:25.831")
     * @return FilterCondition object
     * @throws WorkflowClientException If the condition format is invalid
     */
    private FilterCondition parseIndividualCondition(String conditionString) throws WorkflowClientException {
        
        String[] comparisonOperators = {"eq", "ge", "le"};
        
        // Find the comparison operator in the condition
        String matchedOperator = null;
        int matchedOperatorIndex = -1;
        
        for (String operator : comparisonOperators) {
            int keywordPosition = findKeywordPosition(conditionString, operator, 0);
            if (keywordPosition != -1) {
                if (matchedOperator == null || keywordPosition < matchedOperatorIndex) {
                    matchedOperator = operator;
                    matchedOperatorIndex = keywordPosition;
                }
            }
        }
        
        if (matchedOperator == null) {
            throw new WorkflowClientException("No valid comparison operator found in condition: " + conditionString +
                ". Supported operators are: eq, ge, le");
        }
        
        // Extract field (before operator)
        String fieldName = conditionString.substring(0, matchedOperatorIndex).trim();
        if (fieldName.isEmpty()) {
            throw new WorkflowClientException("Field name is missing in condition: " + conditionString);
        }
        
        // Extract value (after operator)
        String fieldValue = conditionString.substring(matchedOperatorIndex + matchedOperator.length()).trim();
        if (fieldValue.isEmpty()) {
            throw new WorkflowClientException("Value is missing in condition: " + conditionString);
        }
        
        // Validate operator
        if (!isValidOperator(matchedOperator)) {
            throw new WorkflowClientException("Invalid operator: " + matchedOperator +
                ". Supported operators are: eq, ge, le");
        }
        
        return new FilterCondition(fieldName, matchedOperator, fieldValue);
    }
    
    /**
     * Finds the position of a keyword in the filter string, ensuring it's surrounded by word boundaries.
     * 
     * @param filter The filter string to search in
     * @param keyword The keyword to find
     * @param startPosition The position to start searching from
     * @return The position of the keyword, or -1 if not found
     */
    private int findKeywordPosition(String filter, String keyword, int startPosition) {
        
        int position = startPosition;
        while (position < filter.length()) {
            // Find the keyword (case insensitive)
            int keywordPosition = filter.indexOf(keyword, position);
            if (keywordPosition == -1) {
                return -1;
            }
            
            // Check if it's a whole word (surrounded by non-alphanumeric characters or string boundaries)
            boolean isValidStart = (keywordPosition == 0)
                    || !Character.isLetterOrDigit(filter.charAt(keywordPosition - 1));
            boolean isValidEnd = (keywordPosition + keyword.length() >= filter.length()) ||
                              !Character.isLetterOrDigit(filter.charAt(keywordPosition + keyword.length()));
            
            if (isValidStart && isValidEnd) {
                return keywordPosition;
            }
            
            // Move past this occurrence and continue searching
            position = keywordPosition + 1;
        }
        
        return -1;
    }
    
    /**
     * Validates if the given operator is supported.
     * 
     * @param operator The operator to validate
     * @return true if the operator is valid, false otherwise
     */
    private boolean isValidOperator(String operator) {

        return Constants.EQUALS_OPERATOR.equals(operator) ||
               Constants.GREATER_THAN_OR_EQUAL_OPERATOR.equals(operator) ||
               Constants.LESS_THAN_OR_EQUAL_OPERATOR.equals(operator);
    }
    
    /**
     * Inner class to represent a filter condition.
     */
    private static class FilterCondition {

        private final String field;
        private final String operator;
        private final String value;
        
        public FilterCondition(String field, String operator, String value) {

            this.field = field;
            this.operator = operator;
            this.value = value;
        }
        
        public String getField() {

            return field;
        }
        
        public String getOperator() {

            return operator;
        }
        
        public String getValue() {

            return value;
        }
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

    private APIError handleClientError(Response.Status status, Constants.ErrorMessage errorEnum, String data,
                                       WorkflowClientException e) {

        ErrorResponse errorResponse;

        if (e.getMessage() != null) {
            String errorMessage = e.getMessage();
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            errorResponse.setDescription(errorMessage);
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, includeData(e.getMessage(), data));
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleClientError(Constants.ErrorMessage errorEnum, String data, WorkflowClientException e) {

        throw handleClientError(Response.Status.BAD_REQUEST, errorEnum, data, e);
    }

    /**
     * Get the current date time.
     *
     * @return The current time formatted as a string.
     */
    private String getCurrentDateTime() {

        return LocalDateTime.now().format(Constants.WORKFLOW_INSTANCE_DATE_TIME_FORMATTER);
    }

    private LocalDateTime parseDateTime(String date, String dateType) throws WorkflowClientException {

        try {
            return LocalDateTime.parse(date, Constants.WORKFLOW_INSTANCE_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                LocalDate localDate = LocalDate.parse(date, Constants.WORKFLOW_INSTANCE_DATE_FORMATTER);
                return localDate.atStartOfDay();
            } catch (DateTimeParseException e1) {
                throw new WorkflowClientException(String.format("Invalid date for %s. Expected formats: " +
                        "yyyy-MM-dd | yyyy-MM-dd:HH:mm:ss.SSS", dateType));
            }
        }
    }

    private void putValidatedDateInMap(Map<String, String> filterMap, String key, String value, String operator)
            throws WorkflowClientException {

        LocalDateTime newDate = parseDateTime(value, key);
        value = newDate.format(Constants.WORKFLOW_INSTANCE_DATE_TIME_FORMATTER);

        if (Constants.GREATER_THAN_OR_EQUAL_OPERATOR.equals(operator)) {
            String storeKey = Constants.WORKFLOW_INSTANCE_CREATED_START_DATE_KEY;
            if (Constants.WORKFLOW_INSTANCE_UPDATED_DATE_KEY.equals(key)) {
                storeKey = Constants.WORKFLOW_INSTANCE_UPDATED_START_DATE_KEY;
            }
            if (filterMap.get(storeKey) == null) {
                filterMap.put(storeKey, value);
                return;
            }
            LocalDateTime existingDate = LocalDateTime.parse(filterMap.get(storeKey),
                    Constants.WORKFLOW_INSTANCE_DATE_TIME_FORMATTER);
            if (newDate.isAfter(existingDate)) {
                filterMap.put(storeKey, value);
            }
        } else if (Constants.LESS_THAN_OR_EQUAL_OPERATOR.equals(operator)) {
            String storeKey = Constants.WORKFLOW_INSTANCE_CREATED_END_DATE_KEY;
            if (Constants.WORKFLOW_INSTANCE_UPDATED_DATE_KEY.equals(key)) {
                storeKey = Constants.WORKFLOW_INSTANCE_UPDATED_END_DATE_KEY;
            }
            if (filterMap.get(storeKey) == null) {
                filterMap.put(storeKey, value);
                return;
            }
            LocalDateTime existingDate = LocalDateTime.parse(filterMap.get(storeKey),
                    Constants.WORKFLOW_INSTANCE_DATE_TIME_FORMATTER);
            if (existingDate.isAfter(newDate)) {
                filterMap.put(storeKey, value);
            }
        } else {
            throw new WorkflowClientException("Only 'ge' & 'le' operator supported for date filtering");
        }
    }
}

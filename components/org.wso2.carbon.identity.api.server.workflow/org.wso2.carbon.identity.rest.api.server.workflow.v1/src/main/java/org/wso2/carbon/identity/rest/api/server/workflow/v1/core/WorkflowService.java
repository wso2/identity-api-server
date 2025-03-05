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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.workflow.common.Constants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.*;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementAdminService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.Workflow;
import org.wso2.carbon.identity.workflow.mgt.dto.Association;
import org.wso2.carbon.identity.workflow.mgt.dto.Template;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowEvent;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowClientException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;

import javax.ws.rs.core.Response;
import java.util.*;


public class WorkflowService {

    private static final Log log = LogFactory.getLog(WorkflowManagementAdminService.class);
    private final WorkflowManagementService workflowManagementService;

    public WorkflowService(WorkflowManagementService workflowManagementService) {

        this.workflowManagementService = workflowManagementService;

    }

    private WorkflowSummary getWorkflow(Workflow workflowBean) {

        WorkflowSummary workflow = null;

        if (workflowBean != null) {

            workflow = new WorkflowSummary();

            workflow.setId(workflowBean.getWorkflowId());
            workflow.setWorkflowName(workflowBean.getWorkflowName());
            workflow.setWorkflowDescription(workflowBean.getWorkflowDescription());
            workflow.setWorkflowTemplate(workflowBean.getTemplateId());
            workflow.setWorkflowEngine(workflowBean.getWorkflowImplId());
        }
        return workflow;
    }

    private WorkflowAssociation getAssociation(Association associationBean) {

        WorkflowAssociation association = null;

        if (associationBean != null) {

            association = new WorkflowAssociation();

            association.setId(associationBean.getWorkflowId());
            association.setWorkflowAssociationName(associationBean.getAssociationName());
            association.setOperationName(associationBean.getEventName());
            association.setWorkflowName(associationBean.getWorkflowName());
            association.setAssociationCondition(associationBean.getCondition());
            association.setIsEnabled(associationBean.isEnabled());
        }
        return association;
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

    private Workflow createWorkflow(WorkflowCreation workflow, String workflowId) throws WorkflowException {

        Workflow workflowBean = new Workflow();

        workflowBean.setWorkflowId(workflowId);
        workflowBean.setWorkflowName(workflow.getWorkflowName());
        workflowBean.setWorkflowDescription(workflow.getWorkflowDescription());
        String templateId = workflow.getWorkflowTemplate().getName();

        if (templateId == null) {
            throw new WorkflowException("Template id can't be empty");
        }
        workflowBean.setTemplateId(templateId);

        String workflowImplId = workflow.getWorkflowEngine();
        if (workflowImplId == null) {
            throw new WorkflowException("Workflowimpl id can't be empty");
        }
        workflowBean.setWorkflowImplId(workflowImplId);

        return workflowBean;

    }

    /**
     * Add new workflow
     *
     * @param workflow Workflow details
     * @return WorkflowSummary
     */
    public WorkflowSummary addWorkflow(WorkflowCreation workflow) {

        WorkflowSummary workflowSummary;
        Workflow workflowBean;

        try {
            String workflowId = UUID.randomUUID().toString();
            workflowBean = createWorkflow(workflow, workflowId);

            // Setting up the Template ParameterList

            List<WorkflowTemplateParameters> templateProperties = workflow.getWorkflowTemplate().getSteps();

            List<Parameter> parameterList = new ArrayList<>();

            for (WorkflowTemplateParameters properties : templateProperties) {
                for (OptionDetails options : properties.getOptions()) {
                    Parameter parameter = setWorkflowImplParameters(null, "UserAndRole",
                            options.getValues(),
                            "UserAndRole-step-" + properties.getSteps() + "-" + options.getEntity(),
                            "Template");
                    parameterList.add(parameter);
                }

            }

            // Setting up workflow impl parameter list

            Parameter taskParameterDesc = setWorkflowImplParameters(null, "HTDescription",
                    workflow.getApprovalTaskDescription(), "HTDescription", "Workflowimpl");
            parameterList.add(taskParameterDesc);

            Parameter taskParameterSubject = setWorkflowImplParameters(null, "HTSubject",
                    workflow.getApprovalTask(), "HTSubject", "Workflowimpl");
            parameterList.add(taskParameterSubject);

            Parameter workflowParameter = setWorkflowImplParameters(workflowId, "WorkflowName",
                    workflow.getWorkflowName(), "WorkflowName", "Workflowimpl");
            parameterList.add(workflowParameter);

            workflowManagementService.addWorkflow(workflowBean, parameterList,
                    CarbonContext.getThreadLocalCarbonContext().getTenantId());
            workflowSummary = getWorkflow(workflowBean);

        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_WORKFLOW, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_WORKFLOW, null, e);
        }
        return workflowSummary;
    }

    /**
     * Update an existing workflow
     *
     * @param workflow   Workflow details
     * @param workflowId Workflow ID
     * @return WorkflowSummary
     */
    public WorkflowSummary updateWorkflow(WorkflowCreation workflow, String workflowId) {

        WorkflowSummary workflowSummary;
        Workflow workflowBean;

        try {

            Workflow existingWorkflow = workflowManagementService.getWorkflow(workflowId);

            if (existingWorkflow == null) {
                throw new WorkflowClientException("Workflow ID: " + workflowId + " does not exist");
            }

            workflowBean = createWorkflow(workflow, workflowId);

            // Setting up the Template ParameterList

            List<WorkflowTemplateParameters> templateProperties = workflow.getWorkflowTemplate().getSteps();

            List<Parameter> parameterList = new ArrayList<>();

            for (WorkflowTemplateParameters properties : templateProperties) {
                for (OptionDetails options : properties.getOptions()) {
                    Parameter parameter = setWorkflowImplParameters(workflowId, "UserAndRole",
                            options.getValues(),
                            "UserAndRole-step-" + properties.getSteps() + "-" + options.getEntity(),
                            "Template");
                    parameterList.add(parameter);
                }

            }

            // Setting up workflow impl parameter list

            Parameter taskParameterDesc = setWorkflowImplParameters(workflowId, "HTDescription",
                    workflow.getApprovalTaskDescription(), "HTDescription", "Workflowimpl");
            parameterList.add(taskParameterDesc);

            Parameter taskParameterSubject = setWorkflowImplParameters(workflowId, "HTSubject",
                    workflow.getApprovalTask(), "HTSubject", "Workflowimpl");
            parameterList.add(taskParameterSubject);

            Parameter workflowParameter = setWorkflowImplParameters(workflowId, "WorkflowName",
                    workflow.getWorkflowName(), "WorkflowName", "Workflowimpl");
            parameterList.add(workflowParameter);

            workflowManagementService.addWorkflow(workflowBean, parameterList,
                    CarbonContext.getThreadLocalCarbonContext().getTenantId());

            workflowSummary = getWorkflow(workflowBean);

        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_WORKFLOW, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_WORKFLOW, workflowId, e);
        }
        return workflowSummary;
    }


    /**
     * Retrieve workflow from workflow ID
     *
     * @param workflowId workflow id
     * @return DetailedWorkflow
     */
    public DetailedWorkflow getWorkflow(String workflowId) {

        try {
            Workflow workflowBean = workflowManagementService.getWorkflow(workflowId);

            List<Parameter> workflowParameters = workflowManagementService.getWorkflowParameters(workflowId);

            if (workflowBean == null || workflowParameters == null) {
                throw handleException(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_WORKFLOW_NOT_FOUND,
                        workflowId);
            }

            return getDetailedWorkflow(workflowBean, workflowParameters);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_WORKFLOW, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_WORKFLOW, workflowId, e);
        }
    }

    private DetailedWorkflow getDetailedWorkflow(Workflow workflowBean, List<Parameter> workflowParameters)
            throws WorkflowException {

        DetailedWorkflow detailedWorkflow = new DetailedWorkflow();

        detailedWorkflow.setWorkflowName(workflowBean.getWorkflowName());
        detailedWorkflow.setWorkflowDescription(workflowBean.getWorkflowDescription());

        DetailedWorkflowTemplate workflowTemplate = new DetailedWorkflowTemplate();
        workflowTemplate.setName(workflowBean.getTemplateId());

        Template template = workflowManagementService.getTemplate(workflowBean.getTemplateId());
        workflowTemplate.setTemplateDescription(template.getDescription());

        List<WorkflowTemplateParameters> additionalProperties = new ArrayList<>();
        Map<Integer, WorkflowTemplateParameters> templateParamsMap = new HashMap<>();

        for (Parameter parameter : workflowParameters) {

            if ("Template".equals(parameter.getHolder())) {
                String[] params = parameter.getqName().split("-");
                int stepNumber = Integer.parseInt(params[2]);

                // Check if there's already a WorkflowTemplateParameters object for this step
                WorkflowTemplateParameters templateParameters = templateParamsMap.getOrDefault(stepNumber,
                        new WorkflowTemplateParameters());
                templateParameters.setSteps(stepNumber);

                // Create and add new OptionDetails
                OptionDetails details = new OptionDetails();
                details.setEntity(params[3]);
                details.setValues(parameter.getParamValue());

                // Ensure the list exists and add new details
                if (templateParameters.getOptions() == null) {
                    templateParameters.setOptions(new ArrayList<>());
                }
                templateParameters.getOptions().add(details);

                // Update the map
                templateParamsMap.put(stepNumber, templateParameters);
            } else if ("HTSubject".equals(parameter.getParamName())) {

                if (parameter.getParamName().equals("HTSubject")) {
                    detailedWorkflow.setApprovalTask(parameter.getParamValue());
                } else if ("HTDescription".equals(parameter.getParamName())) {
                    detailedWorkflow.setApprovalTaskDescription(parameter.getParamValue());
                }
            }
        }
        List<WorkflowTemplateParameters> templateParams = new ArrayList<>(templateParamsMap.values());

        workflowTemplate.setProperties(templateParams);
        detailedWorkflow.setWorkflowTemplate(workflowTemplate);
        detailedWorkflow.setWorkflowEngine(workflowBean.getWorkflowImplId());

        return detailedWorkflow;
    }


    /**
     * List paginated workflows of a tenant.
     *
     * @param limit  Items per page
     * @param offset Offset
     * @param filter filter string
     * @return WorkflowSummary[]
     */

    public WorkflowSummary[] listPaginatedWorkflows(Integer limit, Integer offset, String filter) {

        List<WorkflowSummary> workflowSummaryList = new ArrayList<>();
        List<Workflow> workflowBeans;
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            if (limit == null || offset == null) {
                limit = 15;
                offset = 0;
            } else {
                limit = limit.intValue();
                offset = offset.intValue();
            }
            workflowBeans = workflowManagementService.listPaginatedWorkflows(tenantId, limit, offset, filter);
            for (Workflow workflow : workflowBeans) {
                WorkflowSummary workflowTmp = getWorkflow(workflow);
                workflowSummaryList.add(workflowTmp);
            }
            return workflowSummaryList.toArray(new WorkflowSummary[workflowSummaryList.size()]);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOWS, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_WORKFLOWS, null, e);

        }

    }

    /**
     * Remove a workflow by a given workflow ID
     *
     * @param workflowId ID of workflow to remove
     * @return Return Success Message
     */
    public String removeWorkflow(String workflowId) {

        try {

            workflowManagementService.removeWorkflow(workflowId);

            return "Workflow successfully removed!";
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_WORKFLOW, workflowId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_WORKFLOW, workflowId, e);
        }
    }

    /**
     * List paginated associations of a tenant.
     *
     * @param limit  Items per page
     * @param offset Offset
     * @param filter Filter
     * @return WorkflowAssociation[]
     */
    public WorkflowAssociation[] listPaginatedAssociations(Integer limit, Integer offset, String filter) {

        List<WorkflowAssociation> associations = new ArrayList<>();
        List<Association> associationBeans = null;

        try {
            if (limit == null || offset == null) {
                limit = 15;
                offset = 0;
            } else {
                limit = limit.intValue();
                offset = offset.intValue();
            }
            associationBeans = workflowManagementService.listPaginatedAssociations(CarbonContext.getThreadLocalCarbonContext().getTenantId(),
                    limit, offset, filter);
            for (Association associationBean : associationBeans) {
                WorkflowAssociation associationTmp = getAssociation(associationBean);
                associations.add(associationTmp);
            }

        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_ASSOCIATIONS, null, e);
        }        catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_ASSOCIATIONS, null, e);
        }
        if (CollectionUtils.isEmpty(associations)) {
            return new WorkflowAssociation[0];
        }
        return associations.toArray(new WorkflowAssociation[associations.size()]);
    }

    /**
     * Remove association
     *
     * @param associationId ID of association to remove
     * @return Success message
     */
    public String removeAssociation(String associationId) {

        try {
            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null) {
                throw new WorkflowClientException("Invalid Association ID provided");
            }
            workflowManagementService.removeAssociation(Integer.parseInt(associationId));
            return "Workflow association successfully removed!";
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_ASSOCIATION, associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_REMOVING_ASSOCIATION, associationId, e);
        }
    }

    /**
     * Add new workflow association
     *
     * @param associationName Name for the association
     * @param workflowId      Workflow to associate
     * @param eventId         Event to associate
     * @param condition       Condition to check the event for associating
     * @return Return success message
     */
    private String addAssociation(String associationName, String workflowId, String eventId, String condition) {

        try {
            Workflow workflowBean = workflowManagementService.getWorkflow(workflowId);
            WorkflowEvent event = workflowManagementService.getEvent(eventId);

            if (workflowBean == null) {
                throw new WorkflowClientException("Invalid workflow ID provided.");
            }

            if (event == null) {
                throw new WorkflowClientException("Invalid event ID provided.");
            }

            workflowManagementService.addAssociation(associationName, workflowId, eventId, condition);
            return "Workflow Association successfully added!";

        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_ASSOCIATION, null, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_ASSOCIATION, null, e);
        }
    }

    public String addAssociation(WorkflowAssociationCreation workflowAssociation) {

        return addAssociation(workflowAssociation.getAssociationName(), workflowAssociation.getWorkflowId(),
                workflowAssociation.getOperationName(), workflowAssociation.getAssociationCondition());
    }

    /**
     * Get an association by ID
     *
     * @param associationId Association ID
     * @return WorkflowAssociation
     */
    public WorkflowAssociation getAssociation(String associationId) {

        WorkflowAssociation workflowAssociation = new WorkflowAssociation();
        try {
            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_ASSOCIATION_NOT_FOUND,
                        associationId);
            }
            return getAssociation(association);
        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_ASSOCIATION, associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_ASSOCIATION, associationId, e);
        }
    }


    private boolean getAssociationState(String associationId, Status status) {

        boolean isEnable;
        try {
            if (status == null) {
                isEnable = workflowManagementService.getAssociation(associationId).isEnabled();
            } else {
                isEnable = (status.getAction() == ActionStatus.ENABLE);
            }
            return isEnable;

        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_ASSOCIATION, associationId, e);
        }
    }

    /**
     * Partially update an association
     *
     * @param associationId       Association ID
     * @param workflowAssociation Association Details
     * @return Success message
     */
    public String changeAssociation(String associationId, WorkflowAssociationPatch workflowAssociation) {

        try {
            boolean isEnable = getAssociationState(associationId, workflowAssociation.getAssociationStatus());
            workflowManagementService.changeAssociation(associationId, workflowAssociation.getAssociationName(),
                    workflowAssociation.getWorkflowId(), workflowAssociation.getOperationName(),
                    workflowAssociation.getAssociationCondition(), isEnable);
            return "Workflow association successfully updated!";

        } catch (WorkflowClientException e) {
            throw handleClientError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_ASSOCIATION, associationId, e);
        } catch (WorkflowException e) {
            throw handleServerError(Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_ASSOCIATION, associationId, e);
        }
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    private ErrorResponse.Builder getErrorBuilder(String errorCode, String errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorCode).withMessage(errorMsg)
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

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
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
            errorResponse.setMessage(errorMessage);
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, includeData(e.getMessage(), data));
        }

        return new APIError(Response.Status.BAD_REQUEST, errorResponse);

    }
}

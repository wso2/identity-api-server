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

package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.model.*;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementAdminService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementServiceImpl;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.Workflow;
import org.wso2.carbon.identity.workflow.mgt.dao.WorkflowDAO;
import org.wso2.carbon.identity.workflow.mgt.dto.Association;
import org.wso2.carbon.identity.workflow.mgt.dto.Template;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowClientException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowRuntimeException;
import org.wso2.carbon.identity.workflow.mgt.internal.WorkflowServiceDataHolder;
import org.wso2.carbon.identity.workflow.mgt.util.WFConstant;

import javax.ws.rs.core.Response;
import java.util.*;


public class WorkflowService {

    private static final Log log = LogFactory.getLog(WorkflowManagementAdminService.class);
    private final WorkflowManagementService workflowManagementService;

    public WorkflowService(WorkflowManagementService workflowManagementService){

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

    private Parameter setWorkflowImplParameters(String workflowId, String paramName, String paramValue, String qName, String holder){

        Parameter parameter = new Parameter();
        parameter.setWorkflowId(workflowId);
        parameter.setParamName(paramName);
        parameter.setParamValue(paramValue);
        parameter.setqName(qName);
        parameter.setHolder(holder);
        return parameter;
    }

    /**
     * Add new workflow
     *
     * Request Body: WorkflowCreation
     * {
     *   "workflowName": "User Registration Workflow",
     *   "workflowDescription": "Workflow to approve new user registrations before account activation",
     *   "workflowTemplate": {
     *     "name": "MultiStepApproval",
     *     "steps": [
     *       {
     *         "step": 1,
     *         "options": [
     *         {
     *           "entity": "roles",
     *           "values": "Internal/admin"
     *         },
     *         {
     *           "entity": "users",
     *           "values": "John"
     *         }
     *
     *         ]
     *       },
     *       {
     *         "step": 2,
     *         "options": [
     *         {
     *           "entity": "roles",
     *           "values": "Manager"
     *         },
     *         {
     *           "entity": "users",
     *           "values": ""
     *         }
     *
     *         ]
     *       }
     *       ]
     *   },
     *   "workflowEngine": "workflowImplSimple",
     *   "ApprovalTask": "User Registration Approval",
     *   "ApprovalTaskDescription": "Approval task to validate and approve new user registrations before account activation"
     * }
     *
     * Response: WorkflowSummary
     * {
     *     "id": "748159b8-67fe-4030-9348-bf322d32b267",
     *     "workflowName": "User Registration Workflow",
     *     "workflowDescription": "Workflow to approve new user registrations before account activation",
     *     "workflowTemplate": "MultiStepApprovalTemplate",
     *     "deployment": "workflowImplSimple"
     * }
     *
     * @param workflow  Workflow details
     * @param id Workflow ID
     * @throws WorkflowException
     */
    public WorkflowSummary addWorkflow(WorkflowCreation workflow, String id) {

        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        WorkflowSummary workflowSummary;
        Workflow workflowBean = new Workflow();

        try {
            if (StringUtils.isBlank(id)) {
                id = UUID.randomUUID().toString();
            } else {
                Workflow existingWorkflow = workflowManagementService.getWorkflow(id);

                if (existingWorkflow == null) {
                    throw new WorkflowClientException("Invalid workflow ID provided.");
                }
            }
            workflowBean.setWorkflowId(id);
            workflowBean.setWorkflowName(workflow.getWorkflowName());
            workflowBean.setWorkflowDescription(workflow.getWorkflowDescription());
//            String templateId = workflow.getTemplateId() == null ? workflow.getTemplate().getTemplateId() :
//                    workflow.getTemplateId();
            String templateId = workflow.getWorkflowTemplate().getName();
            if (templateId == null) {
                throw new WorkflowException("template id can't be empty");
            }
            workflowBean.setTemplateId(templateId);
//            String workflowImplId =
//                    workflow.getWorkflowImplId() == null ? workflow.getWorkflowImpl().getWorkflowImplId() :
//                            workflow.getWorkflowImplId();
            String workflowImplId = workflow.getWorkflowEngine();
            if (workflowImplId == null) {
                throw new WorkflowException("workflowimpl id can't be empty");
            }
            workflowBean.setWorkflowImplId(workflowImplId);

            // Setting up the Template ParameterList

            List<WorkflowTemplateParameters> templateProperties = workflow.getWorkflowTemplate().getSteps();

            List<Parameter> parameterList = new ArrayList<>();

            for (WorkflowTemplateParameters properties : templateProperties) {
                for (OptionDetails options : properties.getOptions()) {
                    Parameter parameter = setWorkflowImplParameters(null, "UserAndRole", options.getValues(), "UserAndRole-step-" + properties.getSteps() + "-" + options.getEntity(), "Template");
                    parameterList.add(parameter);
                }

            }

            // Setting up workflow impl parameter list
            Parameter taskParameterDesc;
            if (StringUtils.isBlank(id)) {
                taskParameterDesc = setWorkflowImplParameters(null, "HTDescription", workflow.getApprovalTaskDescription(), "HTDescription", "Workflowimpl");
            } else {
                taskParameterDesc = setWorkflowImplParameters(id, "HTDescription", workflow.getApprovalTaskDescription(), "HTDescription", "Workflowimpl");
            }
            parameterList.add(taskParameterDesc);

            Parameter taskParameterSubject;
            if (StringUtils.isBlank(id)) {
                taskParameterSubject = setWorkflowImplParameters(null, "HTSubject", workflow.getApprovalTask(), "HTSubject", "Workflowimpl");
            } else {
                taskParameterSubject = setWorkflowImplParameters(id, "HTSubject", workflow.getApprovalTask(), "HTSubject", "Workflowimpl");
            }
            parameterList.add(taskParameterSubject);

            Parameter workflowParameter = setWorkflowImplParameters(id, "WorkflowName", workflow.getWorkflowName(), "WorkflowName", "Workflowimpl");
            parameterList.add(workflowParameter);

//
//            List<Parameter> parameterList = new ArrayList<>();
//            if (workflow.getTemplateParameters() != null) {
//                parameterList.addAll(Arrays.asList(workflow.getTemplateParameters()));
//            }
//            if (workflow.getWorkflowImplParameters() != null) {
//                parameterList.addAll(Arrays.asList(workflow.getWorkflowImplParameters()));
//            }

//            WorkflowServiceDataHolder.getInstance().getWorkflowService()
//                    .addWorkflow(workflowBean, parameterList, tenantId);

            try {
                workflowManagementService.addWorkflow(workflowBean, parameterList, tenantId);
            } catch (NullPointerException e) {
                throw new WorkflowException(e.getMessage());
            }

            workflowSummary = getWorkflow(workflowBean);

        } catch (WorkflowClientException e){

            throw handleClientError(e.getErrorCode(),e.getMessage(), "", e);

        } catch (WorkflowException e) {

//            throw new WorkflowException("Server error occured when adding the workflow");
            throw handleServerError(e.getErrorCode(),e.getMessage(), "", e);

        }
        return workflowSummary;
    }


    /**
     * Retrieve workflow from workflow ID
     *
     * @param workflowId  workflow id
     * @return
     * @throws WorkflowException
     */
    public DetailedWorkflow getWorkflow(String workflowId) {

        try {
//            Workflow workflowBean =
//                    WorkflowServiceDataHolder.getInstance().getWorkflowService().getWorkflow(workflowId);
            Workflow workflowBean = workflowManagementService.getWorkflow(workflowId);

            if (workflowBean == null) {
                throw new WorkflowClientException("Invalid workflow ID provided.");
            }
            List<Parameter> workflowParameters = workflowManagementService.getWorkflowParameters(workflowId);

            return getDetailedWorkflow(workflowBean, workflowParameters);
        } catch (WorkflowClientException e){

            throw handleClientError(e.getErrorCode(),e.getMessage(), "", e);

        } catch (WorkflowException e) {

//            throw new WorkflowException("Server error occured when adding the workflow");
            throw handleServerError(e.getErrorCode(),e.getMessage(), "", e);

        }
    }

    private DetailedWorkflow getDetailedWorkflow(Workflow workflowBean, List<Parameter> workflowParameters) throws WorkflowException{

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
                WorkflowTemplateParameters templateParameters = templateParamsMap.getOrDefault(stepNumber, new WorkflowTemplateParameters());
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
            } else if ("HTSubject".equals(parameter.getParamName())){

                if (parameter.getParamName().equals("HTSubject")){
                    detailedWorkflow.setApprovalTask(parameter.getParamValue());
                } else if ("HTDescription".equals(parameter.getParamName())){
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
     * @param limit  Limit
     * @param offset Offset
     * @param filter filter
     * @return WorkflowWizard[]
     * @throws WorkflowException
     */

    public WorkflowSummary[] listPaginatedWorkflows(Integer limit, Integer offset, String filter) {

        List<WorkflowSummary> workflowSummaryList = new ArrayList<>();
        List<Workflow> workflowBeans = null;
        int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            if (limit == null || offset == null){
                limit = 15;
                offset = 0;
            } else {
                limit = limit.intValue();
                offset = offset.intValue();
            }
            //workflowBeans = WorkflowServiceDataHolder.getInstance().getWorkflowService().listPaginatedWorkflows(tenantId, limit, offset, filter);
            workflowBeans = workflowManagementService.listPaginatedWorkflows(tenantId, limit, offset, filter);
            for (Workflow workflow : workflowBeans) {
                WorkflowSummary workflowTmp = getWorkflow(workflow);
                workflowSummaryList.add(workflowTmp);
            }
            return workflowSummaryList.toArray(new WorkflowSummary[workflowSummaryList.size()]);

        } catch (WorkflowException e) {
            throw handleServerError(e.getErrorCode(),e.getMessage(), "", e);

        }

    }

    /**
     * Remove a workflow
     *
     * @param id ID of workflow to remove
     * @return
     * @throws WorkflowException
     */
    public WorkflowSummary removeWorkflow(String id) {

        try {

            //WorkflowServiceDataHolder.getInstance().getWorkflowService().removeWorkflow(id);
            WorkflowSummary workflow = getWorkflow(workflowManagementService.getWorkflow(id));
            if (workflow == null) {
                throw new WorkflowClientException("Invalid Workflow ID provided.");
            }
            workflowManagementService.removeWorkflow(id);

            return workflow;
        } catch (WorkflowClientException e) {
            //throw new WorkflowException("Server error occurred when removing workflow");
            throw handleClientError(e.getErrorCode(),e.getMessage(), "", e);
        } catch (WorkflowException e) {

            throw handleServerError(e.getErrorCode(),e.getMessage(), "", e);
        }
    }

    /**
     * List paginated associations of a tenant.
     *
     * @param limit  Limit
     * @param offset Offset
     * @param filter Filter
     * @return Association[]
     * @throws WorkflowException
     */
    public WorkflowAssociation[] listPaginatedAssociations(Integer limit, Integer offset, String filter) {

        List<WorkflowAssociation> associations = new ArrayList<>();
        List<Association> associationBeans = null;

        try {

            if (limit == null || offset == null){
                limit = 15;
                offset = 0;
            } else {
                limit = limit.intValue();
                offset = offset.intValue();
            }
            int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
//            associationBeans = WorkflowServiceDataHolder.getInstance().getWorkflowService().listPaginatedAssociations(tenantId, limit, offset, filter);
            associationBeans = workflowManagementService.listPaginatedAssociations(tenantId, limit, offset, filter);
            for (Association associationBean: associationBeans){
                WorkflowAssociation associationTmp = getAssociation(associationBean);
                associations.add(associationTmp);
            }

        } catch (WorkflowException e) {
//            throw new WorkflowException(WFConstant.Exceptions.ERROR_LISTING_ASSOCIATIONS, e);
            log.error("Error occurred when retrieving all the available workflow associations", e);
            throw handleServerError(e.getErrorCode(), e.getMessage(), "", e);

        }
        if (CollectionUtils.isEmpty(associations)) {
            return new WorkflowAssociation[0];
        }
        return associations.toArray(new WorkflowAssociation[associations.size()]);
    }

    /**
     * Remove association
     *
     * @param associationId  ID of association to remove
     * @throws WorkflowException
     */
    public String removeAssociation(String associationId) {

        try {
//            WorkflowServiceDataHolder.getInstance().getWorkflowService()
//                    .removeAssociation(Integer.parseInt(associationId));

            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null) {
                throw new WorkflowClientException("Invalid Association ID provided.");
            }
            workflowManagementService.removeAssociation(Integer.parseInt(associationId));
            return "Workflow association successfully removed!";
        } catch (WorkflowClientException e){
            throw handleClientError(e.getErrorCode(),e.getMessage(), "", e);
        }catch (WorkflowException e) {
//            log.error("Server error when removing association " + associationId, e);
            throw handleServerError(e.getErrorCode(), e.getMessage(), "", e);
        }
    }

    /**
     * Add new workflow association
     *
     * @param associationName  Name for the association
     * @param workflowId  Workflow to associate
     * @param eventId  Event to associate
     * @param condition  Condition to check the event for associating
     * @throws WorkflowException
     */
    private String addAssociation(String associationName, String workflowId, String eventId, String condition) {

        try {
//            WorkflowServiceDataHolder.getInstance().getWorkflowService()
//                    .addAssociation(associationName, workflowId, eventId, condition);
            workflowManagementService.addAssociation(associationName, workflowId, eventId, condition);
            return "Workflow Association successfully added!";

        } catch (WorkflowRuntimeException e) {
            log.error("Error when adding association " + associationName, e);
            return "Error when adding association" + associationName;
        } catch (WorkflowException e) {
//            log.error("Server error when adding association of workflow " + workflowId + " with " + eventId, e);
//            return "Server error when adding association of workflow" + workflowId + " with " + eventId;
            throw handleServerError(e.getErrorCode(), e.getMessage(), "", e);
        }
    }

    public String addAssociation(WorkflowAssociationCreation workflowAssociation){

        return addAssociation(workflowAssociation.getAssociationName(), workflowAssociation.getWorkflowId(), workflowAssociation.getOperationName(), workflowAssociation.getAssociationCondition());
    }

    /**
     * Get an association by ID
     * @param associationId  Association ID
     * @throws WorkflowException
     */
    public WorkflowAssociation getAssociation(String associationId) {

        WorkflowAssociation workflowAssociation = new WorkflowAssociation();
        try {
            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null) {
                throw new WorkflowClientException("Invalid workflow association ID provided.");
            }
            return getAssociation(association);
        } catch (WorkflowClientException e) {
            throw handleClientError(e.getErrorCode(), e.getMessage(), "", e);
        } catch (WorkflowException e) {
            throw handleServerError(e.getErrorCode(), e.getMessage(), "", e);
        }
    }


    private boolean getAssociationState(String associationId, Status status){

        boolean isEnable;
        try {

            if (status == null) {

                isEnable = workflowManagementService.getAssociation(associationId).isEnabled();
            } else {
                isEnable = (status.getAction() == ActionStatus.ENABLE);
            }
            return isEnable;

        } catch (WorkflowException e) {
            throw handleServerError(e.getErrorCode(),e.getMessage(), "", e);
        }
    }

    /**
     * Partially update an association
     *
     * @param associationId  Association ID
     * @param workflowAssociation  Association Details
     * @throws WorkflowException
     */
    public String changeAssociation(String associationId, WorkflowAssociationPatch workflowAssociation){

        try {
            Association association = workflowManagementService.getAssociation(associationId);
            if (association == null ) {
                throw new WorkflowClientException("Invalid workflow association ID provided.");
            }
            boolean isEnable = getAssociationState(associationId, workflowAssociation.getAssociationStatus());
            workflowManagementService.changeAssociation(associationId, workflowAssociation.getAssociationName(), workflowAssociation.getWorkflowId(), workflowAssociation.getOperationName(), workflowAssociation.getAssociationCondition(), isEnable);
            return "Workflow association successfully updated!";

        }  catch (WorkflowClientException e) {
            throw handleClientError(e.getErrorCode(), e.getMessage(), "", e);
        } catch (WorkflowException e) {
            throw handleServerError(e.getErrorCode(), e.getMessage(), "", e);
        }
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

    private APIError handleServerError(String errorCode, String errorMsg, String data, Exception e) {

        ErrorResponse errorResponse = getErrorBuilder(errorCode, errorMsg, data).build(log,
                e, includeData(errorMsg, data));

        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);

    }

    private APIError handleClientError(String errorCode, String errorMsg, String data, WorkflowClientException e) {

        ErrorResponse errorResponse = getErrorBuilder(errorCode, errorMsg, data).build(log,
                e, includeData(errorMsg, data));

        return new APIError(Response.Status.BAD_REQUEST, errorResponse);

    }

}


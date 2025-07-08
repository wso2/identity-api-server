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

package org.wso2.carbon.identity.api.server.action.management.v1.mapper;

import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtServerException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMapperUtil;
import org.wso2.carbon.identity.certificate.management.model.Certificate;
import org.wso2.carbon.identity.user.pre.update.password.action.api.model.PasswordSharing;
import org.wso2.carbon.identity.user.pre.update.password.action.api.model.PreUpdatePasswordAction;

/**
 * Pre Update Password Action Builder.
 */
public class PreUpdatePasswordActionMapper implements ActionMapper {

    @Override
    public Action.ActionTypes getSupportedActionType() {

        return Action.ActionTypes.PRE_UPDATE_PASSWORD;
    }

    @Override
    public Action toAction(ActionModel actionModel) throws ActionMgtException {

        if (!(actionModel instanceof PreUpdatePasswordActionModel)) {
            throw new ActionMgtServerException("Unsupported action model for action type: " + getSupportedActionType());
        }

        Action basicAction = ActionMapperUtil.buildActionRequest(getSupportedActionType(), actionModel);
        return new PreUpdatePasswordAction.RequestBuilder(basicAction)
                .passwordSharing(buildPasswordSharingRequest((PreUpdatePasswordActionModel) actionModel))
                .attributes(((PreUpdatePasswordActionModel) actionModel).getAttributes())
                .build();
    }

    @Override
    public Action toAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException {

        if (!(actionUpdateModel instanceof PreUpdatePasswordActionUpdateModel)) {
            throw new ActionMgtServerException("Unsupported action update model for action type: " +
                    getSupportedActionType());
        }

        Action basicUpdatingAction = ActionMapperUtil.buildUpdatingActionRequest(getSupportedActionType(),
                actionUpdateModel);
        return new PreUpdatePasswordAction.RequestBuilder(basicUpdatingAction)
                .passwordSharing(
                        buildPasswordSharingUpdateRequest((PreUpdatePasswordActionUpdateModel) actionUpdateModel))
                .attributes(((PreUpdatePasswordActionUpdateModel) actionUpdateModel).getAttributes())
                .build();
    }

    @Override
    public ActionResponse toActionResponse(Action action) throws ActionMgtException {

        if (!(action instanceof PreUpdatePasswordAction)) {
            throw new ActionMgtServerException("Unsupported action response for action type: " +
                    getSupportedActionType());
        }

        ActionResponse actionResponse = ActionMapperUtil.buildActionResponse(action);
        return new PreUpdatePasswordActionResponse(actionResponse)
                .passwordSharing(buildPasswordSharingResponse((PreUpdatePasswordAction) action))
                .attributes(((PreUpdatePasswordAction) action).getAttributes());

    }

    private PasswordSharing buildPasswordSharingRequest(PreUpdatePasswordActionModel actionModel) {

        Certificate certificate = null;
        if (actionModel.getPasswordSharing().getCertificate() != null) {
            // Certificate is an optional field.
            certificate = new Certificate.Builder()
                    .certificateContent(actionModel.getPasswordSharing().getCertificate())
                    .build();
        }
        return new PasswordSharing.Builder()
                .format(PasswordSharing.Format.valueOf(actionModel.getPasswordSharing().getFormat().value()))
                .certificate(certificate)
                .build();
    }

    private PasswordSharing buildPasswordSharingUpdateRequest(PreUpdatePasswordActionUpdateModel actionModel) {

        if (actionModel.getPasswordSharing() == null) {
            return null;
        }

        PasswordSharing.Builder passwordSharingBuilder = new PasswordSharing.Builder();

        if (actionModel.getPasswordSharing().getFormat() != null) {
            passwordSharingBuilder
                    .format(PasswordSharing.Format.valueOf(actionModel.getPasswordSharing().getFormat().value()));
        }

        if (actionModel.getPasswordSharing().getCertificate() != null) {
            passwordSharingBuilder.certificate(new Certificate.Builder()
                    .certificateContent(actionModel.getPasswordSharing().getCertificate())
                    .build());
        }

        return passwordSharingBuilder.build();
    }

    private org.wso2.carbon.identity.api.server.action.management.v1.PasswordSharing
        buildPasswordSharingResponse(PreUpdatePasswordAction action) {

        return new org.wso2.carbon.identity.api.server.action.management.v1.PasswordSharing()
                .format(org.wso2.carbon.identity.api.server.action.management.v1.PasswordSharing.FormatEnum
                        .valueOf(action.getPasswordSharing().getFormat().name()))
                .certificate(action.getPasswordSharing().getCertificate() == null ? null :
                        action.getPasswordSharing().getCertificate().getCertificateContent());
    }
}

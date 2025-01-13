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

package org.wso2.carbon.identity.api.server.action.management.v1.builder;

import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PasswordSharing;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.PreUpdatePasswordActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionBuilderUtil;
import org.wso2.carbon.identity.certificate.management.model.Certificate;
import org.wso2.carbon.identity.user.pre.update.password.action.model.PreUpdatePasswordAction;

/**
 * Pre Update Password Action Builder.
 */
public class PreUpdatePasswordActionBuilder implements ActionBuilder {

    @Override
    public Action.ActionTypes getSupportedActionType() {

        return Action.ActionTypes.PRE_UPDATE_PASSWORD;
    }

    @Override
    public Action buildAction(ActionModel actionModel) throws ActionMgtException {

        Action basicAction = ActionBuilderUtil.buildActionRequest(getSupportedActionType(), actionModel);
        return new PreUpdatePasswordAction.RequestBuilder(basicAction)
                .passwordSharing(buildPasswordSharingRequest((PreUpdatePasswordActionModel) actionModel))
                .build();
    }

    @Override
    public Action buildAction(ActionUpdateModel actionUpdateModel) throws ActionMgtException {

        Action basicUpdatingAction = ActionBuilderUtil.buildUpdatingActionRequest(getSupportedActionType(),
                actionUpdateModel);
        return new PreUpdatePasswordAction.RequestBuilder(basicUpdatingAction)
                .passwordSharing(
                        buildPasswordSharingUpdateRequest((PreUpdatePasswordActionUpdateModel) actionUpdateModel))
                .build();
    }

    @Override
    public ActionResponse buildActionResponse(Action action) throws ActionMgtException {

        ActionResponse actionResponse = ActionBuilderUtil.buildActionResponse(action);
        return new PreUpdatePasswordActionResponse(actionResponse)
                .passwordSharing(buildPasswordSharingResponse((PreUpdatePasswordAction) action));
    }

    private org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing
        buildPasswordSharingRequest(PreUpdatePasswordActionModel actionModel) {

        return new org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing.Builder()
                        .format(org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing.Format
                                .valueOf(actionModel.getPasswordSharing().getFormat().value()))
                        .certificate(new Certificate.Builder()
                                .certificateContent(actionModel.getPasswordSharing().getCertificate())
                                .build())
                        .build();
    }

    private org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing
        buildPasswordSharingUpdateRequest(PreUpdatePasswordActionUpdateModel actionModel) {

        if (actionModel.getPasswordSharing() == null) {
            return null;
        }

        org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing.Builder passwordSharingBuilder =
                new org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing.Builder();

        if (actionModel.getPasswordSharing().getFormat() != null) {
            passwordSharingBuilder.format(
                    org.wso2.carbon.identity.user.pre.update.password.action.model.PasswordSharing.Format
                            .valueOf(actionModel.getPasswordSharing().getFormat().value()));
        }

        if (actionModel.getPasswordSharing().getCertificate() != null) {
            passwordSharingBuilder.certificate(new Certificate.Builder()
                    .certificateContent(actionModel.getPasswordSharing().getCertificate())
                    .build());
        }

        return passwordSharingBuilder.build();
    }

    private PasswordSharing buildPasswordSharingResponse(PreUpdatePasswordAction action) {

        return new PasswordSharing()
                .format(PasswordSharing.FormatEnum.valueOf(action.getPasswordSharing().getFormat().name()))
                .certificate(action.getPasswordSharing().getCertificate() == null ? null :
                        action.getPasswordSharing().getCertificate().getCertificateContent());
    }
}

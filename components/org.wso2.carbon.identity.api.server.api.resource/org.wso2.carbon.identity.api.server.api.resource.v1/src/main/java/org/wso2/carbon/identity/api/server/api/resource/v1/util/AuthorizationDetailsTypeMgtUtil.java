package org.wso2.carbon.identity.api.server.api.resource.v1.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesPatchModel;
import org.wso2.carbon.identity.application.common.model.AuthorizationDetailsType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorizationDetailsTypeMgtUtil {

    public static AuthorizationDetailsTypesGetModel toAuthorizationDetailsGetModel(
            final AuthorizationDetailsType authorizationDetailsType) {

        return new AuthorizationDetailsTypesGetModel()
                .id(authorizationDetailsType.getId())
                .name(authorizationDetailsType.getName())
                .description(authorizationDetailsType.getDescription())
                .type(authorizationDetailsType.getType())
                .schema(new Gson().fromJson(authorizationDetailsType.getSchema(), new TypeToken<Map<String, Object>>() {
                }.getType()));
    }

    public static List<AuthorizationDetailsTypesGetModel> toAuthorizationDetailsGetModelsList(
            final List<AuthorizationDetailsType> authorizationDetailsTypes) {

        return CollectionUtils.isEmpty(authorizationDetailsTypes) ? Collections.emptyList() :
                authorizationDetailsTypes.stream().map(AuthorizationDetailsTypeMgtUtil::toAuthorizationDetailsGetModel)
                        .collect(Collectors.toList());
    }

    public static List<AuthorizationDetailsType> toAuthorizationDetailsTypesList(
            final List<AuthorizationDetailsTypesCreationModel> creationModels) {

        return CollectionUtils.isEmpty(creationModels) ? Collections.emptyList() :
                creationModels.stream()
                        .map(AuthorizationDetailsTypeMgtUtil::toAuthorizationDetailsType)
                        .collect(Collectors.toList());
    }

    public static AuthorizationDetailsType toAuthorizationDetailsType(
            final AuthorizationDetailsTypesCreationModel creationModel) {

        final AuthorizationDetailsType authorizationDetailsType = new AuthorizationDetailsType();
        authorizationDetailsType.setType(creationModel.getType());
        authorizationDetailsType.setName(creationModel.getName());
        authorizationDetailsType.setDescription(creationModel.getDescription());
        authorizationDetailsType.setSchema(new JSONObject(creationModel.getSchema()).toString());

        return authorizationDetailsType;
    }

    public static AuthorizationDetailsType toAuthorizationDetailsType(
            String type, AuthorizationDetailsTypesPatchModel patchModel) {

        final AuthorizationDetailsType authorizationDetailsType = new AuthorizationDetailsType();
        authorizationDetailsType.setType(type);
        authorizationDetailsType.setName(patchModel.getName());
        authorizationDetailsType.setDescription(patchModel.getDescription());
        authorizationDetailsType.setSchema(new JSONObject(patchModel.getSchema()).toString());

        return authorizationDetailsType;
    }
}

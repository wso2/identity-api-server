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
            final List<AuthorizationDetailsTypesCreationModel> typesCreationModels) {

        return CollectionUtils.isEmpty(typesCreationModels) ? Collections.emptyList() :
                typesCreationModels.stream()
                        .map(AuthorizationDetailsTypeMgtUtil::toAuthorizationDetailsType)
                        .collect(Collectors.toList());
    }

    public static AuthorizationDetailsType toAuthorizationDetailsType(
            final AuthorizationDetailsTypesCreationModel typesCreationModel) {

        final AuthorizationDetailsType authorizationDetailsType = new AuthorizationDetailsType();
        authorizationDetailsType.setType(typesCreationModel.getType());
        authorizationDetailsType.setName(typesCreationModel.getName());
        authorizationDetailsType.setDescription(typesCreationModel.getDescription());
        authorizationDetailsType.setSchema(new JSONObject(typesCreationModel.getSchema()).toString());

        return authorizationDetailsType;
    }

    public static List<AuthorizationDetailsType> toAuthorizationDetailsTypesList(
            final List<AuthorizationDetailsTypesPatchModel> typesPatchModels) {

        return CollectionUtils.isEmpty(typesPatchModels) ? Collections.emptyList() :
                typesPatchModels.stream()
                        .map(AuthorizationDetailsTypeMgtUtil::toAuthorizationDetailsType)
                        .collect(Collectors.toList());
    }

    public static AuthorizationDetailsType toAuthorizationDetailsType(final String type,
            final AuthorizationDetailsTypesPatchModel typesPatchModel) {

        final AuthorizationDetailsType authorizationDetailsType = new AuthorizationDetailsType();
        authorizationDetailsType.setType(type);
        authorizationDetailsType.setName(typesPatchModel.getName());
        authorizationDetailsType.setDescription(typesPatchModel.getDescription());
        authorizationDetailsType.setSchema(new JSONObject(typesPatchModel.getSchema()).toString());

        return authorizationDetailsType;
    }
}

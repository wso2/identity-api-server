package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;


import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;

/**
 * Converts the API model object into a ServiceProvider object.
 * @param <T> Model object.
 * @param <S> DTO object.
 */
public interface ModelToDTO<T, S> {

    S apply(T t) throws IdentityApplicationManagementClientException;
}

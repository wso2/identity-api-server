/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.UpdateFunction;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import javax.ws.rs.core.Response;

/**
 * Utility functions.
 */
public class Utils {

    private static final Log log = LogFactory.getLog(Utils.class);

    public static void setIfNotNull(String value, Consumer<String> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }

    public static void setIfNotNull(Boolean value, Consumer<Boolean> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }

    public static <T> void updateApplication(ServiceProvider application,
                                             T configsToUpdate,
                                             UpdateFunction<ServiceProvider, T> function) {

        if (configsToUpdate != null) {
            function.update(application, configsToUpdate);
        }
    }

    public static ServiceProvider deepCopyApplication(ServiceProvider application) {

        ObjectOutputStream objOutPutStream;
        ObjectInputStream objInputStream;
        ServiceProvider newObject;
        try {
            ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
            objOutPutStream = new ObjectOutputStream(byteArrayOutPutStream);
            objOutPutStream.writeObject(application);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutPutStream.toByteArray());
            objInputStream = new ObjectInputStream(byteArrayInputStream);
            newObject = (ServiceProvider) objInputStream.readObject();

        } catch (ClassNotFoundException | IOException e) {
            throw buildServerErrorResponse(e, "Error deep cloning application object.");
        }
        return newObject;
    }

    public static APIError buildClientError(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Invalid Request.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    public static APIError buildServerErrorResponse(Exception e, String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    public static APIError buildServerErrorResponse(String message) {

        return buildServerErrorResponse(null, message);
    }

    public static APIError buildApiError(Response.Status statusCode, String message) {

        ErrorResponse errorResponse = new ErrorResponse.Builder().withMessage(message).build(log, message);
        return new APIError(statusCode, errorResponse);
    }

    public static APIError buildNotImplementedErrorResponse(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.NOT_IMPLEMENTED;
        return new APIError(status, errorResponse);
    }
}

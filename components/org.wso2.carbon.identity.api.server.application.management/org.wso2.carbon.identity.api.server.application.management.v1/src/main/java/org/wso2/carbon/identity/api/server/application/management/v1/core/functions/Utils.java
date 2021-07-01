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
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Utility functions.
 */
public class Utils {

    private static final Log log = LogFactory.getLog(Utils.class);

    private Utils() {

    }

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

    public static <T> void setIfNotNull(T value, Consumer<T> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }


    public static <T> Stream<T> arrayToStream(T[] object) {

        return object != null ? Stream.of(object) : Stream.empty();
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
            throw buildServerError("Error deep cloning application object.", e);
        }
        return newObject;
    }

    public static APIError buildBadRequestError(String errorCode, String description) {

        if (errorCode == null) {
            errorCode = INVALID_REQUEST.getCode();
        }
        String errorMessage = "Invalid Request.";
        return buildClientError(errorCode, errorMessage, description);
    }

    public static APIError buildBadRequestError(String description) {

        String errorCode = INVALID_REQUEST.getCode();
        String errorMessage = "Invalid Request.";

        return buildClientError(errorCode, errorMessage, description);
    }

    public static APIError buildServerError(String errorDescription, Exception e) {

        String errorCode = UNEXPECTED_SERVER_ERROR.getCode();
        String errorMessage = "Server error while performing the attempted operation.";

        return buildServerError(errorCode, errorMessage, errorDescription, e);
    }

    public static APIError buildServerError(String errorDescription) {

        return buildServerError(errorDescription, null);
    }

    public static APIError buildNotImplementedError(String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.NOT_IMPLEMENTED;
        return new APIError(status, errorResponse);
    }

    public static APIError buildNotImplementedError(String errorCode, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withCode(errorCode)
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.NOT_IMPLEMENTED;
        return new APIError(status, errorResponse);
    }

    public static APIError buildServerError(String errorCode, String message, String description) {

        throw buildServerError(errorCode, message, description, null);
    }

    public static APIError buildServerError(String errorCode, String message, String description, Exception e) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(message)
                .withDescription(description)
                .build(log, e, description);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    public static APIError buildClientError(String errorCode, String message, String description) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(message)
                .withDescription(description)
                .build(log, description);

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    public static APIError buildNotFoundError(String errorCode, String message, String description) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(message)
                .withDescription(description)
                .build(log, description);

        Response.Status status = Response.Status.NOT_FOUND;
        return new APIError(status, errorResponse);
    }

    public static APIError buildForbiddenError(String errorCode, String message, String description) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(message)
                .withDescription(description)
                .build(log, description);

        Response.Status status = Response.Status.FORBIDDEN;
        return new APIError(status, errorResponse);
    }
}

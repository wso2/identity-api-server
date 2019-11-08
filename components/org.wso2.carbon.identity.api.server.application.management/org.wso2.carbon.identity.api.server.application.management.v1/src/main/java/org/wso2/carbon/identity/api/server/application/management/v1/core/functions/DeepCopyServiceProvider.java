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

import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;

/**
 * Clones the provided service provider object.
 */
public class DeepCopyServiceProvider implements Function<ServiceProvider, ServiceProvider> {

    @Override
    public ServiceProvider apply(ServiceProvider t) {

        ObjectOutputStream objOutPutStream;
        ObjectInputStream objInputStream;
        ServiceProvider newObject;
        try {
            ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
            objOutPutStream = new ObjectOutputStream(byteArrayOutPutStream);
            objOutPutStream.writeObject(t);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutPutStream.toByteArray());
            objInputStream = new ObjectInputStream(byteArrayInputStream);
            newObject = (ServiceProvider) objInputStream.readObject();

        } catch (ClassNotFoundException | IOException e) {
            throw Utils.buildServerErrorResponse(e, "Error deep cloning application object.");
        }
        return newObject;
    }
}

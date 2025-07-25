/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.cors.v1.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSOriginObject;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSOrigin;

import java.util.function.Function;

/**
 * Converts a CORSOrigin instance to a CORSOriginGetObject instance.
 */
public class CORSOriginToCORSOriginObject implements Function<CORSOrigin, CORSOriginObject> {

    private static final Log log = LogFactory.getLog(CORSOriginToCORSOriginObject.class);

    @Override
    public CORSOriginObject apply(CORSOrigin corsOrigin) {

        if (corsOrigin == null) {
            if (log.isDebugEnabled()) {
                log.debug("Null CORS origin provided for transformation");
            }
            return null;
        }

        CORSOriginObject corsOriginGetObject = new CORSOriginObject();
        corsOriginGetObject.setId(corsOrigin.getId());
        corsOriginGetObject.setUrl(corsOrigin.getOrigin());

        return corsOriginGetObject;
    }
}

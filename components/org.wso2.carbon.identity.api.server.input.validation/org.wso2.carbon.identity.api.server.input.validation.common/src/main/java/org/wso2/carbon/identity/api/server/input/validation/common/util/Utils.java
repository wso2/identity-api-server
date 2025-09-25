/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;

import java.util.UUID;

import static org.wso2.carbon.identity.api.server.input.validation.common.util.ValidationManagementConstants
        .CORRELATION_ID;

/**
 * Util class.
 */
public class Utils {

    private static final Log log = LogFactory.getLog(Utils.class);

    /**
     * Get correlation id of current thread.
     *
     * @return Correlation-id.
     */
    public static String getCorrelation() {

        if (isCorrelationIDPresent()) {
            String correlationId = MDC.get(CORRELATION_ID);
            if (log.isDebugEnabled()) {
                log.debug("Retrieved existing correlation ID from MDC: " + correlationId);
            }
            return correlationId;
        }
        String newCorrelationId = UUID.randomUUID().toString();
        if (log.isDebugEnabled()) {
            log.debug("Generated new correlation ID: " + newCorrelationId);
        }
        return newCorrelationId;
    }

    /**
     * Check whether correlation id present in the log MDC.
     *
     * @return Whether the correlation id is present.
     */
    public static boolean isCorrelationIDPresent() {

        return MDC.get(CORRELATION_ID) != null;
    }
}

package org.wso2.carbon.identity.api.server.idle.account.identification.common.util;

import org.slf4j.MDC;
import static org.wso2.carbon.identity.api.server.idle.account.identification.common.util.IdleAccountIdentificationConstants.CORRELATION_ID;

import java.util.UUID;

/**
 * Util class.
 */
public class Utils {

    /**
     * Get correlation id of current thread.
     *
     * @return Correlation-id.
     */
    public static String getCorrelation() {

        if (isCorrelationIDPresent()) {
            return MDC.get(CORRELATION_ID);
        }
        return UUID.randomUUID().toString();
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

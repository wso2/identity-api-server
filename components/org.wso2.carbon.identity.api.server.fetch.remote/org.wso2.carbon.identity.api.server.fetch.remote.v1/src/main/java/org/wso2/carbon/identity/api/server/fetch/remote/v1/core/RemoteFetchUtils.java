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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.fetch.remote.v1.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

/**
 * Utility functions for remote fetch configuration.
 */
public class RemoteFetchUtils {

    private static final Log log = LogFactory.getLog(RemoteFetchUtils.class);

    private RemoteFetchUtils() {

    }

    public static void setIfNotNull(String value, Consumer<String> consumer) {

        if (StringUtils.isNoneBlank(value)) {
            consumer.accept(value);
        }
    }

    public static void setIfNotNull(Boolean value, Consumer<Boolean> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }

    public static void setIfNotNull(List<String> values, Consumer<List<String>> consumer) {

        if (values != null) {
            consumer.accept(values);
        }
    }

    public static void convertDateToStringIfNotNull(Date value, Consumer<String> consumer) {

        if (value != null) {
            if (log.isDebugEnabled()) {
                log.debug("Converting date to ISO-8601 format: " + value);
            }
            //ISO-8601 representation of the date.
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"));
            consumer.accept(ISO_OFFSET_DATE_TIME.format(zonedDateTime));
        } else {
            consumer.accept(null);
        }
    }
}

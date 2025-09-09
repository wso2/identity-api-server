/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the data types for claims.
 */
public enum DataType {
    /**
     * String data type.
     */
    STRING("string"),
    /**
     * Integer data type.
     */
    INTEGER("integer"),
    /**
     * Decimal data type.
     */
    DECIMAL("decimal"),
    /**
     * Boolean data type.
     */
    BOOLEAN("boolean"),
    /**
     * Date time data type.
     */
    DATE_TIME("date_time"),
    /**
     * Date data type.
     */
    DATE("date"),
    /**
     * Epoch timestamp data type.
     */
    EPOCH("epoch"),
    /**
     * Complex data type.
     */
    COMPLEX("complex");

    private final String value;

    DataType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

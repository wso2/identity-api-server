/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.common.error.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Common DTO for all the server bulk API related error responses.
 */
public class BulkErrorDTO extends ErrorDTO implements Serializable {

    private static final long serialVersionUID = 6110302523856444000L;
    private List<? extends ErrorDTO> failedOperations = null;

    @JsonProperty("failedOperations")
    public List<? extends ErrorDTO> getFailedOperations() {

        return this.failedOperations;
    }

    public void setFailedOperations(List<? extends ErrorDTO> failedOperations) {

        this.failedOperations = failedOperations;
    }
}

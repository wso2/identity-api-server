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

import io.swagger.annotations.ApiModel;
import java.util.HashMap;
import java.util.Map;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.AttributeProfileDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
* Attribute profiles.
**/
@ApiModel(description = "Attribute profiles.")
public class ProfilesDTO extends HashMap<String, AttributeProfileDTO> {

    private static final long serialVersionUID = -227086223053735979L;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ProfilesDTO {\n");
        sb.append("  " + super.toString()).append("\n");


        sb.append("}\n");
        return sb.toString();
    }
}

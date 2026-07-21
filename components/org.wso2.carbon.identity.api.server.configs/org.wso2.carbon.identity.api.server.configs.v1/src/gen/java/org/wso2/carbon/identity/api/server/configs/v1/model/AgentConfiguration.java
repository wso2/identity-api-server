/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AgentConfiguration  {

    private Boolean agentsExternallyManaged;

    /**
    * If true, the tenant's agents are managed in an external system and the requested actor is not validated against the local agent user store.
    **/
    public AgentConfiguration agentsExternallyManaged(Boolean agentsExternallyManaged) {

        this.agentsExternallyManaged = agentsExternallyManaged;
        return this;
    }

    @ApiModelProperty(example = "false", value = "If true, the tenant's agents are managed in an external system and the requested actor is not validated against the local agent user store.")
    @JsonProperty("agentsExternallyManaged")
    @Valid
    public Boolean getAgentsExternallyManaged() {
        return agentsExternallyManaged;
    }
    public void setAgentsExternallyManaged(Boolean agentsExternallyManaged) {
        this.agentsExternallyManaged = agentsExternallyManaged;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentConfiguration agentConfiguration = (AgentConfiguration) o;
        return Objects.equals(this.agentsExternallyManaged, agentConfiguration.agentsExternallyManaged);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentsExternallyManaged);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentConfiguration {\n");

        sb.append("    agentsExternallyManaged: ").append(toIndentedString(agentsExternallyManaged)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

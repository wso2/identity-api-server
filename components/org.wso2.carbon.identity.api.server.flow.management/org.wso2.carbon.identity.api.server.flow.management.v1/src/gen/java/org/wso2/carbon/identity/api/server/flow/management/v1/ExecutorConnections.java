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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

@ApiModel(description = "Applicable connections for a given executor")
public class ExecutorConnections {

    private String executorName;
    private List<String> connections = null;


    /**
     * Name of the executor
     **/
    public ExecutorConnections executorName(String executorName) {

        this.executorName = executorName;
        return this;
    }

    @ApiModelProperty(example = "OIDCExecutor", value = "Name of the executor")
    @JsonProperty("executorName")
    @Valid
    public String getExecutorName() {

        return executorName;
    }

    public void setExecutorName(String executorName) {

        this.executorName = executorName;
    }

    /**
     *
     **/
    public ExecutorConnections connections(List<String> connections) {

        this.connections = connections;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("connections")
    @Valid
    public List<String> getConnections() {

        return connections;
    }

    public void setConnections(List<String> connections) {

        this.connections = connections;
    }

    public ExecutorConnections addConnectionsItem(String connectionsItem) {

        if (this.connections == null) {
            this.connections = new ArrayList<String>();
        }
        this.connections.add(connectionsItem);
        return this;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutorConnections executorConnections = (ExecutorConnections) o;
        return Objects.equals(this.executorName, executorConnections.executorName) &&
                Objects.equals(this.connections, executorConnections.connections);
    }

    @Override
    public int hashCode() {

        return Objects.hash(executorName, connections);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExecutorConnections {\n");

        sb.append("    executorName: ").append(toIndentedString(executorName)).append("\n");
        sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
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


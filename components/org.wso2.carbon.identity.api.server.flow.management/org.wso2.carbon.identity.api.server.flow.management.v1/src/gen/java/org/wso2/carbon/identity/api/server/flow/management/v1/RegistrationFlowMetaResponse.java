package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

import javax.validation.Valid;

@ApiModel(description = "Metadata response specific to self registration flow")
public class RegistrationFlowMetaResponse extends BaseFlowMetaResponse {

    private FlowMetaResponseConnectionMeta connections;
    private SelfRegistrationConnectorConfigs connectorConfigs;

    public RegistrationFlowMetaResponse connections(FlowMetaResponseConnectionMeta connections) {
        this.connections = connections;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("connections")
    @Valid
    public FlowMetaResponseConnectionMeta getConnectionMeta() {
        return connections;
    }

    public void setConnectionMeta(FlowMetaResponseConnectionMeta connections) {
        this.connections = connections;
    }

    public RegistrationFlowMetaResponse connectorConfigs(SelfRegistrationConnectorConfigs connectorConfigs) {
        this.connectorConfigs = connectorConfigs;
        super.setConnectorConfigs(connectorConfigs);
        return this;
    }

    @Override
    @JsonProperty("connectorConfigs")
    @Valid
    public SelfRegistrationConnectorConfigs getConnectorConfigs() {
        return connectorConfigs;
    }

    public void setConnectorConfigs(SelfRegistrationConnectorConfigs connectorConfigs) {
        this.connectorConfigs = connectorConfigs;
        super.setConnectorConfigs(connectorConfigs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationFlowMetaResponse that = (RegistrationFlowMetaResponse) o;
        return Objects.equals(this.connections, that.connections) &&
                Objects.equals(this.connectorConfigs, that.connectorConfigs) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connections, connectorConfigs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RegistrationFlowMetaResponse {\n");
        sb.append("    ").append(super.toString()).append("\n");
        sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

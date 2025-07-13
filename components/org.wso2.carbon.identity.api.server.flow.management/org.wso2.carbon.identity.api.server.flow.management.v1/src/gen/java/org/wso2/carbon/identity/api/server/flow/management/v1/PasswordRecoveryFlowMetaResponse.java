package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import javax.validation.Valid;
import java.util.Objects;

@ApiModel(description = "Metadata response specific to password recovery flow")
public class PasswordRecoveryFlowMetaResponse extends BaseFlowMetaResponse {

    private PasswordRecoveryConnectorConfigs connectorConfigs;

    public PasswordRecoveryFlowMetaResponse connectorConfigs(PasswordRecoveryConnectorConfigs connectorConfigs) {
        this.connectorConfigs = connectorConfigs;
        super.setConnectorConfigs(connectorConfigs);
        return this;
    }

    @Override
    @JsonProperty("connectorConfigs")
    @Valid
    public PasswordRecoveryConnectorConfigs getConnectorConfigs() {
        return connectorConfigs;
    }

    public void setConnectorConfigs(PasswordRecoveryConnectorConfigs connectorConfigs) {
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
        PasswordRecoveryFlowMetaResponse that = (PasswordRecoveryFlowMetaResponse) o;
        return Objects.equals(this.connectorConfigs, that.connectorConfigs) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connectorConfigs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordRecoveryFlowMetaResponse {\n");
        sb.append("    ").append(super.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }
}

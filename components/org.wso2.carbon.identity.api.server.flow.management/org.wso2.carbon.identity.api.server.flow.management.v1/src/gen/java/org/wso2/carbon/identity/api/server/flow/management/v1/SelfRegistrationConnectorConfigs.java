package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class SelfRegistrationConnectorConfigs extends BaseConnectorConfigs {

    private Boolean selfRegistrationEnabled;

    public SelfRegistrationConnectorConfigs selfRegistrationEnabled(Boolean selfRegistrationEnabled) {
        this.selfRegistrationEnabled = selfRegistrationEnabled;
        return this;
    }

    @ApiModelProperty(example = "false")
    @JsonProperty("selfRegistrationEnabled")
    public Boolean getSelfRegistrationEnabled() {
        return selfRegistrationEnabled;
    }

    public void setSelfRegistrationEnabled(Boolean selfRegistrationEnabled) {
        this.selfRegistrationEnabled = selfRegistrationEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SelfRegistrationConnectorConfigs that = (SelfRegistrationConnectorConfigs) o;
        return Objects.equals(this.selfRegistrationEnabled, that.selfRegistrationEnabled) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), selfRegistrationEnabled);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SelfRegistrationConnectorConfigs {\n");
        sb.append("    ").append(super.toString()).append("\n");
        sb.append("    selfRegistrationEnabled: ").append(toIndentedString(selfRegistrationEnabled)).append("\n");
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

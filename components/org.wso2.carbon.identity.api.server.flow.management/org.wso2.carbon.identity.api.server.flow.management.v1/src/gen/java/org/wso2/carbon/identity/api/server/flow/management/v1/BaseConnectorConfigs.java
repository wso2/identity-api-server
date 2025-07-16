package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class BaseConnectorConfigs {

    private Boolean multiAttributeLoginEnabled;

    public BaseConnectorConfigs multiAttributeLoginEnabled(Boolean multiAttributeLoginEnabled) {

        this.multiAttributeLoginEnabled = multiAttributeLoginEnabled;
        return this;
    }

    @ApiModelProperty(example = "false")
    @JsonProperty("multiAttributeLoginEnabled")
    public Boolean getMultiAttributeLoginEnabled() {

        return multiAttributeLoginEnabled;
    }

    public void setMultiAttributeLoginEnabled(Boolean multiAttributeLoginEnabled) {

        this.multiAttributeLoginEnabled = multiAttributeLoginEnabled;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseConnectorConfigs that = (BaseConnectorConfigs) o;
        return Objects.equals(this.multiAttributeLoginEnabled, that.multiAttributeLoginEnabled);
    }

    @Override
    public int hashCode() {

        return Objects.hash(multiAttributeLoginEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BaseConnectorConfigs {\n");
        sb.append("    multiAttributeLoginEnabled: ").append(toIndentedString(multiAttributeLoginEnabled)).append("\n");
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

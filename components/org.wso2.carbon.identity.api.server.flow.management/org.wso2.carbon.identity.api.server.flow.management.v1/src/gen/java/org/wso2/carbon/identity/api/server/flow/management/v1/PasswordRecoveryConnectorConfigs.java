package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class PasswordRecoveryConnectorConfigs extends BaseConnectorConfigs {

    private Boolean passwordRecoveryEnabled;

    public PasswordRecoveryConnectorConfigs passwordRecoveryEnabled(Boolean passwordRecoveryEnabled) {
        this.passwordRecoveryEnabled = passwordRecoveryEnabled;
        return this;
    }

    @ApiModelProperty(example = "true")
    @JsonProperty("passwordRecoveryEnabled")
    public Boolean getPasswordRecoveryEnabled() {
        return passwordRecoveryEnabled;
    }

    public void setPasswordRecoveryEnabled(Boolean passwordRecoveryEnabled) {
        this.passwordRecoveryEnabled = passwordRecoveryEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PasswordRecoveryConnectorConfigs that = (PasswordRecoveryConnectorConfigs) o;
        return Objects.equals(this.passwordRecoveryEnabled, that.passwordRecoveryEnabled) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passwordRecoveryEnabled);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordRecoveryConnectorConfigs {\n");
        sb.append("    ").append(super.toString()).append("\n");
        sb.append("    passwordRecoveryEnabled: ").append(toIndentedString(passwordRecoveryEnabled)).append("\n");
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

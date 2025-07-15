package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

@ApiModel(description = "General metadata for a flow type")
public class BaseFlowMetaResponse {

    private String flowType;
    private List<String> supportedExecutors = null;
    private BaseConnectorConfigs connectorConfigs;
    private String attributeProfile;

    public BaseFlowMetaResponse flowType(String flowType) {

        this.flowType = flowType;
        return this;
    }

    @ApiModelProperty(example = "PASSWORD_RECOVERY")
    @JsonProperty("flowType")
    @Valid
    public String getFlowType() {

        return flowType;
    }

    public void setFlowType(String flowType) {

        this.flowType = flowType;
    }

    public BaseFlowMetaResponse supportedExecutors(List<String> supportedExecutors) {

        this.supportedExecutors = supportedExecutors;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("supportedExecutors")
    @Valid
    public List<String> getSupportedExecutors() {

        return supportedExecutors;
    }

    public void setSupportedExecutors(List<String> supportedExecutors) {

        this.supportedExecutors = supportedExecutors;
    }

    public BaseFlowMetaResponse addSupportedExecutorsItem(String supportedExecutorsItem) {

        if (this.supportedExecutors == null) {
            this.supportedExecutors = new ArrayList<>();
        }
        this.supportedExecutors.add(supportedExecutorsItem);
        return this;
    }

    public BaseFlowMetaResponse connectorConfigs(BaseConnectorConfigs connectorConfigs) {

        this.connectorConfigs = connectorConfigs;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("connectorConfigs")
    @Valid
    public BaseConnectorConfigs getConnectorConfigs() {

        return connectorConfigs;
    }

    public void setConnectorConfigs(BaseConnectorConfigs connectorConfigs) {

        this.connectorConfigs = connectorConfigs;
    }

    public BaseFlowMetaResponse attributeProfile(String attributeProfile) {

        this.attributeProfile = attributeProfile;
        return this;
    }

    @ApiModelProperty(example = "End-User-Profile")
    @JsonProperty("attributeProfile")
    @Valid
    public String getAttributeProfile() {

        return attributeProfile;
    }

    public void setAttributeProfile(String attributeProfile) {

        this.attributeProfile = attributeProfile;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseFlowMetaResponse that = (BaseFlowMetaResponse) o;
        return Objects.equals(this.flowType, that.flowType) &&
                Objects.equals(this.supportedExecutors, that.supportedExecutors) &&
                Objects.equals(this.connectorConfigs, that.connectorConfigs) &&
                Objects.equals(this.attributeProfile, that.attributeProfile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(flowType, supportedExecutors, connectorConfigs, attributeProfile);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BaseFlowMetaResponse {\n");
        sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
        sb.append("    supportedExecutors: ").append(toIndentedString(supportedExecutors)).append("\n");
        sb.append("    connectorConfigs: ").append(toIndentedString(connectorConfigs)).append("\n");
        sb.append("    attributeProfile: ").append(toIndentedString(attributeProfile)).append("\n");
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

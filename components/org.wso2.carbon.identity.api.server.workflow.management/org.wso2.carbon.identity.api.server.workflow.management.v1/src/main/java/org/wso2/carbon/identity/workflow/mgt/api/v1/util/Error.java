//package org.wso2.carbon.identity.workflow.mgt.api.v1.util;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.hazelcast.org.codehaus.commons.nullanalysis.NotNull;
//import io.swagger.annotations.ApiModelProperty;
//
//import java.util.Objects;
//
//import javax.validation.Valid;
//
//public class Error  {
//
//    private String code;
//    private String message;
//    private String description;
//    private String traceId;
//
//    /**
//     * An error code.
//     **/
//    public Error code(String code) {
//
//        this.code = code;
//        return this;
//    }
//
//    @ApiModelProperty(example = "ORG-00000", required = true, value = "An error code.")
//    @JsonProperty("code")
//    @Valid
//    @NotNull(message = "Property code cannot be null.")
//
//    public String getCode() {
//        return code;
//    }
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    /**
//     * An error message.
//     **/
//    public Error message(String message) {
//
//        this.message = message;
//        return this;
//    }
//
//    @ApiModelProperty(example = "Some Error Message", required = true, value = "An error message.")
//    @JsonProperty("message")
//    @Valid
//    @NotNull(message = "Property message cannot be null.")
//
//    public String getMessage() {
//        return message;
//    }
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    /**
//     * An error description.
//     **/
//    public Error description(String description) {
//
//        this.description = description;
//        return this;
//    }
//
//    @ApiModelProperty(example = "Some Error Description", value = "An error description.")
//    @JsonProperty("description")
//    @Valid
//    public String getDescription() {
//        return description;
//    }
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    /**
//     * An error trace identifier.
//     **/
//    public Error traceId(String traceId) {
//
//        this.traceId = traceId;
//        return this;
//    }
//
//    @ApiModelProperty(example = "e0fbcfeb-3617-43c4-8dd0-7b7d38e13047", value = "An error trace identifier.")
//    @JsonProperty("traceId")
//    @Valid
//    public String getTraceId() {
//        return traceId;
//    }
//    public void setTraceId(String traceId) {
//        this.traceId = traceId;
//    }
//
//
//
//    @Override
//    public boolean equals(java.lang.Object o) {
//
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        Error error = (Error) o;
//        return Objects.equals(this.code, error.code) &&
//                Objects.equals(this.message, error.message) &&
//                Objects.equals(this.description, error.description) &&
//                Objects.equals(this.traceId, error.traceId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(code, message, description, traceId);
//    }
//
//    @Override
//    public String toString() {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("class Error {\n");
//
//        sb.append("    code: ").append(toIndentedString(code)).append("\n");
//        sb.append("    message: ").append(toIndentedString(message)).append("\n");
//        sb.append("    description: ").append(toIndentedString(description)).append("\n");
//        sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
//        sb.append("}");
//        return sb.toString();
//    }
//
//    /**
//     * Convert the given object to string with each line indented by 4 spaces
//     * (except the first line).
//     */
//    private String toIndentedString(java.lang.Object o) {
//
//        if (o == null) {
//            return "null";
//        }
//        return o.toString().replace("\n", "\n");
//    }
//}
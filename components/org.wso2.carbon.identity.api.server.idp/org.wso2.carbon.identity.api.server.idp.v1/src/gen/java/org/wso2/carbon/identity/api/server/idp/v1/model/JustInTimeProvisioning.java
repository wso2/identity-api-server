package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class JustInTimeProvisioning   {
  
  private Boolean isEnabled = false;


@XmlType(name="SchemeEnum")
@XmlEnum(String.class)
public enum SchemeEnum {

    @XmlEnumValue("PROMPT_USERNAME_PASSWORD_CONSENT") PROMPT_USERNAME_PASSWORD_CONSENT(String.valueOf("PROMPT_USERNAME_PASSWORD_CONSENT")), @XmlEnumValue("PROMPT_PASSWORD_CONSENT") PROMPT_PASSWORD_CONSENT(String.valueOf("PROMPT_PASSWORD_CONSENT")), @XmlEnumValue("PROMPT_CONSENT") PROMPT_CONSENT(String.valueOf("PROMPT_CONSENT")), @XmlEnumValue("PROVISION_SILENTLY") PROVISION_SILENTLY(String.valueOf("PROVISION_SILENTLY"));


    private String value;

    SchemeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static SchemeEnum fromValue(String value) {
        for (SchemeEnum b : SchemeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private SchemeEnum scheme = SchemeEnum.PROVISION_SILENTLY;

  private String userstore = "PRIMARY";


  /**
   **/
  public JustInTimeProvisioning isEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  
  @ApiModelProperty(example = "true", required = true, value = "")
  @JsonProperty("isEnabled")
  @Valid
  @NotNull(message = "Property isEnabled cannot be null.")
  public Boolean getIsEnabled() {
    return isEnabled;
  }
  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }


  /**
   **/
  public JustInTimeProvisioning scheme(SchemeEnum scheme) {
    this.scheme = scheme;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("scheme")
  @Valid
  public SchemeEnum getScheme() {
    return scheme;
  }
  public void setScheme(SchemeEnum scheme) {
    this.scheme = scheme;
  }


  /**
   **/
  public JustInTimeProvisioning userstore(String userstore) {
    this.userstore = userstore;
    return this;
  }

  
  @ApiModelProperty(example = "PRIMARY", value = "")
  @JsonProperty("userstore")
  @Valid
  public String getUserstore() {
    return userstore;
  }
  public void setUserstore(String userstore) {
    this.userstore = userstore;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JustInTimeProvisioning justInTimeProvisioning = (JustInTimeProvisioning) o;
    return Objects.equals(this.isEnabled, justInTimeProvisioning.isEnabled) &&
        Objects.equals(this.scheme, justInTimeProvisioning.scheme) &&
        Objects.equals(this.userstore, justInTimeProvisioning.userstore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isEnabled, scheme, userstore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JustInTimeProvisioning {\n");
    
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
    sb.append("    userstore: ").append(toIndentedString(userstore)).append("\n");
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
    return o.toString().replace("\n", "\n    ");
  }
}


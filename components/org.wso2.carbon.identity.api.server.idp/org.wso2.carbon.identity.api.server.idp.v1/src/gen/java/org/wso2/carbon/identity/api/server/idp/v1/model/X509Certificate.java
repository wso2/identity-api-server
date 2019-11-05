package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class X509Certificate   {
  
  private List<String> certificates = null;


  /**
   **/
  public X509Certificate certificates(List<String> certificates) {
    this.certificates = certificates;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("certificates")
  @Valid
  public List<String> getCertificates() {
    return certificates;
  }
  public void setCertificates(List<String> certificates) {
    this.certificates = certificates;
  }

  public X509Certificate addCertificatesItem(String certificatesItem) {
    if (this.certificates == null) {
      this.certificates = new ArrayList<>();
    }
    this.certificates.add(certificatesItem);
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
    X509Certificate x509Certificate = (X509Certificate) o;
    return Objects.equals(this.certificates, x509Certificate.certificates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(certificates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class X509Certificate {\n");
    
    sb.append("    certificates: ").append(toIndentedString(certificates)).append("\n");
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


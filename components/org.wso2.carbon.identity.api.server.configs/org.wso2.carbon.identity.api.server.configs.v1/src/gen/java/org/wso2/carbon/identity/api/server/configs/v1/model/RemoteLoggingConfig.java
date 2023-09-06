/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RemoteLoggingConfig  {
  
    private String remoteUrl;
    private String connectTimeoutMillis;
    private Boolean verifyHostname = true;
    private String username;
    private String password;
    private String keystoreLocation;
    private String keystorePassword;
    private String truststoreLocation;
    private String truststorePassword;

    /**
    * Remote Server URL
    **/
    public RemoteLoggingConfig remoteUrl(String remoteUrl) {

        this.remoteUrl = remoteUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://test.remote.server.com/api/log", value = "Remote Server URL")
    @JsonProperty("remoteUrl")
    @Valid
    public String getRemoteUrl() {
        return remoteUrl;
    }
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    /**
    * Connect Timeout in Millisecond
    **/
    public RemoteLoggingConfig connectTimeoutMillis(String connectTimeoutMillis) {

        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }
    
    @ApiModelProperty(example = "5000", value = "Connect Timeout in Millisecond")
    @JsonProperty("connectTimeoutMillis")
    @Valid
    public String getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }
    public void setConnectTimeoutMillis(String connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    /**
    * If this property is set to true, Hostname will be verified
    **/
    public RemoteLoggingConfig verifyHostname(Boolean verifyHostname) {

        this.verifyHostname = verifyHostname;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If this property is set to true, Hostname will be verified")
    @JsonProperty("verifyHostname")
    @Valid
    public Boolean getVerifyHostname() {
        return verifyHostname;
    }
    public void setVerifyHostname(Boolean verifyHostname) {
        this.verifyHostname = verifyHostname;
    }

    /**
    * The username to be used for authentication with the remote server
    **/
    public RemoteLoggingConfig username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "The username to be used for authentication with the remote server")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * The password to be used for authentication with the remote server
    **/
    public RemoteLoggingConfig password(String password) {

        this.password = password;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "The password to be used for authentication with the remote server")
    @JsonProperty("password")
    @Valid
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
    * The location of the keystore which contains your private keys and certificates to be sent for authentication to the remote server
    **/
    public RemoteLoggingConfig keystoreLocation(String keystoreLocation) {

        this.keystoreLocation = keystoreLocation;
        return this;
    }
    
    @ApiModelProperty(value = "The location of the keystore which contains your private keys and certificates to be sent for authentication to the remote server")
    @JsonProperty("keystoreLocation")
    @Valid
    public String getKeystoreLocation() {
        return keystoreLocation;
    }
    public void setKeystoreLocation(String keystoreLocation) {
        this.keystoreLocation = keystoreLocation;
    }

    /**
    *  The password of the keystore
    **/
    public RemoteLoggingConfig keystorePassword(String keystorePassword) {

        this.keystorePassword = keystorePassword;
        return this;
    }
    
    @ApiModelProperty(value = " The password of the keystore")
    @JsonProperty("keystorePassword")
    @Valid
    public String getKeystorePassword() {
        return keystorePassword;
    }
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    /**
    *  The location of the truststore which contains the certificates of the remote server
    **/
    public RemoteLoggingConfig truststoreLocation(String truststoreLocation) {

        this.truststoreLocation = truststoreLocation;
        return this;
    }
    
    @ApiModelProperty(value = " The location of the truststore which contains the certificates of the remote server")
    @JsonProperty("truststoreLocation")
    @Valid
    public String getTruststoreLocation() {
        return truststoreLocation;
    }
    public void setTruststoreLocation(String truststoreLocation) {
        this.truststoreLocation = truststoreLocation;
    }

    /**
    *  The password of the truststore
    **/
    public RemoteLoggingConfig truststorePassword(String truststorePassword) {

        this.truststorePassword = truststorePassword;
        return this;
    }
    
    @ApiModelProperty(value = " The password of the truststore")
    @JsonProperty("truststorePassword")
    @Valid
    public String getTruststorePassword() {
        return truststorePassword;
    }
    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteLoggingConfig remoteLoggingConfig = (RemoteLoggingConfig) o;
        return Objects.equals(this.remoteUrl, remoteLoggingConfig.remoteUrl) &&
            Objects.equals(this.connectTimeoutMillis, remoteLoggingConfig.connectTimeoutMillis) &&
            Objects.equals(this.verifyHostname, remoteLoggingConfig.verifyHostname) &&
            Objects.equals(this.username, remoteLoggingConfig.username) &&
            Objects.equals(this.password, remoteLoggingConfig.password) &&
            Objects.equals(this.keystoreLocation, remoteLoggingConfig.keystoreLocation) &&
            Objects.equals(this.keystorePassword, remoteLoggingConfig.keystorePassword) &&
            Objects.equals(this.truststoreLocation, remoteLoggingConfig.truststoreLocation) &&
            Objects.equals(this.truststorePassword, remoteLoggingConfig.truststorePassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteUrl, connectTimeoutMillis, verifyHostname, username, password, keystoreLocation, keystorePassword, truststoreLocation, truststorePassword);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteLoggingConfig {\n");
        
        sb.append("    remoteUrl: ").append(toIndentedString(remoteUrl)).append("\n");
        sb.append("    connectTimeoutMillis: ").append(toIndentedString(connectTimeoutMillis)).append("\n");
        sb.append("    verifyHostname: ").append(toIndentedString(verifyHostname)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    keystoreLocation: ").append(toIndentedString(keystoreLocation)).append("\n");
        sb.append("    keystorePassword: ").append(toIndentedString(keystorePassword)).append("\n");
        sb.append("    truststoreLocation: ").append(toIndentedString(truststoreLocation)).append("\n");
        sb.append("    truststorePassword: ").append(toIndentedString(truststorePassword)).append("\n");
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


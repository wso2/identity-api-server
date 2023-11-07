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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PassiveStsConfiguration  {
  
    private String realm;
    private String replyTo;
    private String replyToLogout;

    /**
    **/
    public PassiveStsConfiguration realm(String realm) {

        this.realm = realm;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("realm")
    @Valid
    @NotNull(message = "Property realm cannot be null.")

    public String getRealm() {
        return realm;
    }
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
    **/
    public PassiveStsConfiguration replyTo(String replyTo) {

        this.replyTo = replyTo;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("replyTo")
    @Valid
    @NotNull(message = "Property replyTo cannot be null.")

    public String getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
    **/
    public PassiveStsConfiguration replyToLogout(String replyToLogout) {

        this.replyToLogout = replyToLogout;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("replyToLogout")
    @Valid
    @NotNull(message = "Property replyToLogout cannot be null.")

    public String getReplyToLogout() {
        return replyToLogout;
    }
    public void setReplyToLogout(String replyToLogout) {
        this.replyToLogout = replyToLogout;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PassiveStsConfiguration passiveStsConfiguration = (PassiveStsConfiguration) o;
        return Objects.equals(this.realm, passiveStsConfiguration.realm) &&
            Objects.equals(this.replyTo, passiveStsConfiguration.replyTo) &&
            Objects.equals(this.replyToLogout, passiveStsConfiguration.replyToLogout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realm, replyTo, replyToLogout);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PassiveStsConfiguration {\n");
        
        sb.append("    realm: ").append(toIndentedString(realm)).append("\n");
        sb.append("    replyTo: ").append(toIndentedString(replyTo)).append("\n");
        sb.append("    replyToLogout: ").append(toIndentedString(replyToLogout)).append("\n");
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


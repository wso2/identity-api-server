/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.application.management.v1;

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

public class IdpInitiatedSingleLogout  {
  
    private Boolean enabled = false;
    private List<String> returnToUrls = null;


    /**
    **/
    public IdpInitiatedSingleLogout enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    **/
    public IdpInitiatedSingleLogout returnToUrls(List<String> returnToUrls) {

        this.returnToUrls = returnToUrls;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("returnToUrls")
    @Valid
    public List<String> getReturnToUrls() {
        return returnToUrls;
    }
    public void setReturnToUrls(List<String> returnToUrls) {
        this.returnToUrls = returnToUrls;
    }

    public IdpInitiatedSingleLogout addReturnToUrlsItem(String returnToUrlsItem) {
        if (this.returnToUrls == null) {
            this.returnToUrls = new ArrayList<>();
        }
        this.returnToUrls.add(returnToUrlsItem);
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
        IdpInitiatedSingleLogout idpInitiatedSingleLogout = (IdpInitiatedSingleLogout) o;
        return Objects.equals(this.enabled, idpInitiatedSingleLogout.enabled) &&
            Objects.equals(this.returnToUrls, idpInitiatedSingleLogout.returnToUrls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, returnToUrls);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdpInitiatedSingleLogout {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    returnToUrls: ").append(toIndentedString(returnToUrls)).append("\n");
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


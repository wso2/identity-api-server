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
import org.wso2.carbon.identity.api.server.application.management.v1.Claim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SubjectConfig  {
  
    private Claim claim;
    private Boolean includeUserDomain;
    private Boolean includeTenantDomain;
    private Boolean useMappedLocalSubject;

    /**
    **/
    public SubjectConfig claim(Claim claim) {

        this.claim = claim;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claim")
    @Valid
    public Claim getClaim() {
        return claim;
    }
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    /**
    **/
    public SubjectConfig includeUserDomain(Boolean includeUserDomain) {

        this.includeUserDomain = includeUserDomain;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("includeUserDomain")
    @Valid
    public Boolean getIncludeUserDomain() {
        return includeUserDomain;
    }
    public void setIncludeUserDomain(Boolean includeUserDomain) {
        this.includeUserDomain = includeUserDomain;
    }

    /**
    **/
    public SubjectConfig includeTenantDomain(Boolean includeTenantDomain) {

        this.includeTenantDomain = includeTenantDomain;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("includeTenantDomain")
    @Valid
    public Boolean getIncludeTenantDomain() {
        return includeTenantDomain;
    }
    public void setIncludeTenantDomain(Boolean includeTenantDomain) {
        this.includeTenantDomain = includeTenantDomain;
    }

    /**
    **/
    public SubjectConfig useMappedLocalSubject(Boolean useMappedLocalSubject) {

        this.useMappedLocalSubject = useMappedLocalSubject;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("useMappedLocalSubject")
    @Valid
    public Boolean getUseMappedLocalSubject() {
        return useMappedLocalSubject;
    }
    public void setUseMappedLocalSubject(Boolean useMappedLocalSubject) {
        this.useMappedLocalSubject = useMappedLocalSubject;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubjectConfig subjectConfig = (SubjectConfig) o;
        return Objects.equals(this.claim, subjectConfig.claim) &&
            Objects.equals(this.includeUserDomain, subjectConfig.includeUserDomain) &&
            Objects.equals(this.includeTenantDomain, subjectConfig.includeTenantDomain) &&
            Objects.equals(this.useMappedLocalSubject, subjectConfig.useMappedLocalSubject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claim, includeUserDomain, includeTenantDomain, useMappedLocalSubject);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SubjectConfig {\n");
        
        sb.append("    claim: ").append(toIndentedString(claim)).append("\n");
        sb.append("    includeUserDomain: ").append(toIndentedString(includeUserDomain)).append("\n");
        sb.append("    includeTenantDomain: ").append(toIndentedString(includeTenantDomain)).append("\n");
        sb.append("    useMappedLocalSubject: ").append(toIndentedString(useMappedLocalSubject)).append("\n");
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


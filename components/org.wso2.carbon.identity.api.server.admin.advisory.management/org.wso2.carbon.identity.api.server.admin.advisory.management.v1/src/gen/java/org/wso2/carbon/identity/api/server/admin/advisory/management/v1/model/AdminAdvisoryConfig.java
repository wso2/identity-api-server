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

package org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;

/**
 * Admin advisory banner configuration response.
 **/
@ApiModel(description = "Admin advisory banner configuration response.")
public class AdminAdvisoryConfig  {
  
    private Boolean enableBanner;
    private String bannerContent;

    /**
    * Admin banner enabled status.
    **/
    public AdminAdvisoryConfig enableBanner(Boolean enableBanner) {

        this.enableBanner = enableBanner;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Admin banner enabled status.")
    @JsonProperty("enableBanner")
    @Valid
    public Boolean getEnableBanner() {
        return enableBanner;
    }
    public void setEnableBanner(Boolean enableBanner) {
        this.enableBanner = enableBanner;
    }

    /**
    * Admin banner content.
    **/
    public AdminAdvisoryConfig bannerContent(String bannerContent) {

        this.bannerContent = bannerContent;
        return this;
    }
    
    @ApiModelProperty(example = "Unauthorized use of this tool is strictly prohibited", value = "Admin banner content.")
    @JsonProperty("bannerContent")
    @Valid
    public String getBannerContent() {
        return bannerContent;
    }
    public void setBannerContent(String bannerContent) {
        this.bannerContent = bannerContent;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminAdvisoryConfig adminAdvisoryConfig = (AdminAdvisoryConfig) o;
        return Objects.equals(this.enableBanner, adminAdvisoryConfig.enableBanner) &&
            Objects.equals(this.bannerContent, adminAdvisoryConfig.bannerContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableBanner, bannerContent);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AdminAdvisoryConfig {\n");
        
        sb.append("    enableBanner: ").append(toIndentedString(enableBanner)).append("\n");
        sb.append("    bannerContent: ").append(toIndentedString(bannerContent)).append("\n");
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


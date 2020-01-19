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

package org.wso2.carbon.identity.api.server.script.library.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibrary;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ScriptLibraryListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<ScriptLibrary> scriptLibraries = null;


    /**
    **/
    public ScriptLibraryListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    **/
    public ScriptLibraryListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    **/
    public ScriptLibraryListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    **/
    public ScriptLibraryListResponse scriptLibraries(List<ScriptLibrary> scriptLibraries) {

        this.scriptLibraries = scriptLibraries;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("scriptLibraries")
    @Valid
    public List<ScriptLibrary> getScriptLibraries() {
        return scriptLibraries;
    }
    public void setScriptLibraries(List<ScriptLibrary> scriptLibraries) {
        this.scriptLibraries = scriptLibraries;
    }

    public ScriptLibraryListResponse addScriptLibrariesItem(ScriptLibrary scriptLibrariesItem) {
        if (this.scriptLibraries == null) {
            this.scriptLibraries = new ArrayList<>();
        }
        this.scriptLibraries.add(scriptLibrariesItem);
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
        ScriptLibraryListResponse scriptLibraryListResponse = (ScriptLibraryListResponse) o;
        return Objects.equals(this.totalResults, scriptLibraryListResponse.totalResults) &&
            Objects.equals(this.startIndex, scriptLibraryListResponse.startIndex) &&
            Objects.equals(this.count, scriptLibraryListResponse.count) &&
            Objects.equals(this.scriptLibraries, scriptLibraryListResponse.scriptLibraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, scriptLibraries);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ScriptLibraryListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    scriptLibraries: ").append(toIndentedString(scriptLibraries)).append("\n");
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


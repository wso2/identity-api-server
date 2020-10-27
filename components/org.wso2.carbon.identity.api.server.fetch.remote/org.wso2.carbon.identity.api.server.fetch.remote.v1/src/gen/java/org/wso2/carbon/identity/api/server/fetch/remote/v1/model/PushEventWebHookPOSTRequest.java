/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestCommits;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestPusher;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestRepository;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestSender;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushEventWebHookPOSTRequest  {
  
    private String ref;
    private String before;
    private String after;
    private Boolean created;
    private Boolean deleted;
    private Boolean forced;
    private String baseRef;
    private String compare;
    private PushEventWebHookPOSTRequestPusher pusher;
    private PushEventWebHookPOSTRequestSender sender;
    private List<PushEventWebHookPOSTRequestCommits> commits = null;

    private PushEventWebHookPOSTRequestCommits headCommit;
    private PushEventWebHookPOSTRequestRepository repository;

    /**
    **/
    public PushEventWebHookPOSTRequest ref(String ref) {

        this.ref = ref;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("ref")
    @Valid
    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest before(String before) {

        this.before = before;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("before")
    @Valid
    public String getBefore() {
        return before;
    }
    public void setBefore(String before) {
        this.before = before;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest after(String after) {

        this.after = after;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("after")
    @Valid
    public String getAfter() {
        return after;
    }
    public void setAfter(String after) {
        this.after = after;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest created(Boolean created) {

        this.created = created;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("created")
    @Valid
    public Boolean getCreated() {
        return created;
    }
    public void setCreated(Boolean created) {
        this.created = created;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest deleted(Boolean deleted) {

        this.deleted = deleted;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("deleted")
    @Valid
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest forced(Boolean forced) {

        this.forced = forced;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("forced")
    @Valid
    public Boolean getForced() {
        return forced;
    }
    public void setForced(Boolean forced) {
        this.forced = forced;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest baseRef(String baseRef) {

        this.baseRef = baseRef;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("base_ref")
    @Valid
    public String getBaseRef() {
        return baseRef;
    }
    public void setBaseRef(String baseRef) {
        this.baseRef = baseRef;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest compare(String compare) {

        this.compare = compare;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("compare")
    @Valid
    public String getCompare() {
        return compare;
    }
    public void setCompare(String compare) {
        this.compare = compare;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest pusher(PushEventWebHookPOSTRequestPusher pusher) {

        this.pusher = pusher;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("pusher")
    @Valid
    public PushEventWebHookPOSTRequestPusher getPusher() {
        return pusher;
    }
    public void setPusher(PushEventWebHookPOSTRequestPusher pusher) {
        this.pusher = pusher;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest sender(PushEventWebHookPOSTRequestSender sender) {

        this.sender = sender;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("sender")
    @Valid
    public PushEventWebHookPOSTRequestSender getSender() {
        return sender;
    }
    public void setSender(PushEventWebHookPOSTRequestSender sender) {
        this.sender = sender;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest commits(List<PushEventWebHookPOSTRequestCommits> commits) {

        this.commits = commits;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("commits")
    @Valid
    public List<PushEventWebHookPOSTRequestCommits> getCommits() {
        return commits;
    }
    public void setCommits(List<PushEventWebHookPOSTRequestCommits> commits) {
        this.commits = commits;
    }

    public PushEventWebHookPOSTRequest addCommitsItem(PushEventWebHookPOSTRequestCommits commitsItem) {
        if (this.commits == null) {
            this.commits = new ArrayList<>();
        }
        this.commits.add(commitsItem);
        return this;
    }

        /**
    **/
    public PushEventWebHookPOSTRequest headCommit(PushEventWebHookPOSTRequestCommits headCommit) {

        this.headCommit = headCommit;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("head_commit")
    @Valid
    public PushEventWebHookPOSTRequestCommits getHeadCommit() {
        return headCommit;
    }
    public void setHeadCommit(PushEventWebHookPOSTRequestCommits headCommit) {
        this.headCommit = headCommit;
    }

    /**
    **/
    public PushEventWebHookPOSTRequest repository(PushEventWebHookPOSTRequestRepository repository) {

        this.repository = repository;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("repository")
    @Valid
    public PushEventWebHookPOSTRequestRepository getRepository() {
        return repository;
    }
    public void setRepository(PushEventWebHookPOSTRequestRepository repository) {
        this.repository = repository;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest = (PushEventWebHookPOSTRequest) o;
        return Objects.equals(this.ref, pushEventWebHookPOSTRequest.ref) &&
            Objects.equals(this.before, pushEventWebHookPOSTRequest.before) &&
            Objects.equals(this.after, pushEventWebHookPOSTRequest.after) &&
            Objects.equals(this.created, pushEventWebHookPOSTRequest.created) &&
            Objects.equals(this.deleted, pushEventWebHookPOSTRequest.deleted) &&
            Objects.equals(this.forced, pushEventWebHookPOSTRequest.forced) &&
            Objects.equals(this.baseRef, pushEventWebHookPOSTRequest.baseRef) &&
            Objects.equals(this.compare, pushEventWebHookPOSTRequest.compare) &&
            Objects.equals(this.pusher, pushEventWebHookPOSTRequest.pusher) &&
            Objects.equals(this.sender, pushEventWebHookPOSTRequest.sender) &&
            Objects.equals(this.commits, pushEventWebHookPOSTRequest.commits) &&
            Objects.equals(this.headCommit, pushEventWebHookPOSTRequest.headCommit) &&
            Objects.equals(this.repository, pushEventWebHookPOSTRequest.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref, before, after, created, deleted, forced, baseRef, compare, pusher, sender, commits, headCommit, repository);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushEventWebHookPOSTRequest {\n");
        
        sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
        sb.append("    before: ").append(toIndentedString(before)).append("\n");
        sb.append("    after: ").append(toIndentedString(after)).append("\n");
        sb.append("    created: ").append(toIndentedString(created)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("    forced: ").append(toIndentedString(forced)).append("\n");
        sb.append("    baseRef: ").append(toIndentedString(baseRef)).append("\n");
        sb.append("    compare: ").append(toIndentedString(compare)).append("\n");
        sb.append("    pusher: ").append(toIndentedString(pusher)).append("\n");
        sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
        sb.append("    commits: ").append(toIndentedString(commits)).append("\n");
        sb.append("    headCommit: ").append(toIndentedString(headCommit)).append("\n");
        sb.append("    repository: ").append(toIndentedString(repository)).append("\n");
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


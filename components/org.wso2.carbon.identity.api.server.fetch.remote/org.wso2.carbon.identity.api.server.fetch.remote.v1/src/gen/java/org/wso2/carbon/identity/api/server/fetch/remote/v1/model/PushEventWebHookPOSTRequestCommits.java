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
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestAuthor;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestCommitter;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushEventWebHookPOSTRequestCommits  {
  
    private String id;
    private String treeId;
    private Boolean distinct;
    private String message;
    private String timestamp;
    private String url;
    private PushEventWebHookPOSTRequestAuthor author;
    private PushEventWebHookPOSTRequestCommitter committer;
    private List<String> added = null;

    private List<String> removed = null;

    private List<String> modified = null;


    /**
    **/
    public PushEventWebHookPOSTRequestCommits id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits treeId(String treeId) {

        this.treeId = treeId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("tree_id")
    @Valid
    public String getTreeId() {
        return treeId;
    }
    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits distinct(Boolean distinct) {

        this.distinct = distinct;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("distinct")
    @Valid
    public Boolean getDistinct() {
        return distinct;
    }
    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits timestamp(String timestamp) {

        this.timestamp = timestamp;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("timestamp")
    @Valid
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits url(String url) {

        this.url = url;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("url")
    @Valid
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits author(PushEventWebHookPOSTRequestAuthor author) {

        this.author = author;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("author")
    @Valid
    public PushEventWebHookPOSTRequestAuthor getAuthor() {
        return author;
    }
    public void setAuthor(PushEventWebHookPOSTRequestAuthor author) {
        this.author = author;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits committer(PushEventWebHookPOSTRequestCommitter committer) {

        this.committer = committer;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("committer")
    @Valid
    public PushEventWebHookPOSTRequestCommitter getCommitter() {
        return committer;
    }
    public void setCommitter(PushEventWebHookPOSTRequestCommitter committer) {
        this.committer = committer;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestCommits added(List<String> added) {

        this.added = added;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("added")
    @Valid
    public List<String> getAdded() {
        return added;
    }
    public void setAdded(List<String> added) {
        this.added = added;
    }

    public PushEventWebHookPOSTRequestCommits addAddedItem(String addedItem) {
        if (this.added == null) {
            this.added = new ArrayList<>();
        }
        this.added.add(addedItem);
        return this;
    }

        /**
    **/
    public PushEventWebHookPOSTRequestCommits removed(List<String> removed) {

        this.removed = removed;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("removed")
    @Valid
    public List<String> getRemoved() {
        return removed;
    }
    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }

    public PushEventWebHookPOSTRequestCommits addRemovedItem(String removedItem) {
        if (this.removed == null) {
            this.removed = new ArrayList<>();
        }
        this.removed.add(removedItem);
        return this;
    }

        /**
    **/
    public PushEventWebHookPOSTRequestCommits modified(List<String> modified) {

        this.modified = modified;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("modified")
    @Valid
    public List<String> getModified() {
        return modified;
    }
    public void setModified(List<String> modified) {
        this.modified = modified;
    }

    public PushEventWebHookPOSTRequestCommits addModifiedItem(String modifiedItem) {
        if (this.modified == null) {
            this.modified = new ArrayList<>();
        }
        this.modified.add(modifiedItem);
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
        PushEventWebHookPOSTRequestCommits pushEventWebHookPOSTRequestCommits = (PushEventWebHookPOSTRequestCommits) o;
        return Objects.equals(this.id, pushEventWebHookPOSTRequestCommits.id) &&
            Objects.equals(this.treeId, pushEventWebHookPOSTRequestCommits.treeId) &&
            Objects.equals(this.distinct, pushEventWebHookPOSTRequestCommits.distinct) &&
            Objects.equals(this.message, pushEventWebHookPOSTRequestCommits.message) &&
            Objects.equals(this.timestamp, pushEventWebHookPOSTRequestCommits.timestamp) &&
            Objects.equals(this.url, pushEventWebHookPOSTRequestCommits.url) &&
            Objects.equals(this.author, pushEventWebHookPOSTRequestCommits.author) &&
            Objects.equals(this.committer, pushEventWebHookPOSTRequestCommits.committer) &&
            Objects.equals(this.added, pushEventWebHookPOSTRequestCommits.added) &&
            Objects.equals(this.removed, pushEventWebHookPOSTRequestCommits.removed) &&
            Objects.equals(this.modified, pushEventWebHookPOSTRequestCommits.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, treeId, distinct, message, timestamp, url, author, committer, added, removed, modified);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushEventWebHookPOSTRequestCommits {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    treeId: ").append(toIndentedString(treeId)).append("\n");
        sb.append("    distinct: ").append(toIndentedString(distinct)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    author: ").append(toIndentedString(author)).append("\n");
        sb.append("    committer: ").append(toIndentedString(committer)).append("\n");
        sb.append("    added: ").append(toIndentedString(added)).append("\n");
        sb.append("    removed: ").append(toIndentedString(removed)).append("\n");
        sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
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


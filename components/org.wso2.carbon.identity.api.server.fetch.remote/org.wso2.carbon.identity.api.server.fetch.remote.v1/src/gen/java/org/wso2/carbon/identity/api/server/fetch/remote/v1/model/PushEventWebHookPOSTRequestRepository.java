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
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestRepositoryOwner;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushEventWebHookPOSTRequestRepository  {
  
    private Integer id;
    private String nodeId;
    private String name;
    private String fullName;
    private Boolean _private;
    private PushEventWebHookPOSTRequestRepositoryOwner owner;
    private String htmlUrl;
    private String description;
    private Boolean fork;
    private String url;
    private String forksUrl;
    private String keysUrl;
    private String collaboratorsUrl;
    private String teamsUrl;
    private String hooksUrl;
    private String issueEventsUrl;
    private String eventsUrl;
    private String assigneesUrl;
    private String branchesUrl;
    private String tagsUrl;
    private String blobsUrl;
    private String gitTagsUrl;
    private String gitRefsUrl;
    private String treesUrl;
    private String statusesUrl;
    private String languagesUrl;
    private String stargazersUrl;
    private String contributorsUrl;
    private String subscribersUrl;
    private String subscriptionUrl;
    private String commitsUrl;
    private String gitCommitsUrl;
    private String commentsUrl;
    private String issueCommentUrl;
    private String contentsUrl;
    private String compareUrl;
    private String mergesUrl;
    private String archiveUrl;
    private String downloadsUrl;
    private String issuesUrl;
    private String pullsUrl;
    private String milestonesUrl;
    private String notificationsUrl;
    private String labelsUrl;
    private String releasesUrl;
    private String deploymentsUrl;
    private Integer createdAt;
    private String updatedAt;
    private Integer pushedAt;
    private String gitUrl;
    private String sshUrl;
    private String cloneUrl;
    private String svnUrl;
    private String homepage;
    private Integer size;
    private Integer stargazersCount;
    private Integer watchersCount;
    private String language;
    private Boolean hasIssues;
    private Boolean hasProjects;
    private Boolean hasDownloads;
    private Boolean hasWiki;
    private Boolean hasPages;
    private Integer forksCount;
    private String mirrorUrl;
    private Boolean archived;
    private Boolean disabled;
    private Integer openIssuesCount;
    private String license;
    private Integer forks;
    private Integer openIssues;
    private Integer watchers;
    private String defaultBranch;
    private Integer stargazers;
    private String masterBranch;

    /**
    **/
    public PushEventWebHookPOSTRequestRepository id(Integer id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    @Valid
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository nodeId(String nodeId) {

        this.nodeId = nodeId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("node_id")
    @Valid
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository fullName(String fullName) {

        this.fullName = fullName;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("full_name")
    @Valid
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository _private(Boolean _private) {

        this._private = _private;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("private")
    @Valid
    public Boolean getPrivate() {
        return _private;
    }
    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository owner(PushEventWebHookPOSTRequestRepositoryOwner owner) {

        this.owner = owner;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("owner")
    @Valid
    public PushEventWebHookPOSTRequestRepositoryOwner getOwner() {
        return owner;
    }
    public void setOwner(PushEventWebHookPOSTRequestRepositoryOwner owner) {
        this.owner = owner;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository htmlUrl(String htmlUrl) {

        this.htmlUrl = htmlUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("html_url")
    @Valid
    public String getHtmlUrl() {
        return htmlUrl;
    }
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository fork(Boolean fork) {

        this.fork = fork;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("fork")
    @Valid
    public Boolean getFork() {
        return fork;
    }
    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository url(String url) {

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
    public PushEventWebHookPOSTRequestRepository forksUrl(String forksUrl) {

        this.forksUrl = forksUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("forks_url")
    @Valid
    public String getForksUrl() {
        return forksUrl;
    }
    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository keysUrl(String keysUrl) {

        this.keysUrl = keysUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("keys_url")
    @Valid
    public String getKeysUrl() {
        return keysUrl;
    }
    public void setKeysUrl(String keysUrl) {
        this.keysUrl = keysUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository collaboratorsUrl(String collaboratorsUrl) {

        this.collaboratorsUrl = collaboratorsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("collaborators_url")
    @Valid
    public String getCollaboratorsUrl() {
        return collaboratorsUrl;
    }
    public void setCollaboratorsUrl(String collaboratorsUrl) {
        this.collaboratorsUrl = collaboratorsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository teamsUrl(String teamsUrl) {

        this.teamsUrl = teamsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("teams_url")
    @Valid
    public String getTeamsUrl() {
        return teamsUrl;
    }
    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hooksUrl(String hooksUrl) {

        this.hooksUrl = hooksUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("hooks_url")
    @Valid
    public String getHooksUrl() {
        return hooksUrl;
    }
    public void setHooksUrl(String hooksUrl) {
        this.hooksUrl = hooksUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository issueEventsUrl(String issueEventsUrl) {

        this.issueEventsUrl = issueEventsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("issue_events_url")
    @Valid
    public String getIssueEventsUrl() {
        return issueEventsUrl;
    }
    public void setIssueEventsUrl(String issueEventsUrl) {
        this.issueEventsUrl = issueEventsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository eventsUrl(String eventsUrl) {

        this.eventsUrl = eventsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("events_url")
    @Valid
    public String getEventsUrl() {
        return eventsUrl;
    }
    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository assigneesUrl(String assigneesUrl) {

        this.assigneesUrl = assigneesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("assignees_url")
    @Valid
    public String getAssigneesUrl() {
        return assigneesUrl;
    }
    public void setAssigneesUrl(String assigneesUrl) {
        this.assigneesUrl = assigneesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository branchesUrl(String branchesUrl) {

        this.branchesUrl = branchesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("branches_url")
    @Valid
    public String getBranchesUrl() {
        return branchesUrl;
    }
    public void setBranchesUrl(String branchesUrl) {
        this.branchesUrl = branchesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository tagsUrl(String tagsUrl) {

        this.tagsUrl = tagsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("tags_url")
    @Valid
    public String getTagsUrl() {
        return tagsUrl;
    }
    public void setTagsUrl(String tagsUrl) {
        this.tagsUrl = tagsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository blobsUrl(String blobsUrl) {

        this.blobsUrl = blobsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("blobs_url")
    @Valid
    public String getBlobsUrl() {
        return blobsUrl;
    }
    public void setBlobsUrl(String blobsUrl) {
        this.blobsUrl = blobsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository gitTagsUrl(String gitTagsUrl) {

        this.gitTagsUrl = gitTagsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("git_tags_url")
    @Valid
    public String getGitTagsUrl() {
        return gitTagsUrl;
    }
    public void setGitTagsUrl(String gitTagsUrl) {
        this.gitTagsUrl = gitTagsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository gitRefsUrl(String gitRefsUrl) {

        this.gitRefsUrl = gitRefsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("git_refs_url")
    @Valid
    public String getGitRefsUrl() {
        return gitRefsUrl;
    }
    public void setGitRefsUrl(String gitRefsUrl) {
        this.gitRefsUrl = gitRefsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository treesUrl(String treesUrl) {

        this.treesUrl = treesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("trees_url")
    @Valid
    public String getTreesUrl() {
        return treesUrl;
    }
    public void setTreesUrl(String treesUrl) {
        this.treesUrl = treesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository statusesUrl(String statusesUrl) {

        this.statusesUrl = statusesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("statuses_url")
    @Valid
    public String getStatusesUrl() {
        return statusesUrl;
    }
    public void setStatusesUrl(String statusesUrl) {
        this.statusesUrl = statusesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository languagesUrl(String languagesUrl) {

        this.languagesUrl = languagesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("languages_url")
    @Valid
    public String getLanguagesUrl() {
        return languagesUrl;
    }
    public void setLanguagesUrl(String languagesUrl) {
        this.languagesUrl = languagesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository stargazersUrl(String stargazersUrl) {

        this.stargazersUrl = stargazersUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("stargazers_url")
    @Valid
    public String getStargazersUrl() {
        return stargazersUrl;
    }
    public void setStargazersUrl(String stargazersUrl) {
        this.stargazersUrl = stargazersUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository contributorsUrl(String contributorsUrl) {

        this.contributorsUrl = contributorsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("contributors_url")
    @Valid
    public String getContributorsUrl() {
        return contributorsUrl;
    }
    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository subscribersUrl(String subscribersUrl) {

        this.subscribersUrl = subscribersUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("subscribers_url")
    @Valid
    public String getSubscribersUrl() {
        return subscribersUrl;
    }
    public void setSubscribersUrl(String subscribersUrl) {
        this.subscribersUrl = subscribersUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository subscriptionUrl(String subscriptionUrl) {

        this.subscriptionUrl = subscriptionUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("subscription_url")
    @Valid
    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }
    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository commitsUrl(String commitsUrl) {

        this.commitsUrl = commitsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("commits_url")
    @Valid
    public String getCommitsUrl() {
        return commitsUrl;
    }
    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository gitCommitsUrl(String gitCommitsUrl) {

        this.gitCommitsUrl = gitCommitsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("git_commits_url")
    @Valid
    public String getGitCommitsUrl() {
        return gitCommitsUrl;
    }
    public void setGitCommitsUrl(String gitCommitsUrl) {
        this.gitCommitsUrl = gitCommitsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository commentsUrl(String commentsUrl) {

        this.commentsUrl = commentsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("comments_url")
    @Valid
    public String getCommentsUrl() {
        return commentsUrl;
    }
    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository issueCommentUrl(String issueCommentUrl) {

        this.issueCommentUrl = issueCommentUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("issue_comment_url")
    @Valid
    public String getIssueCommentUrl() {
        return issueCommentUrl;
    }
    public void setIssueCommentUrl(String issueCommentUrl) {
        this.issueCommentUrl = issueCommentUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository contentsUrl(String contentsUrl) {

        this.contentsUrl = contentsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("contents_url")
    @Valid
    public String getContentsUrl() {
        return contentsUrl;
    }
    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository compareUrl(String compareUrl) {

        this.compareUrl = compareUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("compare_url")
    @Valid
    public String getCompareUrl() {
        return compareUrl;
    }
    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository mergesUrl(String mergesUrl) {

        this.mergesUrl = mergesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("merges_url")
    @Valid
    public String getMergesUrl() {
        return mergesUrl;
    }
    public void setMergesUrl(String mergesUrl) {
        this.mergesUrl = mergesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository archiveUrl(String archiveUrl) {

        this.archiveUrl = archiveUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("archive_url")
    @Valid
    public String getArchiveUrl() {
        return archiveUrl;
    }
    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository downloadsUrl(String downloadsUrl) {

        this.downloadsUrl = downloadsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("downloads_url")
    @Valid
    public String getDownloadsUrl() {
        return downloadsUrl;
    }
    public void setDownloadsUrl(String downloadsUrl) {
        this.downloadsUrl = downloadsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository issuesUrl(String issuesUrl) {

        this.issuesUrl = issuesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("issues_url")
    @Valid
    public String getIssuesUrl() {
        return issuesUrl;
    }
    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository pullsUrl(String pullsUrl) {

        this.pullsUrl = pullsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("pulls_url")
    @Valid
    public String getPullsUrl() {
        return pullsUrl;
    }
    public void setPullsUrl(String pullsUrl) {
        this.pullsUrl = pullsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository milestonesUrl(String milestonesUrl) {

        this.milestonesUrl = milestonesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("milestones_url")
    @Valid
    public String getMilestonesUrl() {
        return milestonesUrl;
    }
    public void setMilestonesUrl(String milestonesUrl) {
        this.milestonesUrl = milestonesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository notificationsUrl(String notificationsUrl) {

        this.notificationsUrl = notificationsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("notifications_url")
    @Valid
    public String getNotificationsUrl() {
        return notificationsUrl;
    }
    public void setNotificationsUrl(String notificationsUrl) {
        this.notificationsUrl = notificationsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository labelsUrl(String labelsUrl) {

        this.labelsUrl = labelsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("labels_url")
    @Valid
    public String getLabelsUrl() {
        return labelsUrl;
    }
    public void setLabelsUrl(String labelsUrl) {
        this.labelsUrl = labelsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository releasesUrl(String releasesUrl) {

        this.releasesUrl = releasesUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("releases_url")
    @Valid
    public String getReleasesUrl() {
        return releasesUrl;
    }
    public void setReleasesUrl(String releasesUrl) {
        this.releasesUrl = releasesUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository deploymentsUrl(String deploymentsUrl) {

        this.deploymentsUrl = deploymentsUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("deployments_url")
    @Valid
    public String getDeploymentsUrl() {
        return deploymentsUrl;
    }
    public void setDeploymentsUrl(String deploymentsUrl) {
        this.deploymentsUrl = deploymentsUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository createdAt(Integer createdAt) {

        this.createdAt = createdAt;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("created_at")
    @Valid
    public Integer getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository updatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("updated_at")
    @Valid
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository pushedAt(Integer pushedAt) {

        this.pushedAt = pushedAt;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("pushed_at")
    @Valid
    public Integer getPushedAt() {
        return pushedAt;
    }
    public void setPushedAt(Integer pushedAt) {
        this.pushedAt = pushedAt;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository gitUrl(String gitUrl) {

        this.gitUrl = gitUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("git_url")
    @Valid
    public String getGitUrl() {
        return gitUrl;
    }
    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository sshUrl(String sshUrl) {

        this.sshUrl = sshUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("ssh_url")
    @Valid
    public String getSshUrl() {
        return sshUrl;
    }
    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository cloneUrl(String cloneUrl) {

        this.cloneUrl = cloneUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("clone_url")
    @Valid
    public String getCloneUrl() {
        return cloneUrl;
    }
    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository svnUrl(String svnUrl) {

        this.svnUrl = svnUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("svn_url")
    @Valid
    public String getSvnUrl() {
        return svnUrl;
    }
    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository homepage(String homepage) {

        this.homepage = homepage;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("homepage")
    @Valid
    public String getHomepage() {
        return homepage;
    }
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository size(Integer size) {

        this.size = size;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("size")
    @Valid
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository stargazersCount(Integer stargazersCount) {

        this.stargazersCount = stargazersCount;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("stargazers_count")
    @Valid
    public Integer getStargazersCount() {
        return stargazersCount;
    }
    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository watchersCount(Integer watchersCount) {

        this.watchersCount = watchersCount;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("watchers_count")
    @Valid
    public Integer getWatchersCount() {
        return watchersCount;
    }
    public void setWatchersCount(Integer watchersCount) {
        this.watchersCount = watchersCount;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository language(String language) {

        this.language = language;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("language")
    @Valid
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hasIssues(Boolean hasIssues) {

        this.hasIssues = hasIssues;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("has_issues")
    @Valid
    public Boolean getHasIssues() {
        return hasIssues;
    }
    public void setHasIssues(Boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hasProjects(Boolean hasProjects) {

        this.hasProjects = hasProjects;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("has_projects")
    @Valid
    public Boolean getHasProjects() {
        return hasProjects;
    }
    public void setHasProjects(Boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hasDownloads(Boolean hasDownloads) {

        this.hasDownloads = hasDownloads;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("has_downloads")
    @Valid
    public Boolean getHasDownloads() {
        return hasDownloads;
    }
    public void setHasDownloads(Boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hasWiki(Boolean hasWiki) {

        this.hasWiki = hasWiki;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("has_wiki")
    @Valid
    public Boolean getHasWiki() {
        return hasWiki;
    }
    public void setHasWiki(Boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository hasPages(Boolean hasPages) {

        this.hasPages = hasPages;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("has_pages")
    @Valid
    public Boolean getHasPages() {
        return hasPages;
    }
    public void setHasPages(Boolean hasPages) {
        this.hasPages = hasPages;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository forksCount(Integer forksCount) {

        this.forksCount = forksCount;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("forks_count")
    @Valid
    public Integer getForksCount() {
        return forksCount;
    }
    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository mirrorUrl(String mirrorUrl) {

        this.mirrorUrl = mirrorUrl;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("mirror_url")
    @Valid
    public String getMirrorUrl() {
        return mirrorUrl;
    }
    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository archived(Boolean archived) {

        this.archived = archived;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("archived")
    @Valid
    public Boolean getArchived() {
        return archived;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository disabled(Boolean disabled) {

        this.disabled = disabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("disabled")
    @Valid
    public Boolean getDisabled() {
        return disabled;
    }
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository openIssuesCount(Integer openIssuesCount) {

        this.openIssuesCount = openIssuesCount;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("open_issues_count")
    @Valid
    public Integer getOpenIssuesCount() {
        return openIssuesCount;
    }
    public void setOpenIssuesCount(Integer openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository license(String license) {

        this.license = license;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("license")
    @Valid
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository forks(Integer forks) {

        this.forks = forks;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("forks")
    @Valid
    public Integer getForks() {
        return forks;
    }
    public void setForks(Integer forks) {
        this.forks = forks;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository openIssues(Integer openIssues) {

        this.openIssues = openIssues;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("open_issues")
    @Valid
    public Integer getOpenIssues() {
        return openIssues;
    }
    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository watchers(Integer watchers) {

        this.watchers = watchers;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("watchers")
    @Valid
    public Integer getWatchers() {
        return watchers;
    }
    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository defaultBranch(String defaultBranch) {

        this.defaultBranch = defaultBranch;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("default_branch")
    @Valid
    public String getDefaultBranch() {
        return defaultBranch;
    }
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository stargazers(Integer stargazers) {

        this.stargazers = stargazers;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("stargazers")
    @Valid
    public Integer getStargazers() {
        return stargazers;
    }
    public void setStargazers(Integer stargazers) {
        this.stargazers = stargazers;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestRepository masterBranch(String masterBranch) {

        this.masterBranch = masterBranch;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("master_branch")
    @Valid
    public String getMasterBranch() {
        return masterBranch;
    }
    public void setMasterBranch(String masterBranch) {
        this.masterBranch = masterBranch;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushEventWebHookPOSTRequestRepository pushEventWebHookPOSTRequestRepository = (PushEventWebHookPOSTRequestRepository) o;
        return Objects.equals(this.id, pushEventWebHookPOSTRequestRepository.id) &&
            Objects.equals(this.nodeId, pushEventWebHookPOSTRequestRepository.nodeId) &&
            Objects.equals(this.name, pushEventWebHookPOSTRequestRepository.name) &&
            Objects.equals(this.fullName, pushEventWebHookPOSTRequestRepository.fullName) &&
            Objects.equals(this._private, pushEventWebHookPOSTRequestRepository._private) &&
            Objects.equals(this.owner, pushEventWebHookPOSTRequestRepository.owner) &&
            Objects.equals(this.htmlUrl, pushEventWebHookPOSTRequestRepository.htmlUrl) &&
            Objects.equals(this.description, pushEventWebHookPOSTRequestRepository.description) &&
            Objects.equals(this.fork, pushEventWebHookPOSTRequestRepository.fork) &&
            Objects.equals(this.url, pushEventWebHookPOSTRequestRepository.url) &&
            Objects.equals(this.forksUrl, pushEventWebHookPOSTRequestRepository.forksUrl) &&
            Objects.equals(this.keysUrl, pushEventWebHookPOSTRequestRepository.keysUrl) &&
            Objects.equals(this.collaboratorsUrl, pushEventWebHookPOSTRequestRepository.collaboratorsUrl) &&
            Objects.equals(this.teamsUrl, pushEventWebHookPOSTRequestRepository.teamsUrl) &&
            Objects.equals(this.hooksUrl, pushEventWebHookPOSTRequestRepository.hooksUrl) &&
            Objects.equals(this.issueEventsUrl, pushEventWebHookPOSTRequestRepository.issueEventsUrl) &&
            Objects.equals(this.eventsUrl, pushEventWebHookPOSTRequestRepository.eventsUrl) &&
            Objects.equals(this.assigneesUrl, pushEventWebHookPOSTRequestRepository.assigneesUrl) &&
            Objects.equals(this.branchesUrl, pushEventWebHookPOSTRequestRepository.branchesUrl) &&
            Objects.equals(this.tagsUrl, pushEventWebHookPOSTRequestRepository.tagsUrl) &&
            Objects.equals(this.blobsUrl, pushEventWebHookPOSTRequestRepository.blobsUrl) &&
            Objects.equals(this.gitTagsUrl, pushEventWebHookPOSTRequestRepository.gitTagsUrl) &&
            Objects.equals(this.gitRefsUrl, pushEventWebHookPOSTRequestRepository.gitRefsUrl) &&
            Objects.equals(this.treesUrl, pushEventWebHookPOSTRequestRepository.treesUrl) &&
            Objects.equals(this.statusesUrl, pushEventWebHookPOSTRequestRepository.statusesUrl) &&
            Objects.equals(this.languagesUrl, pushEventWebHookPOSTRequestRepository.languagesUrl) &&
            Objects.equals(this.stargazersUrl, pushEventWebHookPOSTRequestRepository.stargazersUrl) &&
            Objects.equals(this.contributorsUrl, pushEventWebHookPOSTRequestRepository.contributorsUrl) &&
            Objects.equals(this.subscribersUrl, pushEventWebHookPOSTRequestRepository.subscribersUrl) &&
            Objects.equals(this.subscriptionUrl, pushEventWebHookPOSTRequestRepository.subscriptionUrl) &&
            Objects.equals(this.commitsUrl, pushEventWebHookPOSTRequestRepository.commitsUrl) &&
            Objects.equals(this.gitCommitsUrl, pushEventWebHookPOSTRequestRepository.gitCommitsUrl) &&
            Objects.equals(this.commentsUrl, pushEventWebHookPOSTRequestRepository.commentsUrl) &&
            Objects.equals(this.issueCommentUrl, pushEventWebHookPOSTRequestRepository.issueCommentUrl) &&
            Objects.equals(this.contentsUrl, pushEventWebHookPOSTRequestRepository.contentsUrl) &&
            Objects.equals(this.compareUrl, pushEventWebHookPOSTRequestRepository.compareUrl) &&
            Objects.equals(this.mergesUrl, pushEventWebHookPOSTRequestRepository.mergesUrl) &&
            Objects.equals(this.archiveUrl, pushEventWebHookPOSTRequestRepository.archiveUrl) &&
            Objects.equals(this.downloadsUrl, pushEventWebHookPOSTRequestRepository.downloadsUrl) &&
            Objects.equals(this.issuesUrl, pushEventWebHookPOSTRequestRepository.issuesUrl) &&
            Objects.equals(this.pullsUrl, pushEventWebHookPOSTRequestRepository.pullsUrl) &&
            Objects.equals(this.milestonesUrl, pushEventWebHookPOSTRequestRepository.milestonesUrl) &&
            Objects.equals(this.notificationsUrl, pushEventWebHookPOSTRequestRepository.notificationsUrl) &&
            Objects.equals(this.labelsUrl, pushEventWebHookPOSTRequestRepository.labelsUrl) &&
            Objects.equals(this.releasesUrl, pushEventWebHookPOSTRequestRepository.releasesUrl) &&
            Objects.equals(this.deploymentsUrl, pushEventWebHookPOSTRequestRepository.deploymentsUrl) &&
            Objects.equals(this.createdAt, pushEventWebHookPOSTRequestRepository.createdAt) &&
            Objects.equals(this.updatedAt, pushEventWebHookPOSTRequestRepository.updatedAt) &&
            Objects.equals(this.pushedAt, pushEventWebHookPOSTRequestRepository.pushedAt) &&
            Objects.equals(this.gitUrl, pushEventWebHookPOSTRequestRepository.gitUrl) &&
            Objects.equals(this.sshUrl, pushEventWebHookPOSTRequestRepository.sshUrl) &&
            Objects.equals(this.cloneUrl, pushEventWebHookPOSTRequestRepository.cloneUrl) &&
            Objects.equals(this.svnUrl, pushEventWebHookPOSTRequestRepository.svnUrl) &&
            Objects.equals(this.homepage, pushEventWebHookPOSTRequestRepository.homepage) &&
            Objects.equals(this.size, pushEventWebHookPOSTRequestRepository.size) &&
            Objects.equals(this.stargazersCount, pushEventWebHookPOSTRequestRepository.stargazersCount) &&
            Objects.equals(this.watchersCount, pushEventWebHookPOSTRequestRepository.watchersCount) &&
            Objects.equals(this.language, pushEventWebHookPOSTRequestRepository.language) &&
            Objects.equals(this.hasIssues, pushEventWebHookPOSTRequestRepository.hasIssues) &&
            Objects.equals(this.hasProjects, pushEventWebHookPOSTRequestRepository.hasProjects) &&
            Objects.equals(this.hasDownloads, pushEventWebHookPOSTRequestRepository.hasDownloads) &&
            Objects.equals(this.hasWiki, pushEventWebHookPOSTRequestRepository.hasWiki) &&
            Objects.equals(this.hasPages, pushEventWebHookPOSTRequestRepository.hasPages) &&
            Objects.equals(this.forksCount, pushEventWebHookPOSTRequestRepository.forksCount) &&
            Objects.equals(this.mirrorUrl, pushEventWebHookPOSTRequestRepository.mirrorUrl) &&
            Objects.equals(this.archived, pushEventWebHookPOSTRequestRepository.archived) &&
            Objects.equals(this.disabled, pushEventWebHookPOSTRequestRepository.disabled) &&
            Objects.equals(this.openIssuesCount, pushEventWebHookPOSTRequestRepository.openIssuesCount) &&
            Objects.equals(this.license, pushEventWebHookPOSTRequestRepository.license) &&
            Objects.equals(this.forks, pushEventWebHookPOSTRequestRepository.forks) &&
            Objects.equals(this.openIssues, pushEventWebHookPOSTRequestRepository.openIssues) &&
            Objects.equals(this.watchers, pushEventWebHookPOSTRequestRepository.watchers) &&
            Objects.equals(this.defaultBranch, pushEventWebHookPOSTRequestRepository.defaultBranch) &&
            Objects.equals(this.stargazers, pushEventWebHookPOSTRequestRepository.stargazers) &&
            Objects.equals(this.masterBranch, pushEventWebHookPOSTRequestRepository.masterBranch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nodeId, name, fullName, _private, owner, htmlUrl, description, fork, url, forksUrl, keysUrl, collaboratorsUrl, teamsUrl, hooksUrl, issueEventsUrl, eventsUrl, assigneesUrl, branchesUrl, tagsUrl, blobsUrl, gitTagsUrl, gitRefsUrl, treesUrl, statusesUrl, languagesUrl, stargazersUrl, contributorsUrl, subscribersUrl, subscriptionUrl, commitsUrl, gitCommitsUrl, commentsUrl, issueCommentUrl, contentsUrl, compareUrl, mergesUrl, archiveUrl, downloadsUrl, issuesUrl, pullsUrl, milestonesUrl, notificationsUrl, labelsUrl, releasesUrl, deploymentsUrl, createdAt, updatedAt, pushedAt, gitUrl, sshUrl, cloneUrl, svnUrl, homepage, size, stargazersCount, watchersCount, language, hasIssues, hasProjects, hasDownloads, hasWiki, hasPages, forksCount, mirrorUrl, archived, disabled, openIssuesCount, license, forks, openIssues, watchers, defaultBranch, stargazers, masterBranch);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushEventWebHookPOSTRequestRepository {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
        sb.append("    _private: ").append(toIndentedString(_private)).append("\n");
        sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
        sb.append("    htmlUrl: ").append(toIndentedString(htmlUrl)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    fork: ").append(toIndentedString(fork)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    forksUrl: ").append(toIndentedString(forksUrl)).append("\n");
        sb.append("    keysUrl: ").append(toIndentedString(keysUrl)).append("\n");
        sb.append("    collaboratorsUrl: ").append(toIndentedString(collaboratorsUrl)).append("\n");
        sb.append("    teamsUrl: ").append(toIndentedString(teamsUrl)).append("\n");
        sb.append("    hooksUrl: ").append(toIndentedString(hooksUrl)).append("\n");
        sb.append("    issueEventsUrl: ").append(toIndentedString(issueEventsUrl)).append("\n");
        sb.append("    eventsUrl: ").append(toIndentedString(eventsUrl)).append("\n");
        sb.append("    assigneesUrl: ").append(toIndentedString(assigneesUrl)).append("\n");
        sb.append("    branchesUrl: ").append(toIndentedString(branchesUrl)).append("\n");
        sb.append("    tagsUrl: ").append(toIndentedString(tagsUrl)).append("\n");
        sb.append("    blobsUrl: ").append(toIndentedString(blobsUrl)).append("\n");
        sb.append("    gitTagsUrl: ").append(toIndentedString(gitTagsUrl)).append("\n");
        sb.append("    gitRefsUrl: ").append(toIndentedString(gitRefsUrl)).append("\n");
        sb.append("    treesUrl: ").append(toIndentedString(treesUrl)).append("\n");
        sb.append("    statusesUrl: ").append(toIndentedString(statusesUrl)).append("\n");
        sb.append("    languagesUrl: ").append(toIndentedString(languagesUrl)).append("\n");
        sb.append("    stargazersUrl: ").append(toIndentedString(stargazersUrl)).append("\n");
        sb.append("    contributorsUrl: ").append(toIndentedString(contributorsUrl)).append("\n");
        sb.append("    subscribersUrl: ").append(toIndentedString(subscribersUrl)).append("\n");
        sb.append("    subscriptionUrl: ").append(toIndentedString(subscriptionUrl)).append("\n");
        sb.append("    commitsUrl: ").append(toIndentedString(commitsUrl)).append("\n");
        sb.append("    gitCommitsUrl: ").append(toIndentedString(gitCommitsUrl)).append("\n");
        sb.append("    commentsUrl: ").append(toIndentedString(commentsUrl)).append("\n");
        sb.append("    issueCommentUrl: ").append(toIndentedString(issueCommentUrl)).append("\n");
        sb.append("    contentsUrl: ").append(toIndentedString(contentsUrl)).append("\n");
        sb.append("    compareUrl: ").append(toIndentedString(compareUrl)).append("\n");
        sb.append("    mergesUrl: ").append(toIndentedString(mergesUrl)).append("\n");
        sb.append("    archiveUrl: ").append(toIndentedString(archiveUrl)).append("\n");
        sb.append("    downloadsUrl: ").append(toIndentedString(downloadsUrl)).append("\n");
        sb.append("    issuesUrl: ").append(toIndentedString(issuesUrl)).append("\n");
        sb.append("    pullsUrl: ").append(toIndentedString(pullsUrl)).append("\n");
        sb.append("    milestonesUrl: ").append(toIndentedString(milestonesUrl)).append("\n");
        sb.append("    notificationsUrl: ").append(toIndentedString(notificationsUrl)).append("\n");
        sb.append("    labelsUrl: ").append(toIndentedString(labelsUrl)).append("\n");
        sb.append("    releasesUrl: ").append(toIndentedString(releasesUrl)).append("\n");
        sb.append("    deploymentsUrl: ").append(toIndentedString(deploymentsUrl)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    pushedAt: ").append(toIndentedString(pushedAt)).append("\n");
        sb.append("    gitUrl: ").append(toIndentedString(gitUrl)).append("\n");
        sb.append("    sshUrl: ").append(toIndentedString(sshUrl)).append("\n");
        sb.append("    cloneUrl: ").append(toIndentedString(cloneUrl)).append("\n");
        sb.append("    svnUrl: ").append(toIndentedString(svnUrl)).append("\n");
        sb.append("    homepage: ").append(toIndentedString(homepage)).append("\n");
        sb.append("    size: ").append(toIndentedString(size)).append("\n");
        sb.append("    stargazersCount: ").append(toIndentedString(stargazersCount)).append("\n");
        sb.append("    watchersCount: ").append(toIndentedString(watchersCount)).append("\n");
        sb.append("    language: ").append(toIndentedString(language)).append("\n");
        sb.append("    hasIssues: ").append(toIndentedString(hasIssues)).append("\n");
        sb.append("    hasProjects: ").append(toIndentedString(hasProjects)).append("\n");
        sb.append("    hasDownloads: ").append(toIndentedString(hasDownloads)).append("\n");
        sb.append("    hasWiki: ").append(toIndentedString(hasWiki)).append("\n");
        sb.append("    hasPages: ").append(toIndentedString(hasPages)).append("\n");
        sb.append("    forksCount: ").append(toIndentedString(forksCount)).append("\n");
        sb.append("    mirrorUrl: ").append(toIndentedString(mirrorUrl)).append("\n");
        sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
        sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
        sb.append("    openIssuesCount: ").append(toIndentedString(openIssuesCount)).append("\n");
        sb.append("    license: ").append(toIndentedString(license)).append("\n");
        sb.append("    forks: ").append(toIndentedString(forks)).append("\n");
        sb.append("    openIssues: ").append(toIndentedString(openIssues)).append("\n");
        sb.append("    watchers: ").append(toIndentedString(watchers)).append("\n");
        sb.append("    defaultBranch: ").append(toIndentedString(defaultBranch)).append("\n");
        sb.append("    stargazers: ").append(toIndentedString(stargazers)).append("\n");
        sb.append("    masterBranch: ").append(toIndentedString(masterBranch)).append("\n");
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


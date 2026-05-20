/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * One node in the controlled In-Flow Extension context tree.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "One node in the controlled In-Flow Extension context tree.")
public class InFlowExtensionContextTreeNode  {
  
    private String key;
    private String title;
    private String path;
    private String dataType;

@XmlType(name="NodeTypeEnum")
@XmlEnum(String.class)
public enum NodeTypeEnum {

    @XmlEnumValue("OBJECT") OBJECT(String.valueOf("OBJECT")), @XmlEnumValue("LEAF") LEAF(String.valueOf("LEAF")), @XmlEnumValue("MAP") MAP(String.valueOf("MAP")), @XmlEnumValue("COMPLEX_MAP") COMPLEX_MAP(String.valueOf("COMPLEX_MAP"));


    private String value;

    NodeTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static NodeTypeEnum fromValue(String value) {
        for (NodeTypeEnum b : NodeTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private NodeTypeEnum nodeType;

@XmlType(name="AllowedOperationsEnum")
@XmlEnum(String.class)
public enum AllowedOperationsEnum {

    @XmlEnumValue("EXPOSE") EXPOSE(String.valueOf("EXPOSE")), @XmlEnumValue("MODIFY") MODIFY(String.valueOf("MODIFY"));


    private String value;

    AllowedOperationsEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static AllowedOperationsEnum fromValue(String value) {
        for (AllowedOperationsEnum b : AllowedOperationsEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private List<AllowedOperationsEnum> allowedOperations = null;

    private Boolean readOnly;
    private Boolean replaceable;
    private Boolean dynamicEntryAllowed;
    private String dynamicEntryType;
    private List<InFlowExtensionContextTreeNode> children = null;


    /**
    **/
    public InFlowExtensionContextTreeNode key(String key) {

        this.key = key;
        return this;
    }
    
    @ApiModelProperty(example = "userId", value = "")
    @JsonProperty("key")
    @Valid
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode title(String title) {

        this.title = title;
        return this;
    }
    
    @ApiModelProperty(example = "User ID", value = "")
    @JsonProperty("title")
    @Valid
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode path(String path) {

        this.path = path;
        return this;
    }
    
    @ApiModelProperty(example = "/user/userId", value = "")
    @JsonProperty("path")
    @Valid
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode dataType(String dataType) {

        this.dataType = dataType;
        return this;
    }
    
    @ApiModelProperty(example = "String", value = "")
    @JsonProperty("dataType")
    @Valid
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
    * Tree node type.
    **/
    public InFlowExtensionContextTreeNode nodeType(NodeTypeEnum nodeType) {

        this.nodeType = nodeType;
        return this;
    }
    
    @ApiModelProperty(example = "LEAF", value = "Tree node type.")
    @JsonProperty("nodeType")
    @Valid
    public NodeTypeEnum getNodeType() {
        return nodeType;
    }
    public void setNodeType(NodeTypeEnum nodeType) {
        this.nodeType = nodeType;
    }

    /**
    * Operations the admin may configure on this node.
    **/
    public InFlowExtensionContextTreeNode allowedOperations(List<AllowedOperationsEnum> allowedOperations) {

        this.allowedOperations = allowedOperations;
        return this;
    }
    
    @ApiModelProperty(value = "Operations the admin may configure on this node.")
    @JsonProperty("allowedOperations")
    @Valid
    public List<AllowedOperationsEnum> getAllowedOperations() {
        return allowedOperations;
    }
    public void setAllowedOperations(List<AllowedOperationsEnum> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }

    public InFlowExtensionContextTreeNode addAllowedOperationsItem(AllowedOperationsEnum allowedOperationsItem) {
        if (this.allowedOperations == null) {
            this.allowedOperations = new ArrayList<AllowedOperationsEnum>();
        }
        this.allowedOperations.add(allowedOperationsItem);
        return this;
    }

        /**
    **/
    public InFlowExtensionContextTreeNode readOnly(Boolean readOnly) {

        this.readOnly = readOnly;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("readOnly")
    @Valid
    public Boolean getReadOnly() {
        return readOnly;
    }
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode replaceable(Boolean replaceable) {

        this.replaceable = replaceable;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("replaceable")
    @Valid
    public Boolean getReplaceable() {
        return replaceable;
    }
    public void setReplaceable(Boolean replaceable) {
        this.replaceable = replaceable;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode dynamicEntryAllowed(Boolean dynamicEntryAllowed) {

        this.dynamicEntryAllowed = dynamicEntryAllowed;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("dynamicEntryAllowed")
    @Valid
    public Boolean getDynamicEntryAllowed() {
        return dynamicEntryAllowed;
    }
    public void setDynamicEntryAllowed(Boolean dynamicEntryAllowed) {
        this.dynamicEntryAllowed = dynamicEntryAllowed;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode dynamicEntryType(String dynamicEntryType) {

        this.dynamicEntryType = dynamicEntryType;
        return this;
    }
    
    @ApiModelProperty(example = "String", value = "")
    @JsonProperty("dynamicEntryType")
    @Valid
    public String getDynamicEntryType() {
        return dynamicEntryType;
    }
    public void setDynamicEntryType(String dynamicEntryType) {
        this.dynamicEntryType = dynamicEntryType;
    }

    /**
    **/
    public InFlowExtensionContextTreeNode children(List<InFlowExtensionContextTreeNode> children) {

        this.children = children;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("children")
    @Valid
    public List<InFlowExtensionContextTreeNode> getChildren() {
        return children;
    }
    public void setChildren(List<InFlowExtensionContextTreeNode> children) {
        this.children = children;
    }

    public InFlowExtensionContextTreeNode addChildrenItem(InFlowExtensionContextTreeNode childrenItem) {
        if (this.children == null) {
            this.children = new ArrayList<InFlowExtensionContextTreeNode>();
        }
        this.children.add(childrenItem);
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
        InFlowExtensionContextTreeNode inFlowExtensionContextTreeNode = (InFlowExtensionContextTreeNode) o;
        return Objects.equals(this.key, inFlowExtensionContextTreeNode.key) &&
            Objects.equals(this.title, inFlowExtensionContextTreeNode.title) &&
            Objects.equals(this.path, inFlowExtensionContextTreeNode.path) &&
            Objects.equals(this.dataType, inFlowExtensionContextTreeNode.dataType) &&
            Objects.equals(this.nodeType, inFlowExtensionContextTreeNode.nodeType) &&
            Objects.equals(this.allowedOperations, inFlowExtensionContextTreeNode.allowedOperations) &&
            Objects.equals(this.readOnly, inFlowExtensionContextTreeNode.readOnly) &&
            Objects.equals(this.replaceable, inFlowExtensionContextTreeNode.replaceable) &&
            Objects.equals(this.dynamicEntryAllowed, inFlowExtensionContextTreeNode.dynamicEntryAllowed) &&
            Objects.equals(this.dynamicEntryType, inFlowExtensionContextTreeNode.dynamicEntryType) &&
            Objects.equals(this.children, inFlowExtensionContextTreeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, title, path, dataType, nodeType, allowedOperations, readOnly, replaceable, dynamicEntryAllowed, dynamicEntryType, children);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionContextTreeNode {\n");
        
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
        sb.append("    nodeType: ").append(toIndentedString(nodeType)).append("\n");
        sb.append("    allowedOperations: ").append(toIndentedString(allowedOperations)).append("\n");
        sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
        sb.append("    replaceable: ").append(toIndentedString(replaceable)).append("\n");
        sb.append("    dynamicEntryAllowed: ").append(toIndentedString(dynamicEntryAllowed)).append("\n");
        sb.append("    dynamicEntryType: ").append(toIndentedString(dynamicEntryType)).append("\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
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


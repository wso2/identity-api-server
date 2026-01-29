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

package org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OIDC Scope configuration model for file serialization (XML, YAML, JSON export/import).
 */
@XmlRootElement(name = "Scope")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScopeConfiguration {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "displayName")
    private String displayName;

    @XmlElement(name = "description")
    private String description;

    @XmlElementWrapper(name = "claims")
    @XmlElement(name = "claim")
    private List<String> claims = new ArrayList<>();

    public ScopeConfiguration() {
    }

    /**
     * Constructor to create ScopeConfiguration from Scope model.
     *
     * @param scope Scope object.
     */
    public ScopeConfiguration(Scope scope) {
        if (scope != null) {
            this.name = scope.getName();
            this.displayName = scope.getDisplayName();
            this.description = scope.getDescription();
            this.claims = scope.getClaims() != null ? new ArrayList<>(scope.getClaims()) : new ArrayList<>();
        }
    }

    /**
     * Convert ScopeConfiguration to Scope model.
     *
     * @return Scope object.
     */
    public Scope toScope() {
        Scope scope = new Scope();
        scope.setName(this.name);
        scope.setDisplayName(this.displayName);
        scope.setDescription(this.description);
        scope.setClaims(this.claims != null ? new ArrayList<>(this.claims) : new ArrayList<>());
        return scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getClaims() {
        return claims;
    }

    public void setClaims(List<String> claims) {
        this.claims = claims;
    }
}

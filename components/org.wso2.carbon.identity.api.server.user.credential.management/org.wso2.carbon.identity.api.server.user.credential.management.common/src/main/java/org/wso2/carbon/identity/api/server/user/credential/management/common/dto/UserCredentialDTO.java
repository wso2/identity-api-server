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

package org.wso2.carbon.identity.api.server.user.credential.management.common.dto;

/**
 * Credential Data Transfer Object.
 */
public class UserCredentialDTO {

    private final String credentialId;
    private final String displayName;
    private final String type;

    private UserCredentialDTO(Builder builder) {

        this.credentialId = builder.credentialId;
        this.displayName = builder.displayName;
        this.type = builder.type;
    }

    public String getCredentialId() {

        return credentialId;
    }

    public String getDisplayName() {

        return displayName;
    }

    public String getType() {

        return type;
    }

    /**
     * Builder class for UserCredentialDTO.
     */
    public static class Builder {

        private String credentialId;
        private String displayName;
        private String type;

        public Builder credentialId(String credentialId) {

            this.credentialId = credentialId;
            return this;
        }

        public Builder displayName(String displayName) {

            this.displayName = displayName;
            return this;
        }

        public Builder type(String type) {

            this.type = type;
            return this;
        }

        public UserCredentialDTO build() {

            return new UserCredentialDTO(this);
        }
    }
}

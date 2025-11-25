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

package org.wso2.carbon.identity.api.server.credential.management.common.dto;

/**
 * Data Transfer Object for credential deletion request.
 */
public class CredentialDeletionRequestDTO {

    private String entityId;
    private String credentialId;
    private String type;

    private CredentialDeletionRequestDTO(String entityId, String credentialId, String type) {

        this.entityId = entityId;
        this.credentialId = credentialId;
        this.type = type;
    }

    public String getEntityId() {

        return entityId;
    }

    public String getCredentialId() {

        return credentialId;
    }

    public String getType() {

        return type;
    }

    /**
     * Builder class for CredentialDeletionRequestDTO.
     */
    public static class Builder {

        private String entityId;
        private String credentialId;
        private String type;

        public Builder entityId(String entityId) {

            this.entityId = entityId;
            return this;
        }

        public Builder credentialId(String credentialId) {

            this.credentialId = credentialId;
            return this;
        }

        public Builder type(String type) {

            this.type = type;
            return this;
        }

        public CredentialDeletionRequestDTO build() {

            return new CredentialDeletionRequestDTO(entityId, credentialId, type);
        }
    }
}

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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.model;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * External claim configuration model.
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExternalClaimConfiguration")
public class ExternalClaimConfiguration extends ExternalClaimResDTO implements ClaimConfiguration {

    public ExternalClaimConfiguration() {
        super();
    }

    public ExternalClaimConfiguration(ExternalClaimResDTO externalClaimResDTO) {

        this.setId(externalClaimResDTO.getId());
        this.setClaimURI(externalClaimResDTO.getClaimURI());
        this.setClaimDialectURI(externalClaimResDTO.getClaimDialectURI());
        this.setMappedLocalClaimURI(externalClaimResDTO.getMappedLocalClaimURI());

    }
}

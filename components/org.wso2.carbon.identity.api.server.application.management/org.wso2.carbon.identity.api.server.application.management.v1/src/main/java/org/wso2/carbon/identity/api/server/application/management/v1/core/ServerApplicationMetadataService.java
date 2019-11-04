/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.application.mgt.AbstractInboundAuthenticatorConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Calls internal osgi services to get required application metadata.
 */
public class ServerApplicationMetadataService {

    public List<AuthProtocolMetadata> getInboundProtocols(Boolean customOnly) {

        List<AuthProtocolMetadata> authProtocolMetadataList = new ArrayList<>();

        // Add custom inbound protocols
        Map<String, AbstractInboundAuthenticatorConfig> allCustomAuthenticators =
                ApplicationManagementServiceHolder.getApplicationManagementService().getAllInboundAuthenticatorConfig();

        for (Map.Entry<String, AbstractInboundAuthenticatorConfig> entry : allCustomAuthenticators
                .entrySet()) {
            AuthProtocolMetadata protocol = new AuthProtocolMetadata();
            protocol.setName(entry.getValue().getName());
            protocol.setDisplayName(entry.getValue().getFriendlyName());
            authProtocolMetadataList.add(protocol);
        }

        if (customOnly == null || !customOnly) {
            // Add default inbound protocols
            AuthProtocolMetadata samlProtocol = new AuthProtocolMetadata();
            samlProtocol.setName("saml");
            samlProtocol.setDisplayName("SAML2 Web SSO Configuration");
            authProtocolMetadataList.add(samlProtocol);

            AuthProtocolMetadata oauthProtocol = new AuthProtocolMetadata();
            oauthProtocol.setName("oidc");
            oauthProtocol.setDisplayName("OAuth/OpenID Connect Configuration");
            authProtocolMetadataList.add(oauthProtocol);

            AuthProtocolMetadata openIDProtocol = new AuthProtocolMetadata();
            openIDProtocol.setName("open-id");
            openIDProtocol.setDisplayName("OpenID Configuration");
            authProtocolMetadataList.add(openIDProtocol);

            AuthProtocolMetadata wsFederationProtocol = new AuthProtocolMetadata();
            wsFederationProtocol.setName("ws-federation");
            wsFederationProtocol.setDisplayName("WS-Federation (Passive) Configuration");
            authProtocolMetadataList.add(wsFederationProtocol);

            AuthProtocolMetadata wsTrustProtocol = new AuthProtocolMetadata();
            wsTrustProtocol.setName("ws-trust");
            wsTrustProtocol.setDisplayName("WS-Trust Security Token Service Configuration");
            authProtocolMetadataList.add(wsTrustProtocol);

            AuthProtocolMetadata kerberosProtocol = new AuthProtocolMetadata();
            kerberosProtocol.setName("kerberos");
            kerberosProtocol.setDisplayName("Kerberos KDC");
            authProtocolMetadataList.add(kerberosProtocol);
        }

        return authProtocolMetadataList;
    }

    public Response getCustomProtocolMetadata(String inboundProtocolId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public Response getOIDCMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public Response getSAMLMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public Response getWSTrustMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}

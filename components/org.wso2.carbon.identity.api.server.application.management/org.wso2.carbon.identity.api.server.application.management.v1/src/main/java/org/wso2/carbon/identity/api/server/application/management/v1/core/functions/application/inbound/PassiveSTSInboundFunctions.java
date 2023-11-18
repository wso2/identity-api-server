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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.arrayToStream;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL_LOGOUT;

/**
 * Helper functions for Passive STS inbound management.
 */
public class PassiveSTSInboundFunctions {

    private PassiveSTSInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putPassiveSTSInbound(ServiceProvider application,
                                                                          PassiveStsConfiguration passiveSTSConfig) {

        String currentRealm = InboundFunctions.getInboundAuthKey(application, StandardInboundProtocols.PASSIVE_STS);
        if (currentRealm != null && isRealmValueChanged(passiveSTSConfig, currentRealm)) {
            // We do not allow the inbound unique key to be changed during an update.
            throw buildBadRequestError("Invalid realm value provided for update.");
        }

        return createPassiveSTSInboundConfig(passiveSTSConfig);
    }

    private static boolean isRealmValueChanged(PassiveStsConfiguration passiveSTSConfig, String currentRealm) {

        return !StringUtils.equals(currentRealm, passiveSTSConfig.getRealm());
    }

    public static InboundAuthenticationRequestConfig createPassiveSTSInboundConfig(PassiveStsConfiguration config) {

        InboundAuthenticationRequestConfig passiveStsInbound = new InboundAuthenticationRequestConfig();
        passiveStsInbound.setInboundAuthType(StandardInboundProtocols.PASSIVE_STS);
        passiveStsInbound.setInboundAuthKey(config.getRealm());

        Property passiveStsReplyUrl = new Property();
        passiveStsReplyUrl.setName(IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL);
        passiveStsReplyUrl.setValue(config.getReplyTo());

        if (StringUtils.isNotBlank(config.getReplyToLogout())) {
            Property passiveStsReplyUrlLogout = new Property();
            passiveStsReplyUrlLogout.setName(PASSIVE_STS_REPLY_URL_LOGOUT);
            passiveStsReplyUrlLogout.setValue(config.getReplyToLogout());
            passiveStsInbound.setProperties(new Property[]{passiveStsReplyUrl, passiveStsReplyUrlLogout});
        } else {
            passiveStsInbound.setProperties(new Property[]{passiveStsReplyUrl});
        }

        return passiveStsInbound;
    }

    public static PassiveStsConfiguration getPassiveSTSConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String replyTo = arrayToStream(inboundAuth.getProperties())
                .filter(property -> StringUtils.equals(property.getName(), PASSIVE_STS_REPLY_URL))
                .findAny()
                .map(Property::getValue).orElse(null);

        String replyToLogout = arrayToStream(inboundAuth.getProperties())
                .filter(property -> StringUtils.equals(property.getName(), PASSIVE_STS_REPLY_URL_LOGOUT))
                .findAny()
                .map(Property::getValue).orElse(null);

        return new PassiveStsConfiguration().realm(inboundAuth.getInboundAuthKey()).replyTo(replyTo)
                .replyToLogout(replyToLogout);
    }
}

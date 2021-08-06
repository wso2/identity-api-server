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

package org.wso2.carbon.identity.api.server.userstore.common;

import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.user.store.configuration.UserStoreConfigService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for User Store.
 */
public class UserStoreConfigServiceHolder {

    private static UserStoreConfigServiceHolder instance = new UserStoreConfigServiceHolder();
    private UserStoreConfigService userStoreConfigService;
    private RealmService realmService;
    private ClaimMetadataManagementService claimMetadataManagementService;

    private UserStoreConfigServiceHolder() {}

    public static UserStoreConfigServiceHolder getInstance() {

        return instance;
    }

    public UserStoreConfigService getUserStoreConfigService() {

        return UserStoreConfigServiceHolder.getInstance().userStoreConfigService;
    }

    public void setUserStoreConfigService(UserStoreConfigService userStoreConfigService) {

        UserStoreConfigServiceHolder.getInstance().userStoreConfigService = userStoreConfigService;
    }

    public RealmService getRealmService() {

        return UserStoreConfigServiceHolder.getInstance().realmService;
    }

    public void setRealmService(RealmService realmService) {

        UserStoreConfigServiceHolder.getInstance().realmService = realmService;
    }

    public ClaimMetadataManagementService getClaimMetadataManagementService() {

        return UserStoreConfigServiceHolder.getInstance().claimMetadataManagementService;
    }

    public void setClaimMetadataManagementService(ClaimMetadataManagementService claimMetadataManagementService) {

        UserStoreConfigServiceHolder.getInstance().claimMetadataManagementService = claimMetadataManagementService;
    }
}

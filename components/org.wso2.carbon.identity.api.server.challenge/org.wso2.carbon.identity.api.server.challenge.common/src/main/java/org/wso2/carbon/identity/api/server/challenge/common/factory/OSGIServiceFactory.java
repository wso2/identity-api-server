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

package org.wso2.carbon.identity.api.server.challenge.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the IdentityGovernanceService type of object inside the container.
 */
public class OSGIServiceFactory extends AbstractFactoryBean<ChallengeQuestionManager> {

    private ChallengeQuestionManager challengeQuestionManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected ChallengeQuestionManager createInstance() throws Exception {

        if (this.challengeQuestionManager == null) {
            ChallengeQuestionManager challengeQuestionManager = (ChallengeQuestionManager)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(ChallengeQuestionManager.class, null);
            if (challengeQuestionManager != null) {
                this.challengeQuestionManager = challengeQuestionManager;
            } else {
                throw new Exception("Unable to retrieve ChallengeQuestionManager service.");
            }
        }
        return this.challengeQuestionManager;
    }

}

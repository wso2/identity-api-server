/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created on 3/2/15.
 */
@XmlRootElement(name = "outputEventAdaptersConfig")
public class AdapterConfigs {

    private List<AdapterConfig> adapterConfigs = new ArrayList<AdapterConfig>();

    public List<AdapterConfig> getAdapterConfigs() {
        return adapterConfigs;
    }

    @XmlElement(name = "adapterConfig")
    public void setAdapterConfigs(List<AdapterConfig>
                                              adapterConfigs) {
        this.adapterConfigs = adapterConfigs;
    }

    public AdapterConfig getAdapterConfig(String type) {
        AdapterConfig matchedAdapterConfig = new AdapterConfig();
        for (AdapterConfig adapterConfig : adapterConfigs) {
            if (adapterConfig.getType().equals(type)) {
                matchedAdapterConfig = adapterConfig;
                break;
            }
        }
        return matchedAdapterConfig;
    }
}

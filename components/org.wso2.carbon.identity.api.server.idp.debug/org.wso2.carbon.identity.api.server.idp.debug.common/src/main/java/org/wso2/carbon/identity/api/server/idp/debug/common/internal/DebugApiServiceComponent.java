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

package org.wso2.carbon.identity.api.server.idp.debug.common.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * OSGi service component for API server debug services.
 * This component manages the lifecycle and dependencies of debug framework services
 * needed by the API layer to overcome classloader isolation issues.
 */
@Component(
        name = "api.server.idp.debug.service.component",
        immediate = true
)
public class DebugApiServiceComponent {

    private static final Log log = LogFactory.getLog(DebugApiServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {
        try {
            log.info("API Debug Service Component activated successfully");
            log.info("Ready to provide debug framework service lookup for API layer");
            
        } catch (Exception e) {
            log.error("Error while activating API Debug Service Component", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        try {
            log.info("API Debug Service Component deactivated successfully");
        } catch (Exception e) {
            log.error("Error while deactivating API Debug Service Component", e);
        }
    }

    /**
     * Reference to debug framework service (optional).
     * This creates a dependency tracking without requiring compile-time binding.
     */
    @Reference(
            name = "debug.service",
            service = Object.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            target = "(component.name=debug.framework.service.component)",
            unbind = "unsetDebugService"
    )
    protected void setDebugService(Object debugService) {
        try {
            DebugApiServiceDataHolder.getInstance().setDebugService(debugService);
            log.info("Debug framework service set in API Debug Service Component");
        } catch (Exception e) {
            log.error("Error setting debug service in API Debug Service Component", e);
        }
    }

    protected void unsetDebugService(Object debugService) {
        try {
            DebugApiServiceDataHolder.getInstance().setDebugService(null);
            log.info("Debug framework service unset from API Debug Service Component");
        } catch (Exception e) {
            log.error("Error unsetting debug service from API Debug Service Component", e);
        }
    }
}

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

package org.wso2.carbon.identity.api.server.idp.debug.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;

/**
 * Service holder class for debug framework services.
 * This class provides access to debug framework services through OSGi service lookup
 * to overcome classloader isolation between web applications and OSGi bundles.
 */
public class DebugFrameworkServiceHolder {

    private static final Log log = LogFactory.getLog(DebugFrameworkServiceHolder.class);
    
    private DebugFrameworkServiceHolder() {
        // Private constructor to prevent instantiation
    }



    /**
     * Gets the debug service using OSGi service lookup.
     * This method uses reflection to avoid compile-time dependencies on debug framework classes.
     *
     * @return Debug service instance if available, null otherwise.
     */
    public static Object getDebugService() {
        try {
            // Use OSGi service lookup with class name string to avoid compile-time dependency
            Object debugService = PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .getOSGiService(Class.forName("org.wso2.carbon.identity.debug.framework.DebugService"), null);
            
            if (debugService == null) {
                if (log.isDebugEnabled()) {
                    log.debug("DebugService not available via OSGi service lookup");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved DebugService via OSGi lookup");
                }
            }
            
            return debugService;
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("DebugService class not found: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving DebugService: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets the RequestCoordinator using OSGi service lookup.
     * This method uses reflection to avoid compile-time dependencies on debug framework classes.
     *
     * @return RequestCoordinator instance if available, null otherwise.
     */
    public static Object getRequestCoordinator() {
        try {
            // Use OSGi service lookup with class name string to avoid compile-time dependency
            Object requestCoordinator = PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .getOSGiService(Class.forName("org.wso2.carbon.identity.debug.framework.RequestCoordinator"), null);
            
            if (requestCoordinator == null) {
                if (log.isDebugEnabled()) {
                    log.debug("RequestCoordinator not available via OSGi service lookup");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved RequestCoordinator via OSGi lookup");
                }
            }
            
            return requestCoordinator;
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("RequestCoordinator class not found: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving RequestCoordinator: " + e.getMessage(), e);
            return null;
        }
    }



    /**
     * Invokes a method on the debug service using reflection.
     * This allows us to call methods without compile-time dependencies.
     *
     * @param methodName Method name to invoke.
     * @param parameterTypes Parameter types for the method.
     * @param arguments Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Object invokeDebugServiceMethod(String methodName, Class<?>[] parameterTypes, Object... arguments) {
        Object debugService = getDebugService();
        if (debugService == null) {
            log.warn("DebugService not available for method invocation: " + methodName);
            return null;
        }

        try {
            java.lang.reflect.Method method = debugService.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(debugService, arguments);
        } catch (Exception e) {
            log.error("Error invoking DebugService method '" + methodName + "': " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Checks if debug framework services are available.
     *
     * @return true if debug services are available, false otherwise.
     */
    public static boolean isDebugFrameworkAvailable() {
        return getDebugService() != null;
    }



    /**
     * Creates a new instance of Executer using reflection.
     *
     * @return New Executer instance or null if creation fails.
     */
    public static Object createExecuter() {
        try {
            Class<?> executerClass = Class.forName("org.wso2.carbon.identity.debug.framework.Executer");
            return executerClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            log.debug("Executer class not found for direct instantiation: " + e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error creating Executer instance: " + e.getMessage(), e);
            return null;
        }
    }
}

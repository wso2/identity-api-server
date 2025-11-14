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
import org.wso2.carbon.identity.debug.framework.core.DebugContextResolver;
import org.wso2.carbon.identity.debug.framework.core.DebugExecutor;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;

/**
 * Service holder class for debug framework services.
 * This class provides access to debug framework services through OSGi service lookup.
 */
public class DebugFrameworkServiceHolder {

    private static final Log log = LogFactory.getLog(DebugFrameworkServiceHolder.class);
    
    private DebugFrameworkServiceHolder() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Gets a DebugExecutor instance via OSGi service lookup.
     * The new debug framework uses DebugExecutor interface.
     * OIDC module registers OAuth2Executor as DebugExecutor type.
     *
     * @return DebugExecutor instance if available, null otherwise.
     */
    public static Object getDebugExecutor() {
        try {
            PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            if (context == null) {
                if (log.isDebugEnabled()) {
                    log.debug("PrivilegedCarbonContext not available");
                }
                return null;
            }
            
            // Look up DebugExecutor by interface type.
            Object service = context.getOSGiService(DebugExecutor.class, null);
            
            if (service == null) {
                if (log.isDebugEnabled()) {
                    log.debug("DebugExecutor not available via OSGi service lookup");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved DebugExecutor via OSGi lookup");
                }
            }
            
            return service;
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()) {
                log.debug("NullPointerException while retrieving DebugExecutor: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving DebugExecutor: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets a DebugContextResolver instance via OSGi service lookup.
     * The new debug framework uses DebugContextResolver interface.
     * OIDC module registers OAuth2ContextResolver as DebugContextResolver type.
     *
     * @return DebugContextResolver instance if available, null otherwise.
     */
    public static Object getDebugContextResolver() {
        try {
            PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            if (context == null) {
                if (log.isDebugEnabled()) {
                    log.debug("PrivilegedCarbonContext not available");
                }
                return null;
            }
            
            // Look up DebugContextResolver by interface type.
            Object service = context.getOSGiService(DebugContextResolver.class, null);
            
            if (service == null) {
                if (log.isDebugEnabled()) {
                    log.debug("DebugContextResolver not available via OSGi service lookup");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved DebugContextResolver via OSGi lookup");
                }
            }
            
            return service;
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()) {
                log.debug("NullPointerException while retrieving DebugContextResolver: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving DebugContextResolver: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets a DebugRequestProcessor instance via OSGi service lookup.
     *
     * @return DebugRequestProcessor instance if available, null otherwise.
     */
    public static Object getDebugRequestProcessor() {
        try {
            PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            if (context == null) {
                if (log.isDebugEnabled()) {
                    log.debug("PrivilegedCarbonContext not available");
                }
                return null;
            }
            
            Object service = context.getOSGiService(DebugRequestCoordinator.class, null);
            
            if (service == null) {
                if (log.isDebugEnabled()) {
                    log.debug("DebugRequestProcessor not available via OSGi service lookup");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Successfully retrieved DebugRequestProcessor via OSGi lookup");
                }
            }
            
            return service;
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()) {
                log.debug("NullPointerException while retrieving DebugRequestProcessor: " + e.getMessage());
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving DebugRequestProcessor: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Checks if debug framework services are available.
     *
     * @return true if debug services are available, false otherwise.
     */
    public static boolean isDebugFrameworkAvailable() {
        return getDebugExecutor() != null && getDebugContextResolver() != null;
    }

    /**
     * Invokes a method on the DebugExecutor using reflection.
     *
     * @param debugExecutor The DebugExecutor instance.
     * @param methodName Method name to invoke.
     * @param parameterTypes Parameter types for the method.
     * @param arguments Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Object invokeDebugExecutorMethod(Object debugExecutor, String methodName, 
                                                   Class<?>[] parameterTypes, Object... arguments) {
        if (debugExecutor == null) {
            log.warn("DebugExecutor not available for method invocation: " + methodName);
            return null;
        }

        try {
            java.lang.reflect.Method method = debugExecutor.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(debugExecutor, arguments);
        } catch (Exception e) {
            log.error("Error invoking DebugExecutor method '" + methodName + "': " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Invokes a method on the DebugContextResolver using reflection.
     *
     * @param debugContextResolver The DebugContextResolver instance.
     * @param methodName Method name to invoke.
     * @param parameterTypes Parameter types for the method.
     * @param arguments Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Object invokeDebugContextResolverMethod(Object debugContextResolver, String methodName,
                                                          Class<?>[] parameterTypes, Object... arguments) {
        if (debugContextResolver == null) {
            log.warn("DebugContextResolver not available for method invocation: " + methodName);
            return null;
        }

        try {
            java.lang.reflect.Method method = debugContextResolver.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(debugContextResolver, arguments);
        } catch (Exception e) {
            log.error("Error invoking DebugContextResolver method '" + methodName + "': " + e.getMessage(), e);
            return null;
        }
    }
}

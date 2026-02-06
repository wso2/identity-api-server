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

package org.wso2.carbon.identity.api.server.debug.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.core.extension.DebugContextProvider;
import org.wso2.carbon.identity.debug.framework.core.extension.DebugExecutor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Service holder for debug framework OSGi services.
 * 
 * This class provides access to debug framework services through OSGi service
 * lookup.
 * It serves as a bridge between the API layer and the debug framework, handling
 * service discovery and method invocation.
 * 
 * The class follows the service holder pattern to enable loose coupling
 * between the API layer and the debug framework implementation.
 */
public final class DebugFrameworkServiceHolder {

    private static final Log LOG = LogFactory.getLog(DebugFrameworkServiceHolder.class);

    private static final String COORDINATOR_CLASS_NAME 
        = "org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator";

    private DebugFrameworkServiceHolder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets a DebugExecutor instance via OSGi service lookup.
     *
     * @return DebugExecutor instance if available, null otherwise.
     */
    public static DebugExecutor getDebugExecutor() {

        return getOSGiService(DebugExecutor.class);
    }

    /**
     * Gets a DebugContextProvider instance via OSGi service lookup.
     *
     * @return DebugContextProvider instance if available, null otherwise.
     */
    public static DebugContextProvider getDebugContextProvider() {

        return getOSGiService(DebugContextProvider.class);
    }

    /**
     * Gets a DebugRequestCoordinator instance via OSGi service lookup.
     * Falls back to reflection-based instantiation if OSGi lookup fails.
     *
     * @return DebugRequestCoordinator instance if available, null otherwise.
     */
    public static DebugRequestCoordinator getDebugRequestCoordinator() {

        // First, try OSGi service lookup
        DebugRequestCoordinator service = getOSGiService(DebugRequestCoordinator.class);
        if (service != null) {
            return service;
        }

        // Fallback: Try to instantiate via reflection
        return instantiateViaReflection();
    }

    /**
     * Checks if debug framework services are available.
     *
     * @return true if debug framework services are available, false otherwise.
     */
    public static boolean isDebugFrameworkAvailable() {

        DebugRequestCoordinator coordinator = getDebugRequestCoordinator();
        if (coordinator == null) {
            LOG.warn("Debug framework is NOT available - DebugRequestCoordinator could not be retrieved");
            return false;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Debug framework is available");
        }
        return true;
    }

    /**
     * Invokes a method on the DebugRequestCoordinator.
     *
     * @param coordinator    The coordinator instance.
     * @param methodName     Method name to invoke.
     * @param parameterTypes Parameter types for the method.
     * @param arguments      Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Map<String, Object> invokeDebugRequestCoordinatorMethod(
            Object coordinator,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments) {

        return invokeMethod(coordinator, methodName, parameterTypes, arguments);
    }

    /**
     * Invokes a method on the DebugExecutor.
     *
     * @param executor       The executor instance.
     * @param methodName     Method name to invoke.
     * @param parameterTypes Parameter types for the method.
     * @param arguments      Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Object invokeDebugExecutorMethod(
            Object executor,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments) {

        return invokeMethod(executor, methodName, parameterTypes, arguments);
    }

    /**
     * Invokes a method on the DebugContextProvider.
     *
     * @param contextProvider The context provider instance.
     * @param methodName      Method name to invoke.
     * @param parameterTypes  Parameter types for the method.
     * @param arguments       Arguments to pass to the method.
     * @return Method result or null if invocation fails.
     */
    public static Object invokeDebugContextResolverMethod(
            Object contextProvider,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments) {

        return invokeMethod(contextProvider, methodName, parameterTypes, arguments);
    }

    /**
     * Gets an OSGi service by class type.
     *
     * @param serviceClass The service class to look up.
     * @param <T>          The service type.
     * @return The service instance if available, null otherwise.
     */
    private static <T> T getOSGiService(Class<T> serviceClass) {

        try {
            PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            if (context == null) {
                logDebug("PrivilegedCarbonContext not available for " + serviceClass.getSimpleName());
                return null;
            }

            T service = (T) context.getOSGiService(serviceClass, null);
            if (service != null) {
                logDebug("Successfully retrieved " + serviceClass.getSimpleName() + " via OSGi lookup");
            } else {
                logDebug(serviceClass.getSimpleName() + " not available via OSGi service lookup");
            }
            return service;

        } catch (Exception e) {
            LOG.error("Error retrieving " + serviceClass.getSimpleName() + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Instantiates DebugRequestCoordinator via reflection as a fallback.
     *
     * @return DebugRequestCoordinator instance or null if failed.
     */
    private static DebugRequestCoordinator instantiateViaReflection() {

        try {
            Class<?> coordinatorClass = Class.forName(COORDINATOR_CLASS_NAME);
            Object coordinator = coordinatorClass.getDeclaredConstructor().newInstance();
            logDebug("DebugRequestCoordinator instantiated via reflection");
            return (DebugRequestCoordinator) coordinator;
        } catch (ClassNotFoundException e) {
            logDebug("DebugRequestCoordinator class not found: " + e.getMessage());
        } catch (Exception e) {
            logDebug("Failed to instantiate DebugRequestCoordinator: " + e.getMessage());
        }
        return null;
    }

    /**
     * Generic method invocation helper using reflection.
     *
     * @param target         The target object.
     * @param methodName     Method name to invoke.
     * @param parameterTypes Parameter types.
     * @param arguments      Method arguments.
     * @param <T>            Return type.
     * @return Method result or null if invocation fails.
     */
    private static <T> T invokeMethod(
            Object target,
            String methodName,
            Class<?>[] parameterTypes,
            Object... arguments) {

        if (target == null) {
            LOG.warn("Target object not available for method invocation: " + methodName);
            return null;
        }

        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            return (T) method.invoke(target, arguments);
        } catch (NoSuchMethodException e) {
            LOG.error("Method '" + methodName + "' not found on " + target.getClass().getSimpleName(), e);
        } catch (Exception e) {
            LOG.error("Error invoking '" + methodName + "' on " + target.getClass().getSimpleName() +
                    ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Logs a debug message if debug logging is enabled.
     *
     * @param message The message to log.
     */
    private static void logDebug(String message) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }
}

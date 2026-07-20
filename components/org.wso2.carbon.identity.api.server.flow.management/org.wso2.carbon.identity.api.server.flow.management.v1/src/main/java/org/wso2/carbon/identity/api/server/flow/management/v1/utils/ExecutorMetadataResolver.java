/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.flow.management.v1.ExecutorMetadata;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.flow.execution.engine.metadata.FlowExecutorInfo;
import org.wso2.carbon.identity.flow.execution.engine.metadata.FlowExecutorMetadataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Reconciles the executors this server has always advertised for a flow type with the executors that
 * declare themselves to the flow execution engine at runtime.
 * <p>
 * The union of the two is what the flow composer is offered and what a flow is validated against on
 * save, so a connector deployed into {@code repository/components/dropins} becomes usable as a step
 * with no change here.
 * <p>
 * Only executors outside the baseline get a {@link ExecutorMetadata} entry. Baseline executors are
 * ones the caller has always known how to render from the name alone, so describing them again would
 * only bloat the response. Consequently, when a baseline executor adopts the metadata SPI it starts
 * being described here only once its baseline entry is also removed.
 * <p>
 * <b>Request scoped.</b> Resolution is memoised on the instance, never statically. Meta response
 * handlers are constructed per request, so the widest window in which this can be stale is a single
 * request - which is what lets an executor bound after the web application started still show up.
 */
public class ExecutorMetadataResolver {

    private static final Log LOG = LogFactory.getLog(ExecutorMetadataResolver.class);

    /*
     * Display strings originate in a third party jar and are rendered in an administrative UI, so they
     * are length capped before being handed on.
     */
    private static final int MAX_DISPLAY_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 1024;

    private final String flowType;
    private final List<String> legacyBaseline;

    private List<String> supportedExecutorNames;
    private LinkedHashMap<String, ExecutorMetadata> extensionMetadata;
    private Map<String, String> connectionExecutorMap;

    /**
     * @param flowType       Flow type being described, a
     *                       {@link org.wso2.carbon.identity.flow.mgt.Constants.FlowTypes} name.
     * @param legacyBaseline Executors this server advertises for the flow type regardless of what is
     *                       registered, in the order they should be presented.
     */
    public ExecutorMetadataResolver(String flowType, List<String> legacyBaseline) {

        this.flowType = flowType;
        this.legacyBaseline = legacyBaseline == null ? Collections.emptyList() : legacyBaseline;
    }

    /**
     * Effective executor names for this flow type: the baseline in its established order, followed by
     * any additional executors that declared support for this flow type.
     *
     * @return De-duplicated list of executor names.
     */
    public List<String> getSupportedExecutorNames() {

        resolve();
        return new ArrayList<>(supportedExecutorNames);
    }

    /**
     * Metadata for the executors contributed by a deployed extension, i.e. those that declared
     * themselves and are not part of this server's baseline. Baseline executors appear by name in
     * {@link #getSupportedExecutorNames()} only.
     *
     * @return List of extension executor metadata, empty when no extension contributed one.
     */
    public List<ExecutorMetadata> getExtensionExecutorMetadata() {

        resolve();
        return new ArrayList<>(extensionMetadata.values());
    }

    /**
     * Authenticator name to executor name, used to work out which connections apply to which executor.
     * The legacy mapping is seeded first, then overlaid with the authenticator each registered executor
     * declares for itself.
     *
     * @return Map of authenticator name to executor name.
     */
    public Map<String, String> getConnectionExecutorMap() {

        if (connectionExecutorMap != null) {
            return connectionExecutorMap;
        }
        Map<String, String> map = new HashMap<>(FlowEndpointConstants.LegacyExecutors.CONNECTION_EXECUTOR_MAP);
        for (FlowExecutorInfo info : declaredExecutors()) {
            if (isNotBlank(info.getAssociatedAuthenticator())) {
                map.put(info.getAssociatedAuthenticator(), info.getName());
            }
        }
        connectionExecutorMap = Collections.unmodifiableMap(map);
        return connectionExecutorMap;
    }

    /**
     * Executors for this flow type that carry the given metadata tag.
     *
     * @param tag Tag to match, e.g. {@code FlowExecutorConstants.Tags.RECOVERY_FACTOR}.
     * @return Names of the matching executors.
     */
    public Set<String> getExecutorsWithTag(String tag) {

        Set<String> matching = new HashSet<>();
        if (isBlank(tag)) {
            return matching;
        }
        for (FlowExecutorInfo info : declaredExecutors()) {
            if (info.getTags().contains(tag)) {
                matching.add(info.getName());
            }
        }
        return matching;
    }

    private void resolve() {

        if (supportedExecutorNames != null) {
            return;
        }

        List<FlowExecutorInfo> extensions = new ArrayList<>();
        for (FlowExecutorInfo info : declaredExecutors()) {
            if (!legacyBaseline.contains(info.getName())) {
                extensions.add(info);
            }
        }
        extensions.sort(Comparator.comparing(info -> info.getDisplayName() == null
                ? info.getName().toLowerCase(Locale.ENGLISH) : info.getDisplayName().toLowerCase(Locale.ENGLISH)));

        List<String> names = new ArrayList<>();
        for (String name : legacyBaseline) {
            if (!names.contains(name)) {
                names.add(name);
            }
        }

        LinkedHashMap<String, ExecutorMetadata> metadata = new LinkedHashMap<>();
        for (FlowExecutorInfo extension : extensions) {
            names.add(extension.getName());
            metadata.put(extension.getName(), toModel(extension));
        }

        supportedExecutorNames = names;
        extensionMetadata = metadata;
    }

    private ExecutorMetadata toModel(FlowExecutorInfo declared) {

        String authenticator = isNotBlank(declared.getAssociatedAuthenticator())
                ? declared.getAssociatedAuthenticator() : reverseLookupAuthenticator(declared.getName());

        return new ExecutorMetadata()
                .name(declared.getName())
                .displayName(truncate(declared.getDisplayName(), MAX_DISPLAY_NAME_LENGTH))
                .description(truncate(declared.getDescription(), MAX_DESCRIPTION_LENGTH))
                .tags(declared.getTags().isEmpty() ? null : new ArrayList<>(declared.getTags()))
                .icon(declared.getIcon())
                .associatedAuthenticator(authenticator)
                .requiresConnection(declared.isConnectionRequired() || declared.isIdpRequired()
                        || authenticator != null);
    }

    private String reverseLookupAuthenticator(String executorName) {

        for (Map.Entry<String, String> entry
                : FlowEndpointConstants.LegacyExecutors.CONNECTION_EXECUTOR_MAP.entrySet()) {
            if (entry.getValue().equals(executorName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Executors registered with the engine that declared support for this flow type.
     * <p>
     * Every call into the engine is guarded: executors can be contributed by third party bundles, and a
     * faulty one must not turn the meta endpoint into a server error.
     */
    private List<FlowExecutorInfo> declaredExecutors() {

        try {
            return FlowExecutorMetadataService.getInstance().getComposerExecutors(flowType);
        } catch (Throwable e) {
            LOG.warn("Failed to resolve dynamically registered executors for flow type: " + flowType
                    + ". Falling back to the built in executor list.", e);
            return Collections.emptyList();
        }
    }

    private static String truncate(String value, int maxLength) {

        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private static boolean isBlank(String value) {

        return value == null || value.trim().isEmpty();
    }

    private static boolean isNotBlank(String value) {

        return !isBlank(value);
    }
}

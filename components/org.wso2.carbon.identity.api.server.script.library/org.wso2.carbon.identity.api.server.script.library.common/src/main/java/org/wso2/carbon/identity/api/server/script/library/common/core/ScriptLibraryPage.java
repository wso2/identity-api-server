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

package org.wso2.carbon.identity.api.server.script.library.common.core;

import org.wso2.carbon.identity.functions.library.mgt.model.FunctionLibrary;

import java.util.List;

/**
 * A page of script libraries returned by {@link ServerScriptLibrariesService#getScriptLibraries(Integer, Integer)}.
 */
public class ScriptLibraryPage {

    private final List<FunctionLibrary> scriptLibraries;
    private final int totalResults;
    private final int offset;

    public ScriptLibraryPage(List<FunctionLibrary> scriptLibraries, int totalResults, int offset) {

        this.scriptLibraries = scriptLibraries;
        this.totalResults = totalResults;
        this.offset = offset;
    }

    public List<FunctionLibrary> getScriptLibraries() {

        return scriptLibraries;
    }

    public int getTotalResults() {

        return totalResults;
    }

    public int getOffset() {

        return offset;
    }
}

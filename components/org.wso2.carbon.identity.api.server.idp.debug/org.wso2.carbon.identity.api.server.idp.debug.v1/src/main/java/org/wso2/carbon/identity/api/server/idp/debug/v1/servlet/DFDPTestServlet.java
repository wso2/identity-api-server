/*
* Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.identity.api.server.idp.debug.v1.servlet;

import com.google.gson.Gson;

import org.wso2.carbon.identity.api.server.idp.debug.v1.core.DFDPService;
import org.wso2.carbon.identity.application.authentication.framework.handler.request.impl.DefaultRequestCoordinator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Direct servlet implementation to bypass Carbon security for testing.
 * Supports advanced DFDP flow with framework integration.
 */
public class DFDPTestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DFDPService dfdpService;
    private final Gson gson;

    public DFDPTestServlet() {
        this.dfdpService = new DFDPService();
        this.gson = new Gson();
    }

    /**
     * Sanitizes input to prevent XSS attacks.
     *
     * @param input The input string to sanitize
     * @return Sanitized string or null if input was null
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove potentially dangerous characters that could be used for XSS
        return input.replaceAll("[<>\"'&\\\\]", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response, "POST");
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, String method)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Restrict CORS to localhost for debugging - more secure than wildcard
        String origin = request.getHeader("Origin");
        if (origin != null && (origin.startsWith("http://localhost") || origin.startsWith("https://localhost"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        PrintWriter out = response.getWriter();

        try {
            // Set a special parameter to indicate DFDP debug flow
            request.setAttribute("action", "dfdp-debug");
            // Delegate to DefaultRequestCoordinator for unified routing
            DefaultRequestCoordinator coordinator = new DefaultRequestCoordinator();
            coordinator.handle(request, response);
            // Note: The response will be written by the coordinator/DFDP logic
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            // Sanitize error message to prevent XSS
            String sanitizedMessage = e.getMessage();
            if (sanitizedMessage != null) {
                sanitizedMessage = sanitizedMessage.replaceAll("[<>\"'&]", "");
            }
            errorResponse.put("message", "DFDP service error: " + sanitizedMessage);
            errorResponse.put("timestamp", System.currentTimeMillis());
            errorResponse.put("exception", e.getClass().getSimpleName());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Use Gson to safely serialize JSON - this prevents XSS
            String jsonResponse = gson.toJson(errorResponse);
            out.write(jsonResponse);
        } finally {
            out.flush();
            out.close();
        }
    }
}

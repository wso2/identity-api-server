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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.idp.debug.v1.servlet;

import com.google.gson.Gson;
import org.wso2.carbon.identity.api.server.idp.debug.v1.core.SophisticatedDFDPFlowService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Dedicated servlet for sophisticated DFDP flow testing with framework integration.
 * This servlet implements the advanced flow diagram you designed.
 */
public class SophisticatedDFDPServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private final SophisticatedDFDPFlowService sophisticatedFlowService;
    private final Gson gson;

    /**
     * Constructor for SophisticatedDFDPServlet.
     */
    public SophisticatedDFDPServlet() {
        this.sophisticatedFlowService = new SophisticatedDFDPFlowService();
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
        // Remove potentially dangerous characters that could be used for XSS.
        return input.replaceAll("[<>\"'&\\\\]", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleSophisticatedFlow(request, response, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleSophisticatedFlow(request, response, "POST");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setCORSHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Set CORS headers for cross-origin requests.
     */
    private void setCORSHeaders(HttpServletRequest request, HttpServletResponse response) {
        // Restrict CORS to localhost for debugging - more secure than wildcard.
        String origin = request.getHeader("Origin");
        if (origin != null && (origin.startsWith("http://localhost") || origin.startsWith("https://localhost"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    /**
     * Handle sophisticated DFDP flow requests.
     */
    private void handleSophisticatedFlow(HttpServletRequest request, HttpServletResponse response, String method) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        setCORSHeaders(request, response);

        PrintWriter out = response.getWriter();

        try {
            // Get and sanitize parameters from request to prevent XSS.
            String idpName = sanitizeInput(request.getParameter("idpName"));
            String authenticatorName = sanitizeInput(request.getParameter("authenticatorName"));
            String flowType = sanitizeInput(request.getParameter("flowType"));
            
            // Set defaults if not provided.
            if (idpName == null || idpName.trim().isEmpty()) {
                idpName = "Google";  // Default IdP for testing.
            }
            if (flowType == null || flowType.trim().isEmpty()) {
                flowType = "SOPHISTICATED_FRAMEWORK_BYPASS";
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("servlet", "SophisticatedDFDPServlet");
            responseData.put("flowType", flowType);
            responseData.put("method", method);
            responseData.put("timestamp", System.currentTimeMillis());
            
            // Execute sophisticated DFDP flow.
            Object sophisticatedResult = sophisticatedFlowService.executeSophisticatedFlow(
                idpName, authenticatorName, request, response);
            
            responseData.put("idpName", idpName);
            responseData.put("authenticatorName", authenticatorName);
            responseData.put("sophisticatedFlow", sophisticatedResult);
            responseData.put("status", "SUCCESS");

            response.setStatus(HttpServletResponse.SC_OK);
            // Use Gson to safely serialize JSON - this prevents XSS.
            String jsonResponse = gson.toJson(responseData);
            out.write(jsonResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("servlet", "SophisticatedDFDPServlet");
            errorResponse.put("status", "ERROR");
            // Sanitize error message to prevent XSS.
            String sanitizedMessage = e.getMessage();
            if (sanitizedMessage != null) {
                sanitizedMessage = sanitizedMessage.replaceAll("[<>\"'&]", "");
            }
            errorResponse.put("message", "Sophisticated DFDP flow error: " + sanitizedMessage);
            errorResponse.put("timestamp", System.currentTimeMillis());
            errorResponse.put("exception", e.getClass().getSimpleName());
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Use Gson to safely serialize JSON - this prevents XSS.
            String jsonResponse = gson.toJson(errorResponse);
            out.write(jsonResponse);
            
        } finally {
            out.flush();
            out.close();
        }
    }
}

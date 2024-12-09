/*
 * Copyright (c) 2022-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.v1.factories;

import org.wso2.carbon.identity.api.server.input.validation.v1.ValidationRulesApiService;
import org.wso2.carbon.identity.api.server.input.validation.v1.impl.ValidationRulesApiServiceImpl;

/**
 * Factory to return validationRulesApiService.
 */
public class ValidationRulesApiServiceFactory {

   private final static ValidationRulesApiService SERVICE = new ValidationRulesApiServiceImpl();

   /**
    * Method to get ValidationRulesApiService.
    *
    * @return  ValidationRulesApiService.
    */
   public static ValidationRulesApiService getValidationRulesApi() {

      return SERVICE;
   }
}

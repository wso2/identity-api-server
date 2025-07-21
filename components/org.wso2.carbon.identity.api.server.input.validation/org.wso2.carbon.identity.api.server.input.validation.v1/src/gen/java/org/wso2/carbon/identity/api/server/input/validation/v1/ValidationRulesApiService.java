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

package org.wso2.carbon.identity.api.server.input.validation.v1;

import org.wso2.carbon.identity.api.server.input.validation.v1.*;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RevertFields;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModelForField;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidatorModel;
import javax.ws.rs.core.Response;


public interface ValidationRulesApiService {

      /**
       * Method to get configured validation rules.
       *
       * @return  List of configured validation rules.
       */
      public Response getValidationRules();

      /**
       * Method to get configured validation rules for a field.
       *
       * @param field   Field that validation configurations need to be retrieved.
       * @return  List of configured validation rules for the field.
       */
      public Response getValidationRulesForField(String field);

      /**
       * Method to get available validator configurations.
       *
       * @return  List of validator configurations.
       */
      public Response getValidators();

      /**
       * Method to update validation rules.
       *
       * @param validationConfigModel     List of validation rules to be updated.
       * @return  Updated validation rules.
       */
      public Response updateValidationRules(List<ValidationConfigModel> validationConfigModel);

        /**
         * Method to revert validation rules for fields.
         *
         * @param revertFields   List of fields that validation rules need to be reverted.
         * @return  Response indicating the success or failure of the operation.
         */
      public Response revertValidationRulesForFields(RevertFields revertFields);

      /**
       * Method to update validation rules for a field.
       *
       * @param field                             Field that validations need to be updated.
       * @param validationConfigModelForField     List of validation rules to be updated for the field.
       * @return  Updated validation rules for the field.
       */
      public Response updateValidationRulesForField(String field,
                                                    ValidationConfigModelForField validationConfigModelForField);
}

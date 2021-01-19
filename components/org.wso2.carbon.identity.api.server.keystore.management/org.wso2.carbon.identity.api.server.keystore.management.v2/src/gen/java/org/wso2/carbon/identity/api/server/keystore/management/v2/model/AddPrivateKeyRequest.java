/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.keystore.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AddPrivateKeyRequest  {
  
    private String alias;
    private String certificate;

    /**
    **/
    public AddPrivateKeyRequest alias(String alias) {

        this.alias = alias;
        return this;
    }
    
    @ApiModelProperty(example = "newkey", required = true, value = "")
    @JsonProperty("alias")
    @Valid
    @NotNull(message = "Property alias cannot be null.")

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
    **/
    public AddPrivateKeyRequest certificate(String certificate) {

        this.certificate = certificate;
        return this;
    }
    
    @ApiModelProperty(example = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDPE0Qg/kdSpSE94YVXQ2cfnZZ6V8DQvYvc0kjHiRljQFQpCwRoenhuZQ6v6d2wOT43DtlUFajDIqryju76YjKIIMFQCk5qLRUwU5IxBJIFR+WTqANTvnzC1eS0drfeQL1pclsRwWAfEBIQOcJfb72B74h5SH5t1RuhiP5g4xW0wkCb741rENXrxSTvnQ2vQF0sJlPJkWqOczTZnrvtRt84+PuieeevCUjZOY2Y9jSenOKCfsDaUvbCQ0EDK0zqJWyh16I0LY+KMwcl4EbJQ0QBp0daMfqj8ChbUCYZnN/yfQsix2NaehW5W5CpSWVdNObcJ6BSsnWj93tRMwgNmy6ZAgMBAAECggEAfsAJPLTIjXLcRRds2fw8/CImwJ5EPME5AiKJP/HQY2s12KWQvi7w1onvEohxFPQE/AiPyKM1WLQ4tR3+c651xdNe4JJeaylLRfhxjKWCh875XIdD+heoIE5ynm/onhc8mXXQ/q5CGuFoXRwtuOMZuZYYEDzqfmoqbpb/NejqGWiPyGKHXHxgX3k9vSCqxGSENsZUuBsYtkR/k2zc5lYkYc6kYY0Ko+Rgj8As1c3kFXFqODUrH6Pm2rizLpRl6Qe7tg9r7PtYI6zUm4AvZyR+Hx7cqoEaEwJbXO7kj2cJ7p0S/a58AoZq5HpF2cwlJ7dEeNgcJNB7++PKapiWuy1EAQKBgQDtnUrEuR/G76fxcuCM8fadkIWY9uxasMPaJX9KIlBDfMMba/G/xMPJwUfcTZDrFnoNn7XsEbk9leKfvylXwbqr9b35jCtp+MTyaNd5iNxBQh61ZEhoinK157W99UHVPbL5+Y0OoV7bN9SzmEljirlikrmKyXMUOqHbp0skCWj8gQKBgQDfGQyJ2tfj4/ZD6utbjzx7yCfR4IAh7HhNAK/lw+yQvw53ye69YVcZvUMzoIw8NGsZfbWbh1Qqopm1Q5ONSzMEVPU53kNzDdJFamFrAqQz5VAeklQDUngT5i8gyXBUMXLQL0MJmSNPucLJAIXK2f2x47CjcgbxRUrWNIjQkq+GGQKBgFmiJhxDgTs4GHA6V36Tn+YcKGllbA9h8t+NmZDLlN+AZtEnTRB8Fyc6Y/M0x2AHII+XGbctkGUs70o3gE78Y4COuNU0DQRytUiESBQujneHi7H9XFsjLtZzy9kWdXkNR0CI5K8KLJAhZXlCDx0frYMDuj4ic+jDnV8QooW7jwEBAoGAXs/6cCu9THB5HGweEg9RmRl7MsrCKaihh7gfCPq28tz/fREokPtpVNsGUE6OypdMXMz7/VkKAASSZmKNtqKaz+B7R1iaOa9KCY4oUJ4SwqtV3Wg6pB7MQUi2Bq0VO/K35JnxV4bpZtx/V4UNVyRFmPA3BX9US/0yzM4OxvVP+yECgYAslwxxfxUBsd66CLuTO01w5kFjPrZWX7EIMJ1UVX2j/SSkSCqrsaaQdUIp+vsPJVvbnD+yb5dp6hsNt243RskZt4OkCHkoLBdKUGLknC3nXjcHcI5R7HsR4J9jGLHU0kbESNJVdHfK06r90fQIg06x/NVqneL3bQTMj7yPMokslg==", required = true, value = "")
    @JsonProperty("certificate")
    @Valid
    @NotNull(message = "Property certificate cannot be null.")

    public String getCertificate() {
        return certificate;
    }
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddPrivateKeyRequest addPrivateKeyRequest = (AddPrivateKeyRequest) o;
        return Objects.equals(this.alias, addPrivateKeyRequest.alias) &&
            Objects.equals(this.certificate, addPrivateKeyRequest.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, certificate);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AddPrivateKeyRequest {\n");
        
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}


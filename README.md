# identity-api-server

|  Branch | Build Status | Travis CI Status |
| :------------ |:------------- |:-------------
| master      | [![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fwso2.org%2Fjenkins%2Fjob%2Fplatform-builds%2Fjob%2Fidentity-api-server%2F)](https://wso2.org/jenkins/job/platform-builds/job/identity-api-server/) | [![Travis CI Status](https://travis-ci.org/wso2/identity-api-server.svg?branch=master)](https://travis-ci.org/wso2/identity-api-server?branch=master)|

This repository contains modules related to the server related REST apis. It can be compiled using JDK 8, JDK 11 or JDK 17.

### Plugin to Generate the API Stubs

To generate stub, the [swagger2cxf-maven-plugin](https://github.com/hevayo/swagger2cxf-maven-plugin) is used.

We have done improvements to this plugin so that the stubs can be generated within a given package name. Therefore 
you can define multiple swagger files and deploy them in a single web application. To get the improvements locally, 
please follow the given steps.

1. Clone the repository [https://github.com/madurangasiriwardena/openapi-generator-cxf-wso2](https://github.com/madurangasiriwardena/openapi-generator-cxf-wso2)
```
git clone https://github.com/madurangasiriwardena/openapi-generator-cxf-wso2
```
2. Build the plugin (master branch)
```
mvn clean install
```

Now, the locally built openapi-generator-cxf plugin will be picked from the local .m2 repository when generating the stubs.

In this repository each server resource type is represented by a unique component. Hence, you need to create a new 
maven module for the resource type.
Under this component the implementation of each major version will have a unique component along with version in it's
 name.

#### Implement a new API with a new swagger definition - version one

1. Include the API swagger definition (in OpenAPI 3.0) in the given location of this maven project (identity-api-server). 
The suggested name for the file name of the API definition is `<resource>.yaml`. 

If you are working with Swagger 2.0 still, you have to convert the API definition to OpenAPI 3.0

```
    +-- identity-api-server
    |   +-- components
    |       +-- org.wso2.carbon.identity.api.server.<resource>
    |           +-- org.wso2.carbon.identity.api.server.<resource>.<version>
    |               +-- src
    |                   +-- main
    |                       +-- resources
    |                           +--api.yaml
    |               +-- pom.xml
    |           +-- pom.xml
```
     
   Let's consider sample definition as *challenge.yaml*
     
```
        +-- identity-api-server
        |   +-- components
        |       +-- org.wso2.carbon.identity.api.server.challenge
        |           +-- org.wso2.carbon.identity.api.server.challenge.v1
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- pom.xml
```
   
        
2. Include the given plugin to the `pom.xml` file of the module `org.wso2.carbon.identity.api.server.<resource>
.<version>`

  ```
  <plugin>
      <groupId>org.openapitools</groupId>
      <artifactId>openapi-generator-maven-plugin</artifactId>
      <version>4.1.2</version>
      <executions>
          <execution>
              <goals>
                  <goal>generate</goal>
              </goals>
              <configuration>
                  <inputSpec>${project.basedir}/src/main/resources/<resource>.yaml</inputSpec>
                  <generatorName>org.wso2.carbon.codegen.CxfWso2Generator</generatorName>
                  <configOptions>
                      <sourceFolder>src/gen/java</sourceFolder>
                      <apiPackage>org.wso2.carbon.identity.api.server.<resource>.<version></apiPackage>
                      <modelPackage>org.wso2.carbon.identity.api.server.<resource>.<version>.model</modelPackage>
                      <packageName>org.wso2.carbon.identity.api.server.<resource>.<version></packageName>
                      <dateLibrary>java8</dateLibrary>
                      <hideGenerationTimestamp>true</hideGenerationTimestamp>
                  </configOptions>
                  <output>.</output>
                  <skipOverwrite>false</skipOverwrite>
              </configuration>
          </execution>
      </executions>
      <dependencies>
          <dependency>
              <groupId>org.openapitools</groupId>
              <artifactId>cxf-wso2-openapi-generator</artifactId>
              <version>1.0.0</version>
          </dependency>
      </dependencies>
  </plugin>
  ```

  For our example: 
    
  ```
    <plugin>
        <groupId>org.openapitools</groupId>
        <artifactId>openapi-generator-maven-plugin</artifactId>
        <version>4.1.2</version>
        <executions>
            <execution>
                <goals>
                    <goal>generate</goal>
                </goals>
                <configuration>
                    <inputSpec>${project.basedir}/src/main/resources/challenge.yaml</inputSpec>
                    <generatorName>org.wso2.carbon.codegen.CxfWso2Generator</generatorName>
                    <configOptions>
                        <sourceFolder>src/gen/java</sourceFolder>
                        <apiPackage>org.wso2.carbon.identity.api.server.challenge.v1</apiPackage>
                        <modelPackage>org.wso2.carbon.identity.api.server.challenge.v1.model</modelPackage>
                        <packageName>org.wso2.carbon.identity.api.server.challenge.v1</packageName>
                        <dateLibrary>java8</dateLibrary>
                        <hideGenerationTimestamp>true</hideGenerationTimestamp>
                    </configOptions>
                    <output>.</output>
                    <skipOverwrite>false</skipOverwrite>
                </configuration>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>org.openapitools</groupId>
                <artifactId>cxf-wso2-openapi-generator</artifactId>
                <version>1.0.0</version>
            </dependency>
        </dependencies>
    </plugin>
  ```

3. Do a maven build inside the module `org.wso2.carbon.identity.api.server.<resource>.<version>` to generate the stubs
   ```
    mvn clean install
   ```
4. Comment out the plugin added for your API definition before committing to the git. Because it will regenerate during each build.


#### Implement a new version of an existing API - version one+plus

A new version is introduced only when a major version of API swagger definition is introduced.

1. Locate the correct parent module of the current API implementation. Create a new module with the new version 
and include the API swagger definition in the new module project . Suggested name 
for the file name of the API definition is *<resource>.yaml*

```
    +-- identity-api-server
    |   +-- components
    |       +-- org.wso2.carbon.identity.api.server.<resource>
    |           +-- org.wso2.carbon.identity.api.server.<resource>.<version>
    |               +-- src
    |                   +-- main
    |                       +-- resources
    |                           +--api.yaml
    |               +-- pom.xml
    |           +-- org.wso2.carbon.identity.api.server.<resource>.<version+1>
    |               +-- src
    |                   +-- main
    |                       +-- resources
    |                           +--api.yaml
    |               +-- pom.xml
    |           +-- pom.xml
```
     
   Let's consider sample definition as *challenge.yaml*
     
```
        +-- identity-api-server
        |   +-- components
        |       +-- org.wso2.carbon.identity.api.server.challenge
        |           +-- org.wso2.carbon.identity.api.server.challenge.v1
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- org.wso2.carbon.identity.api.server.challenge.v2
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- pom.xml
```
   
2. Include the given plugin to the `pom.xml` file of the module `org.wso2.carbon.identity.api.server.<resource>.<version+1>`
        
```
      <plugin>
          <groupId>org.openapitools</groupId>
          <artifactId>openapi-generator-maven-plugin</artifactId>
          <version>4.1.2</version>
          <executions>
              <execution>
                  <goals>
                      <goal>generate</goal>
                  </goals>
                  <configuration>
                      <inputSpec>${project.basedir}/src/main/resources/<resource>.yaml</inputSpec>
                      <generatorName>org.wso2.carbon.codegen.CxfWso2Generator</generatorName>
                      <configOptions>
                          <sourceFolder>src/gen/java</sourceFolder>
                          <apiPackage>org.wso2.carbon.identity.api.server.<resource>.<version+1></apiPackage>
                          <modelPackage>org.wso2.carbon.identity.api.server.<resource>.<version+1>.model</modelPackage>
                          <packageName>org.wso2.carbon.identity.api.server.<resource>.<version+1></packageName>
                          <dateLibrary>java8</dateLibrary>
                          <hideGenerationTimestamp>true</hideGenerationTimestamp>
                      </configOptions>
                      <output>.</output>
                      <skipOverwrite>false</skipOverwrite>
                  </configuration>
              </execution>
          </executions>
          <dependencies>
              <dependency>
                  <groupId>org.openapitools</groupId>
                  <artifactId>cxf-wso2-openapi-generator</artifactId>
                  <version>1.0.0</version>
              </dependency>
          </dependencies>
      </plugin>
```
        
   For our example: 
        
```
        <plugin>
            <groupId>org.openapitools</groupId>
            <artifactId>openapi-generator-maven-plugin</artifactId>
            <version>4.1.2</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <inputSpec>${project.basedir}/src/main/resources/challenge.yaml</inputSpec>
                        <generatorName>org.wso2.carbon.codegen.CxfWso2Generator</generatorName>
                        <configOptions>
                            <sourceFolder>src/gen/java</sourceFolder>
                            <apiPackage>org.wso2.carbon.identity.api.server.challenge.v2</apiPackage>
                            <modelPackage>org.wso2.carbon.identity.api.server.challenge.v2.model</modelPackage>
                            <packageName>org.wso2.carbon.identity.api.server.challenge.v2</packageName>
                            <dateLibrary>java8</dateLibrary>
                            <hideGenerationTimestamp>true</hideGenerationTimestamp>
                        </configOptions>
                        <output>.</output>
                        <skipOverwrite>false</skipOverwrite>
                    </configuration>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>org.openapitools</groupId>
                    <artifactId>cxf-wso2-openapi-generator</artifactId>
                    <version>1.0.0</version>
                </dependency>
            </dependencies>
        </plugin>
```
   
3. Do a maven build inside the module `org.wso2.carbon.identity.api.server.<resource>.<version+1>` to generate the stubs
    ```
    mvn clean install
    ```
4. Comment out the plugin added for your API definition before committing to the git.


#### Implementing API definitions

Once you execute the above steps to generate the code it will generate set of java classes defining the Basic API 
definition. 

```    
        ├── src
        │   ├── gen
        │   │   └── java.org.wso2.carbon.identity.rest.api.server.<resource>.<version>
        │   └── main
        │       ├── java
        │       │   └── java.org.wso2.carbon.identity.rest.api.server.<resource>.<version>.impl
```  

In our example

```    
        ├── src
        │   ├── gen
        │   │   └── java.org.wso2.carbon.identity.rest.api.server.challenge.v1
        │   └── main
        │       ├── java
        │       │   └── java.org.wso2.carbon.identity.rest.api.server.challenge.v1.impl
```  

You need to implement the functions of the classes under `java.org.wso2.carbon.identity.rest.api.server.challenge.v1
.impl` package to respond with desired output.

Inorder to improve re-usability of common implementations between versions, we encourage you to include a common 
component `org.wso2.carbon.identity.api.server.<resource>.common` for your resource type component as below.

```
        +-- identity-api-server
        |   +-- components
        |       +-- org.wso2.carbon.identity.api.server.<resource>
        |           +-- org.wso2.carbon.identity.api.server.<resource>.common
        |               +-- src
        |               +-- pom.xml
        |           +-- org.wso2.carbon.identity.api.server.<resource>.<version>
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--api.yaml
        |               +-- pom.xml
        |           +-- org.wso2.carbon.identity.api.server.<resource>.<version+1>
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--api.yaml
        |               +-- pom.xml
        |           +-- pom.xml
        
```
     
  Let's consider sample resource
    
``` 
        +-- identity-api-server
        |   +-- components
        |       +-- org.wso2.carbon.identity.api.server.challenge
        |           +-- org.wso2.carbon.identity.api.server.challenge.common
        |               +-- src
        |               +-- pom.xml
        |           +-- org.wso2.carbon.identity.api.server.challenge.v1
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- org.wso2.carbon.identity.api.server.challenge.v2
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- pom.xml
```
   
You may add this common component in both the api version specific components as dependency and reuse.

#### How to expose the API in  WSO2 Identity Server

To integrate the API implemented in identity-api-server in a single web app , follow the steps in https://github.com/wso2/identity-rest-dispatcher/blob/master/README.md

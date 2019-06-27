# identity-server-api

This repository contains modules related to the server related rest apis 

### Plugin to Generate the API Stubs

To generate stub, the [swagger2cxf-maven-plugin](https://github.com/hevayo/swagger2cxf-maven-plugin) is used.

We have done improvements to this plugin so that the stubs can be generated within a given package name. Therefore 
you can define multiple swagger files and deploy them in a single web application. To get the improvements locally, 
please follow the given steps.

1. Clone the repository [https://github.com/IsuraD/swagger2cxf-maven-plugin](https://github.com/IsuraD/swagger2cxf-maven-plugin)
```
git clone https://github.com/IsuraD/swagger2cxf-maven-plugin.git
```
2. Checkout the branch **swagger_to_jar**
```
git checkout swagger_to_jar
```
3. Build the plugin 
```
mvn clean install
```

Now, the locally built swagger2cxf plugin will be picked from the local .m2 repository when generating the stubs.

In this repository each server resource type is represented by a unique component. Hence, you need to create a new 
maven module for the resource type.
Under this component the implementation of each major version will have a unique component along with version in it's
 name.

#### Implement a new API with a new swagger definition - version one

1. Include the API swagger definition in the given location of this maven project (identity-server-api). Suggested name 
for the file name of the API definition is `<resource>.yaml`
    ```
    +-- identity-server-api
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
     
        +-- identity-server-api
        |   +-- components
        |       +-- org.wso2.carbon.identity.api.server.challenge
        |           +-- org.wso2.carbon.identity.api.server.challenge.v1
        |               +-- src
        |                   +-- main
        |                       +-- resources
        |                           +--challenge.yaml
        |               +-- pom.xml
        |           +-- pom.xml
   
   
        
2. Include the given plugin to the `pom.xml` file of the module `org.wso2.carbon.identity.api.server.<resource>
.<version>`
    ```
    <plugin>
        <groupId>org.wso2.maven.plugins</groupId>
        <artifactId>swagger2cxf-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <inputSpec>${project.basedir}/src/main/resources/<resource>.yaml</inputSpec>
        </configuration>
    </plugin>
    ```
    For our example: 
    ```
    <plugin>
        <groupId>org.wso2.maven.plugins</groupId>
        <artifactId>swagger2cxf-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <inputSpec>${project.basedir}/src/main/resources/challenge.yaml</inputSpec>
        </configuration>
    </plugin>
    ```
3. Run the following command inside the module `org.wso2.carbon.identity.api.server.<resource>.<version>` to generate the stubs
    ```
    mvn swagger2cxf:generate
    ```
4. Comment out the plugin added for your API definition before committing to the git.


#### Implement a new version of an existing API - version one+plus

A new version is introduced only when a major version of API swagger definition is introduced.

1. Locate the correct parent module of the current API implementation. Create a new module with the new version 
and include the API swagger definition in the new module project . Suggested name 
for the file name of the API definition is *<resource>.yaml*
    ```
    +-- identity-server-api
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
     
        +-- identity-server-api
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
2. Include the given plugin to the `pom.xml` file of the module `org.wso2.carbon.identity.api.server.<resource>
.<version>`
    ```
    <plugin>
        <groupId>org.wso2.maven.plugins</groupId>
        <artifactId>swagger2cxf-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <inputSpec>${project.basedir}/src/main/resources/<resource>.yaml</inputSpec>
        </configuration>
    </plugin>
    ```
    For our example: 
    ```
    <plugin>
        <groupId>org.wso2.maven.plugins</groupId>
        <artifactId>swagger2cxf-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <inputSpec>${project.basedir}/src/main/resources/challenge.yaml</inputSpec>
        </configuration>
    </plugin>
    ```
3. Run the following command inside the module `org.wso2.carbon.identity.api.server.<resource>.<version>` to generate the stubs
    ```
    mvn swagger2cxf:generate
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
    +-- identity-server-api
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
        +-- identity-server-api
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

Refer the sample implementation of [server challenge API here](https://github.com/ayshsandu/identity-server-api/tree/master/components/org.wso2.carbon.identity.api.server.challenge)
/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
package org.wso2.carbon.identity.api.server.image.service.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Perform image service related operations.
 */
public class ServerImageService {

    private static final Log log = LogFactory.getLog(ServerImageService.class);

    /**
     * Method to upload the image to file system.
     *
     * @param fileInputStream
     * @param fileDetail
     * @param type
     * @return
     */
    public String uploadImage(InputStream fileInputStream, Attachment fileDetail, String type) {

        // TODO: 11/18/19 Invoke the OSGi serivce. 
        return uploadImageToFileSystem(fileInputStream, fileDetail, type);

    }

    public InputStream downloadImage(String id, String type) {
        // TODO: 11/18/19 Invoke the OSGi Service 
        return downloadImageFromFileSystem(id, type);
    }

    /**
     * Temporary method.
     *
     * @param fileInputStream
     * @param fileDetail
     * @param type
     * @return
     */
    private String uploadImageToFileSystem(InputStream fileInputStream, Attachment fileDetail, String type) {

        String location = null;
        String fileName = UUID.randomUUID().toString();
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + File.separator + "uploads")
                .toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        try {
            Files.copy(fileInputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error while storing the image.");
        }
        if ("idp".equals(type)) {
            location = "i" + "/" + fileName;
        }
        return location;
    }

    private InputStream downloadImageFromFileSystem(String id, String type) {

        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + File.separator + "uploads")
                .toAbsolutePath().normalize();
        String fileName = id;
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("Error while retrieving the image ");
        }
        return inputStream;
    }
}

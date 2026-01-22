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

package org.wso2.carbon.identity.api.server.common.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.Util;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import org.yaml.snakeyaml.inspector.TrustedPrefixesTagInspector;
import org.yaml.snakeyaml.representer.Representer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Utility for serializing and deserializing objects to/from XML, JSON, and YAML formats.
 */
public class FileSerializationUtil {

    private static final Log LOG = LogFactory.getLog(FileSerializationUtil.class);

    /**
     * Serialize an object to file content in the specified format.
     *
     * @param entity   The object to serialize
     * @param fileName Base file name (without extension)
     * @param fileType Media type
     * @param config   Serialization configuration (optional)
     * @param <T>      Type of the entity
     * @return FileContent with serialized data
     * @throws FileSerializationException if serialization fails
     */
    public static <T> FileContent serialize(T entity, String fileName, String fileType,
                                            FileSerializationConfig config) throws FileSerializationException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Serializing entity of type %s to file %s with type %s", 
                    entity.getClass().getSimpleName(), fileName, fileType));
        }
        if (config == null) {
            config = new FileSerializationConfig();
        }

        String mediaType = Util.getMediaType(fileType);
        switch (mediaType) {
            case Constants.MEDIA_TYPE_XML:
                return serializeToXml(entity, fileName, config.getXmlConfig());
            case Constants.MEDIA_TYPE_JSON:
                return serializeToJson(entity, fileName, config.getJsonConfig());
            case Constants.MEDIA_TYPE_YAML:
                return serializeToYaml(entity, fileName, config.getYamlConfig());
            default:
                return handleUnsupportedSerialize(entity, fileName, fileType, config);
        }
    }

    /**
     * Serialize with default config.
     *
     * @param entity   The object to serialize
     * @param fileName Base file name
     * @param fileType Media type
     * @param <T>      Type of the entity
     * @return FileContent with serialized data
     * @throws FileSerializationException if serialization fails
     */
    public static <T> FileContent serialize(T entity, String fileName, String fileType)
            throws FileSerializationException {
        return serialize(entity, fileName, fileType, new FileSerializationConfig());
    }

    /**
     * Deserialize file content to an object of the specified type.
     *
     * @param fileContent The file content to deserialize
     * @param targetClass The target class type
     * @param config      Deserialization configuration (optional)
     * @param <T>         Type of the target class
     * @return Deserialized object
     * @throws FileSerializationException if deserialization fails
     */
    public static <T> T deserialize(FileContent fileContent, Class<T> targetClass,
                                    FileSerializationConfig config) throws FileSerializationException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Deserializing file %s of type %s to class %s",
                    fileContent.getFileName(), fileContent.getFileType(), targetClass.getSimpleName()));
        }
        if (config == null) {
            config = new FileSerializationConfig();
        }

        String mediaType = Util.getMediaType(fileContent.getFileType());
        switch (mediaType) {
            case Constants.MEDIA_TYPE_XML:
                return deserializeFromXml(fileContent, targetClass, config);
            case Constants.MEDIA_TYPE_JSON:
                return deserializeFromJson(fileContent, targetClass, config);
            case Constants.MEDIA_TYPE_YAML:
                return deserializeFromYaml(fileContent, targetClass, config);
            default:
                return handleUnsupportedDeserialize(fileContent, targetClass, config);
        }
    }

    /**
     * Deserialize with default config.
     *
     * @param fileContent The file content to deserialize
     * @param targetClass The target class type
     * @param <T>         Type of the target class
     * @return Deserialized object
     * @throws FileSerializationException if deserialization fails
     */
    public static <T> T deserialize(FileContent fileContent, Class<T> targetClass)
            throws FileSerializationException {
        return deserialize(fileContent, targetClass, new FileSerializationConfig());
    }

    /**
     * Handle unsupported media type during serialization based on config.
     */
    private static <T> FileContent handleUnsupportedSerialize(T entity, String fileName, String fileType,
                                                              FileSerializationConfig config)
            throws FileSerializationException {
        FileSerializationConfig.DefaultFormat defaultFormat = config.getSerializeDefault();

        switch (defaultFormat) {
            case XML:
                LOG.warn(String.format("Unsupported file type %s requested for export. Defaulting to XML.",
                        fileType));
                return serializeToXml(entity, fileName, config.getXmlConfig());
            case JSON:
                LOG.warn(String.format("Unsupported file type %s requested for export. Defaulting to JSON.",
                        fileType));
                return serializeToJson(entity, fileName, config.getJsonConfig());
            case YAML:
                LOG.warn(String.format("Unsupported file type %s requested for export. Defaulting to YAML.",
                        fileType));
                return serializeToYaml(entity, fileName, config.getYamlConfig());
            case ERROR:
            default:
                throw new FileSerializationException(
                        String.format("Unsupported media type: %s. Supported media types are %s, %s, %s",
                                fileType, Constants.MEDIA_TYPE_XML, Constants.MEDIA_TYPE_YAML,
                                Constants.MEDIA_TYPE_JSON),
                        null, fileName, fileType, FileSerializationException.Operation.SERIALIZE);
        }
    }

    /**
     * Handle unsupported media type during deserialization based on config.
     */
    private static <T> T handleUnsupportedDeserialize(FileContent fileContent, Class<T> targetClass,
                                                      FileSerializationConfig config)
            throws FileSerializationException {
        FileSerializationConfig.DefaultFormat defaultFormat = config.getDeserializeDefault();

        switch (defaultFormat) {
            case XML:
                LOG.warn(String.format("Unsupported media type %s for file %s. Defaulting to XML parsing.",
                        fileContent.getFileType(), fileContent.getFileName()));
                return deserializeFromXml(fileContent, targetClass, config);
            case JSON:
                LOG.warn(String.format("Unsupported media type %s for file %s. Defaulting to JSON parsing.",
                        fileContent.getFileType(), fileContent.getFileName()));
                return deserializeFromJson(fileContent, targetClass, config);
            case YAML:
                LOG.warn(String.format("Unsupported media type %s for file %s. Defaulting to YAML parsing.",
                        fileContent.getFileType(), fileContent.getFileName()));
                return deserializeFromYaml(fileContent, targetClass, config);
            case ERROR:
            default:
                throw new FileSerializationException(
                        String.format("Unsupported media type: %s. Supported media types are %s, %s, %s",
                                fileContent.getFileType(), Constants.MEDIA_TYPE_XML, Constants.MEDIA_TYPE_YAML,
                                Constants.MEDIA_TYPE_JSON),
                        null, fileContent.getFileName(), fileContent.getFileType(),
                        FileSerializationException.Operation.DESERIALIZE);
        }
    }

    /**
     * Serialize object to XML format.
     */
    private static <T> FileContent serializeToXml(T entity, String fileName, XmlConfig config)
            throws FileSerializationException {
        StringBuilder fileNameBuilder = new StringBuilder(fileName);
        fileNameBuilder.append(Constants.XML_FILE_EXTENSION);

        try {
            List<Class<?>> classList = new ArrayList<>();
            classList.add(entity.getClass());
            classList.addAll(Arrays.asList(config.getAdditionalJaxbClasses()));
            Class<?>[] classes = classList.toArray(new Class<?>[0]);

            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if (config.getMarshallerCustomizer() != null) {
                config.getMarshallerCustomizer().accept(marshaller);
            }

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(entity, stringWriter);

            return new FileContent(fileNameBuilder.toString(), Constants.MEDIA_TYPE_XML,
                    stringWriter.toString());
        } catch (JAXBException e) {
            throw new FileSerializationException("Failed to serialize to XML", e, fileName,
                    Constants.MEDIA_TYPE_XML, FileSerializationException.Operation.SERIALIZE);
        }
    }

    /**
     * Serialize object to JSON format.
     */
    private static <T> FileContent serializeToJson(T entity, String fileName, JsonConfig config)
            throws FileSerializationException {
        StringBuilder fileNameBuilder = new StringBuilder(fileName);
        fileNameBuilder.append(Constants.JSON_FILE_EXTENSION);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (config.getSubtypes().length > 0) {
                objectMapper.registerSubtypes(config.getSubtypes());
            }

            return new FileContent(fileNameBuilder.toString(), Constants.MEDIA_TYPE_JSON,
                    objectMapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            throw new FileSerializationException("Failed to serialize to JSON", e, fileName,
                    Constants.MEDIA_TYPE_JSON, FileSerializationException.Operation.SERIALIZE);
        }
    }

    /**
     * Serialize object to YAML format.
     */
    private static <T> FileContent serializeToYaml(T entity, String fileName, YamlConfig config)
            throws FileSerializationException {
        StringBuilder fileNameBuilder = new StringBuilder(fileName);
        fileNameBuilder.append(Constants.YAML_FILE_EXTENSION);

        try {
            LoaderOptions loaderOptions = new LoaderOptions();
            Constructor constructor = new Constructor(loaderOptions);
            if (config.getConstructorCustomizer() != null) {
                config.getConstructorCustomizer().accept(constructor);
            }

            DumperOptions dumperOptions = new DumperOptions();
            if (config.getDumperOptionsCustomizer() != null) {
                config.getDumperOptionsCustomizer().accept(dumperOptions);
            }

            Representer representer;
            if (config.getRepresenterFactory() != null) {
                representer = config.getRepresenterFactory().apply(dumperOptions);
            } else {
                representer = new Representer(dumperOptions);
            }
            if (config.getRepresenterCustomizer() != null) {
                config.getRepresenterCustomizer().accept(representer);
            }

            Yaml yaml = new Yaml(constructor, representer);

            return new FileContent(fileNameBuilder.toString(), Constants.MEDIA_TYPE_YAML, yaml.dump(entity));
        } catch (Exception e) {
            throw new FileSerializationException("Failed to serialize to YAML", e, fileName,
                    Constants.MEDIA_TYPE_YAML, FileSerializationException.Operation.SERIALIZE);
        }
    }

    /**
     * Deserialize object from XML format.
     */
    private static <T> T deserializeFromXml(FileContent fileContent, Class<T> targetClass,
                                            FileSerializationConfig config) throws FileSerializationException {
        try {
            List<Class<?>> classList = new ArrayList<>();
            classList.add(targetClass);
            classList.addAll(Arrays.asList(config.getXmlConfig().getAdditionalJaxbClasses()));
            Class<?>[] classes = classList.toArray(new Class<?>[0]);

            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            @SuppressWarnings("unchecked")
            T result = (T) unmarshaller.unmarshal(new StringReader(fileContent.getContent()));
            return result;
        } catch (JAXBException e) {
            throw new FileSerializationException("Failed to deserialize from XML", e,
                    fileContent.getFileName(), Constants.MEDIA_TYPE_XML,
                    FileSerializationException.Operation.DESERIALIZE);
        }
    }

    /**
     * Deserialize object from JSON format.
     */
    private static <T> T deserializeFromJson(FileContent fileContent, Class<T> targetClass,
                                             FileSerializationConfig config) throws FileSerializationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Class<?>[] subtypes = config.getJsonConfig().getSubtypes();
            if (subtypes.length > 0) {
                objectMapper.registerSubtypes(subtypes);
            }

            return objectMapper.readValue(fileContent.getContent(), targetClass);
        } catch (JsonProcessingException e) {
            throw new FileSerializationException("Failed to deserialize from JSON", e,
                    fileContent.getFileName(), Constants.MEDIA_TYPE_JSON,
                    FileSerializationException.Operation.DESERIALIZE);
        }
    }

    /**
     * Deserialize object from YAML format.
     */
    private static <T> T deserializeFromYaml(FileContent fileContent, Class<T> targetClass,
                                             FileSerializationConfig config) throws FileSerializationException {
        try {
            LoaderOptions loaderOptions = new LoaderOptions();

            List<String> trustedTags = new ArrayList<>();
            trustedTags.add(targetClass.getName());
            trustedTags.addAll(config.getYamlConfig().getAdditionalTrustedClassNames());

            TagInspector tagInspector = new TrustedPrefixesTagInspector(trustedTags);
            loaderOptions.setTagInspector(tagInspector);

            Constructor constructor = new Constructor(targetClass, loaderOptions);
            if (config.getYamlConfig().getConstructorCustomizer() != null) {
                config.getYamlConfig().getConstructorCustomizer().accept(constructor);
            }
            Yaml yaml = new Yaml(constructor);

            return yaml.loadAs(fileContent.getContent(), targetClass);
        } catch (Exception e) {
            throw new FileSerializationException("Failed to deserialize from YAML", e,
                    fileContent.getFileName(), Constants.MEDIA_TYPE_YAML,
                    FileSerializationException.Operation.DESERIALIZE);
        }
    }
}

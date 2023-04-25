package org.wso2.carbon.identity.api.server.userstore.v1.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User store configurations model.
 **/
@XmlRootElement(
        name = "UserStoreConfigurations"
)
public class UserStoreConfigurations {

    private String typeName;
    private String typeId;
    private String name;
    private String description;
    private String className;
    private Boolean isLocal;
    private List<Property> properties = new ArrayList<>();

    private List<ClaimAttributeMapping> claimAttributeMappings = null;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addPropertiesItem(Property propertiesItem) {
        this.properties.add(propertiesItem);
    }

    public List<ClaimAttributeMapping> getClaimAttributeMappings() {
        return claimAttributeMappings;
    }

    public void setClaimAttributeMappings(List<ClaimAttributeMapping> claimAttributeMappings) {
        this.claimAttributeMappings = claimAttributeMappings;
    }
}

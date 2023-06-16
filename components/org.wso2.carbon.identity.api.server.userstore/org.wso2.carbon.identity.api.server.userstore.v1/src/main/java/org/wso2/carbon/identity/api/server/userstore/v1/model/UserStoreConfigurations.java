package org.wso2.carbon.identity.api.server.userstore.v1.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User store configurations model.
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(
        name = "UserStoreConfigurations"
)
public class UserStoreConfigurations {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "typeName")
    private String typeName;

    @XmlElement(name = "typeId")
    private String typeId;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "isLocal")
    private Boolean isLocal;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<Property> properties = new ArrayList<>();

    @XmlElementWrapper(name = "claimAttributeMappings")
    @XmlElement(name = "claimAttributeMapping")
    private List<ClaimAttributeMapping> claimAttributeMappings = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

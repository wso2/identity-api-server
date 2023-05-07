package org.wso2.carbon.identity.rest.api.server.claim.management.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimResDTO;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Claim dialect configuration.
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DialectConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimDialectConfiguration extends ClaimDialectResDTO {

    @XmlElementWrapper(name = "claims")
    @XmlElements({
            @XmlElement(name = "ExternalClaimConfiguration", type = ExternalClaimResDTO.class),
            @XmlElement(name = "LocalClaimConfiguration", type = LocalClaimResDTO.class)
    })
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ExternalClaimResDTO.class, name = "ExternalClaim"),
            @JsonSubTypes.Type(value = LocalClaimResDTO.class, name = "LocalClaim")
    })
    private List<ClaimResDTO> claims;

    public ClaimDialectConfiguration() {
        super();
    }

    public ClaimDialectConfiguration(ClaimDialectResDTO claimDialect) {
        this.setDialectURI(claimDialect.getDialectURI());
        this.setId(claimDialect.getId());
        this.setLink(claimDialect.getLink());
        this.claims = new ArrayList<>();
    }

    public void setClaims(List<ClaimResDTO> claims) {

        this.claims = claims;
    }


    public List<ClaimResDTO> getClaims() {

        return claims;
    }

    @JsonIgnore
    public ClaimDialectReqDTO getClaimDialectReqDTO () {
        ClaimDialectReqDTO claimDialectReqDTO = new ClaimDialectReqDTO();
        claimDialectReqDTO.setDialectURI(this.getDialectURI());
        return claimDialectReqDTO;
    }

    @JsonIgnore
    public List<LocalClaimReqDTO> getLocalClaimReqDTOList () {
        List<LocalClaimReqDTO> localClaimReqDTOList = new ArrayList<>();
        for (ClaimResDTO claimResDTO : claims) {
            if (claimResDTO instanceof LocalClaimResDTO) {
                LocalClaimResDTO localClaimResDTO = (LocalClaimResDTO) claimResDTO;
                LocalClaimReqDTO localClaimReqDTO = new LocalClaimReqDTO();
                localClaimReqDTO.setClaimURI(localClaimResDTO.getClaimURI());
                localClaimReqDTO.setDescription(localClaimResDTO.getDescription());
                localClaimReqDTO.setDisplayOrder(localClaimResDTO.getDisplayOrder());
                localClaimReqDTO.setDisplayName(localClaimResDTO.getDisplayName());
                localClaimReqDTO.setReadOnly(localClaimResDTO.getReadOnly());
                localClaimReqDTO.setRegEx(localClaimResDTO.getRegEx());
                localClaimReqDTO.setRequired(localClaimResDTO.getRequired());
                localClaimReqDTO.setSupportedByDefault(localClaimResDTO.getSupportedByDefault());
                localClaimReqDTO.setAttributeMapping(localClaimResDTO.getAttributeMapping());
                localClaimReqDTO.setProperties(localClaimResDTO.getProperties());
                localClaimReqDTOList.add(localClaimReqDTO);
            }
        }
        return localClaimReqDTOList;
    }

    @JsonIgnore
    public List<ExternalClaimReqDTO> getExternalClaimReqDTOList () {
        List<ExternalClaimReqDTO> externalClaimReqDTOList = new ArrayList<>();
        for (ClaimResDTO claimResDTO : claims) {
            if (claimResDTO instanceof ExternalClaimResDTO) {
                ExternalClaimResDTO externalClaimResDTO = (ExternalClaimResDTO) claimResDTO;
                ExternalClaimReqDTO externalClaimReqDTO = new ExternalClaimReqDTO();
                externalClaimReqDTO.setClaimURI(externalClaimResDTO.getClaimURI());
                externalClaimReqDTO.setMappedLocalClaimURI(externalClaimResDTO.getMappedLocalClaimURI());
                externalClaimReqDTOList.add(externalClaimReqDTO);
            }
        }
        return externalClaimReqDTOList;
    }
}


package org.wso2.carbon.identity.api.server.identity.governance.v1;

import org.wso2.carbon.identity.api.server.identity.governance.v1.*;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoriesRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.Error;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface IdentityGovernanceApiService {
      public Response getCategories(Integer limit,  Integer offset,  String filter,  String sort );
      public Response getConnector(String categoryId,  String connectorId );
      public Response getConnectorCategory(String categoryId );
      public Response getConnectorsOfCategory(String categoryId );
      public Response patchConnector(String categoryId,  String connectorId,  ConnectorsPatchReq connectorsPatchReq );
}

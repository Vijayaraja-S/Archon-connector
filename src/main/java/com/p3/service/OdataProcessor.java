package com.p3.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.XMLMetadataRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OdataProcessor {
  private static final String BEARER = "Bearer ";

  public XMLMetadata getXMLMetadata(String accessToken, String metadataUrl) {
    try {
      ODataClient client = ODataClientFactory.getClient();
      URIBuilder uriBuilder = client.newURIBuilder(metadataUrl);
      XMLMetadataRequest xmlMetadataRequest =
          client
              .getRetrieveRequestFactory()
              .getXMLMetadataRequest(String.valueOf(uriBuilder.build()));
      xmlMetadataRequest.addCustomHeader("Authorization", BEARER + accessToken);
      ODataRetrieveResponse<XMLMetadata> response = xmlMetadataRequest.execute();
      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException("Failed to analyze metadata: " + e.getMessage(), e);
    }
  }

  public Long getTableRecordCount(String accessToken, CsdlEntityType csdlEntityType) {
    return 0L;
  }
}

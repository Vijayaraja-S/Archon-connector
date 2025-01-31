package com.p3.service.pre_analysis.d365;

import static com.p3.utils.URLConstants.*;

import com.p3.exception.OdataProcessingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataRawRequest;
import org.apache.olingo.client.api.communication.request.retrieve.XMLMetadataRequest;
import org.apache.olingo.client.api.communication.response.ODataRawResponse;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.client.core.ODataClientFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OdataProcessor {

  private static final String BEARER = "Bearer ";
  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final ODataClient client;

  public OdataProcessor() {
    this.client = ODataClientFactory.getClient();
  }

  public XMLMetadata getXMLMetadata(String accessToken, String metadataUrl) {
    log.info("Fetching XML metadata from: {}", metadataUrl);

    try {
      URI uri = client.newURIBuilder(metadataUrl).build();
      XMLMetadataRequest metadataRequest =
          client.getRetrieveRequestFactory().getXMLMetadataRequest(uri.toString());

      metadataRequest.addCustomHeader(AUTHORIZATION_HEADER, BEARER + accessToken);

      ODataRetrieveResponse<XMLMetadata> response = metadataRequest.execute();

      if (response.getStatusCode() == 200) {
        log.info("Successfully fetched XML metadata.");
        return response.getBody();
      } else {
        String errorMsg = "Failed to fetch metadata. HTTP Status: " + response.getStatusCode();
        log.error(errorMsg);
        throw new OdataProcessingException(errorMsg);
      }
    } catch (Exception e) {
      String errorMsg = "Error occurred while fetching XML metadata from " + metadataUrl;
      log.error(errorMsg, e);
      throw new OdataProcessingException(errorMsg, e);
    }
  }

  public Long getRowCount(String accessToken, String entitySetUrl) {
    if (entitySetUrl==null || entitySetUrl.isEmpty()) {
      return 0L;
    }
    String countQueryUrl = entitySetUrl + SEPARATOR + COUNT;

    try {
      ODataRawResponse response = executeODataRequest(countQueryUrl, accessToken);

      if (response.getStatusCode() == 200) {
        String responseBody = IOUtils.toString(response.getRawResponse(), StandardCharsets.UTF_8);
        return parseRowCount(responseBody);
      } else {
        String errorMsg = "Failed to get row count. HTTP Status: " + response.getStatusCode();
        log.error(errorMsg);
        return 0L;
      }
    } catch (Exception e) {
      String errorMsg = "Error occurred while fetching row count for URL: " + countQueryUrl;
      log.error(errorMsg, e);
      return 0L;
    }
  }

  private ODataRawResponse executeODataRequest(String url, String accessToken) {
    ODataRawRequest request = client.getRetrieveRequestFactory().getRawRequest(URI.create(url));
    request.addCustomHeader(AUTHORIZATION_HEADER, BEARER + accessToken);

    try {
      log.debug("Executing OData request for URL: {}", url);
      return request.execute();
    } catch (Exception e) {
      String errorMsg = "Error executing OData request for URL: " + url;
      log.error(errorMsg, e);
      throw new OdataProcessingException(errorMsg, e);
    }
  }

  private Long parseRowCount(String responseBody) {
    if (responseBody == null || responseBody.trim().isEmpty()) {
      String errorMsg = "Response body is null or empty.";
      log.error(errorMsg);
      throw new OdataProcessingException(errorMsg);
    }
    try {
      long rowCount = Long.parseLong(responseBody.trim());
      if (rowCount < 0) {
        String errorMsg = "Row count cannot be negative: " + responseBody;
        log.error(errorMsg);
        throw new OdataProcessingException(errorMsg);
      }
      return rowCount;
    } catch (NumberFormatException e) {
      String errorMsg = "Failed to parse row count from response body: " + responseBody;
      log.error(errorMsg, e);
      throw new OdataProcessingException(errorMsg, e);
    }
  }

}

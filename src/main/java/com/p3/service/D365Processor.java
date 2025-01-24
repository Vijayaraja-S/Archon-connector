package com.p3.service;

import com.p3.beans.D365ConnectionInfo;
import com.p3.beans.metadata.ETLMetaData;
import com.p3.beans.metadata.ETLSchemaMetaData;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.XMLMetadataRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.edm.provider.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {

  private static final String BEARER = "Bearer ";
  private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = getAccessToken(info);
    String metadataUrl = info.getResourceUrl() + "/api/data/v9.0";
    XMLMetadata xmlMetadata = analyzeMetadata(accessToken, metadataUrl);
    processXmlMetadata(xmlMetadata, info);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata, D365ConnectionInfo info) {
    List<CsdlSchema> schemas = xmlMetadata.getSchemas();
    ETLMetaData etlMetaData =
        ETLMetaData.builder()
            .type("D365")
            .databaseName("dynamic-365-etl")
            .connectionInfo(info)
            .isFullSelected(true)
            .schemaCount(schemas.size())
            .build();
    etlMetaData.setSchemaMetaDataList(
        schemas.stream()
            .map(schema -> ETLSchemaMetaData.builder()
                    .name(schema.getNamespace())
                    .aliasName(schema.getAlias())
                    .archonFormattedName(schema.getNamespace().toUpperCase() + "." + schema.getAlias())
                    .build())
            .collect(Collectors.toList()));
  }

  private BufferedReader getBufferedReader(String authorityUrl, String parameters)
      throws IOException {
    URL url = new URL(authorityUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", CONTENT_TYPE);
    connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
    connection.setDoOutput(true);
    connection.connect();

    try (BufferedWriter out =
        new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
      out.write(parameters);
    }

    return new BufferedReader(new InputStreamReader(connection.getInputStream()));
  }

  private XMLMetadata analyzeMetadata(String accessToken, String metadataUrl) {
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

  private String getAccessToken(D365ConnectionInfo info) throws IOException {
    String parameters = buildUrlParameters(info);
    String authorityUrl =
        String.format("https://login.microsoftonline.com/%s/oauth2/token", info.getTenantId());
    BufferedReader rd = getBufferedReader(authorityUrl, parameters);

    StringBuilder response = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      response.append(line);
    }
    rd.close();

    return parseAccessToken(response.toString());
  }

  private String buildUrlParameters(D365ConnectionInfo info) {
    return "resource="
        + URLEncoder.encode(info.getResourceUrl(), StandardCharsets.UTF_8)
        + "&client_id="
        + URLEncoder.encode(info.getClientId(), StandardCharsets.UTF_8)
        + "&client_secret="
        + URLEncoder.encode(info.getClientSecret(), StandardCharsets.UTF_8)
        + "&grant_type="
        + URLEncoder.encode(info.getGrantType(), StandardCharsets.UTF_8);
  }

  private String parseAccessToken(String jsonResponse) {
    JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonResponse);
    if (jsonObject != null && jsonObject.containsKey("access_token")) {
      return jsonObject.get("access_token").toString();
    } else {
      throw new RuntimeException("Invalid response: access_token not found");
    }
  }
}

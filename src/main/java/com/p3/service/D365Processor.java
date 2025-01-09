package com.p3.service;

import com.p3.beans.D365ConnectionInfo;
import com.p3.beans.metadata.ETLMetaData;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.XMLMetadataRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = GetAccessToken(info);
    String metadataUrl = info.getResourceUrl() + "/api/data/v9.0";
    XMLMetadata xmlMetadata = analyzeMetadata(accessToken, metadataUrl);
    processXmlMetadata(xmlMetadata);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata) {
    ETLMetaData etlMetaData = ETLMetaData.builder().build();

  }

  private BufferedReader getBufferedReader(String AUTHORITY, String parameters) throws IOException {
    URL url;
    HttpURLConnection connection;
    url = new URL(AUTHORITY);
    connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
    connection.setDoOutput(true);
    connection.connect();

    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
    out.write(parameters);
    out.close();

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
      xmlMetadataRequest.addCustomHeader("Authorization", "Bearer " + accessToken);
      ODataRetrieveResponse<XMLMetadata> response = xmlMetadataRequest.execute();
      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private String GetAccessToken(D365ConnectionInfo info) throws IOException {
    String parameters =
        "resource="
            + URLEncoder.encode(info.getResourceUrl(), StandardCharsets.UTF_8)
            + "&client_id="
            + URLEncoder.encode(info.getClientId(), StandardCharsets.UTF_8)
            + "&client_secret="
            + URLEncoder.encode(info.getClientSecret(), StandardCharsets.UTF_8)
            + "&grant_type=client_credentials";

    String AUTHORITY =
        String.format("https://login.microsoftonline.com/%s/oauth2/token", info.getTenantId());
    BufferedReader rd = getBufferedReader(AUTHORITY, parameters);
    String line;
    StringBuilder response = new StringBuilder();
    while ((line = rd.readLine()) != null) {
      response.append(line);
    }
    rd.close();

    Object jResponse;
    jResponse = JSONValue.parse(response.toString());
    JSONObject jObject = (JSONObject) jResponse;
    return jObject.get("access_token").toString();
  }
}

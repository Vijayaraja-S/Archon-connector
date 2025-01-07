package com.p3.d365.service;

import com.p3.d365.beans.D365ConnectionInfo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataRetrieveRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.edm.Edm;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = GetAccessToken(info);
    String metadataUrl = info.getResourceUrl() + "/api/data/v9.0/$metadata";
    processRegularRequest(accessToken, metadataUrl);
    return "metadata processed";
  }

  private void processRegularRequest(String accessToken, String metadataUrl) throws IOException {
    URL url = new URL(metadataUrl);

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    connection.setRequestProperty("Accept", "application/xml");

    int status = connection.getResponseCode();
    if (status != HttpURLConnection.HTTP_OK) {
      throw new IOException("HTTP request failed with status code " + status);
    }

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      String xmlResponse = response.toString();
      System.out.println("Received metadata response: ");
      System.out.println(xmlResponse);
    } finally {
      connection.disconnect();
    }
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

  private void analyzeMetadata(String accessToken, String metadataUrl) {
    try {
      ODataClient client = ODataClientFactory.getClient();
      URIBuilder uriBuilder = client.newURIBuilder(metadataUrl);

      ODataRetrieveRequest<Edm> metadataRequest =
          client.getRetrieveRequestFactory().getMetadataRequest(String.valueOf(uriBuilder.build()));
      metadataRequest.addCustomHeader("Authorization", "Bearer " + accessToken);

      ODataRetrieveResponse<Edm> response = metadataRequest.execute();

      Edm edm = response.getBody();

      System.out.println("Entity Sets:");
      edm.getSchemas()
          .forEach(
              schema -> {
                schema
                    .getEntityContainer()
                    .getEntitySets()
                    .forEach(
                        entitySet -> {
                          System.out.println(" - " + entitySet.getName());
                          entitySet
                              .getEntityType()
                              .getPropertyNames()
                              .forEach(
                                  propertyName -> {
                                    System.out.println("   Property: " + propertyName);
                                  });
                        });
              });

    } catch (Exception e) {

      String message = e.getMessage();
      throw new RuntimeException(message);
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

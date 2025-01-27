package com.p3.service;

import com.p3.beans.D365ConnectionInfo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectionManager {
  private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

  public String getAccessToken(D365ConnectionInfo info) throws IOException {
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
}

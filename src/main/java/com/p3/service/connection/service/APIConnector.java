package com.p3.service.connection.service;

import com.p3.beans.request.DatasourceProfileRequestDTO;
import com.p3.service.connection.service.api.AbstractAPIConnector;
import com.p3.service.connection.service.api.D365Connection;

public class APIConnector implements ConnectorService<String> {

  @Override
  public boolean testConnection(DatasourceProfileRequestDTO ds) {
    AbstractAPIConnector apiConnectionService = getAPIConnectionService(ds);
    return apiConnectionService.testConnection(ds);
  }

  private AbstractAPIConnector getAPIConnectionService(
      DatasourceProfileRequestDTO requestDTO) {
    if (requestDTO.getCategory().equals("D365")) {
      return new D365Connection();
    }
    throw new RuntimeException("Invalid category");
  }

  @Override
  public String getConnection(DatasourceProfileRequestDTO ds) {
    AbstractAPIConnector apiConnectionService = getAPIConnectionService(ds);
    return apiConnectionService.getConnection(ds);
  }
}

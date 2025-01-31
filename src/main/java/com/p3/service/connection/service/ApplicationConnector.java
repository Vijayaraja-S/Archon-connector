package com.p3.service.connection.service;

import com.p3.beans.request.DatasourceProfileRequestDTO;

public class ApplicationConnector implements ConnectorService<String> {
 
  @Override
  public boolean testConnection( DatasourceProfileRequestDTO ds) {
    return false;
  }


  @Override
  public String getConnection( DatasourceProfileRequestDTO ds) {
    return null;
  }
}

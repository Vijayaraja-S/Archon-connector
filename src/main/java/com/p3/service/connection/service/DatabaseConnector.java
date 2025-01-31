package com.p3.service.connection.service;

import com.p3.beans.request.DatasourceProfileRequestDTO;
import java.sql.Connection;

public class DatabaseConnector implements ConnectorService<Connection> {
  
  @Override
  public boolean testConnection( DatasourceProfileRequestDTO ds) {
    return false;
  }


  @Override
  public Connection getConnection( DatasourceProfileRequestDTO ds) {
    return null;
  }
}

package com.p3.service.connection;

import com.p3.beans.request.DatasourceProfileRequestDTO;
import com.p3.service.connection.service.APIConnector;
import com.p3.service.connection.service.ConnectorService;
import com.p3.service.connection.service.DatabaseConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectionManagerService {

  public Boolean testConnection(DatasourceProfileRequestDTO ds) {
    ConnectorService<?> connectionManager = getConnectionManager(ds);
    return connectionManager.testConnection(ds);
  }

  public Object getConnection(DatasourceProfileRequestDTO ds) {
    ConnectorService<?> connectionManager = getConnectionManager(ds);
    return connectionManager.getConnection(ds);
  }

  private ConnectorService<?> getConnectionManager(DatasourceProfileRequestDTO ds) {
    switch (ds.getCategory()) {
      case "RDBMS":
        return new DatabaseConnector();
      case "D365":
        return new APIConnector();
    }
    throw new IllegalArgumentException("Invalid category");
  }
}

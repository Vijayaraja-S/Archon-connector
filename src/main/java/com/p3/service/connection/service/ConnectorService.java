package com.p3.service.connection.service;

import com.p3.beans.request.DatasourceProfileRequestDTO;

public interface ConnectorService<T> {
  boolean testConnection(DatasourceProfileRequestDTO ds);

  T getConnection(DatasourceProfileRequestDTO ds);
}

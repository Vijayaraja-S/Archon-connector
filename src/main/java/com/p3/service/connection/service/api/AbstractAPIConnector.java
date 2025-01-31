package com.p3.service.connection.service.api;

import com.p3.beans.request.DatasourceProfileRequestDTO;

public abstract class AbstractAPIConnector {

 abstract public boolean testConnection(DatasourceProfileRequestDTO ds);

 abstract public String  getConnection(DatasourceProfileRequestDTO ds);
}

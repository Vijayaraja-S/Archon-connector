package com.p3.service.connection.service.database;

import java.sql.Connection;

public abstract class AbstractDataBaseConnector {

  public abstract boolean testConnection();

  public abstract Connection getConnection();
}

package com.p3.service.connection.service.flatfile;

public abstract class AbstractFlatFileConnector {

  public abstract boolean testConnection();

  public abstract String getConnection();
}

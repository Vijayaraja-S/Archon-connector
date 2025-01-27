package com.p3.beans.pre_analysis.identifiers;

import java.io.Serializable;

public enum ArchonDataCategory implements Serializable {
  STRING(1.00, "STRING", "String type"),
  NUMBER(2.00, "INTEGER", "Integer Value"),
  DECIMAL(3.00, "DECIMAL", "Decimal Value"),
  BOOLEAN(4.00, "BOOLEAN", "Boolean Value"),
  DATE(5.00, "DATE", "Date type"),
  TIME(5.01, "TIME", "Time type"),
  DATETIME(5.02, "DATE TIME", "Date & Time type"),
  CLOB(6.00, "CLOB", "Character LOB type"),
  BLOB(7.00, "BLOB", "Binary LOB type"),
  FILE_BLOB(7.01, "FILE BLOB", "Download File path String type"),
  SQLXML(6.01, "SQLXML", "sql xml as clob"),
  DOUBLE(3.01, "DOUBLE", "Double Value"),
  ARRAY(8, "ARRAY", "array list"),
  STRUCT(9, "STRUCT", "collection of documents"),
  BIGINT(10, "BIGINT", "BIGINT"),
  INTEGER(11, "INTEGER", "INTEGER"),
  MAP(12, "MAP", "map of documents");
  private final double id;
  private final String description;
  private final String value;

  ArchonDataCategory(double id, String value, String description) {
    this.id = id;
    this.value = value;
    this.description = description;
  }

  public static ArchonDataCategory getTypeCategory(String type, int size, int decimalDigits) {
    switch (type.toUpperCase()) {
      case "DATE":
      case "YEAR":
        return ArchonDataCategory.DATE;
      case "TIME":
        return ArchonDataCategory.TIME;
      case "DATETIME":
      case "DATETIME2":
      case "TIMESTAMP":
      case "DATE TIME":
      case "SMALLDATETIME":
      // ORACLE TIMESTAMP(fractional_seconds_precision) RANGE 0 TO 9
      case "TIMESTAMP(0)":
      case "TIMESTAMP(1)":
      case "TIMESTAMP(2)":
      case "TIMESTAMP(3)":
      case "TIMESTAMP(4)":
      case "TIMESTAMP(5)":
      case "TIMESTAMP(6)":
      case "TIMESTAMP(7)":
      case "TIMESTAMP(8)":
      case "TIMESTAMP(9)":
      case "DATETIME YEAR TO YEAR":
      case "DATETIME YEAR TO MONTH":
      case "DATETIME YEAR TO DAY":
      case "DATETIME YEAR TO HOUR":
      case "DATETIME YEAR TO MINUTE":
      case "DATETIME YEAR TO SECOND":
      case "DATETIME YEAR TO FRACTION(1)":
      case "DATETIME YEAR TO FRACTION(2)":
      case "DATETIME YEAR TO FRACTION(3)":
      case "DATETIME YEAR TO FRACTION(4)":
      case "DATETIME YEAR TO FRACTION(5)":
      case "DATETIME MONTH TO MONTH":
      case "DATETIME MONTH TO DAY":
      case "DATETIME MONTH TO HOUR":
      case "DATETIME MONTH TO MINUTE":
      case "DATETIME MONTH TO SECOND":
      case "DATETIME MONTH TO FRACTION(1)":
      case "DATETIME MONTH TO FRACTION(2)":
      case "DATETIME MONTH TO FRACTION(3)":
      case "DATETIME MONTH TO FRACTION(4)":
      case "DATETIME MONTH TO FRACTION(5)":
      case "DATETIME DAY TO DAY":
      case "DATETIME DAY TO HOUR":
      case "DATETIME DAY TO MINUTE":
      case "DATETIME DAY TO SECOND":
      case "DATETIME DAY TO FRACTION(1)":
      case "DATETIME DAY TO FRACTION(2)":
      case "DATETIME DAY TO FRACTION(3)":
      case "DATETIME DAY TO FRACTION(4)":
      case "DATETIME DAY TO FRACTION(5)":
      // HOUR
      case "DATETIME HOUR TO FRACTION(1)":
      case "DATETIME HOUR TO FRACTION(2)":
      case "DATETIME HOUR TO FRACTION(3)":
      case "DATETIME HOUR TO FRACTION(4)":
      case "DATETIME HOUR TO FRACTION(5)":
      // MINUTE
      case "DATETIME MINUTE TO FRACTION(1)":
      case "DATETIME MINUTE TO FRACTION(2)":
      case "DATETIME MINUTE TO FRACTION(3)":
      case "DATETIME MINUTE TO FRACTION(4)":
      case "DATETIME MINUTE TO FRACTION(5)":
      // SECOND
      case "DATETIME SECOND TO FRACTION(1)":
      case "DATETIME SECOND TO FRACTION(2)":
      case "DATETIME SECOND TO FRACTION(3)":
      case "DATETIME SECOND TO FRACTION(4)":
      case "DATETIME SECOND TO FRACTION(5)":
      // TIMES
      case "DATETIME HOUR TO HOUR":
      case "DATETIME HOUR TO MINUTE":
      case "DATETIME HOUR TO SECOND":
      case "DATETIME MINUTE TO MINUTE":
      case "DATETIME MINUTE TO SECOND":
      case "DATETIME SECOND TO SECOND":
        return ArchonDataCategory.DATETIME;
      case "BLOB":
      case "TINY BLOB":
      case "TINYBLOB":
      case "MEDIUMBLOB":
      case "MEDIUM BLOB":
      case "LONG BLOB":
      case "VAR BINARY":
      case "NVAR BINARY":
      case "N VAR BINARY":
      case "LONG VARBINARY":
      case "LONGRAW":
      case "PICTURE":
      case "BINARY":
      case "LONGBLOB":
      case "IMAGE":
      case "IMAGES":
      case "PHOTO":
      case "SHORTBLOB":
      case "VARBINARY":
      case "LONGVARBINARY":
      case "NBINARY":
      case "NVARBINARY":
      case "NLONGVARBINARY":
      case "RAW":
      case "LONGNVARBINARY":
      case "LONG NVAR BINARY":
      case "LONG N VAR BINARY":
      case "BYTEA":
      case "VARCHAR () FOR BIT DATA":
      case "VARCHAR FOR BIT DATA":
      case "CHAR () FOR BIT DATA":
      case "CHAR FOR BIT DATA":
      case "LONG VARCHAR FOR BIT DATA":
      case "BYTE":
        return ArchonDataCategory.BLOB;
      case "CLOB":
      case "NCLOB":
        return ArchonDataCategory.CLOB;
      case "XML":
        return ArchonDataCategory.SQLXML;
      case "BOOLEAN":
      case "BIT":
      case "BOOL":
        return ArchonDataCategory.BOOLEAN;
      case "INT":
      case "INTEGER":
      case "AUTONUMBER":
      case "SMALLINT":
      case "BIGINT":
      case "TINYINT":
      case "NUMBER2":
      case "NUMBER":
      case "SERIAL":
        if (size > 8) {
          return ArchonDataCategory.BIGINT;
        } else {
          return ArchonDataCategory.INTEGER;
        }
      case "NUMERIC":
        if (decimalDigits > 0) {
          return ArchonDataCategory.DOUBLE;
        } else if (size > 8) {
          return ArchonDataCategory.BIGINT;
        } else {
          return ArchonDataCategory.INTEGER;
        }
      case "REAL":
      case "INT IDENTITY":
      case "LONG":
      case "TINYINT UNSIGNED":
      case "SMALLINT UNSIGNED":
      case "INT2":
      case "INT4":
      case "INT8":
      case "INT16":
      case "INT32":
      case "INT64":
      case "MEDIUMINT":
      case "MEDIUMINT UNSIGNED":
      case "INT UNSIGNED":
      case "SERIAL8":
        return ArchonDataCategory.NUMBER;
      case "MONEY":
      case "DEC":
      case "DECIMAL":
      case "FLOAT":
      case "DOUBLE":
      case "SMALLMONEY":
      case "DECIMAL128":
      case "SMALLFLOAT":
        return ArchonDataCategory.DECIMAL;
      case "TEXT":
      case "NTEXT":
      case "CHAR":
      case "NCHAR":
      case "VARCHAR":
      case "NVARCHAR":
      case "LONGNVARCHAR":
      case "LONGVARCHAR":
      case "VARCHAR2":
      case "LONG RAW":
      case "CHARACTER":
      case "CHARACTER VARYING":
      case "BINARY VARYING":
      case "INTERVAL":
      case "TIMESTAMP WITH LOCAL TIME ZONE":
      case "TIMESTAMP WITH TIME ZONE":
      case "DATETIMEOFFSET":
      case "UNIQUEIDENTIFIER":
      case "ENUM":
      case "STRING":
      case "SET":
      case "GEOMETRY":
      case "VARIANT":
      case "OBJECTID":
      case "DBREF":
      case "BINARY_DATA":
      case "NULL":
      case "BSON":
      case "JSON":
      case "LVARCHAR":
        return ArchonDataCategory.STRING;
      case "ARRAY":
        return ArchonDataCategory.ARRAY;
      case "STRUCT":
        return ArchonDataCategory.STRUCT;
      case "MAP":
        return ArchonDataCategory.MAP;
      default:
        return ArchonDataCategory.STRING;
    }
  }
}

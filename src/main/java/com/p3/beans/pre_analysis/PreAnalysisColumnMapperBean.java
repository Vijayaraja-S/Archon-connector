package com.p3.beans.pre_analysis;

import com.p3.beans.pre_analysis.identifiers.ArchonDataCategory;
import com.p3.beans.pre_analysis.identifiers.ColumnType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PreAnalysisColumnMapperBean {
  private String columnName;
  private String archonFormattedColumnName;
  private String dataType;
  private Integer ordinalPosition;
  // todo needs to be discussed(for mongo)
  private String cardinality;
  private ColumnType columnType;
  private ArchonDataCategory archonColumntype;
  private String keywordColumnName;
  private String description;
  private String columnSize;
  private String qualifiedName;
  private String tableId;
  private String tableName;
  private String schemaId;
  private String schemaName;
  private String datasourceId;
  private String databaseName;
  private Boolean indexed;
  private Boolean isSelected = true; // will be used by LN/FF/VSAM ....

  /*D365*/
  private boolean isCollection;
  private String mimeType;
  private String mapping;
  private String defaultValue;
  private boolean nullable = true;
  private Integer maxLength;
  private Integer precision;
  private Integer scale;
  private boolean unicode = true;
  private String srid;
  private String annotations;
}

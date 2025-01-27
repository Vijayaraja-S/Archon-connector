package com.p3.beans.pre_analysis;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PreAnalysisTableMapperBean {
  private String tableName;
  private String archonFormattedTableName; // must be in caplocks
  private String keywordTableName;
  private String type; // can be a VIEW / TABLE
  private String schemaId;
  private String schemaName;
  private String datasourceId;
  private String databaseName;
  private String tableDescription;
  private Integer columnCount;
  private Long rowCount;
  private Boolean containsStructured = false;
  private Boolean containsBlob = false;
  private Boolean containsClob = false;
  private Boolean isRelationship = false;
  private List<PreAnalysisColumnMapperBean> columnList;
  private List<PreAnalysisRelationMapperBean> relations; // relations per table...
  private Set<String> relatedTable;
}

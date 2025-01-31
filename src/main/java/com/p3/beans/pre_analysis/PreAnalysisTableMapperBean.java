package com.p3.beans.pre_analysis;

import com.p3.beans.pre_analysis.d365.PreAnalysisActionMapperBean;
import com.p3.beans.pre_analysis.d365.PreAnalysisFunctionMapperBean;
import com.p3.beans.pre_analysis.d365.RelationDetails;
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
  private List<PreAnalysisColumnMapperBean> columnList;// relations per table...
  private Set<String> relatedTable;
  /**
   * D365
    */
  private String  entitySetUrl;
  private List<RelationDetails> relationDetails;
  private List<PreAnalysisActionMapperBean> actionList;
  private List<PreAnalysisFunctionMapperBean> functionList;
}

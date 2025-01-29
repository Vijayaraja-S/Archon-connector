package com.p3.beans.pre_analysis;

import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PreAnalysisSchemaMapperBean {
  private String schemaName;
  private String archonFormattedSchemaName;
  private String description;
  private String datasourceId;
  private String databaseName;
  private Integer tableCount;
  private List<PreAnalysisTableMapperBean> tableList;
}

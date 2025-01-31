package com.p3.beans.pre_analysis;

import java.util.List;

import com.p3.beans.pre_analysis.d365.PreAnalysisComplexTypeMapperBean;
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
  private List<PreAnalysisComplexTypeMapperBean> complexTypes;
}

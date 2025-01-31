package com.p3.beans.pre_analysis.d365;

import com.p3.beans.pre_analysis.PreAnalysisColumnMapperBean;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PreAnalysisComplexTypeMapperBean {
  private String complexTypeName;
  private String schemaId;
  private List<PreAnalysisColumnMapperBean> properties;
}

package com.p3.beans.pre_analysis.d365;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PreAnalysisActionMapperBean {
  private String actionName;
  private boolean isBound;
  private String boundEntity;
  private List<String> parameters;
  private String returnType;
}

package com.p3.beans.pre_analysis;

import com.p3.beans.pre_analysis.identifiers.JoinType;
import lombok.*;

import java.util.List;

/**
 * @author Syed Sirajuddin..❤
 **/
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PreAnalysisRelationMapperBean{
    private String datasourceId;
    private String schemaId;
    private String joinName;
    private String cardinality;
    private JoinType joinType;//USR,SYS
    private Integer hashValue;
    private String sourceTable;
    private String targetTable;
    private List<RDBMSRelationsMapperBean> relationsList;
}

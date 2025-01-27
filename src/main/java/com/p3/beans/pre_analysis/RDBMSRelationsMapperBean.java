package com.p3.beans.pre_analysis;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RDBMSRelationsMapperBean {
    private PreAnalysisColumnMapperBean source;
    private PreAnalysisColumnMapperBean target;
}

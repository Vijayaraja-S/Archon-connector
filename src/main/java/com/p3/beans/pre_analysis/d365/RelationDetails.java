package com.p3.beans.pre_analysis.d365;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RelationDetails {
    private String pkTableName;
    private String fkTableName;
    private String relationName;
    private String partnerName;
    private Boolean isNullAble;

    private Boolean isCollection;

    //composite
    private Boolean isComposite = Boolean.FALSE;
    private List<String> fkColumnName;
    private List<String> pkColumnName;
}

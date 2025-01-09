package com.p3.beans.metadata;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLColumnMetaData {
    private String name;
    private String archonFormattedName;
    private Integer ordinal;
    private String description;
    private String type;
    private String archonFormattedType;
    private Integer typeLength;
    private Integer precision;
    private Integer radix;
    private Boolean isPrimaryKey;
    private Boolean isPartPrimaryKey;
    private String index;
    private Long nullRecordCount;
    private Long distinctRecordCount;

    private Boolean isLengthUniform;
    private Boolean isAllNumeric;
    private String highFrequencyCharData;
    private Integer whiteSpaceCount;
    private Object minValue;
    private Object maxValue;
    @Builder.Default
    private List<Character> specialCharacters = new ArrayList<>();
}

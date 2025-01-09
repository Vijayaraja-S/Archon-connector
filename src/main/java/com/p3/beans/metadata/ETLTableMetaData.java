package com.p3.beans.metadata;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLTableMetaData {
    private String name;
    private String archonFormattedName;
    private String description;
    private Long columnCount;
    private Long totalRecordCount;
    private Integer relationshipCardinality;
    private Integer tableCardinality;
    private Double sizeInMB;
    @Builder.Default
    private List<String> tagValueList = new ArrayList<>();
    @Builder.Default
    private List<String> filePaths = new ArrayList<>();
    @Builder.Default
    private List<ETLColumnMetaData> etlColumnMetaData = new ArrayList<>();
    @Builder.Default
    private List<ETLRelationshipMetaData> etlRelationshipMetaData = new ArrayList<>();
}

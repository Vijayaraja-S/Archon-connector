package com.p3.beans.metadata;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLSchemaMetaData {
    private String name;
    private String archonFormattedName;
    private Integer tableCount;
    private Double totalSizeInMB;
    private String description;

    //FF
    private Long totalFileCount;

    @Builder.Default
    private List<ETLTableMetaData> tableMetaDataList = new ArrayList<>();
}

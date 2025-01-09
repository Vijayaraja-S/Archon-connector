package com.p3.beans.metadata;

import java.util.ArrayList;
import java.util.List;

import com.p3.beans.D365ConnectionInfo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLMetaData {
    private String type;
    private String databaseName;
    private Boolean isFullSelected;
    private D365ConnectionInfo connectionInfo;
    private Integer schemaCount;
    @Builder.Default
    private List<ETLSchemaMetaData> schemaMetaDataList = new ArrayList<>();
}

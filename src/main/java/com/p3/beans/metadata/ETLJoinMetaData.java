package com.p3.beans.metadata;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLJoinMetaData {
    private Boolean isPkTable;
    private String pkSchema;
    private String pkTable;
    private String pkColumn;
    private String fkSchema;
    private String fkTable;
    private String fkColumn;
}

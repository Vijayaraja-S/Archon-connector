package com.p3.beans.metadata;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ETLRelationshipMetaData {
    public String name;
    public String type;
    @Builder.Default
    private List<ETLJoinMetaData> etlJoinMetaData = new ArrayList<>();
}

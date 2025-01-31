package com.p3.beans.request;

import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DatasourceAdvanceFilterRequestDTO {

    private String profileName;
    private String category;
    private boolean listAnalyzerDsProfiles;
    private Long fromDate;
    private Long toDate;
    private List<String> roles;
    private String filter;
    private Boolean inUse;
    private String order;
    @Builder.Default
    private Boolean isCreateWorkspace=false;
}

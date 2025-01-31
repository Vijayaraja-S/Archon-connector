package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UniverseDBConnectionInfoDTO {
    @NotNull(message = "host is mandatory")
    private String host;
    @NotNull(message = "port is mandatory")
    private Integer port;
    @NotNull(message = "databaseName is mandatory")
    private String databaseName;
    @NotNull(message = "columnLimit is mandatory")
    private Integer columnLimit;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO ConnectionCredentials;
    private Map<String,String> connectionMap;
    private String tempFilePath;
}

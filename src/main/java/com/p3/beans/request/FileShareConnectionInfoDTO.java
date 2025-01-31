package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileShareConnectionInfoDTO {
    private String host;
    private Integer port;
    private String path;
    private String schemaName;
    private String tableName;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseCredentials;
}

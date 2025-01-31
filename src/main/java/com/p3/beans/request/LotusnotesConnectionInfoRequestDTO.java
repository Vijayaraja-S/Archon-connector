package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LotusnotesConnectionInfoRequestDTO {

    private String iorString;
    private String host;
    private String port;
    private String databaseName;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseCredentials;
}

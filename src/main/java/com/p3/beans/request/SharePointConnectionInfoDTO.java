package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SharePointConnectionInfoDTO {
    private String host;
    private String databaseId;
    @JsonProperty(required = false)
    private String hostUrlWithoutProtocol;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO ConnectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseConnectionCredentials;
}

package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Syed Sirajuddin.
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RDBMSConnectionInfoDTO {
    @NotNull(message = "host is mandatory")
    private String host;
    @NotNull(message = "port is mandatory")
    private Integer port;
    @NotNull(message = "connection type is mandatory")
    private String type;
    @NotNull(message = "databaseName is mandatory")
    private String databaseName;
    @JsonProperty(required = false)
    private List<String> schemaList;
    private List<String> objectTypes;
    private Boolean isRecordCountAllowed = true;
    private Boolean isSslRequire = false;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO ConnectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseConnectionCredentials;
    @JsonProperty(required = false)
    private Map<String, List<String>> tableList;
    @Builder.Default
    private Integer sqlServerVersionNo = 2009;
    private String informixServerName;
}

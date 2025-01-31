package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Syed Sirajuddin.
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MongoConnectionInfoDTO {
    @NotNull(message = "host is mandatory")
    private String host;
    @NotNull(message = "port is mandatory")
    private Integer port;
    @NotNull(message = "databaseName is mandatory")
    private String databaseName;
    private Boolean isRecordCountAllowed = true;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;
}

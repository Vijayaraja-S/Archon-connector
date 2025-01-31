/**
 * @author nandishamm
 * @created 14/09/20
 */
package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author nandishamm
 * @created 14/09/20
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VsamConnectionInfoRequestDTO {
    private String host;
    private Integer port = 21;
    private String filesPath;
    private String copybookPath;
    @Builder.Default
    private String characterEncoding = "UTF-8";
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseCredentials;
}

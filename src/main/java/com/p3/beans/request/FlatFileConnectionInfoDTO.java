package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * Created by syedsirajuddin..❤
 * on 13/09/20 7:16 PM
 **/
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FlatFileConnectionInfoDTO {

    private Boolean isHeaderAvailable = true;
    private Boolean isDelimited = true;
    @Builder.Default
    private String characterEncoding = "UTF-8";
    @JsonProperty(required = false)
    private String delimiter;

    @NotNull(message = "host is mandatory")
    private String host;
    private Integer port = 21;
    @NotNull(message = "filePath is mandatory")
    private String filePath;
    @NotNull(message = "ignore quotation is mandatory")
    private Boolean ignoreQuotations = false; //newly added
    @NotNull(message = "with strict quotes is mandatory")
    private Boolean withStrictQuotes = false; //newly added
    @NotNull(message = "ignore character is mandatory")
    private String quoteCharacter = "\'"; //newly added
    private Boolean isMetaInfoAvaiable = false;
    @JsonProperty(required = false)
    private String metadataFilePath;
    private Boolean restrictMetaDataMode = false;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;
    @JsonProperty(required = false)
    private KERBROSEConnectionCredentialsDTO kerbroseCredentials;
}

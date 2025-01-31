package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * Created by Suriyanarayanan K
 * on 05/04/21 11:16 AM.
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MobiusConnectionInfoDTO {

    /**
     * Connection Details
     */
    @NotNull(message = "host is mandatory")
    private String host;
    private Integer port = 21;
    @NotNull(message = "Path is mandatory")
    private String path;
    @JsonProperty(required = false)
    private ConnectionCredentialsDTO connectionCredentials;

    /**
     * Mobius Connection Parameter
     */
    @NotNull(message = "host is mandatory")
    private String className;
    @NotNull(message = "Message Class Name is mandatory")
    private String messageClassName;
    @NotNull(message = "LE Runtime Dataset Name is mandatory")
    private String leRuntimeDatasetName;
    @NotNull(message = "Space is mandatory")
    private String space;
    @NotNull(message = "Disk Prefix Name is mandatory")
    private String diskPrefixName;
    @NotNull(message = "VSAM Dataset Name is mandatory")
    private String vsamDatasetName;
    @NotNull(message = "Unit is mandatory")
    private String unit;
    @Builder.Default
    private String highlyLocationQualified = "TST";


    /**
     * External Report File
     */
    private boolean uploadFile;
    private String reportInputFilePath;
    /**
     * Mode
     */
    private boolean testMode;
}

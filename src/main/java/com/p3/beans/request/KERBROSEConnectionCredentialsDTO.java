package com.p3.beans.request;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class KERBROSEConnectionCredentialsDTO implements Serializable {
    private String userPrincipal;
    private String keytab;
}

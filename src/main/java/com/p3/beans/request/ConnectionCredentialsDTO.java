package com.p3.beans.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConnectionCredentialsDTO implements Serializable {
    @NotNull(message = "userName is mandatory")
    private String userName;
    @NotNull(message = "password is mandatory")
    private String password;
}

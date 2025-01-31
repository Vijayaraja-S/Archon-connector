package com.p3.beans.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class D365ConnectionInfo {
  @NotBlank(message = "Client ID cannot be null or blank")
  private String clientId;

  @NotBlank(message = "Client Secret cannot be null or blank")
  private String clientSecret;

  @NotBlank(message = "Resource URL cannot be null or blank")
  private String resourceUrl;

  @NotBlank(message = "Tenant ID cannot be null or blank")
  private String tenantId;

  private String grantType = "client_credentials";
}

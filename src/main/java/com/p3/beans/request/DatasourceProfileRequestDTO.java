package com.p3.beans.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @author syedsirajuddin
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DatasourceProfileRequestDTO {
  @NotNull(message = "ownerId is mandatory")
  private String ownerId;

  @NotNull(message = "profileName is manry")
  private String profileName;

  @NotNull(message = "description is mandatory")
  private String description;

  @NotNull(message = "category is mandatory")
  private String category; // RDBMS,SharePoint,FileShare

  @JsonProperty(required = false)
  private RDBMSConnectionInfoDTO rdbmsConnectionInfo;

  @JsonProperty private UniverseDBConnectionInfoDTO universeDBConnectionInfo;

  @JsonProperty(required = false)
  private MongoConnectionInfoDTO mongoConnectionInfo;

  @JsonProperty(required = false)
  private SharePointConnectionInfoDTO spConnectionInfo;

  @JsonProperty(required = false)
  private LotusnotesConnectionInfoRequestDTO lnConnectionInfo;

  @JsonProperty(required = false)
  private FileShareConnectionInfoDTO fsConnectionInfo;

  @JsonProperty(required = false)
  private VsamConnectionInfoRequestDTO vsamConnectionInfo;

  @JsonProperty(required = false)
  private FlatFileConnectionInfoDTO flatFileConnectionInfo;

  @JsonProperty(required = false)
  private MobiusConnectionInfoDTO mobiusConnectionInfo;

  @JsonProperty(required = false)
  private D365ConnectionInfo d365ConnectionInfo;
}

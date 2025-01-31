package com.p3.beans.request;
import java.util.List;
import lombok.*;

/**
 * @author syedsirajuddin
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DSManageMembersRequestDTO{
    private String databaseId;
    private String ownerId;
    private List<String> promoteUsers;
    private List<String> demoteUsers;
}

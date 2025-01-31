package com.p3.beans.request;


import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DSAddMembersRequestDTO{
    private String databaseId;
    private String ownerId;
    private List<String> addUsers;
    private List<String> removeUsers;
}

package com.aloha.project.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long no;
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String username;
    private String password;
    private String name;
    private String adddress;
    private String birth;
    private String phone;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private int enabled;

    private List<UserAuth> authList;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    
}

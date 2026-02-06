package com.aloha.project.dto;

import java.util.Date;
import lombok.Data;

@Data
public class UserSocial {

    private Long no;
    private Long userNo;
    private String provider;
    private String socialId;

    private Date createdAt;
    private Date updatedAt;

    
}

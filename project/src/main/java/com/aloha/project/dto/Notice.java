package com.aloha.project.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notice {

    private Long no;
    private String title;
    private String content;
    private Date regDate;
}

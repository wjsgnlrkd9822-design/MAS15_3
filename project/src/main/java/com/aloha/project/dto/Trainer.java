package com.aloha.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Trainer {
    private Long trainerNo;  
    private String trainerName;          
    private String detail;
    private String gender;
    private String img;
    public Trainer() {}
}

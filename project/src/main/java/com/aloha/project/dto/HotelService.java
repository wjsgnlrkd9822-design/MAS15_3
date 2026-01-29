package com.aloha.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelService {
    
    public Long serviceNo;      
    public String serviceName;
    public int servicePrice;
    public String description;
}

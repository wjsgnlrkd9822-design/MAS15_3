package com.aloha.project.dto;

import lombok.Data;

@Data
public class MonthlySalesDto {
    private int year;
    private int month;
    private Long totalSales;
}

package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.HotelService;

@Mapper
public interface ServiceMapper {
    
    List<HotelService> list() throws Exception;
    
    int insert(HotelService hotelService) throws Exception;      
    int update(HotelService hotelService) throws Exception;     
    HotelService select(Long serviceNo) throws Exception;
    int delete(Long serviceNo) throws Exception;



}

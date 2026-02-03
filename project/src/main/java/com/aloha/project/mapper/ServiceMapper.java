package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.HotelService;

@Mapper
public interface ServiceMapper {
    
    public List<HotelService> list() throws Exception;
    
    public int insert(HotelService hotelService) throws Exception;      
    public int update(HotelService hotelService) throws Exception;     
    public HotelService select(Long serviceNo) throws Exception;
    public int delete(Long serviceNo) throws Exception;



}

package com.aloha.project.mapper;

import java.util.List;

import com.aloha.project.dto.HotelService;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelServiceMapper {
    int insertHotelService(HotelService service);
    HotelService selectHotelServiceById(Long serviceNo);
    List<HotelService> selectAllHotelServices();
    int updateHotelService(HotelService service);
    int deleteHotelService(Long serviceNo);
}

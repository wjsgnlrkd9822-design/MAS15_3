package com.aloha.project.mapper;

import java.util.List;

import com.aloha.project.dto.HotelService;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelServiceMapper {
   public int insertHotelService(HotelService service);
   public HotelService selectHotelServiceById(Long serviceNo);
   public List<HotelService> selectAllHotelServices();
   public int updateHotelService(HotelService service);
   public int deleteHotelService(Long serviceNo);
}

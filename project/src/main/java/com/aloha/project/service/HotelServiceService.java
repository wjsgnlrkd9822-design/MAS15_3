package com.aloha.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aloha.project.dto.HotelService;
import com.aloha.project.mapper.HotelServiceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelServiceService {

    private final HotelServiceMapper mapper;

    public List<HotelService> getAllServices() {
        return mapper.selectAllHotelServices();
    }

    public HotelService getService(Long serviceNo) {
        return mapper.selectHotelServiceById(serviceNo);
    }

    public int addService(HotelService service) {
        return mapper.insertHotelService(service);
    }

    public int updateService(HotelService service) {
        return mapper.updateHotelService(service);
    }

    public int deleteService(Long serviceNo) {
        return mapper.deleteHotelService(serviceNo);
    }
}

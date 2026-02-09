package com.aloha.project.service;

import java.util.List;

import com.aloha.project.dto.Notice;

public interface NoticeService {
    
    public List<Notice> list() throws Exception;

    public Notice select(Long no) throws Exception;

    public boolean insert(Notice notice) throws Exception;

    public boolean update(Notice notice) throws Exception;

    public boolean delete(Long no) throws Exception;
    
    // ⭐ 메인 페이지용 최근 공지사항 조회 추가
    public List<Notice> getRecentNotices(int limit) throws Exception;
}

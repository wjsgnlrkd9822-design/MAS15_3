package com.aloha.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aloha.project.dto.Notice;
import com.aloha.project.mapper.NoticeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeMapper noticeMapper;

    @Override
    public List<Notice> list() throws Exception {
        return noticeMapper.list();

    }

    @Override
    public Notice select(Long no) throws Exception {
        return noticeMapper.select(no);
    }

    @Override
    public boolean insert(Notice notice) throws Exception {
        int result = noticeMapper.insert(notice);
        return result > 0;

    }

    @Override
    public boolean update(Notice notice) throws Exception {
        int result = noticeMapper.update(notice);
        return result > 0 ;

    }

    @Override
    public boolean delete(Long no) throws Exception {
        int result = noticeMapper.delete(no);
        return result > 0 ;

    }
    
    // ⭐ 메인 페이지용 최근 공지사항 조회 추가
    @Override
    public List<Notice> getRecentNotices(int limit) throws Exception {
        return noticeMapper.getRecentNotices(limit);
    }
    
}

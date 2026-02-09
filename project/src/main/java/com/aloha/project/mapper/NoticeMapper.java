package com.aloha.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.project.dto.Notice;

@Mapper
public interface NoticeMapper {
    
    public List<Notice> list() throws Exception;

    public Notice select(Long no) throws Exception;

    public int insert(Notice notice) throws Exception;

    public int update(Notice notice) throws Exception;

    public int delete(Long no) throws Exception;
    
    // ⭐ 메인 페이지용 최근 공지사항 조회 추가
    public List<Notice> getRecentNotices(int limit) throws Exception;
}

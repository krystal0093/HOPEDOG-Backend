package com.example.hope_dog.mapper.notice;

import com.example.hope_dog.dto.notice.NoticeListDTO;
import com.example.hope_dog.dto.notice.NoticeViewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeListDTO> noticeList();

    List<NoticeViewDTO> noticeView(Long noticeNo);


//    void insertBoard(BoardWriteDTO boardWriteDTO);

//    void updateBoard(BoardUpdateDTO boardUpdateDTO);
//
//    Optional<NoticeViewDTO> selectById(Long noticeNo);
//
//    List<NoticeListDTO> selectAll();
//
//    int selectTotal();
//
//    List<NoticeListDTO> selectAllPage(Criteria criteria);

//    void deleteBoard(Long boardId);

}
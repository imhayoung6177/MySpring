package com.site.service;

import com.site.domain.Board;
import com.site.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<Board> findAll(String searchType, String keyword){
        return boardMapper.findAll(searchType,keyword);
    }

    public void save(Board board) {
        boardMapper.save(board);
    }

    public Board findById(long bno) {
        return boardMapper.findById(bno);
    }

    public void delete(Board board) {
        boardMapper.delete(board);
    }

    public void update(Board board) {
        boardMapper.update(board);
    }
}

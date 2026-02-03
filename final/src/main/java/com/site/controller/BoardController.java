package com.site.controller;

import com.site.domain.Board;
import com.site.domain.User;
import com.site.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시글 전체목록 및 검색 결과 페이지
     * @param searchType : 검색타입 (title, content, writer)
     * @param : keyword : 검색어
     * @param : model : View 에 데이터를 전달하기 위한 객체
     * @return "boards/list"
     */

    @GetMapping
    //@RequestParam : 클라이언트가 전달하는 데이터의 name과 변수명이 동일할 경우 자동으로 매핑
    //required = false 속성 : 파라미터로 전달되지 않아도 된다는 의미 (즉, 에러방지)
    public String list(@RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String keyword,
                       Model model){

        // 전체 게시판 리스트
        List<Board> boards = boardService.findAll(searchType, keyword);
        model.addAttribute("boards",boards);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "boards/list";

    }

    @GetMapping("/writer")
    public String writer(HttpSession session, Model model){
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("id",user.getId());
        return "boards/writer";
    }

    @PostMapping("/writer")
    public String writer(Board board){
        boardService.save(board);
        return "redirect:/boards";
    }

    //주소의 일부분이 변수(Variable)가 되어 서버로 전달되는 방식
    @GetMapping("/{bno}")
    public String detail(@PathVariable long bno, Model model, HttpSession session){
        Board board = boardService.findById(bno);
        model.addAttribute("board",board);
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("id",user.getId());
        return "boards/detail";
    }


    @PostMapping("/delete")
    public void delete(Board board){
        boardService.delete(board);
    }



}

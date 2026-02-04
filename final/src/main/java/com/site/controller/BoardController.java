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

    // 글 작성 페이지로 이동
    @GetMapping("/writer")
    public String writer(HttpSession session, Model model){
        User user = (User) session.getAttribute("loginUser");
        //model.addAttribute("id",user.getId());
        if (user == null){
            return "redirect:/users/login";
        }
        return "boards/writer";
    }

    /**
     * 글 작성 처리
     * @param board : 사용자가 입력한 게시글
//     * @param session : 로그인한 아이디 필요
     */

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
        if(user != null){
        model.addAttribute("id",user.getId());
        }
        return "boards/detail";
    }

    //@PathVariable : 주소창(URL)에 있는 값을 가져와야 할 때 사용 (데이터의 위치 즉, 주소)
    // cf) @RequestParam : 데이터의 조건(옵션)
    @PostMapping("/{bno}/delete")
    public String delete(@PathVariable long bno, Model model){
        Board board = boardService.findById(bno);
        model.addAttribute("board",board);
        boardService.delete(board);
        System.out.println(board.getBno());
        return "redirect:/boards";

    }

    @PostMapping("/editForm")
    public String editBoards(Board board, Model model){
        model.addAttribute("board",board);
        return "boards/editBoards";
    }

    @PostMapping("/editBoard")
    public String editBoard(Board board, Model model){
        model.addAttribute("board",board);
        System.out.println(board.getBno()+" title "+board.getTitle()+"content "+board.getContent());
        boardService.update(board);
        return "redirect:/boards";
    }




}

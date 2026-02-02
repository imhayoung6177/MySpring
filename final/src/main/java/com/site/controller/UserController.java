package com.site.controller;


import com.site.domain.User;
import com.site.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
회원 관련 웹 요청 처리 컨트롤러
@Controller : 이 클래스가 Spring MVC의 컨트롤러임을 나타냄
@RequestMapping("/users") : UserController의 모든 메서드는 '/users'로 시작하는 URL에 매핑된다
                        예 : /users/signup, /users/login, /users/logout 등
@RequiredArgsConstructor : 'final'이 붙은 객체 의존성 주입(DI -> 제어 역전 IoC)
 */

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입 폼 (View)를 보여주는 메서드
    @GetMapping("/signup")
    public String signUpForm() {
        return "users/signupForm";
    }

    //회원 가입 처리 메서드
    /*
        HTTP POST 요청으로 '/user/signup' URL에 접근시 호출
        폼에서 전송된 파라미터(id, password, name, email)가 자동으로 바인딩된 User 객체 생성
        (단, 테이블의 컬럼명과 객체의 필드명이 일치해야 함)
     */
    @PostMapping("/signup")
    public String signUp(User user, RedirectAttributes rttr){
        try {
            //1. 회원가입 비지니스 로직 시도(예외 발생 가능)
            userService.signUp(user);
            //2. 로그인 성공시 msg를 loginForm 화면에 전달
            rttr.addFlashAttribute("msg","회원가입이 완료되었습니다.");
            //3. 회원 가입 성공 시, 로그인 페이지로 이동
            return "redirect:/users/login";
        }catch (IllegalArgumentException e){
            //4. 로그인 실패(중복 아이디인 경우) 서비스에서 발생시킨 에러 메시지 화면에 전달
            rttr.addFlashAttribute("msg",e.getMessage());
            //5. 회원 가입 폼으로 이동
            return "redirect:/users/signup";

        }

    }

    @GetMapping("/login")
    public String login() {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(String id, String password, HttpServletRequest request, Model model) {
        User user = userService.login(id,password);
        if(user != null){
            //로그인 성공 : 세션 생성 및 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",user); //문제점 : 사용자 정보 세션에 노출
            return "redirect:/";
        }else {
            //로그인 실패 : 에러 메시지 전달
            // *미션 : 아이디와 비밀번호 중 무엇을 틀렸는지 알려주기
            model.addAttribute("error","아이디 또는 비밀번호가 일치하지 않습니다.");
            return "users/loginForm";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        // 기존에 세션이 존재하지 않으면 null 반환
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); //세션 무효화
        }
        return "redirect:/";

    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request, RedirectAttributes rttr){
        HttpSession session = request.getSession(false);
        if(session != null){
            User user = (User) session.getAttribute("loginUser");
            if(user != null){
                userService.remove(user.getId());
                session.invalidate();
                rttr.addFlashAttribute("msg","회원탈퇴가 완료되었습니다.");
            }
        }
        return "redirect:/";
            }


}

package com.site.service;

import com.site.domain.User;
import com.site.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;

    public User login(String id, String password){
        User user = userMapper.findById(id).orElse(null); //없는 아이디인 경우 null 반환

        if(user != null && password.equals(user.getPassword())){
            //id가 존재하고 입력된 비밀번호와 DB의 비밀번호가 일치하면 User 객체반환
            return user;
        }
        return null;
    }

    //회원 가입 비즈니스 로직
    @Transactional
    public void signUp(User user) {
        if(userMapper.findById(user.getId()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        //아이디가 중복되지 않은경우
        userMapper.save(user); //회원 정보를 데이터베이스에 저장
    }

    //회원 정보 수정 로직
    @Transactional
    public void modify(User user) {
        userMapper.update(user); //회원 정보를 데이터베이스에 수정
    }

    //회원 탈퇴 로직
    @Transactional
    public void remove(String id) {
        userMapper.deleteById(id); //회원 정보를 데이터베이스에서 삭제
    }

}

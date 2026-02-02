package com.site.mapper;

import com.site.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
    '회원' 데이터에 접근하기 위한 MyBatis 매퍼(Mapper) 인터페이스
    여기에 선언된 메서드들은 resources/mappers/UserMapper.xml'에 정의된 SQL 쿼리와 매핑 처리
    @Mapper : Spring이 인터페이스를 MyBatis 매퍼로 인식하고 구현체를 자동으로 생성해주기위해 어노테이션 사용
 */

@Mapper
public interface UserMapper {
    /**
     * 회원 조회
    @param id : DB에서 조회할 회원의 ID
    @return : 조회된 회원 정보 Optional객체 사용을 통해 결과가 null일 경우 안전하게 처리

    int countById(String id); 학생스타일
     */

    //이걸로 로그인까지 가능
    Optional<User> findById(String id);

    /**
     * 회원 등록
     * @param user : DB에 저장할 회원 정보 객체
     */
    void save(User user);

    /**
     * 회원 정보 수정
     * @param user : DB에서 수정할 회원 정보 객체
     */

    void update(User user);

    /**
     * 회원 탈퇴
     * @param id : 탈퇴할 회원의 ID
     */
    void deleteById(String id);

    //로그인 ? 로그아웃은 세션만료 시키면되기때문에 안만들어도 됨


}

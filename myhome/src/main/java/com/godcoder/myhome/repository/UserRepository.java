package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, CustomizedUserRepository {

    @EntityGraph(attributePaths = {"boards"})//user 모델의 boards값을 넣어줌.
        // 그러면 레이지 처리를 했어도 user에서 겟한 값을 사용해서 보드에서 다시 겟 해도 board랑 조인으로 조회가 가능함. 여러쿼리 발생 방지
            //패치타입(레이지 등)이 무시가 됨
    //엔티티끼리 그래프로 엮는 것!
    List<User> findAll();

    User findByUsername(String username);

    //커스텀쿼리
    @Query("select u from User u where u.username like %?1%")//?1은 첫번째 파라미터라는 뜻
    List<User> findByUsernameQuery(String username);//이 메소드 호출 시 위 jpql 쿼리가 호출

    //커스텀쿼리
    @Query(value = "select * from User u where u.username like %?1%", nativeQuery = true)//?1은 첫번째 파라미터라는 뜻
    List<User> findByUsernameNativeQuery(String username);//이 메소드 호출 시 nativeQuery 설정에 따라 진짜 sql 쿼리가 호출


}

package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"boards"})//user 모델의 boards값을 넣어줌.
        // 그러면 레이지 처리를 했어도 user에서 겟한 값을 사용해서 보드에서 다시 겟 해도 board랑 조인으로 조회가 가능함. 여러쿼리 발생 방지
            //패치타입(레이지 등)이 무시가 됨
    //엔티티끼리 그래프로 엮는 것!
    List<User> findAll();

    User findByUsername(String username);

}

package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitle(String title);//이렇게 선언해두면 findBy~~를 인식해서 알아서 검색해줌
    List<Board> findByTitleOrContent(String title, String content);//타이틀과 내용이 둘다 일치하면 그 내용을 가져와라
    //and조건도 됨. 다양한 규칙은 스프링 홈피 프로젝트 jpa가서 공식 문서를 보면 된다.


}

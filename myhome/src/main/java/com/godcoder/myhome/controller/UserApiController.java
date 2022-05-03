package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import javafx.beans.binding.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RequestMapping("/api")
@RestController
@Slf4j
class UserApiController {

    @Autowired
    private UserRepository repository;


    // Aggregate root
    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method, String text){
        Iterable<User> users = null;
        //log.debug("getBoards().size() : {}", users.get(0).getBoards().size());//이러면 user의 첫번째 파라미터id의 보드 사이즈가 {}안으로 들어감

        if("query".equals(method)){
            users = repository.findByUsernameQuery(text);//jpa가 쿼리 만들어줌
        }else if("nativeQuery".equals(method)){
            users = repository.findByUsernameNativeQuery(text);//쿼리를 직접 지정하는 방법
        }
//        else if("querydsl".equals(method)){
//            QUser user = QUser.user;//Q를 붙이면 엔티티를 사용할 수 있다

        //Predicate predicate = user.username.contains(text);  이렇게도 담을 수 있고
        
    //    BooleanExpression b = user.username.contains(text); IF문 쓸수도 있음
//            if(true){
//              b = b.and(user.username.eq("HI");

//            users = repository.findAll(predicate);
//        } 설정 문제로 이건 사용이 안된다. dsl 사용 방법
        else if("querydslCustom".equals(method)){
            users = repository.findByUsernameCustom(text);
        }//직접 인터페이스와 클래스를 만들어서 쿼리를 지정하는 것. 다양한 방법으로 쿼리 만들 수 있다.
        else if("jdbc".equals(method)){
            users = repository.findByUsernameJdbc(text);
        }//jdbc로 쿼리
        else{
            users = repository.findAll();
        }
        return users;
    }
//커스텀 커리를 쓰는 다양한 방법들

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
//                    User.setTitle(newUser.getTitle());
//                    User.setContent(newUser.getContent());

//                    user.setBoards(newUser.getBoards());//user모델을 통해서 board테이블에 값 저장이 가능.(cascade 설정해둠)
                      user.getBoards().clear();//기존의 user내 board 데이터 다 삭제

                      user.getBoards().addAll(newUser.getBoards());//새로 입력받은 값을 유저 내 보드에 더해줌
                    for(Board board : user.getBoards()){
                            board.setUser(user);
                        }
                        
                    return repository.save(user);
                })//id값이 찾아지면 그걸로 이 사람이 쓴 글 조회해서 User를 통해 board에 저장
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);//안찾아지면 새로 가입
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

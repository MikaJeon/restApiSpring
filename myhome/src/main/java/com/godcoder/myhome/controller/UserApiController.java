package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
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
    List<User> all(){
        List<User> users = repository.findAll();
        log.debug("getBoards().size() : {}", users.get(0).getBoards().size());//이러면 user의 첫번째 파라미터id의 보드 사이즈가 {}안으로 들어감
        return repository.findAll();
    }


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

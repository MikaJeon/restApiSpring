package com.godcoder.myhome.controller;


import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.vaildator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired // 디펜턴시 인젝션용 서버 기동시 여기에 데이터가 들어옴
    private BoardRepository boardRepository;
    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model){
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards",boards);//에드어트리뷰트 해주면 모델에 담긴 값을 타임리프에서 사용 가능
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false)Long id){
        if(id==null){
            model.addAttribute("board", new Board());
        }
        else{
            Board board = boardRepository.findById(id).orElse(null);//조회된 값이 없을때는 null
            model.addAttribute("board",board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String greetingSubmit(@Valid Board board, BindingResult bindingResult) {//위에서 선언한 min등의 조건에 부합하는가를 파라미터로 받음.
        boardValidator.validate(board, bindingResult);//보드를 체크해줘
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        boardRepository.save(board);//해당 변수 아이디값이 이미 있으면 업데이트 없으면 저장
        return "redirect:/board/list";//이 url을 찍어주는 셈. 위 list 메서드가 실행된다.
    }
}

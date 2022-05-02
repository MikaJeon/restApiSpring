package com.godcoder.myhome.controller;


import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.service.BoardService;
import com.godcoder.myhome.vaildator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired // 디펜턴시 인젝션용
    private BoardService boardService;
    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText){
        //Page<Board> boards = boardRepository.findAll(pageable);//페이지 리퀘스트라는 메서드를 쓰면
        //jpa가 해당 개수만큼 끊어서 가져옴. 리턴 타입은 page. jpa는 첫 페이지가 0부터 시작
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText ,pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);//max메소드를 써서 마이너스가 나오지 않게 함
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage", endPage);
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
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {//위에서 선언한 min등의 조건에 부합하는가를 파라미터로 받음.
        // authentication 이 파라미터는 인증정보를 받아옴!
        boardValidator.validate(board, bindingResult);//보드를 체크해줘
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        //a = SecurityContextHolder.getContext().getAuthentication();//컨트롤러에서는 이렇게 인증정보를 가져올 수도 있음
        String username =  authentication.getName();
        boardService.save(username, board);
//        boardRepository.save(board);//해당 변수 아이디값이 이미 있으면 업데이트 없으면 저장
        return "redirect:/board/list";//이 url을 찍어주는 셈. 위 list 메서드가 실행된다.
    }
}

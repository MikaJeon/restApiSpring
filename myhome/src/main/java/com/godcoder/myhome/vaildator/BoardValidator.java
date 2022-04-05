package com.godcoder.myhome.vaildator;

import com.godcoder.myhome.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class BoardValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {//지연할 객체와 사용할 에러 객체를 가져옴
        Board b = (Board) obj;//보드로 형변환
        if(StringUtils.isEmpty(b.getContent())){
            errors.rejectValue("content", "key", "내용을 입력하세요");
            //b의 콘텐츠가 비었으면 콘텐츠 필드에 내용을 입력하세요 라는 값을 보냄. key값도 보내는데 없으면 내용만 보냄
        }
    }
}

package com.godcoder.myhome.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data//게터세터
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();//User리포지토리를 이용해서 조회를 하게 되면 이 유저에 해당하는 권한이 조회가 되서 여기에 담기게 됨
//그냥 생성만 해두면 NULL포인트에러 많이 나니까 어레이리스트 채워서 만들어주기
}

package com.godcoder.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();//User리포지토리를 이용해서 조회를 하게 되면 이 유저에 해당하는 권한이 조회가 되서 여기에 담기게 됨
//그냥 생성만 해두면 NULL포인트에러 많이 나니까 어레이리스트 채워서 만들어주기


    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    //조인된 다른 엔티티도 이 모델을 통해 저장, 삭제 등등 하고싶다! orphanRemoval는 부모가 없는 데이터를 전부 지우라는 뜻.
    //즉 유저를 통해서 보드를 저장하되, 해당 외래키값과 같은 컬럼값을 가지고 있으면서 전달받은 인스턴스 안에 없는 값은 다 지워버림
    //만약에 어떤 자식을(그니까 상대쪽 테이블 값) 변경하게 되면 다른 기존 자식을 모두 null처리 후 삭제해버림
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)//처음에 조회할때 이 조인 값을 미리 가져올것이냐? 필요할때 가져올것이냐
    //lazy를 하면 필요시 보드를 셀렉해옴. 어리어로 하면 초반부터 그냥 다 같이 보드랑 조인해서 가져옴
    //제이슨이그노어하면 이 보드 정보가 제외되서 셀렉 쿼리가 안나감
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();
    
    //User가 boards를 가지고 조인 연결된 board에 연결, board를 저장시켜줄 수 있음
}

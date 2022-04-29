package com.godcoder.myhome.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;//오토와이어드를 붙여서 스프링이 인스턴스를 주입해줌
    //application.properties 안에 있는 데이터소스를 사용할 수 있게 연결해줌
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//http의 인증을 추가함
                    .antMatchers("/","/account/register","/css/**").permitAll()//home만 모두의 접근 허용
                    .anyRequest().authenticated()//다른건 모두 인증 필요
                    .and()//인증 필요시에는
                .formLogin()//여기로 리다이렉트
                    .loginPage("/account/login")//리다이렉트 url
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {//인증을 위한 쿼리
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())//패스워드를 빈으로 관리!
                .usersByUsernameQuery("select username,password,enabled "
                        + "from user "
                        + "where username = ?")//인증처리. 뒤에 여백이 있어야 where절과 안붙음. ?는 알아서 유저네임이 들어감
                .authoritiesByUsernameQuery("select u.username, r.name "
                        + "from user_role ur inner join user u on ur.user_id = u.id "
                        + "inner join role r on ur.role_id = r.id "
                        + "where u.username = ?");//권한처리
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//암호화
    }
    
    //authentication 로그인 관련
    //authroization 권한 관련

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }임시 유저 만드는 메서드
}

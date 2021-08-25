package com.kth.book.springboot.config.auth;

import com.kth.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity//spring security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()//h2-console 화면을 사용하기 위해 disable
                .and()
                    .authorizeRequests()
                //url별 권한 관리 설정 옵션의 시작점
                //authorizeRequests 가 선언되어야 antMatchers 사용가능
                    .antMatchers("/", "/css/**", "images/**", "/js/**", "h2-console").permitAll()//전체 열람 권한
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())//user만 가능
                //antMatchers 권한 관리대상 지정하는 옵션 url,http별 관리 가능
                    .anyRequest().authenticated()//나머지 url은 인증된 사용자만사용 가능
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()//OAuth2 로그인 기능의 여러 설정의 진입점
                        .userInfoEndpoint()//OAuth2 로그인 성공 이후 사용자 정보 가저올 설정 담당
                            .userService(customOAuth2UserService);
        //소셜 로그인 성공시 후속조치를 진행할 UserService 인터페이스 구현체를 등록
        //리소스 서버(즉, 소셜 서비스)에서 사용자정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있습니다.
    }
}

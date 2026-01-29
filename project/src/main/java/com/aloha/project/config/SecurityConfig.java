package com.aloha.project.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.aloha.project.handler.CustomAccessDeniedHandler;
import com.aloha.project.handler.LoginFailureHandler;
import com.aloha.project.handler.LoginSuccessHandler;
import com.aloha.project.handler.LogoutSuccessHandler;
import com.aloha.project.service.UserDetailServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  private final DataSource dataSource;
  private final UserDetailServiceImpl userDetailServiceImpl;
  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;
  private final LogoutSuccessHandler logoutSuccessHandler;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  /**
   * 스프링 시큐리티 설정
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("스프링 시큐리티 설정");

    // 인가 설정
    http.authorizeHttpRequests(auth -> auth
                              .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  
                              .requestMatchers("/**").permitAll()   // 전체 허용
                              );
    // 폼 로그인 설정
    http.formLogin(login -> login
      .loginPage("/login")                    // 커스텀 로그인 페이지 경로
      .loginProcessingUrl("/login")  // 로그인 요청 경로
      .successHandler(loginSuccessHandler)         // 로그인 성공 핸들러 설정
      .failureHandler(loginFailureHandler)         // 로그인 실패 핸들러 설정
    );

    
    // 로그아웃 설정
    http.logout(logout -> logout
      .logoutUrl("/logout")                            // 로그아웃 요청 경로
      // .logoutSuccessUrl("/login?logout=true")   // 로그아웃 성공시 이동할 경로
      .invalidateHttpSession(true)         // 세션 초기화
      .deleteCookies("remember-id")        // 로그아웃 시, 쿠키 삭제(아이디 저장)
      .logoutSuccessHandler(logoutSuccessHandler)  // 로그아웃 성공 핸들러 설정
    );

    // 접근 거부 예외 처리
    http.exceptionHandling(exception -> exception
        .accessDeniedHandler(customAccessDeniedHandler)  // 접근 거부 핸들러 설정
    );

    // 사용자 정의 인증
    http.userDetailsService(userDetailServiceImpl);

    // 자동 로그인 설정
    http.rememberMe(me -> me
                    .key("aloha")
                    .tokenRepository(tokenRepository())             // 자동 로그인 저장소 빈 지정
                    .tokenValiditySeconds(60 * 60 * 24 * 7)         // 자동 로그인 토큰 유효 기간
    );

    return http.build();
  }

  /**
   * 암호화 방식 빈 등록
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  /**
   * AuthenticationManager 인증 관리자 빈 등록
   * @param authenticationConfiguration
   * @return
   * @throws Exception
  */
  @Bean
  public AuthenticationManager authenticationManager( 
                                  AuthenticationConfiguration authenticationConfiguration ) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
  }

  /**
    * 자동 로그인 저장소 빈 등록
    * 데이터 소스
    * persistent_logins 테이블 생성
        create table persistent_logins (
            username varchar(64) not null
            , series varchar(64) primary key
            , token varchar(64) not null
            , last_used timestamp not null
          );
    * 자동 로그인 프로세스
    * 로그인 시 
    *     ➡ (ID, 시리즈, 토큰) 저장
    * 로그아웃 시, 
    *     ➡ (ID, 시리즈, 토큰) 삭제
    * @return
    */
    @Bean
    public PersistentTokenRepository tokenRepository() {
      // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
      JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
      // 토큰 저장소를 사용하는 데이터 소스 지정
      // - 시큐리티가 자동 로그인 프로세스를 처리하기 위한 DB를 지정합니다.
      repositoryImpl.setDataSource(dataSource);   
      // persistent_logins 테이블 생성
      try {
          repositoryImpl.getJdbcTemplate().execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
      } 
      catch (BadSqlGrammarException e) {
          log.error("persistent_logins 테이블이 이미 존재합니다.");   
      }
      catch (Exception e) {
          log.error("자동 로그인 테이블 생성 중 , 예외 발생");
      }
      return repositoryImpl;
  }

  
}

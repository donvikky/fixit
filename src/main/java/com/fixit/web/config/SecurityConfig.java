package com.fixit.web.config;

import com.fixit.web.auth.CustomLoginSuccessHandler;
import com.fixit.web.auth.Oauth2AuthenticationSuccessHandler;
import com.fixit.web.service.CustomOAuth2UserService;
import com.fixit.web.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private CustomOidcUserService oidcUserService;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/login/**", "/oauth/**", "/error", "/jobs/listing/**","/jobs/{id}",
                        "/profiles/search", "/profiles/search/**", "/profiles/view/**", "/verify/**", "/telegram/**",
                        "/privacy").permitAll()
                .antMatchers("/js/**", "/css/**", "/images/**", "/sass/**", "/fonts/**").permitAll()
                .antMatchers("/jobs/create").authenticated()
                .antMatchers("/").access("permitAll")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService)
                        .oidcUserService(this.oidcUserService())
                )
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .loginPage("/login"));

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(){
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            List<GrantedAuthority> mappedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            oidcUserService.processUser(userRequest, oidcUser);
            return oidcUser;
        };
    }


}

package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/js/**","/web/css/**","/web/img/**","/api/login","/api/logout").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .anyRequest().authenticated();

        /*http.authorizeRequests()
                .antMatchers("/web/index.html","/web/css/**","/web/img/**","/web/js/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/**").permitAll()
                .antMatchers("/**").hasAuthority("CLIENT");*/


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .successHandler(((request, response, authException) -> clearAuthenticationAttributes(request)))
                .failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    }
    private void clearAuthenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

package com.securityteste.securityspringteste.config.security;

import com.securityteste.securityspringteste.filter.TokenAuthenticationFilter;

import com.securityteste.securityspringteste.service.auth.TokenService;
import com.securityteste.securityspringteste.service.usuarios.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig{

    @Configuration
    @Order(1)
    public static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter{
        
        @Autowired
        private UsuarioServiceImpl usuarioServiceImpl;

        @Autowired
        private TokenService tokenService;
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/auth").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable()
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        public OncePerRequestFilter tokenAuthenticationFilter(){
            return new TokenAuthenticationFilter(this.tokenService, this.usuarioServiceImpl);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                .ignoring()
                .antMatchers("/h2-console/**");
        }

        @Bean
        @Override
        protected AuthenticationManager authenticationManager() throws Exception{
            return super.authenticationManager();
        }      

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean  
        GrantedAuthorityDefaults grantedAuthorityDefaults() { 
            return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix  
        }
    }

    @Configuration
    @Order(2)
    public static class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/home").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout()
                    //.logoutUrl("/logout")
                    //.addLogoutHandler(new SecurityContextLogoutHandler())
                    //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    //.logoutSuccessUrl("/admin")
                     //.clearAuthentication(true)
                    //.deleteCookies("JSESSIONID")
                    //.invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                .ignoring()
                    .antMatchers("/resources/**");
        }

    }    

}

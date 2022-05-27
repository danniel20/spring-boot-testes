package com.securityteste.securityspringteste.config.security;

import com.securityteste.securityspringteste.api.filter.TokenAuthenticationFilter;
import com.securityteste.securityspringteste.service.token.TokenService;
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

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
	}

	@Configuration
	@Order(1)
	public static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
		public OncePerRequestFilter tokenAuthenticationFilter() {
			return new TokenAuthenticationFilter(this.tokenService, this.usuarioServiceImpl);
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/h2-console/**");
		}

		@Bean
		@Override
		protected AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}

		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.securityteste.securityspringteste.api.controller"))
					.build();
		}

	}

	@Configuration
	@Order(2)
	public static class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.authorizeRequests()
					.antMatchers("/resources/**", "/webjars/**", "/uploads/**").permitAll()
					// .antMatchers("/home").permitAll()
					// .antMatchers( "/public/**").permitAll()
					.anyRequest().authenticated()
					.and()
					.formLogin()
					.loginPage("/login").permitAll()
					.defaultSuccessUrl("/home", true)
					.and()
					.logout().permitAll()
					.invalidateHttpSession(true);
			// .deleteCookies("JSESSIONID")
			// .and()
			// .exceptionHandling()
			// .accessDeniedPage("/accessDenied");
		}

	}

	@Configuration
	@EnableWebMvc
	@Order(3)
	public static class ResourcesWebConfiguration implements WebMvcConfigurer {

		private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
				"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry
					.addResourceHandler("/**")
					.addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);

			registry
					.addResourceHandler("/uploads/**")
					.addResourceLocations("file:./uploads/");
		}

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**");
		}

	}

}

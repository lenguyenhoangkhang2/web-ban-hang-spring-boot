package com.congnghejava.webbanhang.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.congnghejava.webbanhang.security.jwtToken.RestAuthenticationEntryPoint;
import com.congnghejava.webbanhang.security.jwtToken.TokenAuthenticationFilter;
import com.congnghejava.webbanhang.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.congnghejava.webbanhang.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.congnghejava.webbanhang.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.congnghejava.webbanhang.security.services.OAuth2UserServiceImpl;
import com.congnghejava.webbanhang.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private OAuth2UserServiceImpl oAuth2UserServiceImpl;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Autowired
	HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.headers().frameOptions().disable()
			.and()
		.cors()
			.and()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
		.csrf().disable()
		.formLogin().disable()
		.httpBasic().disable()
		.exceptionHandling()
			.authenticationEntryPoint(restAuthenticationEntryPoint)
			.and()
		.authorizeRequests()
			.antMatchers("/","/error","/favicon.ico","/**/*.png","/**/*.gif","/**/*.svg","/**/*.jpg","/**/*.html","/**/*.css","/**/*.js").permitAll()
			.antMatchers("/auth/**", "/test/**","/h2/**","/files/**").permitAll()
			.antMatchers(HttpMethod.GET, "/products/**", "/brands/**", "/categories/**").permitAll()
			.antMatchers(HttpMethod.POST, "/payment/stripe/*").permitAll()
			.anyRequest().authenticated()
			.and()
		.oauth2Login()
			.authorizationEndpoint()
				.baseUri("/oauth2/authorize")
				.authorizationRequestRepository(cookieAuthorizationRequestRepository())
				.and()
			.redirectionEndpoint()
				.baseUri("/oauth2/callback/*")
				.and()
			.userInfoEndpoint()
				.userService(oAuth2UserServiceImpl)
				.and()
			.successHandler(oAuth2AuthenticationSuccessHandler)
			.failureHandler(oAuth2AuthenticationFailureHandler);
		// @formatter:on

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}

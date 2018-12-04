package com.arcus.master.security.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.arcus.master.security.jwt.JwtAuthenticationEntryPoint;
import com.arcus.master.security.jwt.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) 
			throws Exception{
		authenticationManagerBuilder.userDetailsService(this.userDetailsService)
									.passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception{
		return new JwtAuthenticationTokenFilter();
	}
	
    @Bean
    @Override
     public AuthenticationManager authenticationManagerBean() throws Exception {
          return super.authenticationManagerBean();
    } 
	
	@Override
	protected void configure(HttpSecurity httSecurity) throws Exception{
		httSecurity.csrf().disable()
					.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.authorizeRequests()
					.antMatchers(
							HttpMethod.GET,
							"/",
							"/*.html",
							"/favicon.ico",
							"/**/*.html",
							"/**/*.css",
							"/**/*.js"
					).permitAll()
					.antMatchers("/auth/**").permitAll()
					.anyRequest().authenticated();
		httSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		httSecurity.headers().cacheControl();
	}	
}
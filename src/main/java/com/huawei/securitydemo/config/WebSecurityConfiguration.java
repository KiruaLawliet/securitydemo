package com.huawei.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	private int csrfExpiredIntervalMinutes = 5;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(new CustomCookieCsrfTokenRepository(this.csrfExpiredIntervalMinutes))
                .requireCsrfProtectionMatcher(
                        httpServletRequest -> httpServletRequest.getMethod().matches("POST|PUT|DELETE")
                );
    }
	
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // set authenticationManagerBean to prevent spring-security's
    	// auto-configuration and default password's logging in file
        return super.authenticationManagerBean();
    }
    
}

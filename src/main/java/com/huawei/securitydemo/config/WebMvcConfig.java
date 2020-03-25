package com.huawei.securitydemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.huawei.securitydemo.interceptor.AuthenticationInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new AuthenticationInterceptor());
    }
    
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// turn off all suffix pattern matching for security
		configurer.setUseSuffixPatternMatch(false);
		configurer.setUseRegisteredSuffixPatternMatch(true);
	}
    
}

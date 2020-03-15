package com.huawei.securitydemo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huawei.securitydemo.bean.UserInfo;

@RestController
@RequestMapping("/api")
public class HealthCheckerController {
	
	@RequestMapping(value="hello", method=RequestMethod.POST, consumes = "application/json")
	public UserInfo hello(@RequestBody @Validated UserInfo userinfo) 
	{
		return userinfo;
	}
	
} 

package com.huawei.securitydemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huawei.securitydemo.bean.Profile;
import com.huawei.securitydemo.dao.entity.User;
import com.huawei.securitydemo.service.UserInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class HealthCheckerController {
	
	@Autowired
	private UserInfoService userInfoService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="name", method=RequestMethod.POST, consumes = "application/json")
	public User hello(@RequestBody @Validated Profile userinfo) 
	{
		return userInfoService.qureyUserByName(userinfo.getUsername());
	}
	
	
	@RequestMapping(value="id", method=RequestMethod.POST, consumes = "application/json")
	public User find(@RequestParam long id) 
	{
		return userInfoService.qureyUserById(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT, consumes = "application/json")
	public String find(@RequestBody @Validated Profile userinfo) 
	{
		User user = new User();
		user.setId(userinfo.getUserid());
		user.setAddress(userinfo.getAddress());
		user.setAge(userinfo.getAge());
		userInfoService.updateAddressAndAgeById(user);
		return "Success";
	}
	
}

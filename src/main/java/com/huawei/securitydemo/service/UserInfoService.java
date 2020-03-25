package com.huawei.securitydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huawei.securitydemo.dao.entity.User;
import com.huawei.securitydemo.dao.mapper.UserMapper;

@Service
public class UserInfoService {
	@Autowired
	private UserMapper userMapper;
	
	public User qureyUserByName(String name) {
		return userMapper.selectByName(name);
	}
	
	public User qureyUserById(long id) {
		return userMapper.selectById(id);
	}

	public void updateAddressAndAgeById(User user) {
		userMapper.updateAddressAndAgeById(user);
	}
	
}

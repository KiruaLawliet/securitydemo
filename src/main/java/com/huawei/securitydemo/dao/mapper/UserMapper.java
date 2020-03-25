package com.huawei.securitydemo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.huawei.securitydemo.dao.entity.User;

@Component
public interface UserMapper {
	
	User selectByName(@Param("name") String name);
	
	User selectById(@Param("id") long id);
	
	void updateAddressAndAgeById(@Param("user") User user);
	
}

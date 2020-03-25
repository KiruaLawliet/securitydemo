package com.huawei.securitydemo.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Profile {
	
	@Min(value=0, message="integer id > 0")
	public long userid;
	
	@NotNull
	@Pattern(regexp="[\\w \\-·]+", message="usernaem contains invalid char")
	public String username;
	
	@NotNull
	@Min(value=0, message="age < 0")
	@Max(value=150, message="age > 150")
	public long age;
	
	@Size(min=0, max=256)
	public String address;
}

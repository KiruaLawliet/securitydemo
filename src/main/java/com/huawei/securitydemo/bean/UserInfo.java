package com.huawei.securitydemo.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserInfo {
	
	@NotNull
	@Pattern(regexp="[\\w \\-·]+", message="usernaem contains invalid char")
	public String username;
	
	@NotNull
	@Min(0) @Max(150)
	public long age;
	
	@Size(min=0, max=256)
	public String address;
}

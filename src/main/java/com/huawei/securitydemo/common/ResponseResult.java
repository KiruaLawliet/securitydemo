package com.huawei.securitydemo.common;

import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class ResponseResult {

	private HttpStatus status = HttpStatus.OK;
	
	private String msg;
	
	private String data;
	
}

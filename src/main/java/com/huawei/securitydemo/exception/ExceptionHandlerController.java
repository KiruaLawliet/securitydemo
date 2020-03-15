package com.huawei.securitydemo.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.huawei.securitydemo.common.ResponseResult;

@RestControllerAdvice
public class ExceptionHandlerController {
	
	@ExceptionHandler(value=Exception.class)
	public ResponseResult errorHandler(HttpServletRequest request, final Exception e, HttpServletResponse response)
	{
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		ResponseResult x = new ResponseResult();
		return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
	}
	
}

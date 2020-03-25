package com.huawei.securitydemo.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.huawei.securitydemo.common.ResponseResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/* * *
 *  Controller 层全局异常处理
 * 
 */

@RestControllerAdvice
public class ExceptionHandlerController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* * *
	 *  @ExceptionHandler 捕获指定异常
	 *  @ResponseStatus 设置相应码
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseResult errorHandler(HttpServletRequest request, final MethodArgumentNotValidException e, HttpServletResponse response)
	{
		// 获取自定义异常信息
		String customErrorMessage = (String) e.getBindingResult()
								.getAllErrors().stream().map(ObjectError::getDefaultMessage)
								.collect(Collectors.toList()).get(0);
		return new ResponseResult(customErrorMessage);
	}
	
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseResult errorHandler(HttpServletRequest request, final MethodArgumentTypeMismatchException e, HttpServletResponse response)
	{
		return new ResponseResult("参数格式错误");
	}
	
	
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseResult errorHandler(HttpServletRequest request, final HttpMediaTypeNotAcceptableException e, HttpServletResponse response)
	{
		return new ResponseResult("不支持的返回类型转换");
	}
	
	// 捕获 Exception 异常兜底
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseResult errorHandler(HttpServletRequest request, final Exception e, HttpServletResponse response)
	{
		logger.info(e.getMessage(), e);
		return new ResponseResult("服务内部异常");
	}
	
}

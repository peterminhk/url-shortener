package com.peterminhk.app.urlshortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class WebControllerAdvice {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	ResponseEntity<Object> argumentErrorHandler(
			MethodArgumentTypeMismatchException ex) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	ResponseEntity<Object> clientErrorHandler(HttpClientErrorException ex) {
		return new ResponseEntity<>(ex.getStatusCode());
	}

	@ExceptionHandler(HttpServerErrorException.class)
	ResponseEntity<Object> serverErrorHandler(HttpServerErrorException ex) {
		return new ResponseEntity<>(ex.getStatusCode());
	}

}

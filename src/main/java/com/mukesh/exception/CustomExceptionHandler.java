/**
 * 
 */
package com.mukesh.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(exception.getLocalizedMessage());
		ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), "Server Error", details);
		return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public final ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotFoundException,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(dataNotFoundException.getLocalizedMessage());
		ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), "Data Not Found", details);
		return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RequestProcessException.class)
	public final ResponseEntity<Object> handleRequestProcessException(RequestProcessException requestProcessException,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(requestProcessException.getLocalizedMessage());
		ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), "Request Failed to process, Try after sometime", details);
		return new ResponseEntity<>(exceptionDetails, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException argumentNotValidException, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : argumentNotValidException.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), "Validation Failed", details);
		return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
	}

}
/**
 * 
 */
package com.mukesh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @author Mukesh Gehani
 *
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RequestProcessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3789638043640473190L;

	public RequestProcessException(String exception) {
	    super(exception);
	  }


}

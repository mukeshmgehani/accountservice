/**
 * 
 */
package com.mukesh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Mukesh
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3789638043640473190L;

	public DataNotFoundException(String exception) {
	    super(exception);
	  }


}

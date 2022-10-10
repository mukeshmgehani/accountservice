/**
 * 
 */
package com.mukesh.exception;

import java.util.Date;
import java.util.List;

/**
 * @author Mukesh
 *
 */
public class ExceptionDetails {
	
	
 
    //General error message about nature of error
    private String message;
 
    //Specific errors in API request processing
    private List<String> details;
 
 
	private Date timestamp;

	  public ExceptionDetails(Date timestamp, String message, List<String> details) {
	    super();
	    this.timestamp = timestamp;
	    this.message = message;
	    this.details = details;
	  }

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the details
	 */
	public List<String> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<String> details) {
		this.details = details;
	}
	  
}

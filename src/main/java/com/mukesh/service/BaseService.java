/**
 * 
 */
package com.mukesh.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author Mukesh Gehani
 *
 */
public interface BaseService {
	
	public default HttpHeaders prepareHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

}

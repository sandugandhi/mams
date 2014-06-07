package com.exercise.Dramila.customersearch.service;

import java.util.List;

/**
 * Business Partner Search result listener.
 * 
 * @author gatisje
 */
public interface CustSResultListener {
	
	/**
	 * Invoked on successful SOAP response
	 * 
	 * @param response
	 *            search result
	 */
	public void onResponse(List<String> response);
	
	/**
	 * Invoked on exception during SOAP call
	 * 
	 * @param e
	 *            exception to handle
	 */
	public void onError(Exception e);

	/**
	 * Invoked if BS service requires authentication and credentials not
	 * provided or invalid
	 */
	public void onAuthRequied();
	
}

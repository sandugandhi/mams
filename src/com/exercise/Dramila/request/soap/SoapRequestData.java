package com.exercise.Dramila.request.soap;

import java.io.File;
//import org.ksoap2.serialization.SoapObject;

/**
 * Data object representing SOAP call request.
 * 
 * @author gatsje
 */
public class SoapRequestData {

	//private SoapObject soapRequest;
	//private String url;
	private String recNum;
	private String username;
	private String password;
	private File filename;

	public SoapRequestData(String recNum, File filename, String username,
			String password) {
		
		this.recNum = recNum;
		this.filename = filename;
		
		//this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getrecNum() {
		return recNum;
	}

	public void setrecNum(String recNum) {
		this.recNum = recNum;
	}
	public File getFileName() {
		return filename;
	}

	public void setFileName(File filename) {
		this.filename = filename;
	}

	
	/*
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
*/
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

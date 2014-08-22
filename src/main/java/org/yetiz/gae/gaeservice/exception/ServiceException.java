/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.exception;

/**
 *
 * @author yeti
 */
public class ServiceException extends RuntimeException {

	protected int errorCode = -1;
	protected static int CHECKED_EXCEPTION = 99999;

	public ServiceException() {
		super();
	}

	protected ServiceException(String message, int errorCode) {
		super(errorCode + ". " + message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}

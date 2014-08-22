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
public class ServiceOperationException extends ServiceException {

	public ServiceOperationException() {
		super("Service Operation Exception.", CHECKED_EXCEPTION);
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.exception;

import org.yetiz.gae.gaeservice.validator.AdminServiceValidator;

/**
 *
 * @author yeti
 */
public class ServiceArgumentException extends ServiceException {

	public ServiceArgumentException() {
		super("Service Argument Missed.", AdminServiceValidator.EC_SERVICE_ARGUMENT);
	}
}

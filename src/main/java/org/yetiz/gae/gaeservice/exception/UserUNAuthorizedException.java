/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.exception;

import org.yetiz.gae.gaeservice.validator.UserValidator;

/**
 *
 * @author yeti
 */
public class UserUNAuthorizedException extends ServiceException {

	public UserUNAuthorizedException() {
		super("User UNAuthorized.", UserValidator.EC_USER_UNAUTHORIZED);
	}
}

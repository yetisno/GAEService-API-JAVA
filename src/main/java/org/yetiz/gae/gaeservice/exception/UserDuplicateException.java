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
public class UserDuplicateException extends ServiceException {

	public UserDuplicateException() {
		super("User Duplicate.", UserValidator.EC_USER_DUPLICATE);
	}
}

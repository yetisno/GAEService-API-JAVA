/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.validator;

import org.yetiz.gae.gaeservice.ds.GAEValidator;

/**
 *
 * @author yeti
 */
public class UserValidator extends GAEValidator {

	public static final int EC_USER_ARGUMENT = 21;
	public static final int EC_USER_NOT_FOUND = 22;
	public static final int EC_USER_DUPLICATE = 23;
	public static final int EC_USER_UNAUTHORIZED = 24;

}

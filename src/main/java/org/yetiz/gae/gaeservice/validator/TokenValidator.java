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
public class TokenValidator extends GAEValidator {

	public static final int EC_TOKEN_ARGUMENT = 11;
	public static final int EC_TOKEN_NOT_FOUND = 12;
	public static final int EC_TOKEN_UNAUTHORIZED = 13;
	public static final int EC_TOKEN_EXPIRED = 14;

}

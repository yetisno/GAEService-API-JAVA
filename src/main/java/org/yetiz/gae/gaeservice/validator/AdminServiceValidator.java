/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.validator;

import com.google.gson.JsonObject;
import org.yetiz.gae.gaeservice.ds.GAEValidator;
import org.yetiz.gae.gaeservice.exception.ServiceOperationException;

/**
 *
 * @author yeti
 */
public class AdminServiceValidator extends GAEValidator {

	public static final int EC_SERVICE_OK = 0;
	public static final int EC_SERVICE_ARGUMENT = 1;
	public static final int EC_SERVICE_UNAUTHORIZED = 2;

	public static int getStatus(JsonObject obj) throws ServiceOperationException {
		if (obj == null) {
			throw new ServiceOperationException();
		}
		if (!obj.has(AdminServiceValidator.PARAM_STATUS)) {
			throw new ServiceOperationException();
		}
		return obj.get(AdminServiceValidator.PARAM_STATUS).getAsInt();
	}

}

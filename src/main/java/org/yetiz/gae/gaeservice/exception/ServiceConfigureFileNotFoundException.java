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
public class ServiceConfigureFileNotFoundException extends ServiceException {

	public ServiceConfigureFileNotFoundException() {
		super("Service config.json Not Found or ADKey key not found, please add config.json file into src/main/resources/.", 999);
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.yetiz.gae.gaeservice.ds.GAEValidator;
import org.yetiz.gae.gaeservice.ds.Token;
import org.yetiz.gae.gaeservice.ds.User;
import org.yetiz.gae.gaeservice.exception.ServiceArgumentException;
import org.yetiz.gae.gaeservice.exception.ServiceOperationException;
import org.yetiz.gae.gaeservice.exception.ServiceUNAuthorizedException;
import org.yetiz.gae.gaeservice.exception.UserDuplicateException;
import org.yetiz.gae.gaeservice.exception.UserNotFoundException;
import org.yetiz.gae.gaeservice.exception.UserUNAuthorizedException;
import org.yetiz.gae.gaeservice.validator.AdminServiceValidator;
import org.yetiz.gae.gaeservice.validator.UserValidator;

/**
 *
 * @author yeti
 */
public class GAEAdmin {

	private String endPoint = null;
	private final AsyncHttpClient ahc;

	protected GAEAdmin(AsyncHttpClient ahc) {
		this.ahc = ahc;
	}

	protected GAEAdmin setEndPoint(String endPoint) {
		this.endPoint = endPoint;
		return this;
	}

	private String getUser_AddPath(String userID) {
		return endPoint + User.FUNC + "/" + userID;
	}

	private String getUser_DeletePath(String userID) {
		return endPoint + User.FUNC + "/" + userID;
	}

	public Token addUser(String userID, String passphrase) throws ServiceOperationException, UserNotFoundException, UserDuplicateException, UserUNAuthorizedException, ServiceUNAuthorizedException {
		JsonObject obj;
		try {
			Future<Response> f = ahc.preparePost(getUser_AddPath(userID))
				.addQueryParameter(GAEValidator.PARAM_ADKEY, GAEService.ADKEY)
				.addQueryParameter(User.PARAM_PASSPHRASE, passphrase)
				.setBody("")
				.execute();
			String str = f.get().getResponseBody();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException e) {
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case UserValidator.EC_USER_NOT_FOUND:
				throw new UserNotFoundException();
			case UserValidator.EC_USER_DUPLICATE:
				throw new UserDuplicateException();
			case UserValidator.EC_USER_UNAUTHORIZED:
				throw new UserUNAuthorizedException();
			case AdminServiceValidator.EC_SERVICE_UNAUTHORIZED:
				throw new ServiceUNAuthorizedException();
			case AdminServiceValidator.EC_SERVICE_ARGUMENT:
				throw new ServiceArgumentException();
			case AdminServiceValidator.EC_SERVICE_OK:
				return new Token(obj.get(Token.PARAM_KEY).getAsString(), userID, new Date(obj.get(Token.PARAM_EXPIRE).getAsLong()));
			default:
				return null;
		}
	}

	public boolean deleteUser(String userID) throws ServiceOperationException, ServiceUNAuthorizedException {
		JsonObject obj;
		try {
			Future<Response> f = ahc.prepareDelete(getUser_DeletePath(userID))
				.addQueryParameter(AdminServiceValidator.PARAM_ADKEY, GAEService.ADKEY)
				.execute();
			String str = f.get().getResponseBody();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException e) {
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case AdminServiceValidator.EC_SERVICE_UNAUTHORIZED:
				throw new ServiceUNAuthorizedException();
			case AdminServiceValidator.EC_SERVICE_ARGUMENT:
				throw new ServiceArgumentException();
			case AdminServiceValidator.EC_SERVICE_OK:
				return true;
			default:
				return false;
		}
	}
}

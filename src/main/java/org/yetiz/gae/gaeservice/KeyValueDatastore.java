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
import com.ning.http.util.Base64;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yetiz.gae.gaeservice.ds.KVEntity;
import org.yetiz.gae.gaeservice.ds.Token;
import org.yetiz.gae.gaeservice.exception.KVEntityNotFoundException;
import org.yetiz.gae.gaeservice.exception.ServiceArgumentException;
import org.yetiz.gae.gaeservice.exception.ServiceOperationException;
import org.yetiz.gae.gaeservice.exception.TokenNotFoundException;
import org.yetiz.gae.gaeservice.validator.AdminServiceValidator;
import org.yetiz.gae.gaeservice.validator.KVEntityValidator;
import org.yetiz.gae.gaeservice.validator.TokenValidator;

/**
 *
 * @author yeti
 */
public class KeyValueDatastore {

	private static KeyValueDatastore ds = null;
	private String endPoint = null;
	private final AsyncHttpClient ahc;
	private Token token = null;

	private KeyValueDatastore(AsyncHttpClient ahc) {
		this.ahc = ahc;
	}

	protected KeyValueDatastore setEndPoint(String endPoint) {
		this.endPoint = endPoint;
		return this;
	}

	protected KeyValueDatastore setToken(Token token) {
		this.token = token;
		return this;
	}

	protected String getKV_GetPath(String key) {
		return GAEService.getInstance().getEndPoint() + KVEntity.FUNC + "/" + key;
	}

	protected String getKV_PostPath(String key) {
		return GAEService.getInstance().getEndPoint() + KVEntity.FUNC + "/" + key;
	}

	protected String getKV_DeletePath(String key) {
		return GAEService.getInstance().getEndPoint() + KVEntity.FUNC + "/" + key;
	}

	protected static KeyValueDatastore getInstance(AsyncHttpClient ahc) {
		if (ds == null) {
			return createInstance(ahc);
		}
		return ds;
	}

	protected synchronized static KeyValueDatastore createInstance(AsyncHttpClient ahc) {
		if (ds == null) {
			ds = new KeyValueDatastore(ahc);
		}
		return ds;
	}

	public KVEntity get(String key) {
		JsonObject obj;
		try {
			Future<Response> f = ahc.prepareGet(getKV_GetPath(key))
				.addQueryParameter(Token.PARAM_KEY, token.getKey())
				.setBody("")
				.execute();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException ex) {
			Logger.getLogger(GAEService.class.getName()).log(Level.SEVERE, null, ex);
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case TokenValidator.EC_TOKEN_ARGUMENT:
			case TokenValidator.EC_TOKEN_NOT_FOUND:
				throw new TokenNotFoundException();
			case TokenValidator.EC_TOKEN_EXPIRED:
				token = GAEService.getInstance().renewToken().getToken();
				return get(key);
			case KVEntityValidator.EC_KVENTITY_NOT_FOUND:
				return null;
			case AdminServiceValidator.EC_SERVICE_OK:
				return new KVEntity(obj.get(KVEntity.PARAM_KEY).getAsString(),
					obj.get(KVEntity.PARAM_NAME).getAsString(),
					Base64.decode(obj.get(KVEntity.PARAM_VALUE).getAsString()),
					new Date(obj.get(KVEntity.PARAM_DATE).getAsLong()));
			default:
				throw new ServiceOperationException();
		}
	}

	public boolean put(String key, String name, byte[] value) {
		if (key == null || name == null || value == null) {
			throw new ServiceArgumentException();
		}
		JsonObject obj;
		try {
			Future<Response> f = ahc.preparePost(getKV_PostPath(key))
				.addQueryParameter(Token.PARAM_KEY, token.getKey())
				.addQueryParameter(KVEntity.PARAM_NAME, name)
				.setBody(Base64.encode(value))
				.execute();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException ex) {
			Logger.getLogger(GAEService.class.getName()).log(Level.SEVERE, null, ex);
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case TokenValidator.EC_TOKEN_ARGUMENT:
			case TokenValidator.EC_TOKEN_NOT_FOUND:
				throw new TokenNotFoundException();
			case TokenValidator.EC_TOKEN_EXPIRED:
				token = GAEService.getInstance().renewToken().getToken();
				return put(key, name, value);
			case AdminServiceValidator.EC_SERVICE_OK:
				return true;
			default:
				throw new ServiceOperationException();
		}
	}

	public boolean delete(String key) {
		JsonObject obj;
		try {
			Future<Response> f = ahc.prepareDelete(getKV_DeletePath(key))
				.addQueryParameter(Token.PARAM_KEY, token.getKey())
				.execute();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException ex) {
			Logger.getLogger(GAEService.class.getName()).log(Level.SEVERE, null, ex);
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case TokenValidator.EC_TOKEN_ARGUMENT:
			case TokenValidator.EC_TOKEN_NOT_FOUND:
				throw new TokenNotFoundException();
			case TokenValidator.EC_TOKEN_EXPIRED:
				token = GAEService.getInstance().renewToken().getToken();
				return delete(key);
			case KVEntityValidator.EC_KVENTITY_NOT_FOUND:
				throw new KVEntityNotFoundException();
			case AdminServiceValidator.EC_SERVICE_OK:
				return true;
			default:
				throw new ServiceOperationException();
		}
	}
}

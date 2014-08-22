/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yetiz.gae.gaeservice.ds.Token;
import org.yetiz.gae.gaeservice.ds.User;
import org.yetiz.gae.gaeservice.exception.ServiceConfigureFileNotFoundException;
import org.yetiz.gae.gaeservice.exception.ServiceOperationException;
import org.yetiz.gae.gaeservice.exception.TokenArgumentException;
import org.yetiz.gae.gaeservice.exception.TokenExpiredException;
import org.yetiz.gae.gaeservice.exception.TokenNotFoundException;
import org.yetiz.gae.gaeservice.exception.UserNotFoundException;
import org.yetiz.gae.gaeservice.run.MainClass;
import org.yetiz.gae.gaeservice.validator.AdminServiceValidator;
import org.yetiz.gae.gaeservice.validator.TokenValidator;
import org.yetiz.gae.gaeservice.validator.UserValidator;

/**
 *
 * @author yeti
 */
public class GAEService {

	protected static String ADKEY;
	private String endPoint = null;
	private static GAEService gaes = null;
	private GAEAdmin gaea = null;
	private KeyValueDatastore kvds = null;
	private User user = null;
	private Token token = null;
	private final AsyncHttpClient ahc;

	private GAEService() {
		loadConfigureFile();
		ahc = new AsyncHttpClient();
	}

	private void loadConfigureFile() {
		try {
			ADKEY = new JsonParser()
				.parse(
					new InputStreamReader(
						new MainClass()
						.getClass()
						.getClassLoader()
						.getResourceAsStream("config.json")))
				.getAsJsonObject()
				.get("ADKey")
				.getAsString();
		} catch (JsonIOException | JsonSyntaxException e) {
			throw new ServiceConfigureFileNotFoundException();
		}
	}

	public static GAEService getInstance() {
		if (gaes == null) {
			return createInstance();
		}
		return gaes;
	}

	protected synchronized static GAEService createInstance() {
		if (gaes == null) {
			gaes = new GAEService();
		}
		return gaes;
	}

	public synchronized GAEAdmin getGAEAdmin() {
		if (gaea == null) {
			gaea = new GAEAdmin(ahc).setEndPoint(endPoint);
		}
		return gaea;
	}

	public KeyValueDatastore getKeyValueDatastore() {
		if (user == null) {
			throw new UserNotFoundException();
		}
		checkToken();
		if (kvds == null) {
			kvds = KeyValueDatastore.getInstance(ahc);
			kvds.setEndPoint(endPoint)
				.setToken(token);
		}
		return kvds;
	}

	protected GAEService checkToken() {
		if (token == null) {
			token = getToken();
		}
		if (token.isExpire()) {
			token = renewToken().getToken();
		}
		return this;
	}

	public User getUser() {
		return user;
	}

	public GAEService setUser(User user) {
		this.user = user;
		return this;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public GAEService setEndPoint(String endPointPath) {
		this.endPoint = "/".equals(endPointPath.substring(endPointPath.length() - 1, endPointPath.length()))
			? endPointPath : endPointPath.concat("/");
		return this;
	}

	protected Token getToken() {
		if (token != null) {
			if (token.getUserID().equals(user.getUserID())) {
				if (token.isExpire()) {
					renewToken();
				}
				return token;
			}
		}
		JsonObject obj;
		try {
			Future<Response> f = ahc.prepareGet(GAEService.getInstance().getEndPoint() + Token.FUNC)
				.addQueryParameter(User.PARAM_USERID, user.getUserID())
				.addQueryParameter(User.PARAM_PASSPHRASE, user.getPassphrase())
				.execute();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException ex) {
			Logger.getLogger(GAEService.class.getName()).log(Level.SEVERE, null, ex);
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case UserValidator.EC_USER_NOT_FOUND:
				throw new UserNotFoundException();
			case AdminServiceValidator.EC_SERVICE_OK:
				token = new Token(obj.get(Token.PARAM_KEY).getAsString(), user.getUserID(), new Date(obj.get(Token.PARAM_EXPIRE).getAsLong()));
				break;
		}
		return token;
	}

	public GAEService setToken(Token token) {
		this.token = token;
		return this;
	}

	/**
	 *
	 * @return
	 */
	protected GAEService renewToken() {
		JsonObject obj;
		try {
			Future<Response> f = ahc.preparePost(GAEService.getInstance().getEndPoint() + Token.FUNC + "/" + token.getKey())
				.setBody("")
				.execute();
			obj = (JsonObject) new JsonParser().parse(f.get().getResponseBody());
		} catch (IOException | InterruptedException | ExecutionException | JsonSyntaxException ex) {
			Logger.getLogger(GAEService.class.getName()).log(Level.SEVERE, null, ex);
			throw new ServiceOperationException();
		}
		switch (AdminServiceValidator.getStatus(obj)) {
			case TokenValidator.EC_TOKEN_ARGUMENT:
			case UserValidator.EC_USER_NOT_FOUND:
			case UserValidator.EC_USER_ARGUMENT:
			case UserValidator.EC_USER_UNAUTHORIZED:
				throw new TokenArgumentException();
			case TokenValidator.EC_TOKEN_NOT_FOUND:
				throw new TokenNotFoundException();
			case TokenValidator.EC_TOKEN_EXPIRED:
				throw new TokenExpiredException();
			case AdminServiceValidator.EC_SERVICE_OK:
				token = new Token(obj.get(Token.PARAM_KEY).getAsString(), token.getUserID(), new Date(obj.get(Token.PARAM_EXPIRE).getAsLong()));
				break;
		}
		return this;
	}

	public void close() {
		ahc.close();
	}
}

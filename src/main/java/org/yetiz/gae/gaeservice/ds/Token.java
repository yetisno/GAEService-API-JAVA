/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.ds;

import java.util.Date;

/**
 *
 * @author yeti
 */
public class Token {

	public static final String FUNC = "tkn";
	public static final String PARAM_KEY = "tkn";
	public static final String PARAM_EXPIRE = "t";

	protected String key; //PARAM_KEY
	protected String userID; //PARAM_USERID
	protected Date expire; //PARAM_EXPIRE

	public Token(String key, String userID, Date expire) {
		this.key = key;
		this.userID = userID;
		this.expire = expire;
	}

	public String getKey() {
		return key;
	}

	public String getUserID() {
		return userID;
	}

	public boolean isExpire() {
		return new Date().getTime() > expire.getTime();
	}

	public long getExpire() {
		return expire.getTime();
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

}

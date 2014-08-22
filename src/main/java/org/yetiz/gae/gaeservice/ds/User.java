/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.ds;

/**
 *
 * @author yeti
 */
public class User {

	public static final String FUNC = "users";
	public static final String PARAM_USERID = "ui";
	public static final String PARAM_PASSPHRASE = "pa";

	protected String userID; //PARAM_USERID
	private String passphrase; //PARAM_PASSPHRASE

	public User(String userID, String passphrase) {
		this.userID = userID;
		this.passphrase = passphrase;
	}

	public String getUserID() {
		return userID;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public String getPassphrase() {
		return passphrase;
	}

}

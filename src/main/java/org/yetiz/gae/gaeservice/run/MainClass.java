/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yetiz.gae.gaeservice.run;

import org.yetiz.gae.gaeservice.GAEService;
import org.yetiz.gae.gaeservice.ds.KVEntity;
import org.yetiz.gae.gaeservice.ds.User;

/**
 *
 * @author yeti
 */
public class MainClass {

	public static void main(String[] args) {
		GAEService gaes = GAEService.getInstance()
			.setEndPoint("http://myApplicationID.appspot.com/");
		gaes.setToken(gaes.getGAEAdmin().addUser("yeti", "aaa"));
		gaes.setUser(new User("yeti", "aaa"));
		gaes.getKeyValueDatastore().put("abc", "nabc", "aaaaa".getBytes());
		gaes.getKeyValueDatastore().put("abasc", "nabc", "aaaaa".getBytes());
		gaes.getKeyValueDatastore().put("abdc", "nabc", "aaaaa".getBytes());
		gaes.getKeyValueDatastore().put("abac", "nabc", "aaaaa".getBytes());
		System.out.println(gaes.getKeyValueDatastore().get("abc").getKey());
		System.out.println(gaes.getKeyValueDatastore().get("abasc").getKey());
		System.out.println(gaes.getKeyValueDatastore().get("abdc").getKey());
		System.out.println(gaes.getKeyValueDatastore().get("abac").getKey());
		gaes.getKeyValueDatastore().delete("abac");
		gaes.getKeyValueDatastore().delete("abdc");
		gaes.getKeyValueDatastore().delete("abasc");
		gaes.getKeyValueDatastore().delete("abc");
		gaes.getGAEAdmin().deleteUser("yeti");
		gaes.close();
	}
}

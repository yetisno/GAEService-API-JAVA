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
public class KVEntity {

	public static final String FUNC = "kv";
	public static final String PARAM_KEY = "k";
	public static final String PARAM_NAME = "n";
	public static final String PARAM_VALUE = "v";
	public static final String PARAM_DATE = "t";

	protected String key;
	protected String name;
	protected byte[] value;
	protected Date date;

	public KVEntity(String key, String name, byte[] value, Date date) {
		this.key = key;
		this.name = name;
		this.value = value;
		this.date = date;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

}

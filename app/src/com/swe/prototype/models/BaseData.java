package com.swe.prototype.models;

import java.io.Serializable;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;

public abstract class BaseData implements Serializable {

	protected AccountBase account = null;

	public BaseData(AccountBase account) {
		this.account = account;
	}

	public void setAccount(AccountBase account) {
		this.account = account;
	}

	public AccountBase getAccount() {
		return this.account;
	}
	
	/**
	 * returns true if the dataset is up to date.
	 * A Data object is considered up to date
	 * if the local data matches the data on the server
	 */
	public abstract boolean isUpToDate();

	/**
	 * get special gson instance that resolves circular dependencies
	 * 
	 * @return
	 */
	public static Gson getGsonInstance() {
		return new GsonBuilder()
				.setExclusionStrategies(new ExclusionStrategy() {

					public boolean shouldSkipClass(Class<?> clazz) {
						return (clazz == AccountBase.class
								|| clazz == Context.class
								|| clazz == Activity.class);
					}

					/**
					 * Custom field exclusion goes here
					 */
					public boolean shouldSkipField(FieldAttributes f) {
						return false;
					}

				})
				/**
				 * Use serializeNulls method if you want To serialize null
				 * values By default, Gson does not serialize null values
				 */
				.serializeNulls().create();
	}

}

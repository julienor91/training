package com.sdzee.tp.pro.beans;

public class Utilisateur {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private String userEmail;
	private String userPassword;
	private String username;
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
	public Utilisateur() {
		super();
	}

	public Utilisateur(String userEmail, String userPassword, String username) {
		super();
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.username = username;
	}

	/*
	 * ********** GETTER METHODS **************************************************
	 */
	public String getUserEmail() {
		return userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUsername() {
		return username;
	}

	/*
	 * ********** SETTER METHODS **************************************************
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * ********** TOSTRING METHODS **************************************************
	 */
	@Override
	public String toString() {
		return "Utilisateur [userEmail=" + userEmail + ", userPassword="
				+ userPassword + ", username=" + username + "]";
	}
	
}

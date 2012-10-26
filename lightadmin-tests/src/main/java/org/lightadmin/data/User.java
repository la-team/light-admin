package org.lightadmin.data;

public enum User {

	ADMINISTRATOR( "admin", "admin" ),
	INVALID_USER( "invalidUser", "invalidPwd" );

	private String login;
	private String password;

	User( String login, String password ) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
}

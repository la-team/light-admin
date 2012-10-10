package data;

public enum User {

    admin ("admin", "admin"),
    invalidUser ( "invalidUser", "invalidPwd");

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

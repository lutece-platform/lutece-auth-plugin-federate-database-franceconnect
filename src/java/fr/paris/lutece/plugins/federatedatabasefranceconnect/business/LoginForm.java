package fr.paris.lutece.plugins.federatedatabasefranceconnect.business;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginForm {

     @NotEmpty( message = "federatedatabasefranceconnect.errors.loginEmpty" )
	private String _strLogin;
     @NotEmpty( message = "federatedatabasefranceconnect.errors.passwordEmpty" )
	private String _strPassword;
	
	
	public String getLogin() {
		return _strLogin;
	}
	public void setLogin(String _strLogin) {
		this._strLogin = _strLogin;
	}
	public String getPassword() {
		return _strPassword;
	}
	public void setPassword(String _strPassword) {
		this._strPassword = _strPassword;
	}
	

}

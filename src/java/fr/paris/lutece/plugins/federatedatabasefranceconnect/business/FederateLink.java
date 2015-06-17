package fr.paris.lutece.plugins.federatedatabasefranceconnect.business;

public class FederateLink {

	private String _strLuteceUserName;
	private String _strFederateKey;
	
	
	public String getLuteceUserName() {
		return _strLuteceUserName;
	}
	public void setLuteceUserName(String _strLuteceUserName) {
		this._strLuteceUserName = _strLuteceUserName;
	}
	public String getFederateKey() {
		return _strFederateKey;
	}
	public void setFederateKey(String _strFederateKey) {
		this._strFederateKey = _strFederateKey;
	}
}

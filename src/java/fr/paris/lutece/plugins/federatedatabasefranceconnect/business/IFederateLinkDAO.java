package fr.paris.lutece.plugins.federatedatabasefranceconnect.business;

import fr.paris.lutece.portal.service.plugin.Plugin;


/*
 * IFederateLinkDAO
 */
public interface IFederateLinkDAO {

	

	void insert(FederateLink federateLink, Plugin plugin);

	void delete(String strLuteceUserName, Plugin plugin);

	
	FederateLink loadByLuteceUserName(String strLuteceUserName, Plugin plugin);



	FederateLink loadByFederateKey(String strFederateKey, Plugin plugin);

}
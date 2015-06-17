package fr.paris.lutece.plugins.federatedatabasefranceconnect.service;

import fr.paris.lutece.plugins.federatedatabasefranceconnect.business.FederateLink;
import fr.paris.lutece.plugins.federatedatabasefranceconnect.business.IFederateLinkDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public final  class FederateLinkService {

	
	private static final String PLUGIN_NAME="federatedatabasefranceconnect";
	private static final String BEAN_FEDERATE_LINK_DAO ="federatedatabasefranceconnect.federateLinkDAO";
	
	private static FederateLinkService _singleton;
	private  static IFederateLinkDAO _dao;
	private static Plugin _plugin; 
	 
	public static FederateLinkService getInstance()
	{
		if(_singleton==null)
		{
			
			_singleton=new FederateLinkService();
			_dao=SpringContextService.getBean(BEAN_FEDERATE_LINK_DAO);
			_plugin=PluginService.getPlugin( PLUGIN_NAME );
		}
		
		return _singleton;
		
		
	}
	
	 
	
	public  FederateLink getFederateLinkByLuteceUserName(String strLuteceUserName)
	{
		
		return _dao.loadByLuteceUserName(strLuteceUserName, _plugin);
		
		
	}
	
	public  FederateLink getFederateLinkByFederateKey(String strFederateKey)
	{
		
		return _dao.loadByFederateKey(strFederateKey, _plugin);
		
	}
	
	
	public  void addFederateLink(String strLuteceUserName,String strFederateKey)
	{
		FederateLink federateLink= new FederateLink();
		federateLink.setLuteceUserName(strLuteceUserName);
		federateLink.setFederateKey(strFederateKey);
		
		 _dao.insert(federateLink, _plugin);
	}
	
	
	public  void removeFederateLinkByLuteceUserName(String strLuteceUserName)
	{
		
		_dao.delete(strLuteceUserName, _plugin);
		
	}
	
	
	
	

}

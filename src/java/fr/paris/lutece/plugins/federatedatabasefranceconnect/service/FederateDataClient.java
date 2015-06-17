package fr.paris.lutece.plugins.federatedatabasefranceconnect.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import fr.paris.lutece.plugins.federatedatabasefranceconnect.business.FederateLink;
import fr.paris.lutece.plugins.franceconnect.oidc.Token;
import fr.paris.lutece.plugins.franceconnect.oidc.UserInfo;
import fr.paris.lutece.plugins.franceconnect.oidc.dataclient.AbstractDataClient;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.BaseUser;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.business.DatabaseHome;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.service.DatabasePlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class FederateDataClient extends AbstractDataClient {
	private static ObjectMapper _mapper;

	static {
		_mapper = new ObjectMapper();
		_mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void handleToken(Token token, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserInfo userInfo = parse(getData(token));
			String strFederateKey=userInfo!=null ? userInfo.getSub():null;
			if ( !StringUtils.isEmpty(strFederateKey)
					&& FederateLinkService
							.getInstance().getFederateLinkByFederateKey(strFederateKey) != null) {
				
				FederateLink federateLink=FederateLinkService
				.getInstance().getFederateLinkByFederateKey(strFederateKey);
				LuteceAuthentication baseAuthentication = (LuteceAuthentication) SpringContextService.getBean( 
		                    "mylutece-database.authentication" );
				 
				 
				 BaseUser user = DatabaseHome.findLuteceUserByLogin( federateLink.getLuteceUserName(), PluginService.getPlugin( DatabasePlugin.PLUGIN_NAME ), baseAuthentication );

				if(user != null)
				{
						FederateService.addFederateKeyInUserInfo(user, strFederateKey);
						SecurityService.getInstance(  ).registerUser( request, user );
						FederateService.redirect(request, response);
				}
				else
				{
					AppLogService.info( "Unable to find user in the database : " + federateLink.getLuteceUserName() );
					
				
				
					FederateService.saveSessionFederateKey(request, strFederateKey);
					FederateService.redirectFederateView(request, response);
				}

			}
			else
			{
				LuteceUser registerUser=SecurityService.getInstance().getRegisteredUser(request);
				if(registerUser!=null && !StringUtils.isEmpty(strFederateKey))
				{
					FederateLinkService.getInstance().addFederateLink(registerUser.getName(), strFederateKey);
					FederateService.addFederateKeyInUserInfo(registerUser, strFederateKey);
				}
				FederateService.saveSessionFederateKey(request, strFederateKey);
				FederateService.redirectFederateView(request, response);
			}

		} catch (IOException ex) {
			_logger.error("Error parsing UserInfo ", ex);
		}
	}

	/**
	 * parse the JSON for a token
	 * 
	 * @param strJson
	 *            The JSON
	 * @return The UserInfo
	 * @throws java.io.IOException
	 *             if an error occurs
	 */
	UserInfo parse(String strJson) throws IOException {
		return _mapper.readValue(strJson, UserInfo.class);
	}
}

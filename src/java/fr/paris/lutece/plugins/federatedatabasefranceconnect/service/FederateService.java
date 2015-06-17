package fr.paris.lutece.plugins.federatedatabasefranceconnect.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.PortalJspBean;

public final class FederateService {

	private static final String PROPERTY_FEDERATE_VIEW_URL = "federatedatabasefranceconnect.federateViewUrl";
	private static final String LUTECE_USER_FEDERATE_KEY = "franceconnect.federateKey";
	
	
	private static final String SESSION_KEY_FEDERATE_KEY = "federate_key";

	/**
	 * redirect after login or logout
	 * 
	 * @param request
	 *            The HTTP request
	 * @param response
	 *            The HTTP response
	 * @throws IOException
	 *             if an error occurs
	 */
	public static void redirect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	

		response.sendRedirect(getRedirectUrl(request));
	}
	
	public static String  getRedirectUrl(HttpServletRequest request) {
		String strNextURL = PortalJspBean.getLoginNextUrl(request);

		if (strNextURL == null) {
			strNextURL = SecurityService.getInstance().getLoginPageUrl();
		}

		return strNextURL;
	}

	
	

	/**
	 * redirect after login or logout
	 * 
	 * @param request
	 *            The HTTP request
	 * @param response
	 *            The HTTP response
	 * @throws IOException
	 *             if an error occurs
	 */
	public static void redirectFederateView(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {

		String strFederateViewURL = AppPropertiesService
				.getProperty(PROPERTY_FEDERATE_VIEW_URL);
		
		response.sendRedirect(AppPathService.getBaseUrl(request)+strFederateViewURL);
	}

	public static void saveSessionFederateKey(HttpServletRequest request,
			String strFederateKey) {
		request.getSession(true).setAttribute(SESSION_KEY_FEDERATE_KEY,
				strFederateKey);

	}

	public static String getSessionFederateKey(HttpServletRequest request) {

		return request.getSession(true).getAttribute(SESSION_KEY_FEDERATE_KEY) != null ? (String) request
				.getSession(true).getAttribute(SESSION_KEY_FEDERATE_KEY) : null;

	}
	
	public static void addFederateKeyInUserInfo(LuteceUser user,String strFederateKey) {

			user.setUserInfo(LUTECE_USER_FEDERATE_KEY, strFederateKey);

	}

	
	

}

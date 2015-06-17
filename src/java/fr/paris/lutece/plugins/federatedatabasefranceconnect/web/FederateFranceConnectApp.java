/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.federatedatabasefranceconnect.web;

import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.federatedatabasefranceconnect.business.LoginForm;
import fr.paris.lutece.plugins.federatedatabasefranceconnect.service.FederateLinkService;
import fr.paris.lutece.plugins.federatedatabasefranceconnect.service.FederateService;
import fr.paris.lutece.plugins.mylutece.modules.database.authentication.BaseAuthentication;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;


/**
 * France Connect XPage Application
 */
@Controller( xpageName = "federateFranceConnect", pagePathI18nKey = "federatedatabasefranceconnect.federatePagePath", pageTitleI18nKey = "federatedatabasefranceconnect.federatePageTitle" )
public class FederateFranceConnectApp extends MVCApplication
{
	
	//Action
    private static final String ACTION_SAVE_FEDERATION = "saveFederation";
    private static final String ACTION_REMOVE_FEDERATION = "removeFederation";
	// Views
    private static final String VIEW_FEDERATION = "getFederation";
    private static final String VIEW_REMOVE_FEDERATION = "getRemoveFederation";
    private static final String VIEW_HOME = "getHome";
    
    

    // Templates
    private static final String TEMPLATE_HOME = "skin/plugins/federatedatabasefranceconnect/home.html";
    
    private static final String TEMPLATE_FEDERATE_FORM = "skin/plugins/federatedatabasefranceconnect/federate_form.html";
    private static final String TEMPLATE_REMOVE_FEDERATE_LINK_FORM = "skin/plugins/federatedatabasefranceconnect/remove_federate_link_form.html";
    
    //ERRORS
    private static final String  ERROR_NO_FEDERATE_KEY="federatedatabasefranceconnect.errors.noFederateKey";
    private static final String  ERROR_LOGIN_FORM="federatedatabasefranceconnect.errors.loginForm";
    private static final String  ERROR_REMOVE_FEDERATION="federatedatabasefranceconnect.errors.removeFederation";
    
    //ERRORS
    private static final String INFO_FEDERATE_SUCCESS="federatedatabasefranceconnect.infos.federateSuccess";
    private static final String INFO_REMOVE_FEDERATION_SUCCESS="federatedatabasefranceconnect.infos.removeFederationSuccess";
    //MARKER
    private static final String MARK_BACK_URL="back_url";
    private static final String MARK_USER="user";
    private static final String MARK_FEDERATE_LINK="federate_link";
    
    

    private LoginForm _loginForm;
    private String _strBackUrl;
    
    
    
    /**
     * Build the Login page
     * @param request The HTTP request
     * @return The XPage object containing the page content
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage getHomePage( HttpServletRequest request )
    {
        Map<String, Object> model = getModel(  );
        LuteceUser user=SecurityService.getInstance().getRegisteredUser(request);
        if(user!=null)
        {	
        	model.put(MARK_USER, user);
        	model.put(MARK_FEDERATE_LINK,FederateLinkService.getInstance().getFederateLinkByLuteceUserName(user.getName()));
        }
        
        return getXPage( TEMPLATE_HOME, request.getLocale(  ), model );
    }
    
    
    /**
     * Build the Login page
     * @param request The HTTP request
     * @return The XPage object containing the page content
     */
    @View( value = VIEW_FEDERATION )
    public XPage getFederationPage( HttpServletRequest request )
    {  
    	LuteceUser user=SecurityService.getInstance().getRegisteredUser(request);
    	if(user!=null && FederateLinkService.getInstance().getFederateLinkByLuteceUserName(user.getName())!=null)
        {
        	
        	addInfo(INFO_FEDERATE_SUCCESS,getLocale(request));
        	_strBackUrl=FederateService.getRedirectUrl(request);
      
         }
        Map<String, Object> model = getModel(  );

        String strKey=FederateService.getSessionFederateKey(request);
        if(StringUtils.isEmpty(strKey))
        {
        	addError(ERROR_NO_FEDERATE_KEY, getLocale(request));
        	
        }
        
        
        
        	
        model.put(MARK_BACK_URL, _strBackUrl);

       return getXPage( TEMPLATE_FEDERATE_FORM, request.getLocale(  ), model );
    }
    
    
    @Action( ACTION_SAVE_FEDERATION )
    public XPage doSaveFederation( HttpServletRequest request )
    {
        
    	_loginForm=new LoginForm();
    	populate( _loginForm, request );
    	
        // Check constraints
        if ( !validateBean( _loginForm,getLocale(request)) )
        {
            return redirectView( request, VIEW_FEDERATION);
        }
        
        
        BaseAuthentication baseAuthentication = (BaseAuthentication) SpringContextService.getBean( 
                "mylutece-database.authentication" );
        
        
        LuteceUser luteceUser=null;
		try {
			luteceUser = baseAuthentication.login(_loginForm.getLogin(), _loginForm.getPassword(), request);
		} catch (LoginException e) {
		
			AppLogService.debug(e);
		}
        
        String strKey=FederateService.getSessionFederateKey(request);
        
        if ( luteceUser != null && !StringUtils.isEmpty(strKey))
        {
        	if(FederateLinkService.getInstance().getFederateLinkByFederateKey(strKey)==null)
        	{
        		FederateLinkService.getInstance().addFederateLink(luteceUser.getName(), strKey);
        	}
        	SecurityService.getInstance().registerUser(request, luteceUser);
            
       }
        else
        {
            addError( ERROR_LOGIN_FORM,getLocale(request));
        }
       
        
           return redirectView( request, VIEW_FEDERATION);
    }
    
    
    @View( value = VIEW_REMOVE_FEDERATION)
    public XPage getRemoveFederationPage( HttpServletRequest request )
    {
        Map<String, Object> model = getModel(  );
        
        return getXPage( TEMPLATE_REMOVE_FEDERATE_LINK_FORM, request.getLocale(  ), model );
    }
    
    
    @Action( ACTION_REMOVE_FEDERATION )
    public XPage doRemoveFederation( HttpServletRequest request )
    {
        
   

       
        LuteceUser luteceUser= SecurityService.getInstance().getRegisteredUser(request);
        
        if(luteceUser!=null)
        {
        	FederateLinkService.getInstance().removeFederateLinkByLuteceUserName(luteceUser.getName());

        	addInfo(INFO_REMOVE_FEDERATION_SUCCESS,getLocale(request));

        }
        
        else
        {
            addError( ERROR_REMOVE_FEDERATION,getLocale(request));
        }
       
        return redirectView( request, VIEW_REMOVE_FEDERATION );
    }
    
    
    
    
    
    
    
    
    
    
   
}

package fr.paris.lutece.plugins.federatedatabasefranceconnect.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class FederateLinkDAO implements IFederateLinkDAO {


	 private static final String SQL_QUERY_SELECT_BY_LUTECE_USER_NAME = "SELECT lutece_user_name,federate_key FROM federate_fc_link WHERE lutece_user_name = ?";
	 private static final String SQL_QUERY_SELECT_BY_FEDERATE_KEY = "SELECT lutece_user_name,federate_key FROM federate_fc_link WHERE federate_key = ?";
	 private static final String SQL_QUERY_INSERT= "INSERT INTO  federate_fc_link(lutece_user_name,federate_key) VALUES (?,?)";
	 private static final String SQL_QUERY_DELETE_BY_LUTECE_USER_NAME ="DELETE  FROM federate_fc_link WHERE lutece_user_name = ?";
			
	 
	 /**
		 * {@inheritDoc }
		 */
	 @Override
	  public void insert( FederateLink federateLink, Plugin plugin )
	  {
		  DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
		  daoUtil.setString( 1, federateLink.getLuteceUserName() );
	      daoUtil.setString( 2, federateLink.getFederateKey() );
	    
	        daoUtil.executeUpdate(  );
	        daoUtil.free(  );
		  
	  }
	 
	 /**
		 * {@inheritDoc }
		 */
	 @Override
	  public void delete( String strLuteceUserName, Plugin plugin )
	  {
		  DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_LUTECE_USER_NAME, plugin );
		  daoUtil.setString( 1, strLuteceUserName);
		  daoUtil.executeUpdate(  );
	      daoUtil.free(  );
		  
	  }
	  
	 
	 	/**
		 * {@inheritDoc }
		 */
	 	@Override
	    public FederateLink loadByLuteceUserName( String strLuteceUserName, Plugin plugin )
	    {
	        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_LUTECE_USER_NAME, plugin );
	        daoUtil.setString( 1, strLuteceUserName );
	        daoUtil.executeQuery(  );

	        FederateLink federateLink = null;

	        if ( daoUtil.next(  ) )
	        {
	           federateLink = new FederateLink(  );
	           federateLink.setLuteceUserName(daoUtil.getString(1));
	           federateLink.setFederateKey(daoUtil.getString(2));
	        }

	        daoUtil.free(  );

	        return federateLink;
	    }
	    
	    /**
	     * {@inheritDoc }
	     */
	
	    public FederateLink loadByFederateKey( String strFederateKey, Plugin plugin )
	    {
	        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_FEDERATE_KEY, plugin );
	        daoUtil.setString( 1, strFederateKey );
	        daoUtil.executeQuery(  );

	        FederateLink federateLink = null;

	        if ( daoUtil.next(  ) )
	        {
	           federateLink = new FederateLink(  );
	           federateLink.setLuteceUserName(daoUtil.getString(1));
	           federateLink.setFederateKey(daoUtil.getString(2));
	        }

	        daoUtil.free(  );

	        return federateLink;
	    }
	  
	  
	  
}

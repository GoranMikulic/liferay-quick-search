package mikugo.dev.search;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;


public class ConfigrationActionImpl extends DefaultConfigurationAction {
    
    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        super.processAction(portletConfig, actionRequest, actionResponse);
        
	// PortletPreferences prefs = actionRequest.getPreferences();
	//
	// String keyControlled = prefs.getValue("keyControlled", "true");
        
    }
   
}

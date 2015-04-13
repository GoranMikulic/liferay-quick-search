package mikugo.dev.search;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import mikugo.dev.search.helper.Utils;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

public class ConfigrationActionImpl extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
	    throws Exception {

	super.processAction(portletConfig, actionRequest, actionResponse);

	PortletPreferences prefs = actionRequest.getPreferences();

	// Array is by default saved in one String, splitting String to serve it
	// for presentation
	prefs.setValues(Utils.CONFIGURATION_ASSET_TYPES, prefs.getValue(Utils.CONFIGURATION_ASSET_TYPES, "").split(","));
	prefs.store();
    }

}

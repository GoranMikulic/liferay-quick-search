<%@include file="/init.jsp"%>
<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationURL" />

<%
    boolean keyControlled = GetterUtil.getBoolean(portletPreferences.getValue(
		    Utils.CONFIGURATION_KEY_CONTROLLED, StringPool.TRUE));

    int minSearchLetters = GetterUtil.getInteger(portletPreferences.getValue(
		    Utils.CONFIGURATION_MIN_SEARCH_LETTERS, "3"));
    
    int maximumSearchResults = GetterUtil.getInteger(portletPreferences.getValue(
	    Utils.CONFIGURATION_MAXIMUM_SEARCH_RESULTS, "5"));
    
    String[] assetTypes = GetterUtil.getStringValues(portletPreferences.getValues(
		    Utils.CONFIGURATION_ASSET_TYPES, AssetTypes.getAllClassNames()));
%>

<aui:form action="<%=configurationURL%>" method="post" name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />

	<aui:input name="preferences--keyControlled--" type="checkbox"
		value="<%=keyControlled%>" label="config.keyControlled" />
	
	<aui:select name="preferences--minSearchLetters--"
		label="config.minSearchLetters">
		<%
		    for (int i = 1; i < 6; i++) {
		%>
		<aui:option label="<%=i%>" selected="<%=i == minSearchLetters%>"
			value="<%=i%>" />
		<%
		    }
		%>
	</aui:select>
	<aui:select name="preferences--maximumSearchResults--"
		label="config.maximumSearchResults">
		<%
		    for (int i = 1; i < 16; i++) {
		%>
		<aui:option label="<%=i%>" selected="<%=i == maximumSearchResults%>"
			value="<%=i%>" />
		<%
		    }
		%>
	</aui:select>
	<aui:select name="preferences--assetTypes--"
		label="config.minSearchLetters" multiple="true">
		<%
			Map<String, String> map = new HashMap<String, String>();
			for(AssetTypes assetType : AssetTypes.values()){
			    map.put(assetType.getReadableName(), assetType.getClassName());
			    
			}
		
		    for (Map.Entry<String,String> entry : map.entrySet()) {
		%>
			<aui:option label="<%=entry.getKey()%>"
				selected='<%=ArrayUtil.contains(assetTypes, entry.getValue())%>'
				value="<%=entry.getValue()%>" />
		<%
		    }
		%>
	</aui:select>
	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>
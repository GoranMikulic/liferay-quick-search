<%@page import="mikugo.dev.search.Utils"%>
<%@include file="/init.jsp"%>
<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationURL" />

<%
    boolean keyControlled = GetterUtil
		    .getBoolean(portletPreferences.getValue(Utils.CONFIGURATION_KEY_CONTROLLED, StringPool.TRUE));
	
	int minSearchLetters = GetterUtil.getInteger(portletPreferences.getValue(Utils.CONFIGURATION_MIN_SEARCH_LETTERS, "3"));
%>

<aui:form action="<%=configurationURL%>" method="post" name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />

	<aui:input name="preferences--keyControlled--" type="checkbox"
		value="<%=keyControlled%>" />
	<aui:input name="preferences--minSearchLetters--" type="text"
		value="<%=minSearchLetters%>" />

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>
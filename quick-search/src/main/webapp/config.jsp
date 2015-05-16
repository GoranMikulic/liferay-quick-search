<%@include file="/init.jsp"%>
<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationURL" />

<%
    boolean keyControlled = GetterUtil.getBoolean(portletPreferences.getValue(
		    Utils.CONFIGURATION_KEY_CONTROLLED, StringPool.TRUE));

    String[] assetTypes = GetterUtil.getStringValues(portletPreferences.getValues(
		    Utils.CONFIGURATION_ASSET_TYPES, AssetTypes.getAllClassNames()));

    Map<String, String> map = new HashMap<String, String>();

    for (AssetTypes assetType : AssetTypes.values()) {
		map.put(assetType.getReadableName(), assetType.getClassName());
    }

    pageContext.setAttribute("typeMap", map);
%>

<aui:form action="<%=configurationURL%>" method="post" name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />

	<aui:input name="preferences--keyControlled--" type="checkbox"
		value="<%=keyControlled%>" label="config.keyControlled" />

	<aui:select name="preferences--minSearchLetters--"
		label="config.minSearchLetters">
		<c:forEach var="i" begin="1" end="5">
			<aui:option label="${i}"
				selected="${i == portletPreferences.getValue(
		    'minSearchLetters', '2')}"
				value="${i}" />
		</c:forEach>
	</aui:select>

	<aui:select name="preferences--maximumSearchResults--"
		label="config.maximumSearchResults">
		<c:forEach var="i" begin="1" end="16">
			<aui:option label="${i}"
				selected="${i == portletPreferences.getValue(
		    'maximumSearchResults', '5')}"
				value="${i}" />
		</c:forEach>
	</aui:select>
	<aui:select name="preferences--assetTypes--"
		label="config.availableAssetTypes" multiple="true">
		<%
		    for (Map.Entry<String, String> entry : map.entrySet()) {
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
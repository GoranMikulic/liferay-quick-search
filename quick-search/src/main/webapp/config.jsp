<%@include file="/init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="true"
	var="configurationURL" />

<%
    //Configured Types
    String[] configuredAssetTypes = GetterUtil.getStringValues(portletPreferences.getValues(
		    Utils.CONFIGURATION_ASSET_TYPES, AssetTypes.getAllClassNames()));
	
    //Lists required for asset type selection 
    List<KeyValuePair> typesAvailableList = new ArrayList<KeyValuePair>();
    List<KeyValuePair> typesSelected = new ArrayList<KeyValuePair>();

    for (AssetTypes assetType : AssetTypes.values()) {
		
		if (ArrayUtil.contains(configuredAssetTypes, assetType.getClassName())) {
		    typesSelected.add(new KeyValuePair(assetType.getClassName(), assetType.getReadableName()));
		} else {
		    typesAvailableList.add(new KeyValuePair(assetType.getClassName(), assetType.getReadableName()));
		}
    }
%>

<aui:form action="<%=configurationURL%>" method="post" name="fm">
	<aui:input name="<%=Constants.CMD%>" type="hidden"
		value="<%=Constants.UPDATE%>" />
	
	<!-- Setting: Key Controlled -->
	<aui:input name="preferences--keyControlled--" type="checkbox"
		value="<%=GetterUtil.getBoolean(portletPreferences.getValue(
			    Utils.CONFIGURATION_KEY_CONTROLLED, StringPool.TRUE))%>"
		label="config.keyControlled" />
	
	<!-- Setting: Amount of letters to trigger search -->
	<aui:select name="preferences--minSearchLetters--"
		label="config.minSearchLetters">
		<c:forEach var="i" begin="1" end="5">
			<aui:option label="${i}"
				selected="${i == portletPreferences.getValue(
		    'minSearchLetters', '2')}"
				value="${i}" />
		</c:forEach>
	</aui:select>
	
	<!-- Setting: Maximum results displayed -->
	<aui:select name="preferences--maximumSearchResults--"
		label="config.maximumSearchResults">
		<c:forEach var="i" begin="1" end="16">
			<aui:option label="${i}"
				selected="${i == portletPreferences.getValue(
		    'maximumSearchResults', '5')}"
				value="${i}" />
		</c:forEach>
	</aui:select>
	
	<!-- Setting: Asset types available for search -->
	<aui:fieldset label="config.availableAssetTypes">
		<aui:input name="preferences--assetTypes--" type="hidden" />
		<liferay-ui:input-move-boxes leftBoxName="currentClassNameIds"
			leftList="<%=typesSelected%>" leftTitle="selected"
			rightBoxName="availableClassNameIds"
			rightList="<%=typesAvailableList%>" rightTitle="available" />
	</aui:fieldset>
	<aui:button-row>
		<aui:button
			onClick='<%= renderResponse.getNamespace() + "saveSelectBoxes();" %>'
			type="submit" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-base">	
	Liferay.provide(
			window,
			'<portlet:namespace />saveSelectBoxes',
			function() {
				if (document.<portlet:namespace />fm.<portlet:namespace />assetTypes) {
					document.<portlet:namespace />fm.<portlet:namespace />assetTypes.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />currentClassNameIds);
				}
				submitForm(document.<portlet:namespace />fm);
			},
			['liferay-util-list-fields']
		);
</aui:script>
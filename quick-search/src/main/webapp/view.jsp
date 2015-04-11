<%@include file="/init.jsp"%>
<%
    boolean keyControlled = GetterUtil.getBoolean(portletPreferences.getValue(Utils.CONFIGURATION_KEY_CONTROLLED, StringPool.TRUE));
	int minSearchLetters = GetterUtil.getInteger(portletPreferences.getValue(Utils.CONFIGURATION_MIN_SEARCH_LETTERS, "3"));
%>

<portlet:resourceURL var="searchContactsURL">
	<portlet:param name="action" value="search" />
</portlet:resourceURL>

<c:choose>
	<c:when test="<%=keyControlled%>">
		<liferay-ui:message key="info.keyControlledInstruction" />

		<script type="text/javascript">
			$(document).ready(function() {
				$(document).keydown(function(e) {
					// Open lightbox on ctrl + enter
					if (e.ctrlKey && e.keyCode == 13) {
						lightbox(<%=minSearchLetters%>);
					}
					// Close lightbox on  esc
					if (e.keyCode == 27) {
						closeLightbox();
					}
				});
			});
		</script>
	</c:when>
	<c:otherwise>
		<input type="text" name="myInputSearch" id="myInputSearch" />
		<script type="text/javascript">
			$(document).ready(
					function() {
						autocompleteAjax('${ searchContactsURL }', '#myInputSearch',<%=minSearchLetters%>);
					});
		</script>
	</c:otherwise>
</c:choose>

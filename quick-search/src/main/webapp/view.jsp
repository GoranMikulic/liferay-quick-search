<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>

<portlet:defineObjects />

<portlet:resourceURL var="searchContactsURL">
	<portlet:param name="action" value="search" />
</portlet:resourceURL>

<script type="text/javascript">
$(document).ready(function() {

	// URL ajax de recherche, Input field of the search

	$(document).keydown(function(e) {

		//ctrl + enter
		if (e.ctrlKey && e.keyCode == 13) {
			lightbox('#myInputSearch');
		}
		// esc
		if (e.keyCode == 27) {
			closeLightbox(); 		
		}
	});

});
</script>
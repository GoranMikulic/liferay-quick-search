<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>

<%@ page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@ page import="com.liferay.portal.kernel.util.StringPool"%>
<%@ page import="mikugo.dev.search.helper.Utils"%>
<%@ page import="mikugo.dev.search.helper.AssetTypes"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.liferay.portal.kernel.util.ArrayUtil"%>
<%@ page import="java.util.List"%>
<%@ page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@ page import="java.util.ArrayList"%>
<portlet:defineObjects />
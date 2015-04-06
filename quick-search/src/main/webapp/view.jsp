<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.js"></script>
<portlet:defineObjects />

<portlet:resourceURL var="getUsers">
	<portlet:param name="<%=Constants.CMD%>" value="get_users" />
</portlet:resourceURL>
<!-- Typeahead javascript bundle -->

<portlet:resourceURL var="searchContactsURL" >
	<portlet:param name="action" value="search" />
</portlet:resourceURL>

<input type="text" name="myInputSearch" id="myInputSearch" />

<script type="text/javascript">
$(document).ready(function() {

	// URL ajax de recherche, Input field of the search

	autocompleteAjax('${ searchContactsURL }', '#myInputSearch', 3);

});

/**

 * Autocomplete function using the "typeahead.js" plugin.

 * @param ajaxURL, URL of the search: return JSON data

 * @param inputField, input field to search datas

 */

function autocompleteAjax(ajaxURL, inputField, minLetters) {

	var autocompleteName = "results";

	// Cache for the results
	var searchCache = [];

	// To search again if we enter less than "minLetters" caracters: we have to clean the cache
	$(inputField).keyup(function() {

		if ($(this).val().length < minLetters) {
			searchCache = [];
		}

	});

	/**
	 * Function to search if a search pattern is already in the cache.
	 */
	var isInCache = function(pattern) {

		var substrRegex = new RegExp(pattern, 'i');

		var match = false;

		$.each(searchCache, function(i, result) {

			if (substrRegex.test(result.assetType)
			|| substrRegex.test(result.summary)) {
				match = true;
			}
		});

		return match;
	};

	/**
	 * Function to sort datas in the cache to keep only which matches with the search pattern.
	 */
	var sortResults = function(pattern) {
		var matches = [];
		var substrRegex = new RegExp(pattern, 'i');
		$.each(searchCache, function(i, result) {
			if (substrRegex.test(result.assetType)
			|| substrRegex.test(result.summary)) {
				matches.push(result);
			}

		});
		return matches;
	};

	/**
	 * Function to make an ajax call to search contacts. Only if the search pattern is not already in the cache.
	 */
	var ajaxAutocompleteSearch = function() {
		return function findMatches(searchPattern, comboBox) {
			//to remove useless white space
			searchPattern = $.trim(searchPattern).replace(/ {2,}/g, ' ');
			// If the search pattern is not in the cache make an ajax call to get the contacts
			if (!isInCache(searchPattern)) {
				$.ajax({
					type : "POST",
					async : false,
					dataType : "json",
					url : ajaxURL,
					data : {
						pattern : searchPattern
					},
					success : function(results) {
						// To stock the results in the cache
						searchCache = searchCache.concat(results);
						comboBox(results);// To display the suggestions
					}
				});
			} else {
				// To sort suggestions of the cache
				comboBox(sortResults(searchPattern));
			}
		};
	}
	
	// Initialisation of typeahead
	$(inputField).typeahead(
			{
				hint : true,
				highlight : true,
				minLength : minLetters
			},
			{
				name : autocompleteName,
				displayKey : 'email',
				templates : {
					empty : [ '<div class="empty-message">',
							'No asset found', '</div>' ].join('\n'),
					suggestion : function(result) {
						return "<a href='" + result.displayUrl + "'>" + result.title + " " + result.assetType + "</a>";
					}
				},
				source : ajaxAutocompleteSearch()
			});
}
</script>
function lightbox() {

	// add lightbox/shadow <div/>'s if not previously added
	if ($('#lightbox').size() == 0) {
		var theLightbox = $('<div id="lightbox"/>');
		var theShadow = $('<div id="lightbox-shadow"/>');
		$(theShadow).click(function(e) {
			closeLightbox();
		});
		$('body').append(theShadow);
		$('body').append(theLightbox);
	}

	// remove any previously added content
	$('#lightbox').empty();

	var searchContainer = '<div class="searchWrapper"><input type="text" name="myInputSearch" id="myInputSearch" /></div>';
	$('#lightbox').append(searchContainer);

	// move the lightbox to the current window top + 100px
	$('#lightbox').css('top', $(window).scrollTop() + 100 + 'px');

	// display the lightbox
	$('#lightbox').show();
	$('#lightbox-shadow').show();
	
	var searchInput = $('#myInputSearch');
	
	AUI().use('aui-base', 'liferay-portlet-url', 'aui-node', function(A) {
		var resourceURL = Liferay.PortletURL.createResourceURL();
		resourceURL.setPortletId('quicksearch_WAR_quicksearchportlet');
		resourceURL.setResourceId('get_users');
		resourceURL.setParameter('action', 'search');
		autocompleteAjax(resourceURL, searchInput, 3);
	});

	searchInput.focus();

}

// close the lightbox
function closeLightbox() {

	// hide lightbox and shadow <div/>'s
	$('#lightbox').hide();
	$('#lightbox-shadow').hide();

	// remove contents of lightbox in case a video or other content is actively
	// playing
	$('#lightbox').empty();
}

/**
 * Autocomplete function using the "typeahead.js" plugin.
 * 
 * @param ajaxURL,
 *            URL of the search: return JSON data
 * @param inputField,
 *            input field to search datas
 */
function autocompleteAjax(ajaxURL, inputField, minLetters) {

	var autocompleteName = "results";

	// Cache for the results
	var searchCache = [];

	// To search again if we enter less than "minLetters" caracters: we have to
	// clean the cache
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
	 * Function to sort datas in the cache to keep only which matches with the
	 * search pattern.
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
	 * Function to make an ajax call to search contacts. Only if the search
	 * pattern is not already in the cache.
	 */
	var ajaxAutocompleteSearch = function() {
		return function findMatches(searchPattern, comboBox) {
			// to remove useless white space
			searchPattern = $.trim(searchPattern).replace(/ {2,}/g, ' ');
			// If the search pattern is not in the cache make an ajax call to
			// get the contacts
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
					empty : [ '<div class="empty-message">', 'No asset found',
							'</div>' ].join('\n'),
					suggestion : function(result) {
						return "<div class='result-entry'><a href='" + result.displayUrl + "'>"
								+ "<div class='title'>" + result.title + "<span class='asset-type'> - "+ result.assetType + "</span>" + "</div>" 
								+ "<div class='summary'>" + result.summary + "</div>"
								+ "<div class='metadata'>" + "Site: " + result.site + "</div>"
								+"</a></div>";
					}
				},
				source : ajaxAutocompleteSearch()
			}).on('typeahead:selected', selected);

	function selected($e, result, name) {
		window.location.href = result.displayUrl;
	}

}